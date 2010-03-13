[#ftl/]
[#import "/common.ftl" as common/]
[#import "/spring.ftl" as spring/]

[#macro competitorInfo competitor quote]
<div class="user-info">
	<table>
		<tr>
			<td class="user-gravatar32">
				<img src="http://www.gravatar.com/avatar/7566e2a406450d0581dcc3661247badc?s=32&d=identicon&r=PG" width="32" height="32"/>
			</td>
			<td class="user-details">
				<div><a href="#">${competitor}</a></div>
				<div><span class="reputation-score">12k</span> ● <span class="user-level">10</span></div>
				<div><em>${quote}</em></div>
			</td>
		</tr>
	</table>
</div>
[/#macro]

<div class="module">
	<h3>Trận đấu trong khuôn khổ</h3>
	<div class="description-box">
		<p>
			<img class="fl" width="96" height="64" src="[@spring.url "/images/leagues/eng-amateur.png"/]"/>
			<a href="#">English</a> &gt; <a href="#">Giải nghiệp dư</a>
		</p>
	</div>
	<h3>Giữa</h3>
	<div class="description-box">
		[@competitorInfo competitor="mrcold" quote="Sống là không chờ đợi"/]
		[@competitorInfo competitor="ken" quote="Đời là phù du"/]
		[@competitorInfo competitor="xuka" quote="Hận đời vô đối"/]
		[@competitorInfo competitor="thinh_pro" quote="Một đời lang bạt"/]
	</div>
</div>