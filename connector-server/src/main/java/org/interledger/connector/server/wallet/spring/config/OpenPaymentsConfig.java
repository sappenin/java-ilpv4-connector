package org.interledger.connector.server.wallet.spring.config;

import static org.interledger.connector.core.ConfigConstants.ENABLED_PROTOCOLS;
import static org.interledger.connector.core.ConfigConstants.OPEN_PAYMENTS_ENABLED;
import static org.interledger.connector.core.ConfigConstants.SPSP__URL_PATH;
import static org.interledger.connector.core.ConfigConstants.TRUE;

import org.interledger.connector.accounts.sub.LocalDestinationAddressUtils;
import org.interledger.connector.opa.InvoiceService;
import org.interledger.connector.opa.PaymentSystemFacade;
import org.interledger.connector.opa.model.IlpPaymentDetails;
import org.interledger.connector.opa.model.OpenPaymentsSettings;
import org.interledger.connector.opa.model.XrpPayment;
import org.interledger.connector.opa.model.XrpPaymentDetails;
import org.interledger.connector.payments.SendPaymentService;
import org.interledger.connector.payments.StreamPayment;
import org.interledger.connector.persistence.repositories.InvoicesRepository;
import org.interledger.connector.persistence.repositories.PaymentsRepository;
import org.interledger.connector.settings.ConnectorSettings;
import org.interledger.connector.wallet.DefaultRemoteInvoiceService;
import org.interledger.connector.wallet.IlpInvoiceService;
import org.interledger.connector.wallet.IlpPaymentSystemFacade;
import org.interledger.connector.wallet.InvoiceFactory;
import org.interledger.connector.wallet.OpenPaymentsProxyClient;
import org.interledger.connector.wallet.PayIdResolver;
import org.interledger.connector.wallet.RemoteInvoiceService;
import org.interledger.connector.wallet.XrplInvoiceService;
import org.interledger.connector.wallet.mandates.DefaultMandateAccrualService;
import org.interledger.connector.wallet.mandates.InMemoryMandateService;
import org.interledger.connector.wallet.mandates.MandateAccrualService;
import org.interledger.connector.wallet.mandates.MandateService;
import org.interledger.spsp.PaymentPointerResolver;
import org.interledger.stream.receiver.ServerSecretSupplier;
import org.interledger.stream.receiver.StreamConnectionGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

import java.time.Clock;
import java.util.Optional;
import java.util.function.Supplier;

@Configuration
@ConditionalOnProperty(prefix = ENABLED_PROTOCOLS, name = OPEN_PAYMENTS_ENABLED, havingValue = TRUE)
//@EnableConfigurationProperties(OpenPaymentsSettingsFromPropertyFile.class)
@ComponentScan(basePackages = {
  "org.interledger.connector.server.wallet.controllers", // For Wallet
})
public class OpenPaymentsConfig {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public static final String OPA_ILP = "ILP";
  public static final String XRP = "PAY_ID";

  @Bean
  public Supplier<OpenPaymentsSettings> openPaymentsSettingsSupplier(Supplier<ConnectorSettings> connectorSettings) {
    return () -> connectorSettings.get().openPayments().orElseThrow(
      () -> new IllegalStateException("missing open payments config"));
  }

  @Bean
  public InvoiceService<StreamPayment, IlpPaymentDetails> ilpInvoiceService(
    InvoicesRepository invoicesRepository,
    PaymentsRepository paymentsRepository,
    ConversionService conversionService,
    InvoiceFactory invoiceFactory,
    OpenPaymentsProxyClient openPaymentsProxyClient,
    Supplier<OpenPaymentsSettings> openPaymentsSettingsSupplier,
    PaymentSystemFacade<StreamPayment, IlpPaymentDetails> ilpPaymentSystemFacade,
    EventBus eventBus) {
    return new IlpInvoiceService(
      invoicesRepository,
      paymentsRepository,
      conversionService,
      invoiceFactory,
      openPaymentsProxyClient,
      openPaymentsSettingsSupplier,
      ilpPaymentSystemFacade,
      eventBus);
  }

  @Bean
  public InvoiceService<XrpPayment, XrpPaymentDetails> xrpInvoiceService(
    InvoicesRepository invoicesRepository,
    PaymentsRepository paymentsRepository,
    ConversionService conversionService,
    InvoiceFactory invoiceFactory,
    OpenPaymentsProxyClient openPaymentsProxyClient,
    Supplier<OpenPaymentsSettings> openPaymentsSettingsSupplier,
    EventBus eventBus
  ) {
    return new XrplInvoiceService(
      invoicesRepository,
      paymentsRepository,
      conversionService,
      invoiceFactory,
      openPaymentsProxyClient,
      openPaymentsSettingsSupplier,
      eventBus
    );
  }

  @Bean
  @Qualifier(OPA_ILP)
  public PaymentSystemFacade<StreamPayment, IlpPaymentDetails> ilpPaymentSystemFacade(
    PaymentPointerResolver paymentPointerResolver,
    @Value("${" + SPSP__URL_PATH + ":}") final String opaUrlPath,
    StreamConnectionGenerator streamConnectionGenerator,
    ServerSecretSupplier serverSecretSupplier,
    SendPaymentService sendPaymentService,
    LocalDestinationAddressUtils localDestinationAddressUtils) {
    return new IlpPaymentSystemFacade(
      opaUrlPath,
      paymentPointerResolver,
      streamConnectionGenerator,
      serverSecretSupplier,
      sendPaymentService,
      localDestinationAddressUtils);
  }

  @Bean
  public OpenPaymentsProxyClient openPaymentsClient() {
    return OpenPaymentsProxyClient.construct();
  }

  @Bean
  public PaymentPointerResolver opaPaymentPointerResolver() {
    return PaymentPointerResolver.defaultResolver();
  }

  @Bean
  public InvoiceFactory invoiceFactory(
    PaymentPointerResolver paymentPointerResolver,
    PayIdResolver payIdPointerResolver,
    Supplier<ConnectorSettings> connectorSettings,
    Optional<String> opaUrlPath
  ) {
    return new InvoiceFactory(paymentPointerResolver, payIdPointerResolver, openPaymentsSettingsSupplier(connectorSettings), opaUrlPath);
  }

  @Bean
  public PayIdResolver payIdResolver() {
    return PayIdResolver.defaultPayIdResolver();
  }

  @Bean
  public RemoteInvoiceService remoteInvoiceService(OkHttpClient okHttpClient, ObjectMapper objectMapper) {
    return new DefaultRemoteInvoiceService(okHttpClient, objectMapper);
  }

  @Bean
  public MandateAccrualService mandateAccrualService(Clock clock) {
    return new DefaultMandateAccrualService(clock);
  }

  @Bean
  public MandateService mandateService(
    MandateAccrualService mandateAccrualService,
    RemoteInvoiceService remoteInvoiceService,
    Supplier<OpenPaymentsSettings> openPaymentsSettingsSupplier) {
    return new InMemoryMandateService(mandateAccrualService, remoteInvoiceService, openPaymentsSettingsSupplier);
  }

}
