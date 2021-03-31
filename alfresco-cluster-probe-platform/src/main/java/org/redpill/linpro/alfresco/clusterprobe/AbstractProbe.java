package org.redpill.linpro.alfresco.clusterprobe;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.redpill.linpro.alfresco.clusterprobe.repo.ClusterProbeUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.servlet.WebScriptServletRequest;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractProbe extends DeclarativeWebScript implements InitializingBean {

    protected static final String DEFAULT_SERVER = "localhost";


    private static final Logger LOG = Logger.getLogger(AbstractProbe.class);

    protected String configuredServer;

    protected int onlineHttpCode;
    protected int offlineHttpCode;

    protected String onlineText;
    protected String offlineText;

    protected List<String> configuredHosts;

    protected long thresholdLoadWarning;

    private String probeType;

    protected ClusterProbeUtils _clusterProbeUtils;

    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
        try {

            long before = System.currentTimeMillis();
            final Settings settings = getProbeSettings(req);

            final int code = settings.code;
            String text = settings.text;
            Map<String, Object> model = new HashMap<>();
            model.put("result", text);
            status.setCode(code);

            long after = System.currentTimeMillis();
            long time = after - before;
            if(time > thresholdLoadWarning) {
                LOG.warn("Returned status " + settings.text + " in " + time + " ms");
            }
            return model;

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
        Object o = serverObject.get(getType());
        if (o == null) {
            LOG.error("Could not find json clusterprobe settings for server " + server + ", type " + getType());
            return new Settings(server + "-" + offlineText, offlineHttpCode);
        }
        boolean active = (boolean) o;

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

    public void setThresholdLoadWarning(long thresholdLoadWarning) {
        this.thresholdLoadWarning = thresholdLoadWarning;
    }
}
