package org.redpill.alfresco.clusterprobe.repo;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.alfresco.util.GUID;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.redpill.alfresco.clusterprobe.ProbeConfiguration;

public class ClusterProbeUIGetTest {

  Mockery mock;
  ProbeConfiguration probeConfiguration;

  @Before
  public void setUp() {
    mock = new JUnit4Mockery();
    probeConfiguration = mock.mock(ProbeConfiguration.class);
  }

  @Test
  public void testExecuteImplWebScriptRequestStatusCache() {
    final String alfrescoProbeHost = GUID.generate();

    mock.checking(new Expectations() {
      {
        oneOf(probeConfiguration).getProbeHost();
        will(returnValue(alfrescoProbeHost));
      }
    });

    ClusterProbeUIGet script = new ClusterProbeUIGet();
    script.setProbeConfiguration(probeConfiguration);
    Map<String, Object> model = script.executeImpl(null, null, null);

    String probeHost = (String) model.get("probeHost");

    assertEquals(alfrescoProbeHost, probeHost);
  }

}
