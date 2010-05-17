[#ftl/]
[#import "/spring.ftl" as spring/]
[#import "/common.ftl" as common/]
[#assign timeProvider=statics["org.seamoo.utils.TimeProvider"].DEFAULT/] 
[#assign urlFactory=statics["org.seamoo.webapp.UrlFactory"]/]
[#assign pager=statics["org.seamoo.webapp.Pager"]/]
<div class="module">
	<div class="description-box">
		<p>
			<img class="fl" width="96" height="64" src="[@spring.url league.logoUrl/]"/>
			<a href='${urlFactory.getSubjectViewUrl(subject)}'>${subject.name}</a> 
			&gt; <strong>${league.name}</strong>
		</p>
		<p>${league.description}</p>
	</div>
	[#if member?exists && joinable]
		[#assign score=0]
		[#if membership?exists]
			[#assign score=membership.accumulatedScore]
		[/#if]
		<div class="description-box">
			<p>Bạn hiện có <strong>${score}</strong> điểm. 
			[#if (score<100)]
				Bạn còn <strong>${timeProvider.getDaysTillEndOfMonth()}</strong> ngày để tích luỹ đủ <strong>100</strong> điểm 
				để trụ hạng[#if nextLeague?exists] hoặc <strong>300</strong> điểm để thăng hạng lên <a href="${urlFactory.getLeagueViewUrl(nextLeague)}">${nextLeague.name}</a>[/#if].
			[#elseif (nextLeague?exists)]
				[#if (score<300)]
					Bạn đã đủ điểm trụ hạng. [#if nextLeague?exists] Bạn còn <strong>${timeProvider.getDaysTillEndOfMonth()}</strong> ngày để tích luỹ đủ 300 điểm để thăng hạng lên <a href="${urlFactory.getLeagueViewUrl(nextLeague)}">${nextLeague.name}</a>[/#if].
				[#else]
					Bạn đã được suất thăng hạng. Bạn sẽ được thi đấu ở <a href="${urlFactory.getLeagueViewUrl(nextLeague)}">${nextLeague.name}</a> từ đầu tháng sau.
				[/#if]
			[#else]
				Bạn đã trụ hạng.
			[/#if] 
			</p>
		</div>
	[/#if]
</div>
