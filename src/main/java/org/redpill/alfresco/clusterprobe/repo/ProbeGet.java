package org.redpill.alfresco.clusterprobe.repo;

import java.util.Properties;

import org.redpill.alfresco.clusterprobe.AbstractProbe;
import org.redpill.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("webscript.org.redpill.alfresco.clusterprobe.probe.get")
public class ProbeGet extends AbstractProbe {

  @Autowired
  @Qualifier("global-properties")
  private Properties _globalProperties;

  @Autowired
  private ClusterProbeUtils _clusterProbeUtils;

  @Override
  protected Settings getProbeSettings() {
    final String server = getServer();

    final Settings settings = _clusterProbeUtils.getSettings(server);

    return settings;
  }

  @Override
  protected String getConfiguredServer() {
    // get the alfresco host, if that does not exist, take 'localhost'
    final String alfrescoHost = _globalProperties.getProperty("alfresco.host", "localhost");

    // get the probe host, if that does not exist, take the alfresco host
    final String probeHost = _globalProperties.getProperty("alfresco.probe.host", alfrescoHost);

    return probeHost;
  }

}
