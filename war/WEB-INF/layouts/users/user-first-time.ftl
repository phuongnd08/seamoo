[#ftl/]
[#import "/common.ftl" as common/]
[#import "/spring.ftl" as spring/]
<form method="post" action="/users/openidconfirm">    
    <p>OpenID này chưa được lưu trữ trong dữ liệu của [@common.siteName/]:</p>
    <p style="font-size: 150%;" class="openid-identifier">https://me.yahoo.com/a/qgxnztafqvvvmmvlizi2xzqisa_s3q--</p>
    <p>Bạn có muốn tạo một tài khoản [@common.siteName/] mới với Open ID này?</p>

    <div>
        <input type="submit" style="font-size: 120%;" value="Tạo tài khoản mới"/>
        <input type="button" onclick="window.location.href = '[@spring.url "/"/]'" style="font-size: 120%;" value="Hủy bỏ"/>
    </div>            
</form>