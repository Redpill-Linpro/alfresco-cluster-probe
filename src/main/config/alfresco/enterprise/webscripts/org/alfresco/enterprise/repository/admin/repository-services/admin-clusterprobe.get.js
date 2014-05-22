<import resource="classpath:alfresco/enterprise/webscripts/org/alfresco/enterprise/repository/admin/admin-common.lib.js">

function main() {
   var hostname = Admin.getMBeanAttributes(
      "Alfresco:Name=Cluster,Tool=Admin", ["HostName"]
   )["HostName"].value;

   var probeHost = Admin.getMBeanAttributes(
      "Alfresco:Name=GlobalProperties", ["alfresco.probe.host"]
   )["alfresco.probe.host"];

   if (probeHost && probeHost != null) {
      hostname = probeHost.value;
   }

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