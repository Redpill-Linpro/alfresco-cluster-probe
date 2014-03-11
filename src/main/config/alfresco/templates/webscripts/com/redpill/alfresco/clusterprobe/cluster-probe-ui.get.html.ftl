<html>
   <head>
      
      <style>
         .cluster-probe-console { margin: 16px 10px 10px; }
         .cluster-probe-console .title { font-size: 123.1%; font-weight: bold; padding-bottom: 0.5em; }
         .cluster-probe-console .save-buttonbar { padding-top: 0.5em; border-top: 1px solid #bbb; margin-top: 15px; }
         .cluster-probe-console .status { border: 2px solid red; background-color: #FFE0E0; display: inline; margin-left: 20px; display: none; padding: 2px; }
         .cluster-probe-console .header-bar { margin-top: 0.5em; margin-bottom: 0.5em; padding: 0.2em; height: 1.2em; background-color: #e3eaec; border-bottom: 2px solid #f0f3f4; font-weight: bold; font-size: 116%; }
         #http-response-text { width: 300px; }
         #http-response-code { width: 50px; }
         #http-response-text-label,#http-response-code-label  { width: 100px; display: inline; float: left; }
      </style>
      
      <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
      <script src="http://yui.yahooapis.com/2.9.0/build/yahoo/yahoo-min.js"></script>
      <script src="http://yui.yahooapis.com/2.9.0/build/json/json-min.js"></script>
      <script src="http://yui.yahooapis.com/2.9.0/build/event/event-min.js"></script>
      <script src="http://yui.yahooapis.com/2.9.0/build/connection/connection_core-min.js"></script>
      <script src="http://yui.yahooapis.com/2.9.0/build/connection/connection-min.js"></script>
      
      <script type="text/javascript">//<![CDATA[

      $(function() {
         
         function success() {
            $(".status").html("Saved settings");
            $(".status").css("display", "inline");
         }
         
         function error() {
            $(".status").html("Error while saving settings");
            $(".status").css("display", "inline");
         }
         
         function saveSettings() {
            var url = '${url.serviceContext}/com/redpill/alfresco/clusterprobe/settings.json';
            
            var callback = {
               success: success,
               error: error,
               scope: this
            };
            
            var data = {
               "server": "${probeHost}",
               "text": $('input#http-response-text').val(),
               "code": $('input#http-response-code').val()
            };
            
            var dataStr = YAHOO.lang.JSON.stringify(data || {});

            YAHOO.util.Connect.setDefaultPostHeader("application/json");
            YAHOO.util.Connect.setDefaultXhrHeader("application/json");
            YAHOO.util.Connect.initHeader("Content-Type", "application/json");
            
            YAHOO.util.Connect.asyncRequest ("POST", url, callback, dataStr);
         }
         
         $.getJSON('${url.serviceContext}/com/redpill/alfresco/clusterprobe/settings.json', { "server": "${probeHost}" }, function(data) {
            $("input#http-response-text").val(data.text);
            $("input#http-response-code").val(data.code);
         });
         
         $('#save-button').click(function() {
           saveSettings();
         });
                  
         $("#http-response-text,#http-response-code").keypress(function(event) {
           if (event.which != 13) {
              return true;
            }

            event.preventDefault();
            saveSettings();
         });
                  
      });
      
      //]]></script>
      
   </head>
   
   <body>
      <div id="body" class="cluster-probe-console">

      	<div id="main" class="hidden">
            <div>	
      	      <div class="header-bar">${msg("probe-text-response.label")}</div>
               <div>
                  <div id="http-response-text-label">${msg("http-response-text.label")}</div>
                  <div>
                     <input id="http-response-text" type="text" value="" name="http-response-text" />
                  </div>
               </div>
            </div>
      
            <div>
               <div class="header-bar">${msg("probe-http-response.label")}</div>
               <div>
                  <div id="http-response-code-label">${msg("http-response-code.label")}</div>
                  <div>
                     <input id="http-response-code" type="text" value="" name="http-response-code" />
                  </div>
               </div>
            </div>

            <div class="save-buttonbar">
               <button type="button" name="save-button" id="save-button">${msg("button.save")}</button>
               <div class="status"></div>
            </div>
      	</div>
      </div>

   </body>


</html>