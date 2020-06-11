package org.redpill.alfresco.clusterprobe.repo;


import org.springframework.stereotype.Component;

@Component("webscript.org.redpill.alfresco.clusterprobe.probe.get")
public class ProbeGet extends AbstractRepoProbe {



    @Override
    protected String getType() {
        return "repo";
    }


}
