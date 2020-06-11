package org.redpill.alfresco.clusterprobe.repo;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.redpill.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.stereotype.Component;

@Component("webscript.org.redpill.alfresco.clusterprobe.settings.get")
public class SettingsGet extends DeclarativeWebScript {

  @Autowired
  private ClusterProbeUtils _clusterProbeUtils;

  @Value("${cluster.probe.online.httpcode}")
  protected int onlineHttpCode;
  @Value("${cluster.probe.offline.httpcode}")
  protected int offlineHttpCode;

  @Value("${cluster.probe.online.text}")
  protected String onlineText;
  @Value("${cluster.probe.offline.text}")

  protected String offlineText;
  @Override
  protected Map<String, Object> executeImpl(final WebScriptRequest req, final Status status, final Cache cache) {
    final Map<String, Object> result = new HashMap<>();

    JSONArray settingsJSON = _clusterProbeUtils.getSettingsJSON();

    result.put("result", settingsJSON.toJSONString());
    result.put("httpOnline", onlineHttpCode);
    result.put("httpOffline", offlineHttpCode);
    result.put("onlineText", onlineText);
    result.put("offlineText", offlineText);

    return result;
  }

}
