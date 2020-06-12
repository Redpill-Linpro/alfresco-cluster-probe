package org.redpill.alfresco.clusterprobe.repo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.redpill.alfresco.clusterprobe.AbstractProbe;
import org.redpill.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.extensions.webscripts.WebScriptRequest;

public abstract class AbstractRepoProbe extends AbstractProbe {
    private static final Logger LOG = Logger.getLogger(AbstractRepoProbe.class);

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

    @Override
    protected String getConfiguredServer() {
        return configuredServer;
    }

    @Autowired
    protected ClusterProbeUtils _clusterProbeUtils;
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
