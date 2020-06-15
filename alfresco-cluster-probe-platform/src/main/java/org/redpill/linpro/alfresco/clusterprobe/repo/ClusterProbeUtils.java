package org.redpill.linpro.alfresco.clusterprobe.repo;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespaceService;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.redpill.linpro.alfresco.clusterprobe.ClusterProbeModel;
import org.redpill.linpro.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class ClusterProbeUtils implements InitializingBean {


    private SearchService _searchService;


    private Repository _repository;


    private NamespaceService _namespaceService;


    private NodeService _nodeService;


    private FileFolderService _fileFolderService;

    protected String confProbeDiscPath;

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

    public JSONArray getSettingsJSON() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader fileReader = new FileReader(confProbeDiscPath)) {

            Object parse = jsonParser.parse(fileReader);

            JSONArray probeConfigObjects = (JSONArray) parse;

            return probeConfigObjects;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveSettingsJSON(JSONArray jsonObject){

        try(FileWriter fileWriter = new FileWriter(confProbeDiscPath)) {
            jsonObject.writeJSONString(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(_nodeService, "_nodeService is null");
        Assert.notNull(_namespaceService, "_namespaceService is null");
        Assert.notNull(_searchService, "_searchService is null");
        Assert.notNull(_fileFolderService, "_fileFolderService is null");
    }





    public void setNamespaceService(NamespaceService namespaceService) {
        this._namespaceService = namespaceService;
    }


    public void setFileFolderService(FileFolderService fileFolderService) {
        this._fileFolderService = fileFolderService;
    }

    public void setConfProbeDiscPath(String confProbeDiscPath) {
        this.confProbeDiscPath = confProbeDiscPath;
    }
}
