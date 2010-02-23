[#ftl/]
[#macro eventItem moment]
	<tr>
		<td class="time-column">${moment}</td>
		<td>[#nested/]</td>
	</tr>
[/#macro]

[#macro joinEvent competitors]
	[#list competitors as competitor]
		<a href="#">${competitor}</a>
		[#if competitor_has_next] &amp; [/#if]
	[/#list]
	tham gia trận đấu
[/#macro]
[#macro answerEvent competitor questions]
	<a href="#">${competitor}</a> đã trả lời câu hỏi số 
	[#list questions as question]
		${question}[#if question_has_next], [/#if]
	[/#list]
[/#macro]

[#macro ignoreEvent competitor questions]
	<a href="#">${competitor}</a> đã bỏ qua câu hỏi số 
	[#list questions as question]
		${question}[#if question_has_next], [/#if]
	[/#list]
[/#macro]


<div class="description-box">
	<h3>Diễn biến</h3>
	<table class="fw">
		[@eventItem moment="8:00:00"]
			[@joinEvent competitors="mrcold,thinh_pro"?split(",")/]
		[/@eventItem]		
		[@eventItem moment="8:00:05"]
			[@joinEvent competitors="xuka,ken"?split(",")/]
		[/@eventItem]		
		[@eventItem moment="8:00:10"]
			Trận đấu đã bắt đầu
		[/@eventItem]		
		[@eventItem moment="8:00:20"]
			[@answerEvent competitor="thinh_pro" questions=[1,2,3]/]
		[/@eventItem]		
		[@eventItem moment="8:00:22"]
			[@answerEvent competitor="ken" questions=[1,2]/]
		[/@eventItem]		
		[@eventItem moment="8:00:25"]
			[@ignoreEvent competitor="ken" questions=[3]/]
		[/@eventItem]		
		[@eventItem moment="8:00:26"]
			[@answerEvent competitor="mrcold" questions=[1,2,3,4,5]/]
		[/@eventItem]		
		[@eventItem moment="8:00:28"]
			[@ignoreEvent competitor="mrcold" questions=[6]/]
		[/@eventItem]		
		[@eventItem moment="8:00:30"]
			Trận đấu đã kết thúc
		[/@eventItem]		
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
	<tr class="match-review-question-row-${no%2+1}"><td>${no}</td><td colspan="3"><a href="#">${content}</a></td></tr>
[/#macro]
[#macro icon type]
<span class="answer-${type}"></span>
[/#macro]

[#macro answer no author moment content]
	<tr class="match-review-question-row-${no%2+1}">
		<td></td>
		<td><a href="#">${author}</a> <span class="match-review-answer-time">${moment}</span>: <span class="match-review-answer-content">${content}</span> </td>
		<td class="answer-review-grade-column">[#nested/]</td>
		<td>
			<a href="#" class="answer-flag-btn" title="Báo cáo câu trả lời có tính chất spam"></a>
		</td>
	</tr>
[/#macro]

[#macro correctAnswer no content]
	<tr class="match-review-question-row-${no%2+1}">
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