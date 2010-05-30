[#ftl/]
[#import "/match.ftl" as matchX/]
[#import "/spring.ftl" as spring/]
[#if rejoin]
<div class="description-box">
	<a href="[@spring.url "/matches/rejoin?leagueId=${match.leagueAutoId}"/]">Chơi trận khác</a>
</div>
[/#if]

[#macro questionItem no content]
	<tr class="row-${no%2+1}"><td>${no}</td><td colspan="3"><u>${content}</u></td></tr>
[/#macro]
[#macro icon type]
<span class="answer-${type}" title="${type}"></span>
[/#macro]

[#macro answerItem no author moment content]
	<tr class="row-${no%2+1}">
		<td></td>
		<td><a href="#">${author}</a> <span class="match-review-answer-time">${moment}</span>: <span class="match-review-answer-content">${content}</span> </td>
		<td class="answer-review-grade-column">[#nested/]</td>
		<td>
			[#--<a href="#" class="flag-btn" title="Báo cáo câu trả lời có tính chất spam"></a>--]
		</td>
	</tr>
[/#macro]

[#macro correctAnswerItem no content]
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
		[#assign count=0/]
		[#list match.questionIds as questionId]
			[#assign question=questionsMap[questionId?string]/]
			[#assign count=count+1/]
			[@questionItem no=count content="${question.currentRevision.content}"/]
			[#list match.competitors as competitor]
				[#if (competitor.answers?exists && competitor.answers?size >= count)]
					[#assign answer=competitor.answers[count-1]/]
					[#if answer.submittedTime?exists]
						[#assign displayTime="${answer.submittedTime?string('HH:mm:ss')} (UTC)"/]
					[#else]
						[#assign displayTime=""]
					[/#if]
					[#assign member = membersMap[competitor.memberAutoId?string]/]
					[#if (answer.type==enums["org.seamoo.entities.matching.MatchAnswerType"].SUBMITTED)]
						[@answerItem no=count author=member.displayName moment="${displayTime}" content=question.currentRevision.getTranslatedAnswer(answer.content)]
							[#if answer.correct]
								[@icon type="correct"/]
							[#else]
								[@icon type="wrong"/]
							[/#if]
						[/@answerItem]
					[#else]
						[@answerItem no=count author=member.displayName moment="${displayTime}" content=""]
							[@icon type="ignore"/]
						[/@answerItem]
					
					[/#if]
				[/#if]
			[/#list]
			[@correctAnswerItem no=count content="${question.currentRevision.correctAnswer}"][@icon type="correct"/][/@correctAnswerItem]
		[/#list]
	</table>
</div>