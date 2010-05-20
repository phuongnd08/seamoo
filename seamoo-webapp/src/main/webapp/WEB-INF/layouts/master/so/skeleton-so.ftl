[#ftl]
[#import "/spring.ftl" as spring]
[#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]]
[#assign theme="so"]
[#global themeRoot][@spring.url "/themes/${theme}"/][/#global]
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${title} - SeaMoo</title>

<link rel="stylesheet" href="${themeRoot}/all.css"/>

</head>
<body>
<br />
	<div class="container">
		[#include "skeleton-header.ftl"/]
		<div id="content-wrapper">
			<div id="content-header">
			<h2><a href="#" class="question-hyperlink" onclick="window.location.reload(); return false;">${title}</a></h2>
			</div>
			[@tiles.importAttribute/]
			[@tiles.insertAttribute	name="content"]
				[@tiles.putAttribute name="body" value="${body}"/]
				[@tiles.putAttribute name="side" value="${side}"/]
			[/@tiles.insertAttribute]
		</div>
		
		[#include "skeleton-footer.ftl"/]	
	</div>
</body>
</html>