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
[#macro pagingControl pages]
	<div class="pager">
	[#list pages as page]
		<a title="đến trang ${page}" href="[#nested page/]"><span class="page-numbers">${page}</span></a>
	[/#list]
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
[#macro subHeader headers selectedKey]
	<div id="subheader">
		<div id="tabs">
			[#list headers?keys as key]
		    <a href="[@spring.url headers[key]/]"[#if key==selectedKey] class="youarehere"[/#if]>${key}</a>
		    [/#list]
		</div>
	</div>
[/#macro]