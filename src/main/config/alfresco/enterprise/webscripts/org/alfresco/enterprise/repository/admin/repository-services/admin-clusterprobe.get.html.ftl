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
        <div class="control field" style="display:grid;">
            <span class="label">Settings:</span>
            <span id="httpOnlineCode" class="value">Online HTTP-code: </span>
            <span id="httpOfflineCode" class="value">Offline HTTP-code: </span>
            <span id="httpOnlineText" class="value">Online text: </span>
            <span id="httpOfflineText" class="value">Offline text: </span>
        </div>

            <div>
                <table id="cluster-settings-table" >
                    <tr class="headerRow">
                        <th class="headerCell">${msg("clusterprobe.settings.hostname")}</th>
                        <th class="headerCell">${msg("clusterprobe.settings.desc")}</th>
                        <th class="headerCell">${msg("clusterprobe.settings.repo")}</th>
                        <th class="headerCell">${msg("clusterprobe.settings.share")}</th>
                        <th class="headerCell">${msg("clusterprobe.settings.search")}</th>
                    </tr>
                </table>
            </div>

   </div>

   <style>
   #cluster-settings-table {
       border: 2px solid;
   }

   #cluster-settings-table td {
       width: 20%;
       padding-right: 5px;
       padding-top: 5px;
       padding-bottom: 5px;
   }

   #cluster-settings-table .serverRow:nth-child(odd){
     background: #cccccc36;
   }
   #cluster-settings-table .serverRow:nth-child(even){
     background: #FFF;
   }

   #cluster-settings-table tr{
       border: 1px solid;
   }

   .textCell{
       padding-left: 10px;
   }

   .headerCell{
        text-align: center !important;
   }
   .inputCell{
        text-align: center !important;
   }
   .headerRow{
        background: lightgray;
   }
   </style>
   <script type="text/javascript">
 var hostname = '${attributes["hostname"].value}';
 var now = new Date().getTime();

 Admin.request({
     url: '${url.serviceContext}/org/redpill/alfresco/clusterprobe/settings.json?server=' + hostname + '&refresh=' + now,
     fnSuccess: function (res) {

         var servers = res.responseJSON.result;

         document.getElementById("httpOnlineCode").innerText += res.responseJSON.httpOnline;
         document.getElementById("httpOfflineCode").innerText += res.responseJSON.httpOffline;
         document.getElementById("httpOnlineText").innerText += res.responseJSON.onlineText;
         document.getElementById("httpOfflineText").innerText += res.responseJSON.offlineText;

         var table = document.getElementById('cluster-settings-table');
         for (var i = 0; i < servers.length; i++) {

             var server = servers[i];


             var row = document.createElement("tr");
             row.classList.add("serverRow");

             var nameCell = document.createElement("td");
             nameCell.innerText = server.serverName;
             nameCell.classList.add("textCell");

             var descCell = document.createElement("td");
             descCell.innerText = server.description;
             descCell.classList.add("textCell");

             row.appendChild(nameCell);
             row.appendChild(descCell);
             row.appendChild(createTableInputCell(server.repo, server.serverName, "repo"));
             row.appendChild(createTableInputCell(server.share, server.serverName, "share"));
             row.appendChild(createTableInputCell(server.search, server.serverName, "search"));

             table.appendChild(row);

         }

     }
 });

 function createTableInputCell(active, server, type) {

     var tableCell = document.createElement("td");
     var cellInput = document.createElement("input");
     cellInput.checked = active;
     cellInput.disabled = active == null;
     cellInput.id = type + "-" + server;
     cellInput.type = "checkbox";
     cellInput.addEventListener("click", checkBoxChangeFunction);

     var link = document.createElement("a");
     link.href =  "${url.serviceContext}/org/redpill/alfresco/clusterprobe/probe/" + type + "/" + server;
     link.innerText = "TEST";
     link.target = "_blank";
     link.classList.add("test-link");
     link.hidden = active == null;

     tableCell.appendChild(link);
     tableCell.appendChild(cellInput);
     tableCell.classList.add("inputCell");


     return tableCell;

 };
 function checkBoxChangeFunction(event) {
     var idSplit = event.target.id.split("-");
     var type = idSplit[0];
     var server = idSplit[1];
     var value = event.target.checked;
     saveSettings(value, server, type);
 };

 function saveSettings(value, server, type) {
     Admin.request({
         method: 'POST',
         requestContentType: 'application/json',
         responseContentType: 'application/json',
         url: '${url.serviceContext}/org/redpill/alfresco/clusterprobe/settings.json',
         data: {
             'server': server,
             'type': type,
             'value': value
         },
         fnSuccess: function (res) {
             document.location = '${url.serviceContext}/enterprise/admin/admin-clusterprobe?m=clusterprobe.save.success';
         },
         fnError: function (res) {
             document.location = '${url.serviceContext}/enterprise/admin/admin-clusterprobe?e=clusterprobe.save.failure';
         }
     });

 };

   </script>
      
</@page>