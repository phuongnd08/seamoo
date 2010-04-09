[#ftl/]
[#import "/common.ftl" as common]
[#import "/spring.ftl" as spring]
[#function inverseMode mode][#if operatingMode=="normal"][#return "bootstrap"][#else][#return "normal"][/#if][/#function]
[#function modeToName mode][#if mode=="normal"][#return "Bình thường"][#else][#return "Khai trương"][/#if][/#function]
<div class="cbt"></div>
<br/>
<p>Site đang ở chế độ <strong>${modeToName(operatingMode)}</strong></p>
<form action='[@spring.url "/admin/update-site-settings"/]' method="POST">
<input type="hidden" name="operatingMode" value='${inverseMode(operatingMode)}'/>
<p><button type="submit">Chuyển sang chế độ <strong>${modeToName(inverseMode(operatingMode))}</strong></button></p>
</form>