[#ftl/]
[#import "/spring.ftl" as spring/]
[#import "/common.ftl" as common/]
[#macro userInfo user assigned]
<div class="fl user-info">
	<table>
		<tr>
			<td class="user-gravatar32">
				<img src="http://www.gravatar.com/avatar/7566e2a406450d0581dcc3661247badc?s=32&d=identicon&r=PG" width="32" height="32"/>
			</td>
			<td class="user-details">
				<div>
					<div class="fr">
					[#if assigned]
						<a class="remove-btn" title="Loại vai trò điều hành của thành viên này"></a>
					[#else]
						<a class="add-btn" title="Chỉ định thành viên này làm điều hành viên"></a>
					[/#if]
					</div>
					<a href="#">${user}</a>
				</div>
				<div><span class="reputation-score">12k</span> ● <span class="user-level">10</span></div>
				<div><a href="#">English - Giải gà con</a></div>
				<div>
			</td>
		</tr>
	</table>
</div>
[/#macro]
<div class="description-box">
	<h3>Những điều hành viên đã được chỉ định</h3>
	[@userInfo user="Mr C0ld" assigned=true/]
	[@userInfo user="Che si" assigned=true/]
	[@userInfo user="Lucia Tam" assigned=true/]
	[@userInfo user="Kim Thehi" assigned=true/]
	<div class="cbt"></div>
</div>
<br/>
<div class="description-box">
	<h3>Thêm điều hành viên mới</h3>
	<div>
	<span>Tìm thành viên: </span><input type="text" class="searchbox" size="30" maxwidth="80"/>
	</div>
	[@userInfo user="Mr Warm" assigned=false/]
	[@userInfo user="xuka" assigned=false/]
	[@userInfo user="Omo" assigned=false/]
	[@userInfo user="Viet ga" assigned=false/]
	<div class="cbt"></div>
</div>
