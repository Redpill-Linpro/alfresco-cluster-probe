package org.redpill.alfresco.clusterprobe.repo;

import java.util.HashMap;
import java.util.Map;
import org.redpill.alfresco.clusterprobe.ProbeConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.stereotype.Component;

@Component("webscript.org.redpill.alfresco.clusterprobe.cluster-probe-ui.get")
public class ClusterProbeUIGet extends DeclarativeWebScript {

  @Autowired
  @Qualifier("cp.clusterProbeRepoConfiguration")
  private ProbeConfiguration probeConfiguration;

  @Override
  protected Map<String, Object> executeImpl(final WebScriptRequest req, final Status status, final Cache cache) {
    final Map<String, Object> model = new HashMap<>();
    String probeHost = probeConfiguration.getProbeHost();
    model.put("probeHost", probeHost);
    return model;
  }

  //For unit tests
  protected void setProbeConfiguration(ProbeConfiguration probeConfiguration) {
    this.probeConfiguration = probeConfiguration;
  }

}
