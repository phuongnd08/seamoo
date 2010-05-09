[#ftl/]
[#import "/common.ftl" as common/]
<div class="description-box">
<table class="fw">
<tr>
	<td class="hw">
		[#if (member.autoId==user.autoId)]
		<div style="float: right; margin-top: 19px; margin-right: 4px;">
             <a href="/users/edit/${user.autoId?c}">sửa</a> 
        </div>
        [/#if]
		<table class="fw">
			<tr>
				<td class="user-gravatar128">
					[@common.avatar emailHash=user.emailHash size=128 classes="fl user-gravatar128"/]
				</td>
				<td>
					<p>
						<strong>${user.displayName}</strong><br/>
						${user.quote!""}
					</p>
					<p>
						<table class="fw">
							<tr><td>Tuổi</td><td>25</td></tr>
							[#--<tr><td>Thấy lần cuối</td><td>10 phút trước</td></tr>--]
							<tr>
								<td>Website</td>
								<td>[#if (user.website?exists && user.website!="")]<a href="${user.website}">${user.website}</a>[/#if]</td>
							</tr>
							<tr><td>Danh tiếng</td><td>12k</td></tr>
							<tr><td>Cấp</td><td>10</td></tr>
						</table>
					</p>
				</td>
			</tr>
		</table>
	</td>
	<td>
		<p id="user-about-me">
		${((user.aboutMe!"")?xhtml)?replace("\n", "<br/>")}
		</p>
	</td>
</tr>
</table>
</div>  
<br/>
[#macro leagueItem no period league result]
	<tr><td class="tar">${no?c}</td><td>${period}</td><td><a href="#">${league}</a></td><td>${result}</td></tr>
[/#macro]
<div class="description-box">
	<h3>Các giải đấu đã tham gia</h3>
	<table class="fw grid">
		<tr><th class="tar">#</th><th>Giai đoạn</th><th>Giải đấu</th><th>Kết quả</th></tr>
		[@leagueItem no=1000 period="Tháng 2, 2010" league="English &gt; Giải gà con" result="Đang thi đấu"/]
		[@leagueItem no=999 period="Tháng 2, 2010" league="English &gt; Giải nghiệp dư" result="Thăng hạng (330)"/]
		[@leagueItem no=998 period="Tháng 2, 2010" league="English &gt; Giải gà con" result="Tụt hạng (80)"/]
		[@leagueItem no=997 period="Tháng 2, 2010" league="English &gt; Giải nghiệp dư" result="Thăng hạng (350)"/]
		[@leagueItem no=996 period="Tháng 2, 2010" league="English &gt; Giải gà con" result="Tụt hạng (30)"/]
		[@leagueItem no=995 period="Tháng 2, 2010" league="English &gt; Giải gà chọi" result="Tụt hạng (0)"/]
		[@leagueItem no=994 period="Tháng 2, 2010" league="English &gt; Giải đại bàng" result="Tụt hạng (0)"/]
		[@leagueItem no=993 period="Tháng 2, 2010" league="English &gt; Giải gà chọi" result="Thăng hạng (400)"/]
		[@leagueItem no=992 period="Tháng 2, 2010" league="English &gt; Giải gà chọi" result="Trụ hạng (120)"/]
		[@leagueItem no=991 period="Tháng 2, 2010" league="English &gt; Giải nghiệp dư" result="Thăng hạng (320)"/]
	</table>
	[@common.pagingControl "1,2,3,4,5"?split(",")/]
	<div class="cbt"></div>
</div>
<br/>
[#macro matchItem no moment players result]
	<tr>
		<td class="tar">${no?c}</td><td>${moment}</td>
		<td>
			[#list players as player]
				<a href="#">${player}</a>
				[#if player_has_next]
					&amp;
				[/#if]
			[/#list]
		</td>
		<td>${result}</td>
		<td><a href="#">Xem</a></td>
	</tr>
[/#macro]
<div class="description-box">
	<h3>Các trận đấu đã tham gia</h3>
	<table class="fw grid">
		<tr>
			<th class="tar">#</th><th>Thời điểm</th><th>Đấu với</th><th>Kết quả</th><th></th>
		</tr>
		[@matchItem no=1020 moment="12 tháng 2, 2010 - 8:30" players="thinh_pro,azazu,xuka"?split(",") result="1st"/]
		[@matchItem no=1019 moment="12 tháng 2, 2010 - 8:25" players="thinh_pro,azazu,xuka"?split(",") result="1st"/]
		[@matchItem no=1018 moment="12 tháng 2, 2010 - 8:20" players="thinh_pro,azazu,xuka"?split(",") result="1st"/]
		[@matchItem no=1017 moment="11 tháng 2, 2010 - 8:30" players="thinh_pro,azazu,xuka"?split(",") result="1st"/]
		[@matchItem no=1016 moment="11 tháng 2, 2010 - 8:25" players="thinh_pro,azazu,xuka"?split(",") result="1st"/]
		[@matchItem no=1015 moment="11 tháng 2, 2010 - 8:15" players="thinh_pro,azazu,xuka"?split(",") result="1st"/]
		[@matchItem no=1014 moment="10 tháng 2, 2010 - 8:30" players="thinh_pro,azazu,xuka"?split(",") result="1st"/]
		[@matchItem no=1013 moment="10 tháng 2, 2010 - 8:15" players="thinh_pro,azazu,xuka"?split(",") result="1st"/]
		[@matchItem no=1012 moment="10 tháng 2, 2010 - 8:00" players="thinh_pro,azazu,xuka"?split(",") result="1st"/]
		[@matchItem no=1011 moment="9 tháng 2, 2010 - 8:30" players="thinh_pro,azazu,xuka"?split(",") result="1st"/]
	</table>
	[@common.pagingControl "1,2,3,4,5"?split(",")/]
	<div class="cbt"></div>
</div>