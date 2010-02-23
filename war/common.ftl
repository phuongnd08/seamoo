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