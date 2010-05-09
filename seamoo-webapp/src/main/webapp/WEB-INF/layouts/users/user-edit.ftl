[#ftl/]
[#import "/common.ftl" as common/]
[#import "/spring.ftl" as spring/]
<form method="post" action="[@spring.url "/users/edit/${user.autoId?c}"/]" id="user-edit-form">
	<table width="600">
		<tr>
			<td>Tên gọi</td>
			<td><input type="text" style="width: 260px;" maxlength="30"
				value="${(user.realDisplayName!"")?html}" id="displayName"
				name="displayName"></td>
		</tr>

		<tr>
			<td>Email</td>
			<td><input type="text" style="width: 260px;" maxlength="100"
				value="${(user.email!"")?html}" name="email">
			<div class="form-item-info">Không hiển thị, chỉ dùng cho thông báo và gravatar</div>
			</td>
		</tr>
		<tr>
			<td>Ngày sinh</td>
			<td><input type="text" style="width: 260px;" maxlength="50"
				name="birthday" value="${birthdayString}" id="birthday"> <span
				style="color: rgb(136, 136, 136);">YYYY/MM/DD</span>
			<div class="form-item-info">Không hiển thị, chỉ dùng để tính tuổi</div>
			</td>
		</tr>
		<tr>
			<td>Website</td>
			<td><input type="text" style="width: 260px;" maxlength="200"
				value="${(user.website!"")?html}" name="website"></td>
		</tr>
		<tr>
			<td>Châm ngôn sống</td>
			<td><input type="text" style="width: 260px;" maxlength="100"
				value="${(user.quote!"")?html}" name="quote"></td>
		</tr>
		<tr>
			<td style="vertical-align: top;">Về bản thân</td>
			<td><textarea rows="12" name="aboutMe" id="aboutMe" cols="55">${(user.aboutMe!"")?html}</textarea>
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
				onclick="location.href='[@spring.url "/users/${user.autoId?c}/${user.alias!'n-a'}"/]'"
				value="Hủy" name="cancel" id="cancel"></div>
			</td>
		</tr>
	</table>
</form>