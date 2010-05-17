[#ftl/]
[#import "/spring.ftl" as spring/]
[#import "/common.ftl" as common/]
[#assign urlFactory=statics["org.seamoo.webapp.UrlFactory"]/]
<div class="module">
	<div class="description-box">
		<p>
			<img class="fl" width="96" height="64" src="[@spring.url league.logoUrl/]"/>
			<a href='${urlFactory.getSubjectViewUrl(subject)}'>${subject.name}</a> 
			&gt; <a href='${urlFactory.getLeagueViewUrl(league)}'>${league.name}</a>
		</p>
		<p>
			${league.description}
		</p>
	</div>
	<div class="description-box" id="sidePlaceHolder">
	</div>
</div>