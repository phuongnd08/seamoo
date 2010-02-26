[#ftl/]
[#import "/common.ftl" as common/]
<div class="description-box">
	Bạn có <strong>120</strong> câu hỏi trong bộ sưu tập cá nhân.
</div>
<div>
	Tìm kiếm <input type="text" class="searchbox" maxlength="80"/> <a href="#" title="Hiện lại tất cả các câu hỏi" class="search-reset-btn"></a>
</div>
<br/>

[#macro questionItem no content lastRep nextRep keywords]
	<tr>
		<td class="tar">${no}</td>
		<td>
			<div class="fr">			
				<a href="#" title="Sửa câu hỏi" class="ib edit-btn"></a>
				<a href="#" title="Bỏ câu hỏi ra khỏi bộ sưu tập" class="ib remove-btn"></a>
			</div>
			<div><a href="#">${content}</a></div>
			<div class="question-personal-info">
			<span class="date-description" title="Lần luyện tập cuối">${lastRep}</span> - <span class="date-description" title="Lần luyện tập kế tiếp">${nextRep}</span><br/>
			[@common.tagList tags=keywords/]
			</div>
		</td>
	</tr>
[/#macro]
<div>
<table class="fw grid">
	<tr><th class="tar number-column">#</th><th>Nội dung</th></tr>
	[@questionItem no=1 content="What is the suitable preposition" lastRep="12 tháng 9, 2009" nextRep="12 tháng 2, 2010" keywords="Grammar,English"?split(",")/]
	[@questionItem no=2 content="What is the suitable preposition" lastRep="12 tháng 9, 2009" nextRep="12 tháng 2, 2010" keywords="Grammar,English"?split(",")/]
	[@questionItem no=3 content="What is the suitable preposition" lastRep="12 tháng 9, 2009" nextRep="12 tháng 2, 2010" keywords="Grammar,English"?split(",")/]
	[@questionItem no=4 content="What is the suitable preposition" lastRep="12 tháng 9, 2009" nextRep="12 tháng 2, 2010" keywords="Grammar,English"?split(",")/]
	[@questionItem no=5 content="What is the suitable preposition" lastRep="12 tháng 9, 2009" nextRep="12 tháng 2, 2010" keywords="Grammar,English"?split(",")/]
	[@questionItem no=6 content="What is the suitable preposition" lastRep="12 tháng 9, 2009" nextRep="12 tháng 2, 2010" keywords="Grammar,English"?split(",")/]
	[@questionItem no=7 content="What is the suitable preposition" lastRep="12 tháng 9, 2009" nextRep="12 tháng 2, 2010" keywords="Grammar,English"?split(",")/]
	[@questionItem no=8 content="What is the suitable preposition" lastRep="12 tháng 9, 2009" nextRep="12 tháng 2, 2010" keywords="Grammar,English"?split(",")/]
	[@questionItem no=9 content="What is the suitable preposition" lastRep="12 tháng 9, 2009" nextRep="12 tháng 2, 2010" keywords="Grammar,English"?split(",")/]
	[@questionItem no=10 content="What is the suitable preposition" lastRep="12 tháng 9, 2009" nextRep="12 tháng 2, 2010" keywords="Grammar,English"?split(",")/]
</table>
[@common.pagingControl pages="1,2,3,4,5"?split(",")/]
<div class="cbt"></div>
</div>