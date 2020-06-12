package org.redpill.linpro.alfresco.clusterprobe;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.redpill.linpro.alfresco.clusterprobe.repo.ClusterProbeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.extensions.webscripts.servlet.WebScriptServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class AbstractProbe extends AbstractWebScript {

    protected static final String DEFAULT_SERVER = "localhost";


    private static final Logger LOG = Logger.getLogger(AbstractProbe.class);

    @Value("${cluster.probe.host}")
    protected String configuredServer;

    @Value("${cluster.probe.online.httpcode}")
    protected int onlineHttpCode;
    @Value("${cluster.probe.offline.httpcode}")
    protected int offlineHttpCode;

    @Value("${cluster.probe.online.text}")
    protected String onlineText;
    @Value("${cluster.probe.offline.text}")
    protected String offlineText;
    @Autowired
    protected ClusterProbeUtils _clusterProbeUtils;

    @Override
    public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {
        try {
            final Settings settings = getProbeSettings(req);

            final int code = settings.code;
            String text = settings.text;

            res.addHeader("Content-Length", String.valueOf(text.length()));
            res.addHeader("Cache-Control", "no-cache");
            res.addHeader("Pragma", "no-cache");
            res.setContentType("text/plain");
            res.setContentEncoding("UTF-8");
            res.setStatus(code);
            res.getWriter().write(text);

            res.getWriter().flush();
            res.getWriter().close();
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            invalidateSession(req);
        }
    }

    private void invalidateSession(WebScriptRequest req) {
        if (!(req instanceof WebScriptServletRequest)) {
            return;
        }

        WebScriptServletRequest request = (WebScriptServletRequest) req;

        HttpServletRequest servletRequest = request.getHttpServletRequest();

        HttpSession session = servletRequest.getSession(false);

        if (session == null) {
            return;
        }

        session.invalidate();
    }

    protected String getServer() {
        String server = getConfiguredServer();

        if (server == null || server.length() == 0) {
            server = DEFAULT_SERVER;
        }

        return server;
    }


    protected Settings getProbeSettings() {
        return getProbeSettings(null);
    }

    protected String getConfiguredServer() {
        return configuredServer;
    }


    protected Settings getProbeSettings(WebScriptRequest req) {

        JSONArray serverObjects = _clusterProbeUtils.getSettingsJSON();
        if (serverObjects == null) {
            LOG.error("Could not find json clusterprobe settings");
            return new Settings(getConfiguredServer() + "-" + offlineText, offlineHttpCode);
        }

        String server = getConfiguredServer();
        String hostName = req.getServiceMatch().getTemplateVars().get("hostName");
        if (!StringUtils.isEmpty(hostName)) {
            server = hostName;
        }

        JSONObject serverObject = null;
        for (Object o : serverObjects) {
            JSONObject json = (JSONObject) o;
            String serverName = (String) json.get("serverName");
            if (serverName.equals(server)) {
                serverObject = json;
                break;
            }
        }

        if (serverObject == null) {
            LOG.error("Could not find json clusterprobe settings for server " + server);
            return new Settings(server + "-" + offlineText, offlineHttpCode);
        }

        boolean active = (boolean) serverObject.get(getType());
        Settings settings;
        if (active) {
            settings = new Settings(server + "-" + onlineText, onlineHttpCode);
        } else {
            settings = new Settings(server + "-" + offlineText, offlineHttpCode);
        }
        return settings;

    }

    protected abstract String getType();
}
