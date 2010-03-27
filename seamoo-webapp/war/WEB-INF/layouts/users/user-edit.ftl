[#ftl/]
[#import "/common.ftl" as common/]
[#import "/spring.ftl" as spring/]
<form method="post" action="/users/edit/244000" id="user-edit-form">
	<div id="edit-block">
		<input type="text" name="author" id="author"/> 
		<input type="hidden" value="c5ccc5f27bded4fc0de82db12385bbf0" name="fkey" id="fkey"/>
		<input type="hidden" value="fI0Dhu" name="i1l"/>
	</div>
	<table width="600">
		<tr>
			<td>Tên gọi</td>
			<td><input type="text" style="width: 260px;" maxlength="30"
				value="Phuong Nguyen de ManCity fan" id="display-name"
				name="displayName"></td>
		</tr>

		<tr>
			<td>Email</td>
			<td><input type="text" style="width: 260px;" maxlength="100"
				value="phuongnd08@gmail.com" name="email">
			<div class="form-item-info">Không hiển thị, chỉ dùng cho thông báo và gravatar</div>
			</td>
		</tr>
		<tr>
			<td>Ngày sinh</td>
			<td><input type="text" style="width: 260px;" maxlength="50"
				name="Birthday" value="1987/01/18" id="Birthday"> <span
				style="color: rgb(136, 136, 136);">YYYY/MM/DD</span>
			<div class="form-item-info">Không hiển thị, chỉ dùng để tính tuổi</div>
			</td>
		</tr>
		<tr>
			<td>Website</td>
			<td><input type="text" style="width: 260px;" maxlength="200"
				value="" name="website"></td>
		</tr>
		<tr>
			<td>Châm ngôn sống</td>
			<td><input type="text" style="width: 260px;" maxlength="100"
				value="" name="quote"></td>
		</tr>
		<tr>
			<td style="vertical-align: top;">Về bản thân</td>
			<td><textarea rows="12" name="aboutMe" id="aboutMe" cols="55"></textarea>
			<div class="form-item-info">Không cho phép HTML</div>
			</td>
		</tr>
		<tr>
			<td></td>
			<td class="space">
			<div class="form-error"></div>
			<div class="form-submit"><input type="submit"
				value="Lưu lại" id="submit-button"> <input
				type="button"
				onclick="location.href='[@spring.url "/users/244000/phuong-nguyen-de-mancity-fan"/]'"
				value="Hủy" name="cancel" id="cancel"></div>
			</td>
		</tr>
	</table>
</form>