/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redpill.alfresco.clusterprobe;

import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 *
 * @author mars
 */
public class ProbeConfigurationTest {
  TestProbeConfiguration testProbeConfiguration;
  
  @Before
  public void setUp() {
    testProbeConfiguration = new TestProbeConfiguration();
  }
  @Test
  public void testGetHostname() {
    String hostname = testProbeConfiguration.getHostname();
    Assert.notNull(hostname);
  }

    
  public class TestProbeConfiguration extends AbstractProbeConfiguration {

    @Override
    protected String getEnvCustomHostname() {
      return "x";
    }
  }

}
