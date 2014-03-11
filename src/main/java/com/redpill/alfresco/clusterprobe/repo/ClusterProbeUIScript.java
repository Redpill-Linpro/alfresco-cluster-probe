package com.redpill.alfresco.clusterprobe.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class ClusterProbeUIScript extends DeclarativeWebScript {

  private Properties _globalProperties;

  public void setGlobalProperties(final Properties globalProperties) {
    _globalProperties = globalProperties;
  }

  @Override
  protected Map<String, Object> executeImpl(final WebScriptRequest req, final Status status, final Cache cache) {
    final Map<String, Object> model = new HashMap<String, Object>();

    // get the alfresco host, if that does not exist, take 'localhost'
    final String alfrescoHost = _globalProperties.getProperty("alfresco.host", "localhost");

    // get the probe host, if that does not exist, take the alfresco host
    final String probeHost = _globalProperties.getProperty("alfresco.probe.host", alfrescoHost);

    model.put("probeHost", probeHost);

    return model;
  }

}
