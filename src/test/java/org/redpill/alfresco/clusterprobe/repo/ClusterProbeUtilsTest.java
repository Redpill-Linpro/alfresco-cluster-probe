package org.redpill.alfresco.clusterprobe.repo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.repo.model.Repository;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.SearchService;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.redpill.alfresco.clusterprobe.ClusterProbeModel;
import org.redpill.alfresco.clusterprobe.Settings;

public class ClusterProbeUtilsTest {

  Mockery _mock;

  private SearchService _searchService;

  private Repository _repository;

  private NodeService _nodeService;

  private ClusterProbeUtils _clusterProbeUtils;

  @Before
  public void before() throws Exception {
    _mock = new JUnit4Mockery() {
      {
        setThreadingPolicy(new Synchroniser());
        setImposteriser(ClassImposteriser.INSTANCE);
      }
    };

    _searchService = _mock.mock(SearchService.class);
    _repository = _mock.mock(Repository.class);
    _nodeService = _mock.mock(NodeService.class);

    _clusterProbeUtils = new ClusterProbeUtils();
    _clusterProbeUtils.setSearchService(_searchService);
    _clusterProbeUtils.setRepository(_repository);
    _clusterProbeUtils.setNodeService(_nodeService);
  }

  @Test
  public void testGetSettings() {
    final String host = "localhost";

    _mock.checking(new Expectations() {
      {
        NodeRef rootHome = new NodeRef("workspace://SpacesStore/root_home");
        NodeRef settingsNode = new NodeRef("workspace://SpacesStore/settings_node");
        List<NodeRef> settingsResult = new ArrayList<NodeRef>();
        settingsResult.add(settingsNode);

        allowing(_repository).getRootHome();
        will(returnValue(rootHome));

        allowing(_searchService).selectNodes(rootHome, String.format("/app:company_home/app:dictionary/cm:custom_preferences/cm:%s", host), null, null, false);
        will(returnValue(settingsResult));

        allowing(_nodeService).getProperty(settingsNode, ClusterProbeModel.PROP_TEXT);
        will(returnValue(host + "-ONLINE"));

        allowing(_nodeService).getProperty(settingsNode, ClusterProbeModel.PROP_CODE);
        will(returnValue(200));
      }
    });

    Settings settings = _clusterProbeUtils.getSettings("localhost");

    assertNotNull(settings);

    assertEquals(host + "-ONLINE", settings.text);
    assertEquals(200, settings.code);
  }

}
