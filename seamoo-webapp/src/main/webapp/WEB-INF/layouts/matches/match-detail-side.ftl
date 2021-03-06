[#ftl/]
[#import "/common.ftl" as common/]
[#import "/spring.ftl" as spring/]
[#assign urlFactory=statics["org.seamoo.webapp.UrlFactory"]/]

[#macro minuteAndSecond milliseconds]
${(milliseconds/1000/60)?string("0")}:${(milliseconds/1000 % 60)?string("00")}
[/#macro]
[#macro competitorInfo competitor]
	[#assign member=membersMap[competitor.memberAutoId?string]/]
	<div class="user-info">
		<table class="fw">
			<tr>
				<td class="user-gravatar32">
					[@common.avatar emailHash=member.emailHash size=32/]
				</td>
				<td class="user-details">
					<div class="competitor-rank[#if competitor.rank==1] competitor-rank-first[/#if]">${competitor.rank}</div>
					<div><a href="[@spring.url "/users/${member.autoId}/${member.alias}"/]">${member.displayName}</a></div>
					<div><span class="reputation-score">12k</span> ● <span class="user-level">10</span></div>
					<div><em>${(member.quote!"")?html}</em></div>
					<div>Tổng điểm <strong>${competitor.totalScore}</strong></div>
					<div>● Đúng <strong>${competitor.correctCount}</strong></div>
					<div>● Sai <strong>${competitor.wrongCount}</strong></div>
					<div>● Bỏ qua <strong>${competitor.ignoreCount}</strong></div>
					<div>● Thời gian <strong>[@minuteAndSecond competitor.finishedMoment-match.startedMoment/]</strong></div>
					<div>Tích luỹ <strong>+${competitor.additionalAccumulatedScore}</strong></div>
				</td>
			</tr>
		</table>
	</div>
[/#macro]

<div class="module">
	<h3>Trận đấu trong khuôn khổ</h3>
	<div class="description-box">
		<p>
			<img class="fl" width="96" height="64" src="[@spring.url league.logoUrl/]"/>
			<a href='${urlFactory.getSubjectViewUrl(subject)}'>${subject.name}</a> 
			&gt; <a href='${urlFactory.getLeagueViewUrl(league)}'>${league.name}</a>
		</p>
	</div>
	<h3>Giữa</h3>
	<div class="description-box">
		[#list match.competitors as competitor]
		[@competitorInfo competitor/]
		[/#list]
	</div>
</div>