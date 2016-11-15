package org.redpill.alfresco.clusterprobe.share;

import java.util.Properties;
import org.redpill.alfresco.clusterprobe.AbstractProbeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.extensions.config.ConfigService;
import org.springframework.stereotype.Component;

/**
 *
 * @author Marcus Svartmark - Redpill Linpro AB
 */
@Component("cp.clusterProbeShareConfiguration")
public class ShareProbeConfigurationImpl extends AbstractProbeConfiguration {

  @Autowired
  @Qualifier("web.config")
  private ConfigService configService;

  @Override
  public String getProbeHost() {
    String hostname = super.getProbeHost();

    String probeHost = configService.getGlobalConfig().getConfigElementValue("probe-host");
    if (probeHost == null) {
      probeHost = hostname;
    }
    return probeHost;
  }

}
