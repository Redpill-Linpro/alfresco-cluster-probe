package org.redpill.linpro.alfresco.clusterprobe.repo;


import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.redpill.linpro.alfresco.clusterprobe.AbstractProbe;
import org.redpill.linpro.alfresco.clusterprobe.Settings;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.util.Assert;

/**
 * @author Marcus Svartmark - Redpill Linpro AB
 */
public class ProbeSearch extends AbstractProbe {
    private static final Logger LOG = Logger.getLogger(ProbeSearch.class);

    private SearchService searchService;

    @Override
    protected Settings getProbeSettings(WebScriptRequest req) {

        JSONArray serverObjects = _clusterProbeUtils.getSettingsJSON();
        if (serverObjects == null) {
            LOG.error("Could not find json clusterprobe settings");
            return new Settings(getConfiguredServer() + "-" + offlineText, offlineHttpCode);
        }

        String server = getConfiguredServer();
        String hostName = req.getServiceMatch().getTemplateVars().get("hostName");
        if (!StringUtils.isEmpty(hostName)) {
            server = hostName;
        }
        checkConfiguredServer(server);
        JSONObject serverObject = null;
        for (Object o : serverObjects) {
            JSONObject json = (JSONObject) o;
            String serverName = (String) json.get("serverName");
            if (serverName.equals(server)) {
                serverObject = json;
                break;
            }
        }

        if (serverObject == null) {
            LOG.error("Could not find json clusterprobe settings for server " + server);
            return new Settings(server + "-" + offlineText, offlineHttpCode);
        }

        boolean active = (boolean) serverObject.get(getType());
        Settings settings;
        if (active) {
            settings = new Settings(server + "-" + onlineText, onlineHttpCode);

            final SearchParameters sp = new SearchParameters();
            sp.setLanguage(SearchService.LANGUAGE_FTS_ALFRESCO);
            sp.setQuery("cm:name:xycjalksjflkajsfoiajfqoi OR cm:name:poqoifjsdjgskdhgkjsndkg"); //Dummy query which forces solr to be used over transactional
            sp.addStore(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
            try {
                AuthenticationUtil.runAsSystem((AuthenticationUtil.RunAsWork<Void>) () -> {
                            searchService.query(sp);
                            return null;
                        }
                );
            } catch (Exception e) {
                settings.text = server + "-" + offlineText;
                settings.code = offlineHttpCode;
                LOG.error("Failed to execute search", e);
            }

        } else {
            settings = new Settings(server + "-" + offlineText, offlineHttpCode);
        }
        return settings;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.notNull(searchService, "searchService is null");
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }
}
