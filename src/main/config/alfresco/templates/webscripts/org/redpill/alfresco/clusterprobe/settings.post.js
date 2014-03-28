function main() {
   var content = jsonUtils.toObject(requestbody.content);
   
   var server = content['server'];
   var text = content["text"];
   var code = content["code"];

   if (!server || server == null || server.length == 0) {
      status.code = 500;
      status.redirect = true;
      return;
   }

   if (!text || text == null || text.length == 0) {
      status.code = 500;
      status.redirect = true;
      return;
   }

   if (!code || code == null || code.length == 0) {
      status.code = 500;
      status.redirect = true;
      return;
   }
   
   var settings = getServerSettings(server);

   settings.properties["cp:text"] = text;
   settings.properties["cp:code"] = code;

   settings.save();
}

function findDictionary() {
   var result = search.luceneSearch('PATH:"/app:company_home/app:dictionary"');

   return result[0];
}

function getCustomPreferences() {
   var dictionary = findDictionary();

   var result = search.luceneSearch('PATH:"/app:company_home/app:dictionary/cm:custom_preferences"');

   if (result.length < 1) {
      var customPreferences = dictionary.createNode("custom_preferences", "cm:folder");

      return customPreferences;
   }

   return result[0];
}

function getServerSettings(server) {
   var customPreferences = getCustomPreferences();

   var result = search.luceneSearch('PATH:"/app:company_home/app:dictionary/cm:custom_preferences/cm:' + server + '"');

   if (result.length < 1) {
      var settings = customPreferences.createNode(server, "cm:content");

      settings.addAspect("cp:probe-settings");
      
      settings.properties["cm:name"] = server;

      return settings;
   }

   return result[0];
}

main();