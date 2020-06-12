package org.redpill.linpro.alfresco.clusterprobe.repo;


import org.redpill.linpro.alfresco.clusterprobe.AbstractProbe;
import org.springframework.stereotype.Component;

@Component("webscript.org.redpill.alfresco.clusterprobe.probe.get")
public class ProbeGet extends AbstractProbe {



    @Override
    protected String getType() {
        return "repo";
    }


}
