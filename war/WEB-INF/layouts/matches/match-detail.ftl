[#ftl/]
[#import "/match.ftl" as match/]
<div class="description-box">
	<h3>Diễn biến</h3>
	<table class="fw">
		[@match.eventItem moment="8:00:00"]
			[@match.joinEvent competitors="mrcold,thinh_pro"?split(",")/]
		[/@match.eventItem]		
		[@match.eventItem moment="8:00:05"]
			[@match.joinEvent competitors="xuka,ken"?split(",")/]
		[/@match.eventItem]		
		[@match.eventItem moment="8:00:10"]
			Trận đấu đã bắt đầu
		[/@match.eventItem]		
		[@match.eventItem moment="8:00:20"]
			[@match.answerEvent competitor="thinh_pro" questions=[1,2,3]/]
		[/@match.eventItem]		
		[@match.eventItem moment="8:00:22"]
			[@match.answerEvent competitor="ken" questions=[1,2]/]
		[/@match.eventItem]		
		[@match.eventItem moment="8:00:25"]
			[@match.ignoreEvent competitor="ken" questions=[3]/]
		[/@match.eventItem]		
		[@match.eventItem moment="8:00:26"]
			[@match.answerEvent competitor="mrcold" questions=[1,2,3,4,5]/]
		[/@match.eventItem]		
		[@match.eventItem moment="8:00:28"]
			[@match.ignoreEvent competitor="mrcold" questions=[6]/]
		[/@match.eventItem]		
		[@match.eventItem moment="8:00:30"]
			Trận đấu đã kết thúc
		[/@match.eventItem]		
	</table>
</div>
<br/>
<div class="description-box">
	<h3>Kết quả</h3>
	<ol>
		<li>mrcold</li>
		<li>thinh_pro</li>
		<li>xuka</li>
		<li>ken</li>
	</ol>
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