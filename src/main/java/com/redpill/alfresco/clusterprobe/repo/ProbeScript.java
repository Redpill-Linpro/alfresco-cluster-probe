package com.redpill.alfresco.clusterprobe.repo;

import java.util.Properties;

import com.redpill.alfresco.clusterprobe.AbstractProbe;
import com.redpill.alfresco.clusterprobe.Settings;

public class ProbeScript extends AbstractProbe {

  private Properties _globalProperties;

  private ClusterProbeUtils _clusterProbeUtils;

  public void setGlobalProperties(final Properties globalProperties) {
    _globalProperties = globalProperties;
  }

  public void setClusterProbeUtils(final ClusterProbeUtils clusterProbeUtils) {
    _clusterProbeUtils = clusterProbeUtils;
  }

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
