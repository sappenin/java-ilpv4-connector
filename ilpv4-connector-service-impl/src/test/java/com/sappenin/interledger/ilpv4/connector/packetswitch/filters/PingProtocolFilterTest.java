package com.sappenin.interledger.ilpv4.connector.packetswitch.filters;

import com.sappenin.interledger.ilpv4.connector.packetswitch.PacketRejector;
import org.interledger.connector.accounts.AccountId;
import org.interledger.connector.accounts.AccountSettings;
import org.interledger.core.InterledgerAddress;
import org.interledger.core.InterledgerCondition;
import org.interledger.core.InterledgerErrorCode;
import org.interledger.core.InterledgerFulfillPacket;
import org.interledger.core.InterledgerFulfillment;
import org.interledger.core.InterledgerPreparePacket;
import org.interledger.core.InterledgerRejectPacket;
import org.interledger.core.InterledgerResponsePacket;
import org.interledger.core.InterledgerResponsePacketHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Base64;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PingProtocolFilter}.
 */
public class PingProtocolFilterTest {

  private static final InterledgerAddress TARGET_ADDRESS = InterledgerAddress.of("example.target");
  private static final InterledgerRejectPacket REJECT_PACKET = InterledgerRejectPacket.builder()
    .triggeredBy(InterledgerAddress.of("test.conn"))
    .code(InterledgerErrorCode.F00_BAD_REQUEST)
    .message("error message")
    .build();

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Mock
  AccountSettings accountSettingsMock;

  @Mock
  PacketRejector packetRejectorMock;

  @Mock
  PacketSwitchFilterChain filterChainMock;

  private PingProtocolFilter pingProtocolFilter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(packetRejectorMock.reject(any(), any(), any(), any())).thenReturn(REJECT_PACKET);
    this.pingProtocolFilter = new PingProtocolFilter(packetRejectorMock, () -> TARGET_ADDRESS);

    when(accountSettingsMock.getAccountId()).thenReturn(AccountId.of("alice"));
  }

  @Test
  public void testFulfillmentFromBase64() {
    byte[] bytes = Base64.getDecoder().decode("cGluZ3BpbmdwaW5ncGluZ3BpbmdwaW5ncGluZ3Bpbmc=");
    final InterledgerFulfillment expectedFulfillment = InterledgerFulfillment.of(bytes);

    assertThat(expectedFulfillment, is(pingProtocolFilter.PING_PROTOCOL_FULFILLMENT));
    assertThat(expectedFulfillment.getCondition(), is(pingProtocolFilter.PING_PROTOCOL_CONDITION));
    assertThat(expectedFulfillment.validateCondition(pingProtocolFilter.PING_PROTOCOL_CONDITION), is(true));
  }

  @Test
  public void testFulfillmentFromAscii() throws UnsupportedEncodingException {
    final InterledgerFulfillment expectedFulfillment =
      InterledgerFulfillment.of("pingpingpingpingpingpingpingping".getBytes("US-ASCII"));

    assertThat(expectedFulfillment, is(pingProtocolFilter.PING_PROTOCOL_FULFILLMENT));
    assertThat(expectedFulfillment.getCondition(), is(pingProtocolFilter.PING_PROTOCOL_CONDITION));
    assertThat(expectedFulfillment.validateCondition(pingProtocolFilter.PING_PROTOCOL_CONDITION), is(true));
  }

  @Test
  public void testConditionFromBase64() {
    byte[] bytes = Base64.getDecoder().decode("jAC8DGFPZPfh4AtZpXuvXFe2oRmpDVSvSJg2oT+bx34=");
    final InterledgerCondition expectedCondition = InterledgerCondition.of(bytes);

    assertThat(expectedCondition, is(pingProtocolFilter.PING_PROTOCOL_CONDITION));
    assertThat(pingProtocolFilter.PING_PROTOCOL_FULFILLMENT.getCondition(),
      is(pingProtocolFilter.PING_PROTOCOL_CONDITION));
    assertThat(
      pingProtocolFilter.PING_PROTOCOL_FULFILLMENT.validateCondition(pingProtocolFilter.PING_PROTOCOL_CONDITION),
      is(true));
  }

  @Test
  public void sendPacketWithInvalidCondition() {
    final InterledgerPreparePacket preparePacket = InterledgerPreparePacket.builder()
      .amount(BigInteger.TEN)
      .executionCondition(InterledgerCondition.of(new byte[32]))
      .destination(TARGET_ADDRESS)
      .expiresAt(Instant.now())
      .build();
    InterledgerResponsePacket response = pingProtocolFilter.doFilter(
      accountSettingsMock, preparePacket, filterChainMock
    );

    new InterledgerResponsePacketHandler() {

      @Override
      protected void handleFulfillPacket(final InterledgerFulfillPacket interledgerFulfillPacket) {
        logger.error("InterledgerFulfillPacket: {}", interledgerFulfillPacket);
        fail("Expected a Reject!");
      }

      @Override
      protected void handleRejectPacket(final InterledgerRejectPacket interledgerRejectPacket) {
        assertThat(interledgerRejectPacket.getCode(), is(InterledgerErrorCode.F00_BAD_REQUEST));
        assertThat(interledgerRejectPacket.getTriggeredBy().isPresent(), is(true));
        assertThat(interledgerRejectPacket.getTriggeredBy(), is(REJECT_PACKET.getTriggeredBy()));
      }
    }.handle(response);
    verifyZeroInteractions(filterChainMock);
  }

  @Test
  public void sendPacketWithInvalidAddress() {
    final InterledgerPreparePacket preparePacket = InterledgerPreparePacket.builder()
      .amount(BigInteger.TEN)
      .executionCondition(pingProtocolFilter.PING_PROTOCOL_CONDITION)
      .destination(InterledgerAddress.of("example.foo"))
      .expiresAt(Instant.now())
      .build();
    pingProtocolFilter.doFilter(
      accountSettingsMock, preparePacket, filterChainMock
    );

    // Not a ping packet, so verify processing continues.
    verify(filterChainMock).doFilter(any(), any());
  }

  @Test
  public void sendPacket() {
    final InterledgerPreparePacket preparePacket = InterledgerPreparePacket.builder()
      .amount(BigInteger.TEN)
      .executionCondition(pingProtocolFilter.PING_PROTOCOL_CONDITION)
      .destination(TARGET_ADDRESS)
      .expiresAt(Instant.now())
      .build();
    InterledgerResponsePacket response = pingProtocolFilter.doFilter(
      accountSettingsMock, preparePacket, filterChainMock
    );

    new InterledgerResponsePacketHandler() {

      @Override
      protected void handleFulfillPacket(final InterledgerFulfillPacket interledgerFulfillPacket) {
        assertThat(interledgerFulfillPacket.getFulfillment(), is(pingProtocolFilter.PING_PROTOCOL_FULFILLMENT));
      }

      @Override
      protected void handleRejectPacket(final InterledgerRejectPacket interledgerRejectPacket) {
        logger.error("InterledgerRejectPacket: {}", interledgerRejectPacket);
        fail("Expected a Fulfill!");
      }
    }.handle(response);

    verifyZeroInteractions(filterChainMock);
  }
}