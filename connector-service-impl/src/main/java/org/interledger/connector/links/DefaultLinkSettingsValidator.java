package org.interledger.connector.links;

import org.interledger.connector.crypto.ConnectorEncryptionService;
import org.interledger.crypto.EncryptedSecret;
import org.interledger.link.LinkSettings;
import org.interledger.link.http.IlpOverHttpLinkSettings;
import org.interledger.link.http.IncomingLinkSettings;
import org.interledger.link.http.OutgoingLinkSettings;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

public class DefaultLinkSettingsValidator implements LinkSettingsValidator {

  private final ConnectorEncryptionService encryptionService;

  public DefaultLinkSettingsValidator(ConnectorEncryptionService encryptionService) {
    this.encryptionService = encryptionService;
  }


  @Override
  public <T extends LinkSettings> T validateSettings(T linkSettings) {
    if (linkSettings instanceof IlpOverHttpLinkSettings) {
      return (T) validateIlpLinkSettings((IlpOverHttpLinkSettings) linkSettings);
    }
    return linkSettings;
  }

  private IlpOverHttpLinkSettings validateIlpLinkSettings(IlpOverHttpLinkSettings linkSettings) {
    EncryptedSecret incomingSecret =
        validate(getOrCreateEncryptedSecret(linkSettings.incomingHttpLinkSettings().encryptedTokenSharedSecret()));

    EncryptedSecret outgoingSecret =
        validate(getOrCreateEncryptedSecret(linkSettings.incomingHttpLinkSettings().encryptedTokenSharedSecret()));

    IncomingLinkSettings incomingLinkSettings =
        IncomingLinkSettings.builder().from(linkSettings.incomingHttpLinkSettings())
            .encryptedTokenSharedSecret(incomingSecret.encodedValue())
            .build();

    OutgoingLinkSettings outgoingLinkSettings =
        OutgoingLinkSettings.builder().from(linkSettings.outgoingHttpLinkSettings())
            .encryptedTokenSharedSecret(outgoingSecret.encodedValue())
            .build();

    Map<String, Object> newCustomSettings = Maps.newHashMap(linkSettings.getCustomSettings());
    newCustomSettings.put(IncomingLinkSettings.HTTP_INCOMING_SHARED_SECRET,
        incomingLinkSettings.encryptedTokenSharedSecret());
    newCustomSettings.put(OutgoingLinkSettings.HTTP_OUTGOING_SHARED_SECRET,
        outgoingLinkSettings.encryptedTokenSharedSecret());

    return IlpOverHttpLinkSettings.builder().from(linkSettings)
        .incomingHttpLinkSettings(incomingLinkSettings)
        .outgoingHttpLinkSettings(outgoingLinkSettings)
        .customSettings(newCustomSettings)
        .build();
  }

  private EncryptedSecret getOrCreateEncryptedSecret(String sharedSecret) {
    if (StringUtils.isEmpty(sharedSecret)) {
      throw new IllegalArgumentException("sharedSecret cannot be empty");
    }
    if (sharedSecret.startsWith("enc:")) {
      return EncryptedSecret.fromEncodedValue(sharedSecret);
    } else {
      byte[] secretBytes = null;
      try {
        secretBytes = Base64.getDecoder().decode(sharedSecret);
        return encryptionService.encryptWithAccountSettingsKey(secretBytes);
      } finally {
        Arrays.fill(secretBytes, (byte) 0);
      }
    }
  }

  private EncryptedSecret validate(EncryptedSecret encryptedSecret) {
    return encryptionService.getDecryptor().withDecrypted(encryptedSecret, (decrypted) -> encryptedSecret);
  }

}
