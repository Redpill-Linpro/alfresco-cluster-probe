package org.redpill.linpro.alfresco.clusterprobe.repo;

import org.redpill.linpro.alfresco.clusterprobe.ProbeConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class ClusterProbeUIGet extends DeclarativeWebScript implements InitializingBean {


    private ProbeConfiguration probeConfiguration;

    @Override
    protected Map<String, Object> executeImpl(final WebScriptRequest req, final Status status, final Cache cache) {
        final Map<String, Object> model = new HashMap<>();
        String probeHost = probeConfiguration.getProbeHost();
        model.put("probeHost", probeHost);
        return model;
    }

    //For unit tests
    public void setProbeConfiguration(ProbeConfiguration probeConfiguration) {
        this.probeConfiguration = probeConfiguration;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(probeConfiguration, "probeConfiguration is null");
    }


}
