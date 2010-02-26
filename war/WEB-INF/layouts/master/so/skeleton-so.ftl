[#ftl]
[#import "/spring.ftl" as spring]
[#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]]
[#assign theme="so"]
[#assign themeRoot][@spring.url "/themes/${theme}"/][/#assign]
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
<div id="header">
<div id="topbar">
<div id="hlinks">
[#if remoteUser?exists]
		<a href="[@spring.url "/users/recent/244000"/]"><img
			src="${themeRoot}/images/replies-off.png"
			title="you have no new replies" alt="" height="10" width="15"></a>
		<a href="[@spring.url "/users/244000/phuong-nguyen"/]">Phuong
		Nguyen</a>&nbsp;<span class="reputation-score" title="reputation score">47</span>
		<span title="4 bronze badges"></span>
		<span class="lsep">|</span>
		<a href="[@spring.url "/users/logout?returnurl=#"/]">logout</a>
[#else]
		<a href="[@spring.url "/users/login?returnurl=#"/]">login</a>
[/#if] 

<span class="lsep">|</span> <a href="[@spring.url "/about"/]">about</a>
<span class="lsep">|</span> <a href="[@spring.url "/faq"/]">faq</a>
</div>
</div>
<br class="cbt" />
<div id="hlogo"><a href="[@spring.url "/"/]"><img
	src="${themeRoot}/images/logo.png" alt="Stack Overflow" height="78"
	width="255"></a></div>
<div id="hmenus">
<div class="nav">
<ul>
	<li class="youarehere"><a href="[@spring.url "/subjects"/]">Lĩnh
	vực</a></li>
	<li><a href="[@spring.url "/users"/]">Thành viên</a></li>
	<li><a href="[@spring.url "/discussion"/]">Trao đổi</a></li>
	<li><a href="[@spring.url "/reports"/]">Báo cáo</a></li>
	<li><a href="[@spring.url "/questions/hot"/]">Câu hỏi nóng</a></li>
</ul>
</div>
<div class="nav" style="float: right;">
<ul>
	<li style="margin-right: 0px;"><a
		href="[@spring.url "/questions/create"/]">Đăng câu hỏi</a></li>
</ul>
</div>
</div>
</div>
<div id="content-wrapper">
<div id="content-header">
<h2><a href="#" class="question-hyperlink">${title}</a></h2>
</div>
[@tiles.importAttribute/]
[@tiles.insertAttribute	name="content"]
	[@tiles.putAttribute name="body" value="${body}"/]
	[@tiles.putAttribute name="side" value="${side}"/]
[/@tiles.insertAttribute]
</div>
<div id="footer">
<div class="container">
<div id="footer-menu"><a href="[@spring.url "/about"/]">about</a>
| <a href="[@spring.url "/faq"/]">faq</a> | <a
	href="http://blog.stackoverflow.com/">blog</a> | <a
	href="http://itc.conversationsnetwork.org/series/stackoverflow.html">podcast</a>
| <a href="[@spring.url "/privacy"/]">privacy policy</a> | <a
	href="http://ads.stackoverflow.com/a.aspx?ZoneID=0&amp;BannerID=323&amp;AdvertiserID=5&amp;CampaignID=194&amp;Task=Click&amp;SiteID=1">advertising
info</a> | <b><a href="mailto:team@stackoverflow.com">contact us</a></b> | <b><a
	href="http://meta.stackoverflow.com/">feedback always welcome</a></b><a><br />
</a></div>
</div>
</div>
</div>
</body>
</html>