package org.redpill.alfresco.clusterprobe.share;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.extensions.surf.util.ResourceBundleBootstrapComponent;

@Configuration
public class BootstrapConfiguration {
  
  @Bean
  public ResourceBundleBootstrapComponent clusterProbeResourceBundles() {
    List<String> resourceBundles = new ArrayList<String>();
    resourceBundles.add("alfresco.messages.cluster-probe-console");
    
    ResourceBundleBootstrapComponent rbbc = new ResourceBundleBootstrapComponent();
    rbbc.setResourceBundles(resourceBundles);
    
    return rbbc;
  }

}
