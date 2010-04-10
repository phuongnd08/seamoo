[#ftl]
[#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]]
<div id="mainbar-full">
<div id="content">
	<div class="page-description">
	[@tiles.importAttribute/]
	[@tiles.insertAttribute name="body"]
	[/@tiles.insertAttribute]
	</div>
</div>
</div>