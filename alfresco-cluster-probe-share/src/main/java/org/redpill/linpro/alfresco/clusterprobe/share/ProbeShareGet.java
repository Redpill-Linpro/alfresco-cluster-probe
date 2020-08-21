package org.redpill.linpro.alfresco.clusterprobe.share;

import org.alfresco.error.StackTraceUtil;
import org.apache.log4j.Logger;
import org.redpill.linpro.alfresco.clusterprobe.AbstractProbe;
import org.redpill.linpro.alfresco.clusterprobe.ProbeConfiguration;
import org.redpill.linpro.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.extensions.surf.RequestContext;
import org.springframework.extensions.surf.ServletUtil;
import org.springframework.extensions.surf.support.ThreadLocalRequestContext;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.connector.Connector;
import org.springframework.extensions.webscripts.connector.ConnectorService;
import org.springframework.extensions.webscripts.connector.Response;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;

public class ProbeShareGet extends AbstractProbe implements InitializingBean {
    private static final Logger LOG = Logger.getLogger(ProbeShareGet.class);
    protected static final String ENDPOINT_ID = "alfresco";


    private ProbeConfiguration probeConfiguration;

    @Override
    protected Settings getProbeSettings(final WebScriptRequest req) {
        try {
            final RequestContext requestContext = ThreadLocalRequestContext.getRequestContext();

            final ConnectorService connService = requestContext.getServiceRegistry().getConnectorService();

            final String currentUserId = requestContext.getUserId();

            final HttpSession currentSession = ServletUtil.getSession(true);

            final Connector connector = connService.getConnector(ENDPOINT_ID, currentUserId, currentSession);

            String alfrescoURL = "/org/redpill/alfresco/clusterprobe/probe/share";

            String hostName = req.getServiceMatch().getTemplateVars().get("hostName");
            if(!StringUtils.isEmpty(hostName)){
                alfrescoURL += "/" + hostName;
            }else {
                alfrescoURL += "/" + getConfiguredServer();
            }
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

    public void setProbeConfiguration(ProbeConfiguration probeConfiguration) {
        this.probeConfiguration = probeConfiguration;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(probeConfiguration, "probeConfiguration is null");
    }
}
