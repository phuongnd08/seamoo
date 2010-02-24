[#ftl/]
[#import "/common.ftl" as common/]
[#macro reportItem no sender sentTime question]
	<tr class="row-${no%2+1}">
		<td class="tar">${no}</td>
		<td>
			<strong>${question}</strong><br/>
			<a href="#">${sender}</a> - <span class="report-time">${sentTime}</span>: [#nested/]
		</td>
		<td><a href="#">Chi tiết</a></td>
	<tr>
[/#macro]
<div class="description-box">
	<h3>Chưa xử lí</h3>
	<table class="fw grid">
		<tr>
			<th class="number-column tar">#</th>
			<th>Chi tiết</th>
			<th></th>
		</tr>
		[@reportItem no=1 sender="thinh_pro" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@reportItem]
		[@reportItem no=2 sender="thinh_pro" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position"]
			Misspelling
		[/@reportItem] 
		[@reportItem no=3 sender="lucia" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position"]
			Câu hỏi không phù hợp
		[/@reportItem] 
		[@reportItem no=4 sender="lucia" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position"]
			Cocacola number 1
		[/@reportItem] 
		[@reportItem no=5 sender="akurik" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position"]
			Nên là một trường hợp đặc biệt
		[/@reportItem] 
		[@reportItem no=6 sender="xuka" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position"]
			Không chấp nhận
		[/@reportItem] 
		[@reportItem no=7 sender="nobita" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@reportItem] 
		[@reportItem no=8 sender="chaien" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@reportItem] 
		[@reportItem no=9 sender="xeko" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@reportItem] 
		[@reportItem no=10 sender="dekhi" sentTime="24 tháng 2, 2009 - 8:00" question="What is the proper preposition for this position"]
			Lựa chọn đúng nên là to chứ không phải at
		[/@reportItem] 
	</table>
	[@common.pagingControl pages=[1,2,3,4,5]/]
	<div class="cbt"></div>
</div>