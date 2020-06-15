package org.redpill.linpro.alfresco.clusterprobe;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.redpill.linpro.alfresco.clusterprobe.repo.ClusterProbeUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.extensions.webscripts.servlet.WebScriptServletRequest;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractProbe extends AbstractWebScript implements InitializingBean {

    protected static final String DEFAULT_SERVER = "localhost";


    private static final Logger LOG = Logger.getLogger(AbstractProbe.class);

    protected String configuredServer;

    protected int onlineHttpCode;
    protected int offlineHttpCode;

    protected String onlineText;
    protected String offlineText;

    protected List<String> configuredHosts;


    private String probeType;

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
        checkConfiguredServer(server);

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

    protected void checkConfiguredServer(String server) {
        if (!configuredHosts.contains(server)) {
            throw new RuntimeException("Server " + server + " is not configured");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(_clusterProbeUtils, "_clusterProbeUtils is null");
        Assert.notNull(probeType, "probeType is null");
    }

    protected String getType() {
        return probeType;
    }


    public void setConfiguredServer(String configuredServer) {

        this.configuredServer = configuredServer;
    }

    public void setOnlineHttpCode(int onlineHttpCode) {
        this.onlineHttpCode = onlineHttpCode;
    }

    public void setOfflineHttpCode(int offlineHttpCode) {
        this.offlineHttpCode = offlineHttpCode;
    }

    public void setOnlineText(String onlineText) {
        this.onlineText = onlineText;
    }

    public void setOfflineText(String offlineText) {
        this.offlineText = offlineText;
    }

    public void setClusterProbeUtils(ClusterProbeUtils clusterProbeUtils) {
        this._clusterProbeUtils = clusterProbeUtils;
    }

    public void setProbeType(String probeType) {
        this.probeType = probeType;
    }

    public void setConfiguredHosts(String configuredHosts) {
        this.configuredHosts = Arrays.asList(configuredHosts.split(","));
    }
}
