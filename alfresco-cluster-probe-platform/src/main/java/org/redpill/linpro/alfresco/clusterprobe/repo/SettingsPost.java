package org.redpill.linpro.alfresco.clusterprobe.repo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsPost extends DeclarativeWebScript implements InitializingBean {

  private ClusterProbeUtils _clusterProbeUtils;
  protected List<String> configuredHosts;
  @Override
  protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
    Map<String, Object> model = new HashMap<String, Object>();

    String server;
    String type;
    boolean value;

    try {
      JSONParser jsonParser = new JSONParser();
      JSONObject json = (JSONObject) jsonParser.parse(req.getContent().getContent());

      server = (String) json.get("server");
      type = (String) json.get("type");
      value = (boolean) json.get("value");
    } catch ( IOException | ParseException ex) {
      throw new RuntimeException(ex);
    }

    if (!StringUtils.hasText(server)) {
      status.setCode(Status.STATUS_BAD_REQUEST);
      status.setMessage("The required parameter server is not set.");
      status.setRedirect(true);
      return model;
    }

    if(!configuredHosts.contains(server)){
      status.setCode(Status.STATUS_BAD_REQUEST);
      status.setMessage("Server is not configurable");
      status.setRedirect(true);
      return model;
    }

    if (!StringUtils.hasText(type)) {
      status.setCode(Status.STATUS_BAD_REQUEST);
      status.setMessage("The required parameter type is not set.");
      status.setRedirect(true);
      return model;
    }


    JSONArray settingsJSON = _clusterProbeUtils.getSettingsJSON();
    for (Object o : settingsJSON) {
      JSONObject json = (JSONObject) o;
      String serverName = (String) json.get("serverName");
      if(serverName.equals(server)){
        json.put(type, value);
        break;
      }
    }

    if(!_clusterProbeUtils.saveSettingsJSON(settingsJSON)){
      status.setCode(Status.STATUS_INTERNAL_SERVER_ERROR);
      status.setMessage("Failed to save changes");
      status.setRedirect(true);
      return model;
    }
    return model;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(_clusterProbeUtils,"_clusterProbeUtils is null");
  }

  public void setClusterProbeUtils(ClusterProbeUtils clusterProbeUtils) {
    this._clusterProbeUtils = clusterProbeUtils;
  }
  public void setConfiguredHosts(String configuredHosts) {
    this.configuredHosts = Arrays.asList(configuredHosts.split(","));
  }
}
