[#ftl]
[#import "/spring.ftl" as spring/]
[#assign urlFactory=statics["org.seamoo.webapp.UrlFactory"]/]
[#--
 * optionBox
 *
 * Macro to create an option-box (used to display a subject for selection).
 --]
[#macro optionBox link title img]
<div class="option-box">
<table class="fw">
	<tr>
		<td><a href="[@spring.url link/]"><img src="[@spring.url img/]" width="96" height="64" /></a></td>
		<td>
		<div><a href="${link}">${title}</a></div>
		<div>[#nested/]</div>
		</td>
	</tr>
</table>
</div>
[/#macro]

<div>
[#list subjects as subject]
[@optionBox link="${urlFactory.getSubjectViewUrl(subject)}" title="${subject.name}" img="${subject.logoUrl}"]
	${subject.description}
[/@optionBox]
[/#list]
</div>
<div class="cbt"></div>