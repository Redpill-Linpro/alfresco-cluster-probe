package org.redpill.linpro.alfresco.clusterprobe.repo;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.InitializingBean;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClusterProbeUtils implements InitializingBean {

    private static final Logger LOG = Logger.getLogger(ClusterProbeUtils.class);

    protected String confProbeDiscPath;




    public JSONArray getSettingsJSON() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader fileReader = new FileReader(confProbeDiscPath)) {

            Object parse = jsonParser.parse(fileReader);

            return (JSONArray) parse;

        } catch (Exception e) {
            LOG.error("Failed to get settings", e);
            return null;
        }
    }

    public boolean saveSettingsJSON(JSONArray jsonObject){

        try(FileWriter fileWriter = new FileWriter(confProbeDiscPath)) {
            jsonObject.writeJSONString(fileWriter);
        } catch (IOException e) {
            LOG.error("Failed to save settings", e);
            return false;
        }
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }



    public void setConfProbeDiscPath(String confProbeDiscPath) {
        this.confProbeDiscPath = confProbeDiscPath;
    }
}
