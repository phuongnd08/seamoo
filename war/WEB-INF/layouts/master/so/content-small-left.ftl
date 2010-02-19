[#ftl]
[#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]]
<div id="left-sidebar" style="width: 220px;"><br />
<div class="module">
<h4>Side-header</h4>
<div class="related">Side content1<br />
Side content 2<br />
</div>
</div>
</div>

<div id="mainbar">
<div id="content">[@tiles.insertAttribute name="body" /]</div>
</div>