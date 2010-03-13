[#ftl/]
[#import "/common.ftl" as common/]
[#macro processingReportItem no sender sentTime question processor]
	<tr class="row-${no%2+1}">
		<td class="tar">${no}</td>
		<td>
			<strong>${question}</strong><br/>
			<a href="#">${sender}</a> - <span class="report-time">${sentTime}</span>: [#nested/]
		</td>
		<td><a href="#">${processor}</a></td>
	<tr>
[/#macro]
<div class="description-box">
	<h3>Đang xử lí</h3>
	<table class="fw grid">
		<tr>
			<th class="number-column tar">#</th>
			<th>Chi tiết</th>
			<th>Người xử lí</th>
		</tr>
		[@processingReportItem no=1 sender="thinh_pro" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="mrcold"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@processingReportItem]
		[@processingReportItem no=2 sender="thinh_pro" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="mrcold"]
			Misspelling
		[/@processingReportItem] 
		[@processingReportItem no=3 sender="lucia" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="mrcold"]
			Câu hỏi không phù hợp
		[/@processingReportItem] 
		[@processingReportItem no=4 sender="lucia" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="mrcold"]
			Cocacola number 1
		[/@processingReportItem] 
		[@processingReportItem no=5 sender="akurik" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="chesi"]
			Nên là một trường hợp đặt biệt
		[/@processingReportItem] 
		[@processingReportItem no=6 sender="xuka" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="chesi"]
			Không chấp nhận
		[/@processingReportItem] 
		[@processingReportItem no=7 sender="nobita" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="chesi"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@processingReportItem] 
		[@processingReportItem no=8 sender="chaien" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="chesi"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@processingReportItem] 
		[@processingReportItem no=9 sender="xeko" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="xaku"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@processingReportItem] 
		[@processingReportItem no=10 sender="dekhi" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="ken_de_MC"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@processingReportItem] 
	</table>
	[@common.pagingControl pages=[1,2,3,4,5]/]
	<div class="cbt"></div>
</div>