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
             <div class="header-bar">Cluster probe</div>
                <div class="control field settings-info">
                    <b class="label">${msg("probe.settings.desc")}</b>
                    <div class="settings-info-div">
                        <span class="value">${msg("probe.online.code")}</span>
                        <span id="httpOnlineCode" class="value"></span>
                    </div>
                    <div class="settings-info-div">
                        <span class="value">${msg("probe.offline.code")}</span>
                        <span id="httpOfflineCode" class="value"></span>
                    </div>
                    <div class="settings-info-div">
                        <span class="value">${msg("probe.online.text")}</span>
                        <span id="httpOnlineText" class="value"></span>
                    </div>
                    <div class="settings-info-div">
                        <span class="value">${msg("probe.offline.text")}</span>
                        <span id="httpOfflineText" class="value"></span>
                    </div>
                </div>
               <div class="cluster-settings-table">
                <div id="${el}-result" class="$cluster-settings-table-result"></div>
               </div>
              </div>
            </div>
	</div>
</div>
