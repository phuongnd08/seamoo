[#ftl/]
[#import "/spring.ftl" as spring/]
[#import "/common.ftl" as common/]
<div class="module">
	<div class="description-box">
		<p>
			<img class="fl" width="96" height="64" src="[@spring.url league.logoUrl/]"/>
			<a href='${statics["org.seamoo.webapp.client.shared.UrlFactory"].getSubjectViewUrl(subject)}'>${subject.name}</a> 
			&gt; <a href='${statics["org.seamoo.webapp.client.shared.UrlFactory"].getLeagueViewUrl(league)}'>${league.name}</a>
		</p>
		<p>
			${league.description}
		</p>
	</div>
	<div class="description-box" id="sidePlaceHolder">
	</div>
</div>