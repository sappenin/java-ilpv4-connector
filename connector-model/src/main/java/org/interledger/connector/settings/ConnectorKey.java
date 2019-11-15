package org.interledger.connector.settings;

import org.immutables.value.Value;

/**
 * Lookup info for a key that a connector will use. Mainly to capture key alias/version/etc from config.
 */
@Value.Immutable
public interface ConnectorKey {

  static ImmutableConnectorKey.Builder builder() {
    return ImmutableConnectorKey.builder();
  }

  /**
   * Key alias
   * @return alias
   */
  String alias();

  /**
   * key version
   * @return version
   */
  String version();

}
