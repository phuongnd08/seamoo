[#ftl/]
[#import "/spring.ftl" as spring/]
<script type="text/javascript" src="[@spring.url "/js/jquery-1.2.6.min.js"/]"></script>
<script type="text/javascript" src="[@spring.url "/js/openid-jquery.js"/]"></script>
<script type="text/javascript">
	$(document).ready(function() {
		openid.img_path = "[@spring.url "/themes/so/images/openid/"/]";
	    openid.init('openid_identifier');
	});

</script>
<form method="post" id="openid_form" action="?returnUrl=${returnUrl!""}">
    [#-- Simple OpenID Selector --]
	<div id="openid_choice" style="display: block;">
	    <p>Chọn nhà cung cấp <a href="http://openid.net/what/">OpenID</a> của bạn:</p>
	    <div id="openid_btns">
	    	[#--jquery-openid plugin will init buttons here--]
		</div>
		
	</div>
	<div style="color:red;font-size:1.4em">${openid_servlet_filter_msg!""}</div>
	<div id="openid_input_area">
		<input id="openid_identifier" name="openid_identifier" type="text" value="http://" />
		<input id="openid_submit" type="submit" value="Sign-In"/>
	</div>
</form>
<div id="cbt"></div>
<p>&nbsp;</p>
<p>Nếu bạn không sử dụng dịch vụ của một trong các nhà cung cấp trên, <a href="https://www.google.com/accounts/NewAccount">click vào đây để đăng kí một tài khoản với Google</a></p>