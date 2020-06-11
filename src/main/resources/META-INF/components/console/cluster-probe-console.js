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

            parent.widgets.dataSource = new YAHOO.util.DataSource(Alfresco.constants.PROXY_URI + "org/redpill/alfresco/clusterprobe/settings.json", {
               responseType: YAHOO.util.DataSource.TYPE_JSON,
               responseSchema: {
                  resultsList: "result"
               }
            });

            this._setupDataTable();
         },

         _setupDataTable: function () {

            var renderCellCheckbox = function (cell, record, column, data) {
               var active = data == "true";
               var checked = active ? "checked" : "";
               var serverName = record._oData.serverName;
               var type = column.field;
               var id = type + "-" + serverName;
               cell.innerHTML = "<input id='" + id + "'type=" + '"checkbox"'  + checked + '/>';
               console.log(cell);
            };


            var renderCellInnerHTML = function (cell, record, column, data) {
               cell.innerHTML = $html(data);
            };

            var columnDefinitions = [{
               key: "serverName",
               label: "Hostname",
               sortable: false,
               formatter: renderCellInnerHTML
            },{
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

         Alfresco.util.Ajax.jsonRequest({
            url: Alfresco.constants.PROXY_URI + "org/redpill/alfresco/clusterprobe/settings.json",
            method: Alfresco.util.Ajax.POST,
            dataObj: {
               "server": self.options.server,
               "text": self.widgets.textField.value,
               "code": self.widgets.codeField.value
            },
            successCallback: {
               fn: function (res) {
                  Alfresco.util.PopupManager.displayMessage({
                     text: self.msg("save.success")
                  });
               },
               scope: this
            },
            failureCallback: {
               fn: function () {
                  Alfresco.util.PopupManager.displayMessage({
                     text: self.msg("save.failure")
                  });
               },
               scope: this
            }
         });
      }

   });
})();
