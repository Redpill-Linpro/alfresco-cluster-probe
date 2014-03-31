<#include "../admin-template.ftl" />

<@page title=msg("clusterprobe.title") readonly=true>
   
   <div class="column-left">
      <@section label=msg("clusterprobe.ingress") />
      <#-- Example - Retrieve keys - which are attribute names - use to index into attribute hash -->
      <#-- You can index directly by attribute name e.g. <@control attribute=attributes["Subject"] /> -->
      <@attrfield attribute=attributes["hostname"] label=msg("clusterprobe.hostname") description=msg("clusterprobe.hostname.description") />

      <div class="control field">
         <span class="label">${msg("clusterprobe.url.label")}:</span>
         <span class="value">
            ${url.serviceContext}/org/redpill/alfresco/clusterprobe/probe
         </span>
         <span class="description">
            ${msg("clusterprobe.url.description")}
         </span>
      </div>
      
      <div class="control text">
         <span class="label">${msg("clusterprobe.http-response-text.label")}</span>
         <span class="value">
            <input id="http-response-text" name="http-response-text" />
         </span>
         <span class="description">
            ${msg("clusterprobe.http-response-text.description")}
         </span>
      </div>
      
      <div class="control text">
         <span class="label">${msg("clusterprobe.http-response-code.label")}</span>
         <span class="value">
            <input id="http-response-code" name="http-response-code" />
         </span>
         <span class="description">
            ${msg("clusterprobe.http-response-code.description")}
         </span>
      </div>
      
      <#--
      <@attrtext label=msg("inboundemail.inbound-email.anonymous-username") description=msg("inboundemail.inbound-email.anonymous-username.description") />
      -->
      
      <@button id="save-probe-settings-button" label=msg("clusterprobe.button.save") onclick="saveSettings();" />
   </div>
   
   <script type="text/javascript">
      var hostname = '${attributes["hostname"].value}';
      var now = new Date().getTime();
      
      Admin.request({
         url: '${url.serviceContext}/org/redpill/alfresco/clusterprobe/settings.json?server=' + hostname + '&refresh=' + now,
         fnSuccess: function(res) {
            document.getElementById('http-response-text').value = res.responseJSON.text;
            document.getElementById('http-response-code').value = res.responseJSON.code;
         }
      });
      
      function saveSettings() {
         Admin.request({
            method: 'POST',
            requestContentType: 'application/json',
            responseContentType: 'application/json',
            url: '${url.serviceContext}/org/redpill/alfresco/clusterprobe/settings.json',
            data: {
               'server': hostname,
               'text': document.getElementById('http-response-text').value,
               'code': document.getElementById('http-response-code').value
            },
            fnSuccess: function(res) {
               document.location = '${url.serviceContext}/enterprise/admin/admin-clusterprobe?m=clusterprobe.save.success';
            },
            fnError: function(res) {
               document.location = '${url.serviceContext}/enterprise/admin/admin-clusterprobe?e=clusterprobe.save.failure';
            }
         });
      
      };
   
   </script>
      
</@page>