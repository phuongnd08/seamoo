[#ftl/]
[#import "/spring.ftl" as spring]
[#import "/common.ftl" as common]
[#assign urlFactory=statics["org.seamoo.webapp.UrlFactory"]/]
[#--
matchItem
display a row containing brief information of a match
--]
[#macro matchItem no members mode="happening"]
<tr>
<td class="tar">${no}</td>
<td>
[#assign ms=members?split(",")/]
[#assign count=0/]
[#list ms as member]
	[#assign count=count+1]
	<a href="/users/${member}">${member}</a> 
	([#if mode=="done"]
		[#switch count]
			[#case 1] 1st
			[#break/]
			[#case 2] 2nd
			[#break/]
			[#case 3] 3rd
			[#break/]
			[#case 4] 4th
			[#break/] 
		[/#switch]
	[#else]
		${8-count}/20
	[/#if])
[#if member_has_next] vs [/#if]
[/#list]
</td>
<td><a href="#">Xem</a></td>
</tr>
[/#macro]

<div>
	[#if member?exists]
		[#if joinable]
			<button class="big-button" type="button" onclick="window.location.replace('${urlFactory.getParticipateUrl(league)}')">Tham gia &gt;&gt;</button>
		[#else]
			Bạn chưa được phép tham gia giải đấu này. Vui lòng bắt đầu ở giải đấu thấp hơn và tích luỹ điểm số để được thăng hạng
		[/#if]
	[#else]
		Vui lòng <a href="${urlFactory.getLogInUrl(urlFactory.getLeagueViewUrl(league))}">đăng nhập</a> để có thể tham gia giải đấu
	[/#if]
</div>

<br/>
<h3>Các trận đấu đang diễn ra</h3>
<br/>
<div>
<table class="fw grid">
<tr><th>#</th><th>Thông tin</th><th></th></tr>
[@matchItem no="1" members="mica,andy,hante,choi"/]
[@matchItem no="2" members="m0ld,tomabe,hen,char"/]
[@matchItem no="3" members="nany,tomasky,horn,mai_chee"/]
[@matchItem no="4" members="kanu,my,tache_hang,lee_ngaa"/]
[@matchItem no="5" members="hateco,vian,phuc_map,bach_n"/]
[@matchItem no="6" members="fu,hotacheung,hung_b,girl_kute"/]
[@matchItem no="7" members="solo,thate,kiniko,anjiruka"/]
[@matchItem no="8" members="mica,andy,hante,ch"/]
[@matchItem no="9" members="nany,tomasky,hen,char"/]
[@matchItem no="10" members="kanu,my,tache_hang,lee_ngaa"/]
</table>
</div>
[@common.pagingControl "1,2,3,4,5"?split(",");page]
	[@spring.url "/leagues/view/1/${page}"/]
[/@common.pagingControl]
<div class="cbt"></div>
<br/>
<h3>Các trận đấu đã diễn ra</h3>
<br/>
<div>
<table class="fw grid">
<tr><th>#</th><th>Thông tin</th><th></th></tr>
[@matchItem no="1" members="mica,andy,hante,choi" mode="done"/]
[@matchItem no="2" members="m0ld,tomabe,hen,char" mode="done"/]
[@matchItem no="3" members="nany,tomasky,horn,mai_chee" mode="done"/]
[@matchItem no="4" members="kanu,my,tache_hang,lee_ngaa" mode="done"/]
[@matchItem no="5" members="hateco,vian,phuc_map,bach_n" mode="done"/]
[@matchItem no="6" members="fu,hotacheung,hung_b,girl_kute" mode="done"/]
[@matchItem no="7" members="solo,thate,kiniko,anjiruka" mode="done"/]
[@matchItem no="8" members="mica,andy,hante,ch" mode="done"/]
[@matchItem no="9" members="nany,tomasky,hen,char" mode="done"/]
[@matchItem no="10" members="kanu,my,tache_hang,lee_ngaa" mode="done"/]
</table>
</div>
[@common.pagingControl "1,2,3,4,5"?split(",");page]
	[@spring.url "/leagues/view/1/${page}"/]
[/@common.pagingControl]
<div class="cbt"></div>