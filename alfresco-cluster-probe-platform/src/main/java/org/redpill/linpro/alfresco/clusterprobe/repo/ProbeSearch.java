package org.redpill.linpro.alfresco.clusterprobe.repo;


import org.redpill.linpro.alfresco.clusterprobe.AbstractProbe;
import org.springframework.stereotype.Component;

/**
 * @author Marcus Svartmark - Redpill Linpro AB
 */
@Component("webscript.org.redpill.alfresco.clusterprobe.probesearch.get")
public class ProbeSearch extends AbstractProbe {

    @Override
    protected String getType() {
        return "search";
    }
}
