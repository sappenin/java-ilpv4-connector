package org.interledger.crypto;

public class CryptoConfigConstants {

  public static final String DOT = ".";
  public static final String ENABLED = "enabled";
  public static final String TRUE = "true";
  public static final String FALSE = "false";

  public static final String ILPV4_CONNECTOR_KEYSTORE = "ilpv4.connector.keystore";

  public static final String ILPV4_CONNECTOR_KEYSTORE_GCP = ILPV4_CONNECTOR_KEYSTORE + ".gcpkms";
  public static final String ILPV4_CONNECTOR_KEYSTORE_GCP_ENABLED = ILPV4_CONNECTOR_KEYSTORE_GCP + DOT + ENABLED;

  public static final String GOOGLE_CLOUD_PROJECT= "GOOGLE_CLOUD_PROJECT";
  public static final String ILPV4_CONNECTOR_KEYSTORE_LOCATION_ID = ILPV4_CONNECTOR_KEYSTORE_GCP + ".locationId";


  ///////////////
  // JKS
  ///////////////

  public static final String FILENAME = "filename";
  public static final String PASSWORD = "password";

  public static final String ILPV4_CONNECTOR_KEYSTORE_JKS = ILPV4_CONNECTOR_KEYSTORE + ".jks";
  public static final String ILPV4_CONNECTOR_KEYSTORE_JKS_ENABLED = ILPV4_CONNECTOR_KEYSTORE_JKS + DOT + ENABLED;
  public static final String ILPV4_CONNECTOR_KEYSTORE_JKS_FILENAME = ILPV4_CONNECTOR_KEYSTORE_JKS + DOT + FILENAME;
  public static final String ILPV4_CONNECTOR_KEYSTORE_JKS_PASSWORD = ILPV4_CONNECTOR_KEYSTORE_JKS + DOT + PASSWORD;
  public static final String ILPV4_CONNECTOR_KEYSTORE_JKS_SECRET0_ALIAS =
    ILPV4_CONNECTOR_KEYSTORE_JKS + ".secret0_alias";
  public static final String ILPV4_CONNECTOR_KEYSTORE_JKS_SECRET0_PASSWORD =
    ILPV4_CONNECTOR_KEYSTORE_JKS + ".secret0_password";

  public static final String ILPV4_CONNECTOR_KEYSTORE_JKS_FILENAME_DEFAULT = "crypto/crypto.p12";
  public static final String ILPV4_CONNECTOR_KEYSTORE_JKS_PASSWORD_DEFAULT = "password";
  public static final String ILPV4_CONNECTOR_KEYSTORE_JKS_SECRET0_ALIAS_DEFAULT = "secret0";
  public static final String ILPV4_CONNECTOR_KEYSTORE_JKS_SECRET0_PASSWORD_DEFAULT = "password";
}
