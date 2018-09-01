package com.sappenin.ilpv4.settings;

import com.sappenin.ilpv4.model.settings.ConnectorSettings;

/**
 * Provides runtime-reloadable access to all settings for this connector (peers, routing, etc).
 */
public interface ConnectorSettingsService {

  /**
   * Accessor for a view of the current settings configured for this Connector.
   *
   * @return An immutable instance of {@link ConnectorSettings}.
   */
  ConnectorSettings getConnectorSettings();
}
