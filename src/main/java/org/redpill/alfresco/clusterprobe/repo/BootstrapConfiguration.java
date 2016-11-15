package org.redpill.alfresco.clusterprobe.repo;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.i18n.ResourceBundleBootstrapComponent;
import org.alfresco.repo.dictionary.DictionaryBootstrap;
import org.alfresco.repo.dictionary.DictionaryDAO;
import org.alfresco.repo.tenant.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BootstrapConfiguration {
 
  @Autowired
  private DictionaryDAO dictionaryDAO;
  
  @Autowired
  private TenantService tenantService;
  
  public BootstrapConfiguration() {
    super();
  }

  @Bean(initMethod="bootstrap")
  @DependsOn("dictionaryBootstrap")
  public DictionaryBootstrap clusterProbeDictionaryBootstrap() {
    List<String> models = new ArrayList<String>();
    models.add("alfresco/extension/clusterProbeModel.xml");

    DictionaryBootstrap bootstrap = new DictionaryBootstrap();
    bootstrap.setDictionaryDAO(dictionaryDAO);
    bootstrap.setTenantService(tenantService);
    bootstrap.setModels(models);
    
    return bootstrap;
  }
  
  @Bean
  public ResourceBundleBootstrapComponent clusterProbeResourceBundles() {
    List<String> resourceBundles = new ArrayList<String>();
    resourceBundles.add("alfresco.enterprise.messages.alfresco-cluster-probe");
    
    ResourceBundleBootstrapComponent rbbc = new ResourceBundleBootstrapComponent();
    rbbc.setResourceBundles(resourceBundles);
    
    return rbbc;
  }

}
