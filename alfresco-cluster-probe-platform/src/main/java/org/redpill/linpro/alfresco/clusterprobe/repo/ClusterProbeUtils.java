package org.redpill.linpro.alfresco.clusterprobe.repo;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClusterProbeUtils implements InitializingBean {

    private static final Logger LOG = Logger.getLogger(ClusterProbeUtils.class);

    protected String confProbeDiskPath;


    public JSONArray getSettingsJSON() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader fileReader = new FileReader(confProbeDiskPath)) {

            Object parse = jsonParser.parse(fileReader);

            return (JSONArray) parse;

        } catch (Exception e) {
            LOG.error("Failed to get settings", e);
            return null;
        }
    }

    public boolean saveSettingsJSON(JSONArray jsonObject) {

        try (FileWriter fileWriter = new FileWriter(confProbeDiskPath)) {
            jsonObject.writeJSONString(fileWriter);
        } catch (IOException e) {
            LOG.error("Failed to save settings", e);
            return false;
        }
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(confProbeDiskPath, "confProbeDiskPath is null");
        if (confProbeDiskPath.startsWith("$")) {
            throw new IllegalArgumentException("confProbeDiskPath is not set. Did you set the cluster.probe.diskpath property in alfresco-global.properties?");
        }
    }

    public void setConfProbeDiskPath(String confProbeDiskPath) {
        this.confProbeDiskPath = confProbeDiskPath;
    }
}
