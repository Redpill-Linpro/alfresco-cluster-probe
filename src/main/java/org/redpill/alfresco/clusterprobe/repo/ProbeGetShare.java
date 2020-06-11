package org.redpill.alfresco.clusterprobe.repo;

import org.springframework.stereotype.Component;

@Component("webscript.org.redpill.alfresco.clusterprobe.probeshare.get")
public class ProbeGetShare extends AbstractRepoProbe {

    @Override
    protected String getType() {
        return "share";
    }

}
