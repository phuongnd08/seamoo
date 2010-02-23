[#ftl/]
<form method="post" action="/users/authenticate" id="openid_form">       
            [#-- Simple OpenID Selector --]
    <div id="openid_choice" style="display: block;">
        <p>Chọn nhà cung cấp <a href="http://openid.net/what/">OpenID</a> của bạn:</p>
        <div id="openid_btns">
        	<a class="openid_large_btn google" title="Google"></a>
        	<a class="openid_large_btn yahoo" title="Yahoo"></a>
        	<a class="openid_large_btn myopenid" title="MyOpenID"></a>
        	<a class="openid_large_btn aol" title="AOL"></a>
        	<br/>
        	<a class="openid_small_btn livejournal" title="LiveJournal"></a>
        	<a class="openid_small_btn wordpress" title="Wordpress"></a>
        	<a class="openid_small_btn blogger" title="Blogger"></a>
        	<a class="openid_small_btn verisign"title="Verisign"></a>
        	<a class="openid_small_btn claimid" title="ClaimID"></a>
        	<a class="openid_small_btn clickpass" title="ClickPass"></a>
        	[#--<a class="openid_small_btn google_profile" title="Google Profile"></a>--]
    	</div>
    </div>
	
    <div id="cbt"></div>
    <p>&nbsp;</p>
    <p>Nếu bạn không sử dụng dịch vụ của một trong các nhà cung cấp trên, <a href="#">click vào đây để đăng kí với myOpenID</a></p>
        
</form>