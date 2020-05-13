package org.interledger.connector.settings;

import static org.assertj.core.api.Assertions.assertThat;

import org.interledger.connector.accounts.AccountId;
import org.interledger.connector.opa.model.OpenPaymentsMetadata;
import org.interledger.connector.opa.model.OpenPaymentsSettings;
import org.interledger.core.InterledgerAddress;
import org.interledger.core.InterledgerAddressPrefix;
import org.interledger.crypto.CryptoKey;
import org.interledger.crypto.CryptoKeys;
import org.interledger.link.Link;

import okhttp3.HttpUrl;
import org.junit.Test;

import java.time.Duration;
import java.util.Optional;

/**
 * Unit tests for {@link ConnectorSettings}.
 */
public class ConnectorSettingsTest {

  @Test
  public void globalPrefix() {
    final ConnectorSettings defaultConnectorSettings = ImmutableConnectorSettings.builder()
      .globalRoutingSettings(GlobalRoutingSettings.builder().routingSecret("foo").build())
      .openPayments(OpenPaymentsSettings.builder()
        .connectorUrl(HttpUrl.parse("https://connector.example.com"))
        .ilpOperatorAddress(InterledgerAddress.of("example.connector"))
        .metadata(OpenPaymentsMetadata.builder()
          .issuer(HttpUrl.parse("https://connector.example.com"))
          .build())
        .build())
      .build();

    assertThat(defaultConnectorSettings.globalPrefix()).isEqualTo(InterledgerAddressPrefix.GLOBAL);
    assertThat(defaultConnectorSettings.operatorAddress()).isEqualTo(Link.SELF);
    assertThat(defaultConnectorSettings.websocketServerEnabled()).isFalse();
    assertThat(defaultConnectorSettings.toChildAddress(AccountId.of("foo"))).isEqualTo(Link.SELF.with("foo"));
    assertThat(defaultConnectorSettings.minMessageWindowMillis()).isEqualTo(1000);
    assertThat(defaultConnectorSettings.maxHoldTimeMillis()).isEqualTo(30000);

    assertThat(defaultConnectorSettings.globalRoutingSettings()).isNotNull();
    assertThat(defaultConnectorSettings.globalRoutingSettings().routeExpiry()).isEqualTo(Duration.ofMillis(45000L));
    assertThat(defaultConnectorSettings.globalRoutingSettings().routeBroadcastInterval())
      .isEqualTo(Duration.ofMillis(30000L));
    assertThat(defaultConnectorSettings.globalRoutingSettings().routingSecret()).isEqualTo(Optional.of("foo"));
    assertThat(defaultConnectorSettings.globalRoutingSettings().isUseParentForDefaultRoute()).isFalse();
    assertThat(defaultConnectorSettings.globalRoutingSettings().isRouteBroadcastEnabled()).isFalse();
    assertThat(defaultConnectorSettings.globalRoutingSettings().maxEpochsPerRoutingTable()).isEqualTo(50);

    assertThat(defaultConnectorSettings.enabledProtocols()).isNotNull();
    assertThat(defaultConnectorSettings.enabledProtocols().isPeerRoutingEnabled()).isTrue();
    assertThat(defaultConnectorSettings.enabledProtocols().isPingProtocolEnabled()).isTrue();
    assertThat(defaultConnectorSettings.enabledProtocols().isIldcpEnabled()).isTrue();
    assertThat(defaultConnectorSettings.enabledProtocols().isIlpOverHttpEnabled()).isTrue();

    assertThat(defaultConnectorSettings.enabledFeatures()).isNotNull();
    assertThat(defaultConnectorSettings.enabledFeatures().isRateLimitingEnabled()).isFalse();
    assertThat(defaultConnectorSettings.enabledFeatures().isLocalSpspFulfillmentEnabled()).isFalse();

    assertThat(defaultConnectorSettings.keys()).isEqualTo(
      CryptoKeys.builder()
        .accountSettings(CryptoKey.builder().alias("accountSettings").version("1").build())
        .secret0(CryptoKey.builder().alias("secret0").version("1").build())
        .build()
    );

  }
}
