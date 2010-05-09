[#ftl/]
[#import "/match.ftl" as matchX/]
[#import "/spring.ftl" as spring/]
<div class="description-box">
	<h3>Diễn biến</h3>
	<table class="fw">
		[@matchX.eventItem moment="8:00:00"]
			[@matchX.joinEvent competitors="mrcold,thinh_pro"?split(",")/]
		[/@matchX.eventItem]		
		[@matchX.eventItem moment="8:00:05"]
			[@matchX.joinEvent competitors="xuka,ken"?split(",")/]
		[/@matchX.eventItem]		
		[@matchX.eventItem moment="8:00:10"]
			Trận đấu đã bắt đầu
		[/@matchX.eventItem]		
		[@matchX.eventItem moment="8:00:20"]
			[@matchX.answerEvent competitor="thinh_pro" questions=[1,2,3]/]
		[/@matchX.eventItem]		
		[@matchX.eventItem moment="8:00:22"]
			[@matchX.answerEvent competitor="ken" questions=[1,2]/]
		[/@matchX.eventItem]		
		[@matchX.eventItem moment="8:00:25"]
			[@matchX.ignoreEvent competitor="ken" questions=[3]/]
		[/@matchX.eventItem]		
		[@matchX.eventItem moment="8:00:26"]
			[@matchX.answerEvent competitor="mrcold" questions=[1,2,3,4,5]/]
		[/@matchX.eventItem]		
		[@matchX.eventItem moment="8:00:28"]
			[@matchX.ignoreEvent competitor="mrcold" questions=[6]/]
		[/@matchX.eventItem]		
		[@matchX.eventItem moment="8:00:30"]
			Trận đấu đã kết thúc
		[/@matchX.eventItem]		
	</table>
</div>
<br/>
<div class="description-box">
	<h3>Kết quả</h3>
	<table>
	<tr>
		<th>#</th>
		<th>Thành viên</th>
		<th>Đúng (x 1.0)</th>
		<th>Sai (x -0.5)</th>
		<th>Bỏ qua (0)</th>
		<th>Tổng điểm</th>
	</tr>
	[#list match.competitors?sort_by("rank") as competitor]
	<tr>
		<td>${competitor.rank}</td>
		<td>${competitor.member.displayName}</td>
		<td>${competitor.correctCount}</td>
		<td>${competitor.wrongCount}</td>
		<td>${competitor.ignoreCount}</td>
		<td>${competitor.totalScore}</td>
	</tr>
	[/#list]
	</table>
</div>

[#macro question no content]
	<tr class="row-${no%2+1}"><td>${no}</td><td colspan="3"><a href="#">${content}</a></td></tr>
[/#macro]
[#macro icon type]
<span class="answer-${type}"></span>
[/#macro]

[#macro answer no author moment content]
	<tr class="row-${no%2+1}">
		<td></td>
		<td><a href="#">${author}</a> <span class="match-review-answer-time">${moment}</span>: <span class="match-review-answer-content">${content}</span> </td>
		<td class="answer-review-grade-column">[#nested/]</td>
		<td>
			<a href="#" class="flag-btn" title="Báo cáo câu trả lời có tính chất spam"></a>
		</td>
	</tr>
[/#macro]

[#macro correctAnswer no content]
	<tr class="row-${no%2+1}">
		<td></td>
		<td><em>Câu trả lời đúng</em>: <span class="match-review-answer-content">${content}</span> </td>
		<td class="answer-review-grade-column">[#nested/]</td>
		<td>
		</td>
	</tr>
[/#macro]

<div class="description-box">
	<h3>Câu hỏi</h3>
	<table class="fw">
		<tr>
			<th class="number-column"></th><th></th><th style="width: 50px;"></th><th class="answer-review-grade-column"></th>
		</tr>
		[@question no=1 content="What is the proper preposition"/]
		[@answer no=1 author="mrcold" moment="08:00:20" content="at"][@icon type="correct"/][/@answer]
		[@answer no=1 author="xuka" moment="08:00:23" content="on"][@icon type="wrong"/][/@answer]
		[@answer no=1 author="thinh_pro" moment="08:00:20" content="up"][@icon type="wrong"/][/@answer]
		[@answer no=1 author="thinh_pro" moment="08:00:20" content=""][@icon type="ignore"/][/@answer]
		[@correctAnswer no=1 content="at"][@icon type="correct"/][/@correctAnswer]

		[@question no=2 content="What is in the sound"/]
		[@answer no=2 author="mrcold" moment="08:00:20" content="Ridiculous"]0%[/@answer]
		[@answer no=2 author="xuka" moment="08:00:23" content="God damn it"]0%[/@answer]
		[@answer no=2 author="thinh_pro" moment="08:00:20" content="I don't even know"]0%[/@answer]
		[@answer no=2 author="thinh_pro" moment="08:00:20" content=""][@icon type="ignore"/][/@answer]
		[@correctAnswer no=2 content="God damn it"]100%[/@correctAnswer]
	</table>
</div>

<div class="description-box">
	<a href="[@spring.url "/matches/rejoin?leagueId=${match.leagueAutoId}"/]">Chơi trận khác</a>
</div>