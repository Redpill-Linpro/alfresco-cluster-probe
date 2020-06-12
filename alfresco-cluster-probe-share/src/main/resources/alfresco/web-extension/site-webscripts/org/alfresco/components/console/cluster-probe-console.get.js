/**
 * Admin Console Javascript Console component
 */

function getServer() {
  var ctx = Packages.org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext();
  var probeConfig = ctx.getBean('cp.clusterProbeShareConfiguration', Packages.org.redpill.alfresco.clusterprobe.ProbeConfiguration);

  return probeConfig.getProbeHost();
}

function main() {
  model.server = getServer();
}

main();
