package org.redpill.alfresco.clusterprobe.share;

import javax.servlet.http.HttpSession;

import org.alfresco.error.StackTraceUtil;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.redpill.alfresco.clusterprobe.AbstractProbe;
import org.redpill.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.extensions.config.ConfigService;
import org.springframework.extensions.surf.RequestContext;
import org.springframework.extensions.surf.ServletUtil;
import org.springframework.extensions.surf.support.ThreadLocalRequestContext;
import org.springframework.extensions.webscripts.connector.Connector;
import org.springframework.extensions.webscripts.connector.ConnectorService;
import org.springframework.extensions.webscripts.connector.Response;
import org.springframework.stereotype.Component;

@Component("webscript.org.redpill.alfresco.clusterprobe.probe.get")
public class ProbeGet extends AbstractProbe {
  private static final Logger LOG = Logger.getLogger(ProbeGet.class);
  protected static final String ENDPOINT_ID = "alfresco";

  @Autowired
  @Qualifier("web.config")
  private ConfigService _configService;

  @Override
  protected Settings getProbeSettings() {
    try {
      final String server = getServer();

      final RequestContext requestContext = ThreadLocalRequestContext.getRequestContext();

      final ConnectorService connService = requestContext.getServiceRegistry().getConnectorService();

      final String currentUserId = requestContext.getUserId();

      final HttpSession currentSession = ServletUtil.getSession(true);

      final Connector connector = connService.getConnector(ENDPOINT_ID, currentUserId, currentSession);

      final String alfrescoURL = "/org/redpill/alfresco/clusterprobe/settings?server=" + server;

      final Response response = connector.call(alfrescoURL);

      final String jsonResponse = response.getResponse();

      final JSONObject json = new JSONObject(new JSONTokener(jsonResponse));

      return new Settings(json.getString("text"), json.getInt("code"));
    } catch (final Exception ex) {
      LOG.error(ex.getMessage(), ex);
      final StringBuilder sb = new StringBuilder();

      StackTraceUtil.buildStackTrace(ex.getMessage(), ex.getStackTrace(), sb, 0);

      return new Settings("Couldn't get settings from repo server.\n" + sb.toString(), 500);
    }
  }

  @Override
  protected String getConfiguredServer() {
    return _configService.getGlobalConfig().getConfigElementValue("probe-host");
  }

}
