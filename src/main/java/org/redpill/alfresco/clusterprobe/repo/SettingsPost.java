package org.redpill.alfresco.clusterprobe.repo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.redpill.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("webscript.org.redpill.alfresco.clusterprobe.settings.post")
public class SettingsPost extends DeclarativeWebScript {

  @Autowired
  private ClusterProbeUtils _clusterProbeUtils;

  @Override
  protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
    Map<String, Object> model = new HashMap<String, Object>();

    String server;
    String text;
    int code;

    try {
      JSONObject json = new JSONObject(req.getContent().getContent());

      server = json.has("server") ? json.getString("server") : null;
      text = json.has("text") ? json.getString("text") : null;
      code = json.has("code") ? json.getInt("code") : 0;
    } catch (JSONException | IOException ex) {
      throw new RuntimeException(ex);
    }

    if (!StringUtils.hasText(server)) {
      status.setCode(Status.STATUS_BAD_REQUEST);
      status.setMessage("The required parameter server is not set.");
      status.setRedirect(true);
      return model;
    }

    if (!StringUtils.hasText(text)) {
      status.setCode(Status.STATUS_BAD_REQUEST);
      status.setMessage("The required parameter text is not set.");
      status.setRedirect(true);
      return model;
    }

    if (code == 0) {
      status.setCode(Status.STATUS_BAD_REQUEST);
      status.setMessage("The required parameter code is not set.");
      status.setRedirect(true);
      return model;
    }

    Settings settings = new Settings(text, code);
    _clusterProbeUtils.saveSettings(server, settings);

    return model;
  }

}
