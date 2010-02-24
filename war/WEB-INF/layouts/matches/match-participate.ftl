[#ftl/]
[#import "/match.ftl" as match/]
<div class="description-box">
	<h3>Diễn biến</h3>
	<table class="fw">
		[@match.eventItem moment="8:00:00"]
			<a href="#">mrcold</a> đang chờ
		[/@match.eventItem]		
	</table>
</div>