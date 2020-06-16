package org.interledger.openpayments.mandates;

import static org.slf4j.LoggerFactory.getLogger;

import org.interledger.openpayments.ApproveMandateRequest;
import org.interledger.openpayments.Charge;
import org.interledger.openpayments.ChargeId;
import org.interledger.openpayments.ChargeStatus;
import org.interledger.openpayments.ImmutableCharge;
import org.interledger.openpayments.ImmutableMandate;
import org.interledger.openpayments.Invoice;
import org.interledger.openpayments.Mandate;
import org.interledger.openpayments.MandateId;
import org.interledger.openpayments.MandateStatus;
import org.interledger.openpayments.NewCharge;
import org.interledger.openpayments.NewMandate;
import org.interledger.openpayments.PayIdAccountId;
import org.interledger.openpayments.PayInvoiceRequest;
import org.interledger.openpayments.UserAuthorizationRequiredException;
import org.interledger.openpayments.config.OpenPaymentsSettings;
import org.interledger.openpayments.events.MandateApprovedEvent;
import org.interledger.openpayments.events.MandateDeclinedEvent;
import org.interledger.openpayments.problems.MandateInsufficientBalanceProblem;
import org.interledger.openpayments.problems.MandateNotApprovedProblem;
import org.interledger.openpayments.problems.MandateNotFoundProblem;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import okhttp3.HttpUrl;
import org.interleger.openpayments.InvoiceService;
import org.interleger.openpayments.InvoiceServiceFactory;
import org.interleger.openpayments.PaymentSystemFacade;
import org.interleger.openpayments.PaymentSystemFacadeFactory;
import org.interleger.openpayments.client.WebhookClient;
import org.interleger.openpayments.mandates.MandateAccrualService;
import org.interleger.openpayments.mandates.MandateService;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class InMemoryMandateService implements MandateService {

  private static final Logger LOGGER = getLogger(InMemoryMandateService.class);

  private final HashMap<MandateId, Mandate> mandates = new HashMap<>();

  private final MandateAccrualService mandateAccrualService;
  private final InvoiceServiceFactory invoiceServiceFactory;
  private final PaymentSystemFacadeFactory paymentSystemFacadeFactory;
  private final WebhookClient webhookClient;
  private final Supplier<OpenPaymentsSettings> openPaymentsSettingsSupplier;

  public InMemoryMandateService(MandateAccrualService mandateAccrualService,
                                InvoiceServiceFactory invoiceServiceFactory,
                                PaymentSystemFacadeFactory paymentSystemFacadeFactory,
                                EventBus eventBus,
                                WebhookClient webhookClient, Supplier<OpenPaymentsSettings> openPaymentsSettingsSupplier) {
    this.mandateAccrualService = mandateAccrualService;
    this.invoiceServiceFactory = invoiceServiceFactory;
    this.paymentSystemFacadeFactory = paymentSystemFacadeFactory;
    this.webhookClient = webhookClient;
    this.openPaymentsSettingsSupplier = openPaymentsSettingsSupplier;
    eventBus.register(this);
  }

  @Override
  public Mandate createMandate(PayIdAccountId payIdAccountId, NewMandate newMandate) {
    MandateId mandateId = MandateId.of(UUID.randomUUID().toString());

    PaymentSystemFacade paymentSystemFacade = paymentSystemFacadeFactory.get(newMandate.paymentNetwork())
      .orElseThrow(() -> new UnsupportedOperationException("No payment facade service for " + newMandate.paymentNetwork()));

    Mandate mandate = Mandate.builder().from(newMandate)
      .mandateId(mandateId)
      .accountId(payIdAccountId.value())
      .balance(newMandate.amount())
      .status(MandateStatus.AWAITING_APPROVAL)
      .account(makeAccountUrl(payIdAccountId))
      .id(makeMandateUrl(payIdAccountId, mandateId))
      .userAuthorizationUrl(paymentSystemFacade.getMandateAuthorizationUrl(
        ApproveMandateRequest.builder()
          .mandateId(mandateId)
          .accountId(payIdAccountId)
          .memoToUser("Approve " + newMandate.description().orElse("recurring payment"))
          .redirectUrl(newMandate.userRedirectUrl())
          .build()
      ))
      .build();

    mandates.put(mandate.mandateId(), mandate);

    return mandate;
  }

  @Override
  public Optional<Mandate> findMandateById(PayIdAccountId payIdAccountId, MandateId mandateId) {
    return Optional.ofNullable(mandates.get(mandateId))
      .filter(mandate -> mandate.accountId().equals(payIdAccountId.value()))
      .map(mandate -> Mandate.builder().from(mandate)
        .balance(mandateAccrualService.calculateBalance(mandate))
        .build()
      );
  }

  @Override
  public List<Mandate> findMandatesByAccountId(PayIdAccountId payIdAccountId) {
    return mandates.values().stream()
      .filter(mandate -> mandate.accountId().equals(payIdAccountId.value()))
      .map(mandate -> Mandate.builder().from(mandate)
        .balance(mandateAccrualService.calculateBalance(mandate))
        .build())
      .collect(Collectors.toList());
  }

  @Override
  public Optional<Charge> findChargeById(PayIdAccountId payIdAccountId, MandateId mandateId, ChargeId chargeId) {
    return findMandateById(payIdAccountId, mandateId).flatMap(mandate -> mandate.charges().stream()
      .filter(charge -> charge.chargeId().equals(chargeId))
      .findAny());
  }

  @Override
  public Charge createCharge(PayIdAccountId payIdAccountId, MandateId mandateId, NewCharge newCharge) {
    Mandate mandate = findMandateById(payIdAccountId, mandateId)
      .orElseThrow(() -> new MandateNotFoundProblem(mandateId));

    if (!mandate.status().equals(MandateStatus.APPROVED)) {
      throw new MandateNotApprovedProblem(mandateId);
    }

    InvoiceService<?, ?> invoiceService = invoiceServiceFactory.get(mandate.paymentNetwork())
      .orElseThrow(() -> new UnsupportedOperationException("No invoice service for " + mandate.paymentNetwork()));

    Invoice invoice = invoiceService.findInvoiceByUrl(newCharge.invoice(), payIdAccountId)
      .orElseGet(() -> invoiceService.syncInvoice(newCharge.invoice(), payIdAccountId));

    synchronized (mandates) {
      if (mandate.balance().compareTo(invoice.amount()) >= 0) {
        ChargeId chargeId = ChargeId.of(UUID.randomUUID().toString());
        Charge charge = Charge.builder()
          .amount(invoice.amount())
          .invoice(invoice.receiverInvoiceUrl())
          .mandate(mandate.id())
          .mandateId(mandate.mandateId())
          .status(ChargeStatus.CREATED)
          .chargeId(chargeId)
          .id(mandate.id().newBuilder().addPathSegment("charges").addPathSegment(chargeId.value()).build())
          .build();

        Mandate updated = Mandate.builder()
          .from(mandate)
          .addCharges(charge)
          .build();

        mandates.put(mandate.mandateId(), updated);

        try {
          invoiceService.payInvoice(
            invoice.id(),
            invoice.accountId(),
            Optional.of(PayInvoiceRequest.builder().amount(invoice.amount()).build())
          );
          updateChargeStatus(payIdAccountId, mandateId, chargeId, ChargeStatus.PAYMENT_INITIATED);
        } catch (UserAuthorizationRequiredException e) {
          updateAuthorizationUrl(payIdAccountId, mandateId, chargeId, e.getUserAuthorizationUrl().toString());
          updateChargeStatus(payIdAccountId, mandateId, chargeId, ChargeStatus.PAYMENT_AWAITING_USER_AUTH);
        } catch (Exception e) {
          LOGGER.error("charging invoice {} to mandate {} failed", invoice.id(), mandate.mandateId(), e);
          updateChargeStatus(payIdAccountId, mandateId, chargeId, ChargeStatus.PAYMENT_FAILED);
        }
        return findChargeById(payIdAccountId, mandateId, chargeId).get();
      } else {
        throw new MandateInsufficientBalanceProblem(mandateId);
      }
    }
  }

  private void updateAuthorizationUrl(PayIdAccountId payIdAccountId, MandateId mandateId, ChargeId chargeId, String authorizationUrl) {
    updateCharge(payIdAccountId, mandateId, chargeId, (builder) -> builder.userAuthorizationUrl(authorizationUrl));
  }

  @Subscribe
  public void onMandateApproved(MandateApprovedEvent mandateApprovedEvent) {
    updateMandateStatus(mandateApprovedEvent.accountId(), mandateApprovedEvent.mandateId(), MandateStatus.APPROVED);
  }

  @Subscribe
  public void onMandateDeclined(MandateDeclinedEvent mandateApprovedEvent) {
    updateMandateStatus(mandateApprovedEvent.accountId(), mandateApprovedEvent.mandateId(), MandateStatus.DECLINED);
  }

  private void updateMandateStatus(PayIdAccountId payIdAccountId, MandateId mandateId, MandateStatus status) {
    updateMandate(payIdAccountId, mandateId, (builder) -> builder.status(status));
    findMandateById(payIdAccountId, mandateId).ifPresent(webhookClient::sendMandateStatusChange);
  }

  private void updateChargeStatus(PayIdAccountId payIdAccountId, MandateId mandateId, ChargeId chargeId, ChargeStatus status) {
    updateCharge(payIdAccountId, mandateId, chargeId, (builder) -> builder.status(status));
  }

  private void updateCharge(PayIdAccountId payIdAccountId,
                            MandateId mandateId,
                            ChargeId chargeId,
                            Consumer<ImmutableCharge.Builder> chargeUpdater) {
    findMandateById(payIdAccountId, mandateId).ifPresent(mandate -> {
      List<Charge> chargesToUpdate = Lists.newArrayList(mandate.charges());
      chargesToUpdate.stream()
        .filter(c -> c.chargeId().equals(chargeId))
        .findAny()
        .map(toUpdate -> {
          chargesToUpdate.remove(toUpdate);
          ImmutableCharge.Builder updateBuilder = Charge.builder().from(toUpdate);
          chargeUpdater.accept(updateBuilder);
          Mandate updated = Mandate.builder()
            .from(mandate)
            .charges(chargesToUpdate)
            .addCharges(updateBuilder.build())
            .build();
          mandates.put(mandateId, updated);
          return true;
        }).orElseGet(() -> {
        LOGGER.error("could not update missing charge id {}", chargeId);
        return false;
      });
    });
  }

  private void updateMandate(PayIdAccountId payIdAccountId,
                             MandateId mandateId,
                             Consumer<ImmutableMandate.Builder> mandateUpdater) {
    findMandateById(payIdAccountId, mandateId).ifPresent(mandate -> {
      ImmutableMandate.Builder updateBuilder = Mandate.builder().from(mandate);
      mandateUpdater.accept(updateBuilder);
      mandates.put(mandateId, updateBuilder.build());
    });
  }

  private HttpUrl makeAccountUrl(PayIdAccountId payIdAccountId) {
    return openPaymentsSettingsSupplier.get().metadata().issuer()
      .newBuilder()
      .addPathSegment(payIdAccountId.value())
      .build();
  }

  private HttpUrl makeMandateUrl(PayIdAccountId payIdAccountId, MandateId mandateId) {
    return makeAccountUrl(payIdAccountId).newBuilder()
      .addPathSegment("mandates")
      .addPathSegment(mandateId.value())
      .build();
  }

}