package org.redpill.alfresco.clusterprobe;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcus Svartmark - Redpill Linpro AB
 */
public abstract class AbstractProbeConfiguration implements ProbeConfiguration {

  public static final String HOSTNAME_ENV_CUSTOM_REPO = "ALFRESCO_PROBE_REPO_HOST";
  public static final String HOSTNAME_ENV_CUSTOM_SHARE = "ALFRESCO_PROBE_SHARE_HOST";
  public static final String HOSTNAME_DEFAULT = "localhost";

  /**
   * Return the default environment hostname
   *
   * @return
   */
  protected String getHostname() {
    String hostName = null;
    try {
      hostName = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException ex) {
      Logger.getLogger(AbstractProbeConfiguration.class.getName()).log(Level.SEVERE, null, ex);
    }

    return hostName;
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
      hostname = getHostname();
    }
    if (hostname == null) {
      hostname = HOSTNAME_DEFAULT;
    }
    return hostname;
  }

}
