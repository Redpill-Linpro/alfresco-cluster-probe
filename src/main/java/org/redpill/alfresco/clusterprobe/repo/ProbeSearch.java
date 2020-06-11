package org.redpill.alfresco.clusterprobe.repo;


import org.springframework.stereotype.Component;

/**
 * @author Marcus Svartmark - Redpill Linpro AB
 */
@Component("webscript.org.redpill.alfresco.clusterprobe.probesearch.get")
public class ProbeSearch extends AbstractRepoProbe {

    @Override
    protected String getType() {
        return "search";
    }
}
