package org.redpill.alfresco.clusterprobe.repo.it;

import static org.junit.Assert.*;

import org.junit.Test;
import org.redpill.alfresco.clusterprobe.Settings;
import org.redpill.alfresco.clusterprobe.repo.ClusterProbeUtils;
import org.redpill.alfresco.test.AbstractRepoIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

public class ClusterProbeUtilsIntegrationTest extends AbstractRepoIntegrationTest {

  @Autowired
  private ClusterProbeUtils _clusterProbeUtils;

  @Test
  public void testGetSettingsSuccess() {
    String server = "foobar-server";

    Settings settings = _clusterProbeUtils.getSettings(server);

    assertEquals(200, settings.code);
    assertEquals(server + "-ONLINE", settings.text);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSettingsFailureOnNullServer() {
    _clusterProbeUtils.getSettings(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSettingsFailureOnEmptyServer() {
    _clusterProbeUtils.getSettings(null);
  }

  @Test
  public void testCreateSettingsNode() {
    String server = "server-" + System.currentTimeMillis();

    assertNotNull(_clusterProbeUtils.createSettingsNode(server));
  }

}
