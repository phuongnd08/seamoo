[#ftl/]
[#import "/spring.ftl" as spring]
[#assign urlFactory=statics["org.seamoo.webapp.UrlFactory"]/]
[#macro leagueControl league]
	[#assign link="/leagues/${league.autoId}/${league.alias}"/]
<div class="description-box">
<table class="fw">
<tr>
	<td class="logo-cell">
		<a href="${link}">
			<img src="[@spring.url league.logoUrl/]" width="96" height="64"/>
		</a>
	</td>
	<td>
		<div><a href="${link}">${league.name}</a></div>
		<div>${league.description}</div>
		<div>
		[#if member?exists]
			[#if joinable[league.autoId?string]]
				[#if memberships[league.autoId?string]?exists]
					[#assign ms=memberships[league.autoId?string]/]
					<div>
						Bạn hiện có <strong>${ms.accumulatedScore}</strong> điểm ở giải đấu này.   
					</div>
				[/#if]
				<button type="button" onclick="window.location.replace('${urlFactory.getParticipateUrl(league)}')">Tham gia</button>
			[#else]
				<em>Bạn không được tham giải đấu này</em>
			[/#if]
		[#else]
			Vui lòng <a href="${urlFactory.getLogInUrl(urlFactory.getSubjectViewUrl(subject))}">đăng nhập</a> để tham gia 
		[/#if]
		</div>
	</td>
</tr>
</table>
</div>
[/#macro]
[#list leagues as league]
[@leagueControl league]
[/@leagueControl]
[/#list]