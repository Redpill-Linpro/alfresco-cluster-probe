package org.redpill.alfresco.clusterprobe.repo;

import java.util.Properties;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.log4j.Logger;

import org.redpill.alfresco.clusterprobe.AbstractProbe;
import org.redpill.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("webscript.org.redpill.alfresco.clusterprobe.probesearch.get")
public class ProbeSearch extends AbstractProbe {

  private static final Logger LOG = Logger.getLogger(ProbeSearch.class);

  @Autowired
  @Qualifier("global-properties")
  private Properties _globalProperties;

  @Autowired
  @Qualifier("SearchService")
  private SearchService searchService;

  @Override
  protected Settings getProbeSettings() {
    final String server = getServer();

    final Settings settings = new Settings("Search is working on server " + server, 200);

    final SearchParameters sp = new SearchParameters();
    sp.setLanguage(SearchService.LANGUAGE_FTS_ALFRESCO);
    sp.setQuery("cm:name:xycjalksjflkajsfoiajfqoi OR cm:name:poqoifjsdjgskdhgkjsndkg"); //Dummy query which forces solr to be used over transactional
    sp.addStore(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
    try {
      AuthenticationUtil.runAsSystem(new AuthenticationUtil.RunAsWork<Void>() {

        @Override
        public Void doWork() throws Exception {
          searchService.query(sp);
          return null;
        }
      }
      );
    } catch (Exception e) {
      //Set code to 500 on
      settings.text = "Search not available. An occured error on server " + server + ": " + e.getMessage();
      settings.code = 500;
      LOG.error(settings.text, e);
    }

    return settings;
  }

  @Override
  protected String getConfiguredServer() {

    // get the alfresco host, if that does not exist, take 'localhost'
    final String alfrescoHost = _globalProperties.getProperty("alfresco.host", "localhost");

    // get the probe host, if that does not exist, take the alfresco host
    final String probeHost = _globalProperties.getProperty("alfresco.probe.host", alfrescoHost);

    return probeHost;
  }

}
