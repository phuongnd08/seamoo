[#ftl/]
[#import "/common.ftl" as common/]
<div class="module">
	<div class="description-box tac">
		<div>
			[@common.avatar emailHash=user.emailHash size=128/]
		</div>
		<br/>
		<p style="font-size: 200%; font-weight: bold;"><a
			title="Gravatar của bạn được xác định theo email của bạn"
			target="_blank" href="http://www.gravatar.com/">Đổi avatar</a></p>
	</div>
	<br/>
	<div class="description-box">
		<p>
		[@common.siteName/] không lưu trữ ảnh của thành viên. 
		Thay vào đó [@common.siteName/] sử dụng dịch vụ gravatar, là dịch vụ chuyên cung cấp avatar. 
		Chỉ cần bạn nhập vào email, [@common.siteName/] sẽ kết nối với gravatar để hiện thị avatar của bạn.
		</p>
	</div>
</div>
	