<#assign el=args.htmlid?html>
<script type="text/javascript">//<![CDATA[
   new RL.ClusterProbeConsole("${el}").setOptions({
      server: '${server}'
   }).setMessages(
      ${messages}
   );
//]]></script>

<div id="${el}-body" class="cluster-probe-console">

	<div id="${el}-main" class="hidden">
      <div>	
	      <div class="header-bar">${msg("probe-text-response.label")}</div>
         <div>
            <span>${msg("http-response-text.label")}</span>
            <input id="${el}-http-response-text" type="text" value="" name="http-response-text" />
         </div>
      </div>
      
      <div>
         <div class="header-bar">${msg("probe-http-response.label")}</div>
         <div>
            <span>${msg("http-response-code.label")}</span>
            <input id="${el}-http-response-code" type="text" value="" name="http-response-code" />
         </div>
      </div>

      <div class="save-buttonbar">
         <button type="button" name="${el}-save-button" id="${el}-save-button">${msg("button.save")}</button>
      </div>
	</div>
</div>
