[#ftl/]
[#--
	eventItem
	display an event of the match
--]
[#macro eventItem moment]
	<tr>
		<td class="time-column">${moment}</td>
		<td>[#nested/]</td>
	</tr>
[/#macro]

[#--
	joinEvent
	When competitors joins the match
--]
[#macro joinEvent competitors]
	[#list competitors as competitor]
		<a href="#">${competitor}</a>
		[#if competitor_has_next] &amp; [/#if]
	[/#list]
	tham gia trận đấu
[/#macro]

[#--
	answerEvent
	When competitor answer questions
--]
[#macro answerEvent competitor questions]
	<a href="#">${competitor}</a> đã trả lời câu hỏi số 
	[#list questions as question]
		${question}[#if question_has_next], [/#if]
	[/#list]
[/#macro]

[#--
	ignoreEvent
	When competitor ignore questions
--]
[#macro ignoreEvent competitor questions]
	<a href="#">${competitor}</a> đã bỏ qua câu hỏi số 
	[#list questions as question]
		${question}[#if question_has_next], [/#if]
	[/#list]
[/#macro]
