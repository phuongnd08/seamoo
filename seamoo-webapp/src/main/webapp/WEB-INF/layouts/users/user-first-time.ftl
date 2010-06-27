[#ftl/]
[#import "/common.ftl" as common/]
[#import "/spring.ftl" as spring/]
    <p>OpenID này chưa được lưu trữ trong dữ liệu của [@common.siteName/]:</p>
    <p style="font-size: 150%;" class="openid-identifier">${identifier}</p>
    <p>Bạn có muốn tạo một tài khoản [@common.siteName/] mới với Open ID này?</p>

    <div>
		<form method="post">
			<input type="hidden" id="cmd" name="cmd"/>    
	        <input type="submit" style="font-size: 120%;" value="Tạo tài khoản mới" onclick="document.getElementById('cmd').value='create';"/>
	        <input type="submit" style="font-size: 120%;" value="Hủy bỏ" onclick="document.getElementById('cmd').value='cancel';"/>
        </form>
    </div>            
</form>