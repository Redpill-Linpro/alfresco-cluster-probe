package org.redpill.linpro.alfresco.clusterprobe.share;

import org.alfresco.error.StackTraceUtil;
import org.alfresco.util.Pair;
import org.apache.log4j.Logger;
import org.redpill.linpro.alfresco.clusterprobe.AbstractProbe;
import org.redpill.linpro.alfresco.clusterprobe.ProbeConfiguration;
import org.redpill.linpro.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.extensions.surf.RequestContext;
import org.springframework.extensions.surf.ServletUtil;
import org.springframework.extensions.surf.support.ThreadLocalRequestContext;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.connector.Connector;
import org.springframework.extensions.webscripts.connector.ConnectorService;
import org.springframework.extensions.webscripts.connector.Response;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 *
 * @author Marcus Svartmark - Redpill Linpro AB
 */
@Component("webscript.org.redpill.alfresco.clusterprobe.probetransform.get")
public class ProbeTransform extends AbstractProbe {

  private static final Logger LOG = Logger.getLogger(ProbeTransform.class);
  protected static final String ENDPOINT_ID = "alfresco";

  @Autowired
  @Qualifier("cp.clusterProbeShareConfiguration")
  private ProbeConfiguration probeConfiguration;

  protected Pair<String, String> parseRequestedTransformation(final WebScriptRequest req) {
    Map<String, String> templateVars = req.getServiceMatch().getTemplateVars();
    String sourceExtension = templateVars.get("sourceExtension");
    String targetExtension = templateVars.get("targetExtension");
    return new Pair<>(sourceExtension, targetExtension);
  }

  @Override
  protected Settings getProbeSettings(final WebScriptRequest req) {
    try {
      Pair<String, String> parseRequestedTransformation = parseRequestedTransformation(req);
      final RequestContext requestContext = ThreadLocalRequestContext.getRequestContext();

      final ConnectorService connService = requestContext.getServiceRegistry().getConnectorService();

      final String currentUserId = requestContext.getUserId();

      final HttpSession currentSession = ServletUtil.getSession(true);

      final Connector connector = connService.getConnector(ENDPOINT_ID, currentUserId, currentSession);

      final String alfrescoURL = "/org/redpill/alfresco/clusterprobe/probe/transform/" + parseRequestedTransformation.getFirst() + "/" + parseRequestedTransformation.getSecond();

      final Response response = connector.call(alfrescoURL);

      return new Settings(response.getResponse(), response.getStatus().getCode());
    } catch (final Exception ex) {
      LOG.error(ex.getMessage(), ex);
      final StringBuilder sb = new StringBuilder();

      StackTraceUtil.buildStackTrace(ex.getMessage(), ex.getStackTrace(), sb, 0);

      return new Settings("Couldn't get settings from repo server.\n" + sb.toString(), 500);
    }
  }

  @Override
  protected String getConfiguredServer() {
    return probeConfiguration.getProbeHost();
  }

}
