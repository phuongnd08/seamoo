[#ftl/]
[#import "/common.ftl" as common/]
[#macro processedReportItem no sender sentTime question processor]
	<tr class="row-${no%2+1}">
		<td class="tar">${no}</td>
		<td>
			<strong>${question}</strong><br/>
			<a href="#">${sender}</a> - <span class="report-time">${sentTime}</span>: [#nested/]
		</td>
		<td><a href="#">${processor}</a></td>
		<td><a href="#">Xem lại</a></td>
	<tr>
[/#macro]
<div class="description-box">
	<h3>Đã xử lí</h3>
	<table class="fw grid">
		<tr>
			<th class="number-column tar">#</th>
			<th>Chi tiết</th>
			<th>Người xử lí</th>
			<th></th>
		</tr>
		[@processedReportItem no=1 sender="thinh_pro" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="mrcold"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@processedReportItem]
		[@processedReportItem no=2 sender="thinh_pro" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="mrcold"]
			Misspelling
		[/@processedReportItem] 
		[@processedReportItem no=3 sender="lucia" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="mrcold"]
			Câu hỏi không phù hợp
		[/@processedReportItem] 
		[@processedReportItem no=4 sender="lucia" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="mrcold"]
			Cocacola number 1
		[/@processedReportItem] 
		[@processedReportItem no=5 sender="akurik" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="chesi"]
			Nên là một trường hợp đặt biệt
		[/@processedReportItem] 
		[@processedReportItem no=6 sender="xuka" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="chesi"]
			Không chấp nhận
		[/@processedReportItem] 
		[@processedReportItem no=7 sender="nobita" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="chesi"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@processedReportItem] 
		[@processedReportItem no=8 sender="chaien" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="chesi"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@processedReportItem] 
		[@processedReportItem no=9 sender="xeko" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="xaku"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@processedReportItem] 
		[@processedReportItem no=10 sender="dekhi" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position" processor="ken_de_MC"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@processedReportItem] 
	</table>
	[@common.pagingControl pages=[1,2,3,4,5]/]
	<div class="cbt"></div>
</div>