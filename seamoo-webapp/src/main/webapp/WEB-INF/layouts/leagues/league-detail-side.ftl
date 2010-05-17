[#ftl/]
[#import "/spring.ftl" as spring/]
[#import "/common.ftl" as common/]
[#assign timeProvider=statics["org.seamoo.utils.TimeProvider"].DEFAULT/] 
[#assign urlFactory=statics["org.seamoo.webapp.UrlFactory"]/]
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

[#--
rankItem
a macro for displaying a ranking item
--]
[#macro rankItem no member score matches]
	<tr><td>${no}</td><td><a href="/users/${member}">${member}</a></td><td class="tar">${score}</td><td class="tar">${matches}</td></tr>
[/#macro]
<div class="module">
	<h3>Bảng xếp hạng</h3>
	<div>
	<table class="fw grid">
	<tr><th>Hạng #</th><th>Thành viên</th><th>Điểm</th><th>Trận</th></tr>
	[@rankItem no="1" member="mrcold" score="3900" matches="1000"/]
	[@rankItem no="2" member="mica" score="3800" matches="1000"/]
	[@rankItem no="3" member="bond" score="3450" matches="1000"/]
	[@rankItem no="4" member="tanj" score="302" matches="100"/]
	[@rankItem no="5" member="kiniko" score="295" matches="100"/]
	[@rankItem no="6" member="mai_chee" score="290" matches="50"/]
	[@rankItem no="7" member="kenya" score="293" matches="50"/]
	[@rankItem no="8" member="char" score="290" matches="50"/]
	[@rankItem no="9" member="nany" score="252" matches="50"/]
	[@rankItem no="10" member="hen" score="230" matches="50"/]
	</table>
	</div>
</div>