<import resource="classpath:alfresco/enterprise/webscripts/org/alfresco/enterprise/repository/admin/admin-common.lib.js">

function main() {
  
  var ctx = Packages.org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext();
  var probeConfig = ctx.getBean('cp.clusterProbeRepoConfiguration', Packages.org.redpill.alfresco.clusterprobe.ProbeConfiguration);
  var hostname = probeConfig.getProbeHost();

   model.attributes = [];
   model.attributes["hostname"] = {
      qname: '',
      name: 'hostname',
      value: hostname,
      description: '',
      type: '',
      readonly: true
   };

   model.tools = Admin.getConsoleTools("admin-clusterprobe");
   model.metadata = Admin.getServerMetaData();
}

main();