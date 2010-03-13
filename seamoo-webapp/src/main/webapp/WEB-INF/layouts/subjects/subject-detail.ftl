[#ftl/]
[#import "/spring.ftl" as spring]
[#macro leagueControl link title img joinable]
<div class="description-box">
<table class="fw">
<tr>
	<td class="logo-cell">
		<a href="${link}">
			<img src="[@spring.url img/]" width="96" height="64"/>
		</a>
	</td>
	<td>
		<div><a href="${link}">${title}</a></div>
		<div>[#nested/]</div>
		<div>
		[#if joinable]
			<button>Tham gia</button>
		[#else]
			<em>Bạn chưa được quyền tham giải đấu này</em>
		[/#if]
		</div>
	</td>
</tr>
</table>
</div>
[/#macro]
[@leagueControl link="/leagues/view/1" title="Giải nghiệp dư" img="/images/leagues/eng-amateur.png" joinable=true]
Dành cho những người lần đầu tham dự. Bạn đang có <strong>40</strong> điểm. 
Bạn còn <strong>20</strong> ngày để tích lũy đủ <strong>300</strong> điểm để duy trì quyền tham gia <strong>Giải gà con</strong>
[/@leagueControl]

[@leagueControl link="/leagues/view/2" title="Giải gà con" img="/images/leagues/eng-league-2.png" joinable=true]
Dành cho những đấu thủ đã vuợt qua được thử thách ở giải nghiệp dư. Bạn đang có <strong>40</strong> điểm. 
Bạn còn <strong>20</strong> ngày để tích lũy <strong>300</strong> điểm để có thể thăng hạng lên <strong>Giải gà chọi</strong>
[/@leagueControl]

[@leagueControl link="/leagues/view/2" title="Giải gà chọi" img="/images/leagues/eng-league-1.png" joinable=false]
Dành cho những đấu thủ có đẳng cấp.
[/@leagueControl]

[@leagueControl link="/leagues/view/3" title="Giải đại bàng" img="/images/leagues/eng-pro-league.png" joinable=false]
Dành cho những đấu thủ đẳng cấp cao
[/@leagueControl]