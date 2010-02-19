[#ftl]
[#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]]
<div id="mainbar-full">
<div id="content-header">
<h2><a href="#" class="question-hyperlink">${title}</a></h2>
</div>
<div id="content">[@tiles.insertAttribute name="body" /]</div>
</div>