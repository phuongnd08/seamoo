[#ftl/]
[#import "/spring.ftl" as spring]
[#macro leagueControl link title img joinable]
<div class="description-box">
<table class="fw">
<tr>
	<td class="logo-cell">
		<a href="${link}">
			<img src="[@spring.url img/]" width="96" height="64"/>
		</a>
	</td>
	<td>
		<div><a href="${link}">${title}</a></div>
		<div>[#nested/]</div>
		<div>
		[#if joinable]
			<button>Tham gia</button>
		[#else]
			<em>Bạn chưa được quyền tham giải đấu này</em>
		[/#if]
		</div>
	</td>
</tr>
</table>
</div>
[/#macro]
[#list leagues as league]
[@leagueControl link="/leagues/${league.autoId}/${league.alias}" title="${league.name}" img="${league.logoUrl}" joinable=true]
${league.description}. Bạn đang có <strong>40</strong> điểm. 
Bạn còn <strong>20</strong> ngày để tích lũy đủ <strong>300</strong> điểm để duy trì quyền tham gia <strong>Giải gà con</strong>
[/@leagueControl]
[/#list]