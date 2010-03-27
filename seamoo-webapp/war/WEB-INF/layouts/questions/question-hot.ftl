[#ftl/]
[#import "/common.ftl" as common/]
[#macro hotQuestion no totalView revisionView content author moment type link]
	<tr class="row-${no%2+1}">
		<td class="tar" rowspan="2">${no}</t>
		<td class="question-total-view" title="Tổng lượt xem">${totalView}</td>
		<td rowspan="2">
			<strong>${type}</strong>: <a href="${link}">${content}</a><br/>
			<div class="sc">
				${author} <span class="date-description">${moment}</span>
			</div>
		</td>
	</tr>
	<tr class="row-${no%2+1}">
		<td class="question-revision-view" title="Lượt xem kể từ lúc ${type}">${revisionView}</td>
	</tr>
[/#macro]
<table class="fw">
	<th class="number-column tar"></th><th class="number-column"></th><th></th></tr>
	[@hotQuestion no=1 totalView="100" revisionView="50" content="What is the proper preposition" author="mrcold" moment="Hôm nay - 8:30" type="Sửa" link="#"/]
	[@hotQuestion no=2 totalView="89" revisionView="40" content="What is the proper preposition" author="mrcold" moment="Hôm nay - 8:30" type="Sửa" link="#"/]
	[@hotQuestion no=3 totalView="90" revisionView="20" content="What is the proper preposition" author="mrcold" moment="Hôm qua - 10:30" type="Sửa" link="#"/]
	[@hotQuestion no=4 totalView="82" revisionView="30" content="What is the proper preposition" author="mrcold" moment="Hôm qua - 9:30" type="Sửa" link="#"/]
	[@hotQuestion no=5 totalView="72" revisionView="30" content="What is the proper preposition" author="mrcold" moment="Hôm qua - 8:30" type="Sửa" link="#"/]
	[@hotQuestion no=6 totalView="62" revisionView="40" content="What is the proper preposition" author="mrcold" moment="12 tháng 2, 2009 - 8:30" type="Sửa" link="#"/]
	[@hotQuestion no=7 totalView="60" revisionView="45" content="What is the proper preposition" author="mrcold" moment="11 tháng 2, 2009 - 8:30" type="Sửa" link="#"/]
	[@hotQuestion no=8 totalView="55" revisionView="55" content="What is the proper preposition" author="mrcold" moment="10 tháng 2, 2009 - 8:30" type="Tạo mới" link="#"/]
	[@hotQuestion no=9 totalView="53" revisionView="53" content="What is the proper preposition" author="mrcold" moment="9 tháng 2, 2009 - 8:30" type="Tạo mới" link="#"/]
	[@hotQuestion no=10 totalView="50" revisionView="50" content="What is the proper preposition" author="mrcold" moment="8 tháng 2, 2009 - 8:30" type="Tạo mới" link="#"/]
</table>
[@common.pagingControl pages=[1,2,3,4,5]/]
<div class="cbt"></div>