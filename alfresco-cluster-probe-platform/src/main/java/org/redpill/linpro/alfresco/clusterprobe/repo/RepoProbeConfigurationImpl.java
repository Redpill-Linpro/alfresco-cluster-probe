package org.redpill.linpro.alfresco.clusterprobe.repo;

import org.redpill.linpro.alfresco.clusterprobe.AbstractProbeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 *
 * @author Marcus Svartmark - Redpill Linpro AB
 */
@Component("cp.clusterProbeRepoConfiguration")
public class RepoProbeConfigurationImpl extends AbstractProbeConfiguration {

  @Autowired
  @Qualifier("global-properties")
  private Properties globalProperties;

  @Override
  public String getProbeHost() {
    // get the probe host, if that does not exist, rely on environment variables
    String probeHost = globalProperties.getProperty("alfresco.probe.host");
    if (probeHost == null) {
      probeHost = super.getProbeHost();
    }
    return probeHost;
  }

  @Override
  protected String getEnvCustomHostname() {
    return System.getenv(HOSTNAME_ENV_CUSTOM_REPO);
  }

}
