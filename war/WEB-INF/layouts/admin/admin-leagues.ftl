[#ftl/]
[#import "/spring.ftl" as spring/]
[#import "/common.ftl" as common/]
[#macro league name link img level enabled]
	<div class="option-box">
		<table class="fw">
			<tr>
				<td class="logo-cell"><img src="[@spring.url img/]" width="96" height="64"/></td>
				<td>
					<div><a href="${link}">${name}</a>
						<div class="fr">
							<a href="#" class="edit-btn"></a>
							<a href="#" class="remove-btn"></a>
							<a href="#" class="preview-btn"></a>		
						</div>
					</div>
					<p>
						[#nested/] 
					</p>
					<p>
						<em><strong>Cấp độ ${level}, [#if enabled]Enabled[#else]Disabled[/#if]</strong></em>
					</p>
				</td>
			</tr>
		</table>
	</div>
[/#macro]

[#macro leagueCreator name img level enabled]
	<div class="option-box">
		<table class="fw">
			<tr>
				<td class="logo-cell tac">
					<img src="[@spring.url img/]" width="96" height="64"/><br/>
					<button>Đổi ảnh</button>
				</td>
				<td>
					<div>
						<input type="text" size="25" value="${name}"/>
					</div>
					<p>
						<textarea rows="3" cols="25">[#nested/]</textarea> 
					</p>
					<p>
						[@common.combobox name="level" options={"0": "Level 0 (Amateur)", "1": "Level 1 (League 2)", "2": "Level 2 (League 1)", "3": "Level 3 (Pro League)"} selectedKey="${level}"/]
					</p>
					<p>
						<input type="checkbox"[#if enabled] checked[/#if]/> Enabled
					</p>
				</td>
			</tr>
		</table>
	</div>
[/#macro]

[@league name="Giải nghiệp dư" link="#" level=0 enabled=true img="/images/leagues/eng-amateur.png"]Dành cho những người lần đầu tham gia[/@league]
[@league name="Giải gà con" link="#" level=1 enabled=true img="/images/leagues/eng-league-2.png"]Dành cho những đấu thủ đã thể hiện được mình ở Giải nghiệp dư[/@league]
[@league name="Giải gà chọi" link="#" level=2 enabled=false img="/images/leagues/eng-league-1.png"]Dành cho những đấu thủ đẳng cấp[/@league]
[@leagueCreator name="Giải đại bàng" level=3 enabled=false img="/images/leagues/eng-pro-league.png"]Dành cho những đấu thủ đẳng cấp cao[/@leagueCreator]