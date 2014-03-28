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
(function() {
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
   RL.ClusterProbeConsole = function(htmlId) {
      this.name = "RL.ClusterProbeConsole";
      RL.ClusterProbeConsole.superclass.constructor.call(this, htmlId);

      /* Register this component */
      Alfresco.util.ComponentManager.register(this);

      /* Load YUI Components */
      Alfresco.util.YUILoaderHelper.require([ "button", "container", "datasource", "datatable", "paginator", "json", "history" ], this.onComponentsLoaded, this);

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
         onLoad : function onLoad() {
            parent.widgets.textField = Dom.get(parent.id + "-http-response-text");
            parent.widgets.codeField = Dom.get(parent.id + "-http-response-code");

            // Buttons
            parent.widgets.saveButton = Alfresco.util.createYUIButton(parent, "save-button", parent.onSaveClick);

            new YAHOO.util.KeyListener(parent.widgets.textField, {
               keys : YAHOO.util.KeyListener.KEY.ENTER
            }, {
               fn : function() {
                  parent.onSaveClick();
               },
               scope : this,
               correctScope : true
            }, "keydown").enable();

            new YAHOO.util.KeyListener(parent.widgets.codeField, {
               keys : YAHOO.util.KeyListener.KEY.ENTER
            }, {
               fn : function() {
                  parent.onSaveClick();
               },
               scope : this,
               correctScope : true
            }, "keydown").enable();
         }
      });

      new ListPanelHandler();

      return this;
   };

   YAHOO.extend(RL.ClusterProbeConsole, Alfresco.ConsoleTool, {

      loadProbeSettings : function ACJC_loadProbeSettings(settings) {
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
      onReady : function ACJC_onReady() {
         // Call super-class onReady() method
         RL.ClusterProbeConsole.superclass.onReady.call(this);
         var self = this;
         var server = this.options.server;

         // Load Scripts from Repository
         Alfresco.util.Ajax.request({
            url : Alfresco.constants.PROXY_URI + "org/redpill/alfresco/clusterprobe/settings.json",
            method : Alfresco.util.Ajax.GET,
            dataObj : {
               "server" : server
            },
            requestContentType : Alfresco.util.Ajax.JSON,
            successCallback : {
               fn : function(res) {
                  var settings = res.json;
                  this.loadProbeSettings(settings);
               },
               scope : this
            }
         });
      },

      /**
       * Fired when the user clicks on the Save button.
       * 
       * @method onSaveClick
       */
      onSaveClick : function(e, p_obj) {
         var self = this;

         Alfresco.util.Ajax.jsonRequest({
            url : Alfresco.constants.PROXY_URI + "org/redpill/alfresco/clusterprobe/settings.json",
            method : Alfresco.util.Ajax.POST,
            dataObj : {
               "server" : self.options.server,
               "text" : self.widgets.textField.value,
               "code" : self.widgets.codeField.value
            },
            successCallback : {
               fn : function(res) {
                  Alfresco.util.PopupManager.displayMessage({
                     text : self.msg("save.success")
                  });
               },
               scope : this
            },
            failureCallback : {
               fn : function() {
                  Alfresco.util.PopupManager.displayMessage({
                     text : self.msg("save.failure")
                  });
               },
               scope : this
            }
         });
      }

   });
})();
