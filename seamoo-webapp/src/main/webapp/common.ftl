[#ftl/]
[#import "/spring.ftl" as spring/]
<#--
 * common.ftl
 *
 * This file consists of a collection of FreeMarker macros 
 * used across different seamoo web pages, intended to simulate
 * asp.net user (and custom) controls
 *
 * @author Phuong Nguyen (phuongnd08)
 -->

[#--
	pagingControl
	Display a pager 
	Nested content will be used to generate a link
--]
[#macro pagingControl min max current]
	[#assign pager=statics["org.seamoo.webapp.Pager"]/]
	[#assign pages=pager.getPages(min, max, current)]
	<div class="pager">
	[#if (pages?first > min)]
		<a title="đến trang ${current-1}" href="[#nested current-1/]<span class="page-numbers">trước</span></a>
	[/#if]
	[#list pages as page]
		<a title="đến trang ${page}" href="[#nested page page/]"><span class="page-numbers">${page}</span></a>
	[/#list]
	[#if (pages?last < max)]
		<a title="đến trang ${current+1}" href="[#nested current+1/]<span class="page-numbers">sau</span></a>
	[/#if]
	</div>
[/#macro]

[#--
	siteName
	Display site name in a proper color and format
--]
[#macro siteName]<span class="seamoo sea">Sea</span><span class="seamoo moo">Moo</span>[/#macro]


[#--
	keyValue
	Display a key and a value in a impressive form
--]
[#macro keyValue key value]
	<p class="label-key">${key}</p>
	<p class="label-value">${value}</p>
[/#macro]


[#--
	combobox
	Rendering a combobox
--]
[#macro combobox name options selectedKey]
    <select name="${name}">
        [#list options?keys as key]
        [#assign isSelected = selectedKey==key]
        <option value="${key?html}"[#if isSelected] selected="selected"[/#if]>${options[key]?html}</option>
        [/#list]
    </select>
[/#macro]

[#--
	tagList
	Display a list of tag in proper format
--]
[#macro tagList tags]
	<div class="question-taglist">
		[#list tags as tag]<a href="#" class="question-tag">${tag}</a>[#if tag_has_next]&nbsp;[/#if][/#list]
	</div>
[/#macro]

[#--
	subHeader
	Display a subheader in a form of tab list
--]
[#macro subHeader headers selectedIndex]
	<div id="subheader">
		<div id="tabs">
			[#assign index=0/]
			[#list headers?keys as key]
		    <a href="[@spring.url headers[key]/]"[#if index?string==selectedIndex] class="youarehere"[/#if]>${key}</a>
			[#assign index=index+1/]
		    [/#list]
		</div>
	</div>
[/#macro]

[#macro gwt prefix module params={}]
	<script type="text/javascript">
	[@compress single_line=true]
		var Vars = {
			module: "${module}"
			[#assign index=0/]
			[#list params?keys as key],
				"${key}": "${params[key]}"
			[/#list]
		}
	[/@compress]
	</script>
	<script type="text/javascript" src="[@spring.url "/${prefix}Gwt/${prefix}Gwt.nocache.js"/]"></script>
[/#macro]

[#macro adminGwt module params={}]
	[@gwt prefix="admin" module=module params=params/]
[/#macro]

[#macro userGwt module params={}]
	[@gwt prefix="user" module=module params=params/]
[/#macro]

[#macro avatar size emailHash="" classes=""]
	<img class="${classes}" width="${size?c}" height="${size?c}" src="http://www.gravatar.com/avatar/${emailHash!"na"}?s=${size?c}&d=wavatar&r=PG"/>
[/#macro]