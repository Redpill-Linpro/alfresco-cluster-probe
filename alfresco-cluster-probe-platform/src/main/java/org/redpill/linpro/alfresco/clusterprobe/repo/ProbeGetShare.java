package org.redpill.linpro.alfresco.clusterprobe.repo;

import org.redpill.linpro.alfresco.clusterprobe.AbstractProbe;
import org.springframework.stereotype.Component;

@Component("webscript.org.redpill.alfresco.clusterprobe.probeshare.get")
public class ProbeGetShare extends AbstractProbe {

    @Override
    protected String getType() {
        return "share";
    }

}
