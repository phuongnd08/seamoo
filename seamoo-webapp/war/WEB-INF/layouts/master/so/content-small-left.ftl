[#ftl]
[#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]]
<div id="left-sidebar" style="width: 220px;">
[@tiles.insertAttribute name="side"/]
</div>

<div id="mainbar">
	<div id="content">
		<div class="page-description">
			[@tiles.insertAttribute name="body" /]
		</div>
	</div>
</div>