package org.redpill.alfresco.clusterprobe.repo;

import java.util.HashMap;
import java.util.Map;

import org.redpill.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.stereotype.Component;

@Component("webscript.org.redpill.alfresco.clusterprobe.settings.get")
public class SettingsGet extends DeclarativeWebScript {

  @Autowired
  private ClusterProbeUtils _clusterProbeUtils;

  @Override
  protected Map<String, Object> executeImpl(final WebScriptRequest req, final Status status, final Cache cache) {
    final Map<String, Object> result = new HashMap<String, Object>();

    final String server = req.getParameter("server");

    final Settings settings = _clusterProbeUtils.getSettings(server);

    result.put("text", settings.text);
    result.put("code", settings.code);

    return result;
  }

}
