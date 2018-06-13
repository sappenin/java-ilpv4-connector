package com.sappenin.ilpv4;

import com.sappenin.ilpv4.model.Account;
import com.sappenin.ilpv4.peer.PeerManager;
import com.sappenin.ilpv4.settings.ConnectorSettings;
import org.interledger.core.InterledgerFulfillPacket;
import org.interledger.core.InterledgerPreparePacket;
import org.interledger.core.InterledgerProtocolException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.Future;

/**
 * A default implementation of {@link IlpConnector}.
 */
public class DefaultIlpConnector implements IlpConnector {

  //private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final ConnectorSettings connectorSettings;
  private final PeerManager peerManager;

  public DefaultIlpConnector(final ConnectorSettings connectorSettings, final PeerManager peerManager) {
    this.connectorSettings = Objects.requireNonNull(connectorSettings);
    this.peerManager = Objects.requireNonNull(peerManager);
  }

  @PostConstruct
  private final void init() {
    connectorSettings.getPeers().stream().map(ConnectorSettings.PeerSettings::toPeer).forEach(peerManager::add);
  }

  @PreDestroy
  public void shutdown() {
    // peerManager#shutdown is called automatically by spring due to naming convention.
    //this.peerManager.shutdown();
  }

  @Override
  public ConnectorSettings getConnectorSettings() {
    return this.connectorSettings;
  }

  @Override
  public Future<InterledgerFulfillPacket> handleIncomingData(Account sourceAccount, InterledgerPreparePacket interledgerPreparePacket) throws InterledgerProtocolException {

    throw new RuntimeException("Not yet implemented!");
  }

  @Override
  public Future<Void> handleIncomingMoney(BigInteger amount) {
    return null;
  }
}
