package com.redpill.alfresco.clusterprobe.repo;

import java.io.Serializable;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.ResultSetRow;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;

import com.redpill.alfresco.clusterprobe.ClusterProbeModel;
import com.redpill.alfresco.clusterprobe.Settings;

public class ClusterProbeUtils {

  private SearchService _searchService;

  public void setSearchService(final SearchService searchService) {
    _searchService = searchService;
  }

  public Settings getSettings(final String server) {
    final String query = "PATH:\"/app:company_home/app:dictionary/cm:custom_preferences/cm:" + server + "\"";

    final SearchParameters searchParameters = new SearchParameters();

    searchParameters.addStore(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
    searchParameters.setLanguage(SearchService.LANGUAGE_LUCENE);
    searchParameters.setQuery(query);

    final ResultSet settingsResult = AuthenticationUtil.runAs(new AuthenticationUtil.RunAsWork<ResultSet>() {

      @Override
      public ResultSet doWork() throws Exception {
        return _searchService.query(searchParameters);
      }

    }, AuthenticationUtil.getSystemUserName());

    final Settings settings = new Settings(server + "-ONLINE", 200);

    if (settingsResult.length() > 0) {
      final ResultSetRow row = settingsResult.getRow(0);

      final Serializable text = row.getValue(ClusterProbeModel.PROP_TEXT);
      final Serializable code = row.getValue(ClusterProbeModel.PROP_CODE);

      settings.text = (String) text;
      settings.code = (Integer) code;
    }

    return settings;
  }

}
