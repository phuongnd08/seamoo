[#ftl/]
[#import "/common.ftl" as common/]
[#assign urlFactory=statics["org.seamoo.webapp.UrlFactory"]/]
<div class="description-box">
<table class="fw">
<tr>
	<td class="hw">
		[#if (member?exists && member.autoId==user.autoId)]
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
		${((user.aboutMe!"")?html)?replace("\n", "<br/>")}
		</p>
	</td>
</tr>
</table>
</div>  
<br/>
[#macro membershipItem membership]
	[#assign league=leaguesMap[membership.leagueAutoId?string]/]
	[#assign subject=subjectsMap[league.subjectAutoId?string]/]
	[#assign leagueResult=enums["org.seamoo.entities.LeagueResult"]/]
	[#assign membershipFromIndex=membershipFromIndex+1/]
	<tr>
		<td class="tar">${membershipFromIndex}</td>
		<td>${membership.year}-${membership.month}</td>
		<td>
			<a href="${urlFactory.getSubjectViewUrl(subject)}">${subject.name}</a> &gt;
			<a href="${urlFactory.getLeagueViewUrl(league)}">${league.name}</a> 
		</td>
		<td>[#switch membership.result]
		[#case leagueResult.UNDETERMINED]
			Đang thi đấu
		[#break]
		[#case leagueResult.STAY]
			Trụ hạng
		[#break]
		[#case leagueResult.UP_RELEGATED]
			Thăng hạng
		[#break]
		[#case leagueResult.DOWN_RELEGATED]
			Xuống hạng
		[#break]
		[/#switch]
		</td>
	</tr>
[/#macro]
<div class="description-box">
	<h3>Các giải đấu đã tham gia</h3>
	<table class="fw grid">
		<tr><th class="tar">#</th><th>Giai đoạn</th><th>Giải đấu</th><th>Kết quả</th></tr>
		[#list memberships as membership]
		[@membershipItem membership/]
		[/#list]
	</table>
	[@common.pagingControl min=1 max=membershipPageCount current=membershipPage;page]
		${urlFactory.getPagedUserViewUrl(user, matchPage, page)}
	[/@common.pagingControl]
	<div class="cbt"></div>
</div>
<br/>
[#macro matchItem match]
	<tr>
	<td>
	[#assign count=0/]
	[#list match.competitors as competitor]
		[#assign count=count+1/]
		[#assign member=membersMap[competitor.memberAutoId?string]/]
		<a href="${urlFactory.getUserViewUrl(member)}">${member.displayName}</a> 
		([@compress single_line=true]
			[#switch competitor.rank]
			[#case 1] 1st
			[#break/]
			[#case 2] 2nd
			[#break/]
			[#case 3] 3rd
			[#break/]
			[#case 4] 4th
			[#break/] 
		[/#switch][/@compress])
		[#if competitor_has_next] vs [/#if]
	[/#list]
	</td>
	<td><a href="${urlFactory.getMatchViewUrl(match)}">Xem</a></td>
	</tr>
[/#macro]
<div class="description-box">
	<h3>Các trận đấu đã tham gia</h3>
	<table class="fw grid">
		[#list matches as match]
			[@matchItem match/]
		[/#list]
	</table>
	[@common.pagingControl min=1 max=matchPageCount current=matchPage;page]
		${urlFactory.getPagedUserViewUrl(user, page, membershipPage)}
	[/@common.pagingControl]
	<div class="cbt"></div>
</div>