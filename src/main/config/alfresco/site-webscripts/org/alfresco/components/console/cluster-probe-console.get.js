/**
 * Admin Console Javascript Console component
 */

function getServer() {
   var configElement = config.global['probe-host'];
   
   var server = "localhost";
   
   if (configElement != null) {
      var server = configElement.value;

      if (server == null) {
         server = "localhost";
      }
   }

   return server;
}

function main() {
   model.server = getServer();
}

main();
