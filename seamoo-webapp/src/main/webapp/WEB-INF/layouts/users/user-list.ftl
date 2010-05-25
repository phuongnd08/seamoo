[#ftl/]
[#import "/common.ftl" as common/]
[#macro userInfo user]
<div class="fl user-info">
	<table>
		<tr>
			<td class="user-gravatar32">
				<img src="http://www.gravatar.com/avatar/7566e2a406450d0581dcc3661247badc?s=32&d=identicon&r=PG" width="32" height="32"/>
			</td>
			<td class="user-details">
				<div><a href="#">${user}</a></div>
				<div><span class="reputation-score">12k</span> ● <span class="user-level">10</span></div>
				<div><a href="#">English - Giải gà con</a></div>
			</td>
		</tr>
	</table>
</div>
[/#macro]
<div>
<span>Tìm thành viên: </span><input type="text" class="searchbox" size="30" maxwidth="80"/>
</div>

[@userInfo "Mr C0ld"/]
[@userInfo "Che si"/]
[@userInfo "Lucia Tam"/]
[@userInfo "Kim Thehi"/]
[@userInfo "Jang Dong Gun"/]
[@userInfo "Charming Mai"/]
[@userInfo "Hong Anh"/]
[@userInfo "Nguyen Thuy Mai"/]
[@userInfo "Dam Vi Vu"/]
[@userInfo "John"/]
[@userInfo "Kelvin"/]
[@userInfo "Clark Ken"/]
[@userInfo "Spider"/]
[@userInfo "Snake"/]
[@userInfo "Elephant"/]
[@userInfo "Rhodes"/]
[@userInfo "Ha Ba Bop"/]
[@userInfo "Kanu"/]
[@userInfo "Ken"/]
[@userInfo "Jeff"/]
<div class="cbt"></div>
[@common.pagingControl min=1 max=5 current=1/]
<div class="cbt"/>