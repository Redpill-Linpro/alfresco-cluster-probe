package org.redpill.alfresco.clusterprobe;

/**
 * 
 * @author Marcus Svartmark - Redpill Linpro AB
 */
public abstract class AbstractProbeConfiguration implements ProbeConfiguration {

  public static final String HOSTNAME_ENV_WINDOWS = "COMPUTERNAME";
  public static final String HOSTNAME_ENV_LINUX = "HOSTNAME";
  public static final String HOSTNAME_ENV_CUSTOM_REPO = "ALFRESCO.PROBE.REPO.HOST";
  public static final String HOSTNAME_ENV_CUSTOM_SHARE = "ALFRESCO.PROBE.SHARE.HOST";
  public static final String HOSTNAME_DEFAULT = "localhost";

  /**
   * Return the default environment hostname
   *
   * @return
   */
  protected String getEnvHostname() {
    //For Windows
    String host = System.getenv(HOSTNAME_ENV_WINDOWS);
    if (host != null) {
      return host;
    }
    //For Linux
    host = System.getenv(HOSTNAME_ENV_LINUX);
    if (host != null) {
      return host;
    }
    return null;
  }

  /**
   * Return a custom set environment variable
   *
   * @return
   */
  protected abstract String getEnvCustomHostname();
  
  @Override
  public String getProbeHost() {
    String hostname = getEnvCustomHostname();
    if (hostname == null) {
      hostname = getEnvHostname();
    }
    if (hostname == null) {
      hostname = HOSTNAME_DEFAULT;
    }
    return hostname;
  }

}
