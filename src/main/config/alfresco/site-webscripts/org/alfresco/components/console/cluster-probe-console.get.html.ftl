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
               <div class="cluster-settings-table">
                <div id="${el}-result" class="$cluster-settings-table-result"></div>
               </div>
              </div>
            </div>
	</div>
</div>
