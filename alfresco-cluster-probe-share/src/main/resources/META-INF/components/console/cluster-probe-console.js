/**
 * RL root namespace.
 * 
 * @namespace RL
 */
// Ensure RL root object exists
if (typeof RL == "undefined" || !RL) {
   var RL = {};
}

/**
 * Admin Console Cluster Probe Console
 * 
 * @namespace Alfresco
 * @class RL.ClusterProbeConsole
 */
(function () {
   /**
    * YUI Library aliases
    */
   var Dom = YAHOO.util.Dom, Event = YAHOO.util.Event, Element = YAHOO.util.Element;

   /**
    * Alfresco Slingshot aliases
    */
   var $html = Alfresco.util.encodeHTML, $hasEventInterest = Alfresco.util.hasEventInterest;

   /**
    * ClusterProbeConsole constructor.
    * 
    * @param {String}
    *           htmlId The HTML id of the parent element
    * @return {RL.ClusterProbeConsole} The new ClusterProbeConsole instance
    * @constructor
    */
   RL.ClusterProbeConsole = function (htmlId) {
      this.name = "RL.ClusterProbeConsole";
      RL.ClusterProbeConsole.superclass.constructor.call(this, htmlId);

      /* Register this component */
      Alfresco.util.ComponentManager.register(this);

      /* Load YUI Components */
      Alfresco.util.YUILoaderHelper.require(["button", "container", "datasource", "datatable", "paginator", "json", "history"], this.onComponentsLoaded, this);

      /* Define panel handlers */
      var parent = this;

      /* File List Panel Handler */
      ListPanelHandler = function ListPanelHandler_constructor() {
         ListPanelHandler.superclass.constructor.call(this, "main");
      };

      YAHOO.extend(ListPanelHandler, Alfresco.ConsolePanelHandler, {
         /**
          * Called by the ConsolePanelHandler when this panel shall be loaded
          * 
          * @method onLoad
          */
         onLoad: function onLoad() {

            this._setupConfigText();
            parent.widgets.dataSource = new YAHOO.util.DataSource(Alfresco.constants.PROXY_URI + "org/redpill/alfresco/clusterprobe/settings.json", {
               responseType: YAHOO.util.DataSource.TYPE_JSON,
               responseSchema: {
                  resultsList: "result"
               }
            });

            this._setupDataTable();

         },
         _setupConfigText: function () {
            Alfresco.util.Ajax.jsonRequest({
               url: Alfresco.constants.PROXY_URI + "org/redpill/alfresco/clusterprobe/settings.json",
               method: Alfresco.util.Ajax.GET,
               successCallback: {
                  fn: function (res) {
                     document.getElementById("httpOnlineCode").innerText += " " + res.json.httpOnline;
                     document.getElementById("httpOfflineCode").innerText += " " + res.json.httpOffline;
                     document.getElementById("httpOnlineText").innerText += " " + res.json.onlineText;
                     document.getElementById("httpOfflineText").innerText += " " + res.json.offlineText;
                  },
                  scope: this
               },
               failureCallback: {
                  fn: function () {
                  },
                  scope: this
               }
            });
         },
         _setupDataTable: function () {

            var renderCellCheckbox = function (cell, record, column, data) {

               var serverName = record._oData.serverName;
               var type = column.field;
               var id = type + "-" + serverName;
               var cellInput = document.createElement("input");
               cellInput.type = "checkbox";
               cellInput.id=id;
               cellInput.checked = data ? "checked" : "";
               cellInput.disabled = data == null ? "disabled" : "";
               cellInput.addEventListener("change", parent.onSaveClick);

               var link = document.createElement("a");
               link.href = Alfresco.constants.PROXY_URI + "org/redpill/alfresco/clusterprobe/probe/" + type + "/" + serverName;
               link.innerText = "TEST";
               link.target = "_blank";
               link.classList.add("test-link");
               link.hidden = data == null ? "disabled" : "";

               cell.appendChild(link);
               cell.appendChild(cellInput);

            };


            var renderCellInnerHTML = function (cell, record, column, data) {
               cell.innerHTML = $html(data);
            };

            var columnDefinitions = [{
               key: "serverName",
               label: "Hostname",
               sortable: false,
               formatter: renderCellInnerHTML
            }, {
               key: "description",
               label: "Description",
               sortable: false,
               formatter: renderCellInnerHTML
            },
            {
               key: "repo",
               label: "Repo",
               sortable: false,
               formatter: renderCellCheckbox
            }, {
               key: "share",
               label: "Share",
               sortable: false,
               formatter: renderCellCheckbox
            }, {
               key: "search",
               label: "Search",
               sortable: false,
               formatter: renderCellCheckbox
            }
            ];

            // Customize request sent to server to be able to set total # of records
            var generateRequest = function (oState, oSelf) {
               // Get states or use defaults
               oState = oState || {
                  pagination: null,
                  sortedBy: null
               };
               // Initial request when load the page
               return "";
            };

            parent.widgets.dataTable = new YAHOO.widget.DataTable(parent.id + "-result", columnDefinitions, parent.widgets.dataSource, {
               MSG_EMPTY: parent.msg("message.empty"),
               dynamicData: true,
               generateRequest: generateRequest,
               initialRequest: generateRequest()
            });

            parent.widgets.dataTable.doBeforeLoadData = function (request, response, payload) {
               return payload;
            }

         }
      });

      new ListPanelHandler();

      return this;
   };

   YAHOO.extend(RL.ClusterProbeConsole, Alfresco.ConsoleTool, {

      loadProbeSettings: function ACJC_loadProbeSettings(settings) {
         this.widgets.textField.value = settings.text;
         this.widgets.codeField.value = settings.code;
      },

      /**
       * Fired by YUI when parent element is available for scripting. Component
       * initialisation, including instantiation of YUI widgets and event
       * listener binding.
       * 
       * @method onReady
       */
      onReady: function ACJC_onReady() {
         // Call super-class onReady() method
         RL.ClusterProbeConsole.superclass.onReady.call(this);
      },

      /**
       * Fired when the user clicks on the Save button.
       * 
       * @method onSaveClick
       */
      onSaveClick: function (e, p_obj) {
         var self = this;
         var idSplit = event.target.id.split("-");
         var type = idSplit[0];
         var server = idSplit[1];
         var value = event.target.checked;
         Alfresco.util.Ajax.jsonRequest({
            url: Alfresco.constants.PROXY_URI + "org/redpill/alfresco/clusterprobe/settings.json",
            method: Alfresco.util.Ajax.POST,
            dataObj: {
               'server': server,
               'type': type,
               'value': value
            },
            successCallback: {
               fn: function (res) {
                  window.location.reload();
               },
               scope: this
            },
            failureCallback: {
               fn: function () {
                  window.location.reload();
               },
               scope: this
            }
         });
      }

   });
})();
