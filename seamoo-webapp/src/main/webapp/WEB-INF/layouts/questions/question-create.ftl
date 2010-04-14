[#ftl/]
[#import "/spring.ftl" as spring/]
[#import "/common.ftl" as common/]
<div class="description-box">
	<h3>Sử dụng câu hỏi này cho</h3>
	<table>
		<tr>
			<td>
				[@common.combobox name="subjectId" options={"1": "English", "2": "Toán học"} selectedKey="1"/]
			</td>
			<td>
				[@common.combobox name="leagueId" options={"1": "Giải nghiệp dư", "2": "Giải gà con", "3": "Giải gà chọi", "4": "Giải đại bàng"} selectedKey="2"/]
			</td>
		</tr>
		<tr>
			<td><img src="[@spring.url "/images/subjects/english.png"/]" width="96" height="64"/></td>
			<td><img src="[@spring.url "/images/leagues/eng-league-2.png"/]" width="96" height="64"/></td>
		</tr>
	</table>
</div>
<br/>
<div class="description-box">
	Chọn loại câu hỏi [@common.combobox name="questionType" options={"1": "Chọn phương án đúng", "2": "Điền vào ô trống", "3": "Nhận dạng đoạn hội thoại"} selectedKey="2"/]
</div>
<br/>

[#macro similarQuestion vote content link]
<div class="question-summary">
	<div class="question-votes">${vote}</div>
	<div style="width: 90%;" class="question-link"><a title="[#nested/]" class="question-hyperlink" href="${link}">${content}</a></div>
</div>
[/#macro] 
<div class="description-box">
	<h3>Câu hỏi</h3>
	<div>
		Điền vào chỗ trống<br/>
		<textarea rows="3" cols="80"></textarea>
	</div>
	<br/>
	<h3>Câu trả lời</h3>
	<div><input type="text" size="60"/></div>
	<div class="sc">
		<h3>Các câu hỏi tương tự</h3>
		<div style="height: 150px; overflow-y: scroll">
		[@similarQuestion vote="1" content="What is the proper preposition for I look ... her" link="#"]
			What is the proper preposition for I look ... her
			Answer: at
		[/@similarQuestion]
		[@similarQuestion vote="2" content="The proper preposition for I look ... her" link="#"]
			The proper preposition for I look ... her
			Answer: into
		[/@similarQuestion]
		[@similarQuestion vote="5" content="What is the proper preposition for I look ... her" link="#"]
			What is the proper preposition for I look ... her
			Choices: [into] [*at*] [on]
		[/@similarQuestion]
		</div>
	</div>
	<br/>
	<div>
		<h3>Từ khóa</h3>
		<div><em>Nối từ-khóa-dài bằng dấu gạch, dùng dấu cách để nối tối đa 5 từ khóa (Ví dụ: <strong>tiếng-anh phát-âm</strong>)</em></div>
		<p><input type="text" size="60" value="english preposition"/></p>
	</div>
	<br/>
	<div>
		<button>Tạo</button> <button>Hủy bỏ</button>
	</div>
</div>