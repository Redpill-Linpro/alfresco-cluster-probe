package org.redpill.linpro.alfresco.clusterprobe.share;

import org.redpill.linpro.alfresco.clusterprobe.AbstractProbeConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.extensions.config.ConfigService;
import org.springframework.util.Assert;

/**
 *
 * @author Marcus Svartmark - Redpill Linpro AB
 */
public class ShareProbeConfigurationImpl extends AbstractProbeConfiguration implements InitializingBean {


  private ConfigService configService;

  @Override
  public String getProbeHost() {
    String probeHost = configService.getGlobalConfig().getConfigElementValue("probe-host");
    if (probeHost == null) {
      probeHost = super.getProbeHost();;
    }

    return probeHost;
  }

  @Override
  protected String getEnvCustomHostname() {
    return System.getenv(HOSTNAME_ENV_CUSTOM_SHARE);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(configService, "configService is null");
  }

  public void setConfigService(ConfigService configService) {
    this.configService = configService;
  }
}
