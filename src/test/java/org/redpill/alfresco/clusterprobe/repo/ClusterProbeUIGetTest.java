package org.redpill.alfresco.clusterprobe.repo;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Properties;

import org.alfresco.util.GUID;
import org.junit.Test;

public class ClusterProbeUIGetTest {

  @Test
  public void testExecuteImplWebScriptRequestStatusCache() {
    String alfrescoHost = GUID.generate();
    String alfrescoProbeHost = GUID.generate();

    Properties globalProperties = new Properties();
    globalProperties.put("alfresco.host", alfrescoHost);
    globalProperties.put("alfresco.probe.host", alfrescoProbeHost);

    ClusterProbeUIGet script = new ClusterProbeUIGet();
    script.setGlobalProperties(globalProperties);

    Map<String, Object> model = script.executeImpl(null, null, null);
    
    String probeHost = (String) model.get("probeHost");

    assertEquals(alfrescoProbeHost, probeHost);
  }

}
