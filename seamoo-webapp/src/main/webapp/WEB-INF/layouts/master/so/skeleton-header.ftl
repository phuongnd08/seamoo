[#ftl/]
[#import "/spring.ftl" as spring/]
<div id="header">
	<div id="hlogo"><a href="[@spring.url "/"/]"><img
		src="${themeRoot}/images/logo.png" alt="Stack Overflow" height="78"
		width="255"></a>
	</div>
	<div id="topbar">
		<div id="hlinks">
			[#if openid_user?exists]
					<a href="#">
					<img
						src="${themeRoot}/images/replies-off.png"
						title="you have no new replies" alt="" height="10" width="15"></a>
					<a href="[@spring.url "/users/244000/phuong-nguyen"/]">Phuong Nguyen</a>&nbsp;<span class="reputation-score" title="reputation score">47</span>
					<span title="4 bronze badges"></span>
					<span class="lsep">|</span>
					<a href="[@spring.url "/users/logout?returnUrl=#"/]">logout</a>
			[#else]
					<a href="[@spring.url "/users/login?returnUrl=#"/]">login</a>
			[/#if] 
			
			<span class="lsep">|</span> <a href="[@spring.url "/about"/]">about</a>
			<span class="lsep">|</span> <a href="[@spring.url "/faq"/]">faq</a>
		</div>
	</div>
	<br class="cbt" />
	<div id="hmenus">
		<div class="nav">
			<ul>
				<li class="youarehere"><a href="[@spring.url "/subjects"/]">Thi đấu</a></li>
				<li><a href="[@spring.url "/users"/]">Thành viên</a></li>
				<li><a href="[@spring.url "/discussion"/]">Trao đổi</a></li>
				<li><a href="[@spring.url "/reports"/]">Báo cáo</a></li>
				<li><a href="[@spring.url "/questions/hot"/]">Câu hỏi nóng</a></li>
				<li><a href="[@spring.url "/training/go"/]">Luyện tập</a></li>
			</ul>
		</div>
		<div class="nav" style="float: right;">
			<ul>
				<li style="margin-right: 0px;"><a
					href="[@spring.url "/questions/create"/]">Đăng câu hỏi</a></li>
			</ul>
		</div>
		<div class="cbt"></div>
	</div>
</div>