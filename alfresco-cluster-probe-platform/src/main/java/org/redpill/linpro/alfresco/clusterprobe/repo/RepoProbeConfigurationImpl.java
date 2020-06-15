package org.redpill.linpro.alfresco.clusterprobe.repo;

import org.redpill.linpro.alfresco.clusterprobe.AbstractProbeConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Properties;

/**
 *
 * @author Marcus Svartmark - Redpill Linpro AB
 */

public class RepoProbeConfigurationImpl extends AbstractProbeConfiguration implements InitializingBean {

  private Properties globalProperties;

  @Override
  public String getProbeHost() {
    // get the probe host, if that does not exist, rely on environment variables
    String probeHost = globalProperties.getProperty("cluster.probe.host");
    if (probeHost == null) {
      probeHost = super.getProbeHost();
    }
    return probeHost;
  }

  @Override
  protected String getEnvCustomHostname() {
    return System.getenv(HOSTNAME_ENV_CUSTOM_REPO);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(globalProperties, "globalProperties is null");
  }

  public void setGlobalProperties(Properties globalProperties) {
    this.globalProperties = globalProperties;
  }
}
