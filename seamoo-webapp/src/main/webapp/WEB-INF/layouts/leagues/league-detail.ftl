[#ftl/]
[#import "/spring.ftl" as spring]
[#import "/common.ftl" as common]
[#assign urlFactory=statics["org.seamoo.webapp.UrlFactory"]/]
[#--
matchItem
display a row containing brief information of a match
--]
[#macro matchItem match]
<tr>
<td>
[#assign count=0/]
[#list match.competitors as competitor]
	[#assign count=count+1/]
	<a href="${urlFactory.getUserViewUrl(competitor.member)}">${competitor.member.displayName}</a> 
	([@compress single_line=true]
		[#switch competitor.rank]
		[#case 1] 1st
		[#break/]
		[#case 2] 2nd
		[#break/]
		[#case 3] 3rd
		[#break/]
		[#case 4] 4th
		[#break/] 
	[/#switch][/@compress])
	[#if competitor_has_next] vs [/#if]
[/#list]
</td>
<td><a href="${urlFactory.getMatchViewUrl(match)}">Xem</a></td>
</tr>
[/#macro]

<div>
	[#if member?exists]
		[#if joinable]
			<button class="big-button" type="button" onclick="window.location.replace('${urlFactory.getParticipateUrl(league)}')">Tham gia &gt;&gt;</button>
		[#else]
			Bạn chưa được phép tham gia giải đấu này. Vui lòng bắt đầu ở giải đấu thấp hơn và tích luỹ điểm số để được thăng hạng
		[/#if]
	[#else]
		Vui lòng <a href="${urlFactory.getLogInUrl(urlFactory.getLeagueViewUrl(league))}">đăng nhập</a> để có thể tham gia giải đấu
	[/#if]
</div>

<br/>

[#--
rankItem
a macro for displaying a ranking item
--]
[#macro rankItem rank rankIndex]
	<tr>
		[#assign member=memberMap[rank.memberAutoId?c]/]
		<td>${rankIndex}</td>
		<td><a href="${urlFactory.getUserViewUrl(member)}">${member.displayName}</a></td><td class="tar">${rank.accumulatedScore}</td><td class="tar">${rank.matchCount}</td></tr>
[/#macro]
<div class="description-box">
	<h3>Bảng xếp hạng</h3>
	<div>
	<table class="fw grid">
	<tr><th>Hạng#</th><th>Thành viên</th><th>Điểm</th><th>Trận</th></tr>
	[#list ranks as rank]
		[@rankItem rank=rank rankIndex=rankFrom/]
		[#assign rankFrom=rankFrom+1/]
	[/#list]
	</table>
	</div>
	[@common.pagingControl min=1 max=rankPageCount current=rankPage;page]
		[@spring.url "${urlFactory.getPagedLeagueViewUrl(league, page, matchPage)}"/]
	[/@common.pagingControl]
	<div class="cbt"></div>
</div>

<div class="description-box">
	<h3>Các trận đấu gần đây</h3>
	<br/>
	<div>
	<table class="fw grid">
	[#list matches as match]
	[@matchItem match/]
	[/#list]
	</table>
	</div>
	[@common.pagingControl min=1 max=matchPageCount current=matchPage;page]
		[@spring.url "${urlFactory.getPagedLeagueViewUrl(league, rankPage, page)}"/]
	[/@common.pagingControl]
	<div class="cbt"></div>
</div>