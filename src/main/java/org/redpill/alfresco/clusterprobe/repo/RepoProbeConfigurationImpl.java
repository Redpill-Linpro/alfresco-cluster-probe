package org.redpill.alfresco.clusterprobe.repo;

import java.util.Properties;
import org.redpill.alfresco.clusterprobe.AbstractProbeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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
    String hostname = super.getProbeHost();

    // get the probe host, if that does not exist, rely on environment variables
    final String probeHost = globalProperties.getProperty("alfresco.probe.host", hostname);

    return probeHost;
  }

}
