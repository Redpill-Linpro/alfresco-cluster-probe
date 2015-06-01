package org.redpill.alfresco.clusterprobe.repo;

import java.io.Serializable;
import java.util.List;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespaceService;
import org.redpill.alfresco.clusterprobe.ClusterProbeModel;
import org.redpill.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component("cp.clusterProbeUtils")
public class ClusterProbeUtils {

  @Autowired
  @Qualifier("SearchService")
  private SearchService _searchService;

  @Autowired
  @Qualifier("repositoryHelper")
  private Repository _repository;

  @Autowired
  @Qualifier("NamespaceService")
  private NamespaceService _namespaceService;

  @Autowired
  @Qualifier("NodeService")
  private NodeService _nodeService;

  @Autowired
  @Qualifier("FileFolderService")
  private FileFolderService _fileFolderService;

  public Settings getSettings(final String server) {
    Assert.hasText(server, "You must supply a server parameter");

    return AuthenticationUtil.runAsSystem(new AuthenticationUtil.RunAsWork<Settings>() {

      @Override
      public Settings doWork() throws Exception {
        NodeRef node = getSettingsNode(server);

        Settings settings = new Settings(server + "-ONLINE", 200);

        if (node != null) {
          Serializable text = _nodeService.getProperty(node, ClusterProbeModel.PROP_TEXT);
          Serializable code = _nodeService.getProperty(node, ClusterProbeModel.PROP_CODE);

          settings.text = (String) text;
          settings.code = (Integer) code;
        }

        return settings;
      }

    });
  }

  public NodeRef getSettingsNode(final String server) {
    Assert.hasText(server, "You must supply a server parameter");

    return AuthenticationUtil.runAsSystem(new AuthenticationUtil.RunAsWork<NodeRef>() {

      @Override
      public NodeRef doWork() throws Exception {
        String query = "/app:company_home/app:dictionary/cm:custom_preferences/cm:" + server;

        List<NodeRef> settingsResult = _searchService.selectNodes(_repository.getRootHome(), query, null, _namespaceService, false);

        return settingsResult.size() != 0 ? settingsResult.get(0) : null;
      }

    });
  }

  public void saveSettings(final String server, final Settings settings) {
    Assert.hasText(server, "You must supply a server parameter");
    Assert.notNull(settings, "You must supply a settings parameter");

    AuthenticationUtil.runAsSystem(new AuthenticationUtil.RunAsWork<Void>() {

      @Override
      public Void doWork() throws Exception {
        NodeRef node = getSettingsNode(server);

        if (node == null) {
          node = createSettingsNode(server);
        }

        _nodeService.setProperty(node, ClusterProbeModel.PROP_TEXT, settings.text);
        _nodeService.setProperty(node, ClusterProbeModel.PROP_CODE, settings.code);

        return null;
      }

    });
  }

  public NodeRef createSettingsNode(final String server) {
    Assert.hasText(server, "You must supply a server parameter");

    return AuthenticationUtil.runAsSystem(new AuthenticationUtil.RunAsWork<NodeRef>() {

      @Override
      public NodeRef doWork() throws Exception {
        NodeRef customPreferences = getCustomPreferences();

        return _fileFolderService.create(customPreferences, server, ContentModel.TYPE_CONTENT).getNodeRef();
      }

    });
  }

  public NodeRef getCustomPreferences() {
    return AuthenticationUtil.runAsSystem(new AuthenticationUtil.RunAsWork<NodeRef>() {

      @Override
      public NodeRef doWork() throws Exception {
        String query = "/app:company_home/app:dictionary/cm:custom_preferences";

        List<NodeRef> nodes = _searchService.selectNodes(_repository.getRootHome(), query, null, _namespaceService, false);

        if (nodes.size() == 0) {
          return _fileFolderService.create(getDictionaryHome(), "custom_preferences", ContentModel.TYPE_FOLDER).getNodeRef();
        }

        return nodes.get(0);
      }

    });
  }

  public NodeRef getDictionaryHome() {
    return AuthenticationUtil.runAsSystem(new AuthenticationUtil.RunAsWork<NodeRef>() {

      @Override
      public NodeRef doWork() throws Exception {
        String query = "/app:company_home/app:dictionary";

        List<NodeRef> nodes = _searchService.selectNodes(_repository.getRootHome(), query, null, _namespaceService, false);

        if (nodes.size() == 0) {
          throw new RuntimeException("Data Dictionary not found!!!");
        }

        return nodes.get(0);
      }

    });
  }

  /*
   * For unit test purposes
   */
  public void setSearchService(SearchService searchService) {
    _searchService = searchService;
  }
  
  public void setRepository(Repository repository) {
    _repository = repository;
  }
  
  public void setNodeService(NodeService nodeService) {
    _nodeService = nodeService;
  }
  
}
