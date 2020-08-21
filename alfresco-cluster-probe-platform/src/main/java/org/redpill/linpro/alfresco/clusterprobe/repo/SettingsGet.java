package org.redpill.linpro.alfresco.clusterprobe.repo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsGet extends DeclarativeWebScript implements InitializingBean {

    private ClusterProbeUtils _clusterProbeUtils;

    protected int onlineHttpCode;
    protected int offlineHttpCode;

    protected String onlineText;
    protected String offlineText;

    protected List<String> configuredHosts;

    @Override
    protected Map<String, Object> executeImpl(final WebScriptRequest req, final Status status, final Cache cache) {
        final Map<String, Object> result = new HashMap<>();

        JSONArray settingsJSON = _clusterProbeUtils.getSettingsJSON();

        settingsJSON.removeIf(o -> {
            JSONObject jsonObject = (JSONObject) o;
            return !configuredHosts.contains(jsonObject.get("serverName"));
        });
        result.put("result", settingsJSON.toJSONString());
        result.put("httpOnline", onlineHttpCode);
        result.put("httpOffline", offlineHttpCode);
        result.put("onlineText", onlineText);
        result.put("offlineText", offlineText);

        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(_clusterProbeUtils, "_clusterProbeUtils is null");
    }

    public void setClusterProbeUtils(ClusterProbeUtils clusterProbeUtils) {
        this._clusterProbeUtils = clusterProbeUtils;
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

    public void setConfiguredHosts(String configuredHosts) {
        this.configuredHosts = Arrays.asList(configuredHosts.split(","));
    }
}
