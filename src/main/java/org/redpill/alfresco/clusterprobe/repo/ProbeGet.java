package org.redpill.alfresco.clusterprobe.repo;


import org.redpill.alfresco.clusterprobe.AbstractProbe;
import org.redpill.alfresco.clusterprobe.ProbeConfiguration;
import org.redpill.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.stereotype.Component;

@Component("webscript.org.redpill.alfresco.clusterprobe.probe.get")
public class ProbeGet extends AbstractProbe {

  @Autowired
  @Qualifier("cp.clusterProbeRepoConfiguration")
  private ProbeConfiguration probeConfiguration;

  @Autowired
  private ClusterProbeUtils clusterProbeUtils;

  @Override
  protected Settings getProbeSettings(final WebScriptRequest req) {
    final String server = getServer();
    return clusterProbeUtils.getSettings(server);
  }

  @Override
  protected String getConfiguredServer() {
    return probeConfiguration.getProbeHost();
  }

}
