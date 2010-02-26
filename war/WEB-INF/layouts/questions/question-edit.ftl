[#ftl/]
[#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]]
[#import "/common.ftl" as common/]
<div class="description-box">
	<strong>English</strong> [@common.combobox name="league" options={"0": "Giải nghiệp dư", "1": "Giải gà con", "2": "Giải gà chọi", "3": "Giải đại bàng"} selectedKey="0"/]
</div>
<div class="description-box">
	<h3>Câu hỏi</h3>
	<div>Điển vào chỗ trống</div>
	<p>
		<textarea rows="3" cols="80">(Dùng giới từ thích hợp) I looked ... her with curious eyes</textarea>
	</p>
	<br/>

	<h3>Câu trả lời</h3>
	<p><input type="text" size="80" value="at"/></p>
	<br/>

	<h3>Từ khóa</h3>
	<div><em>Nối từ-khóa-dài bằng dấu gạch, dùng dấu cách để ngăn cách tối đa 5 từ khóa (Ví dụ: <strong>tiếng-anh phát-âm</strong>)</em></div>
	<p><input type="text" size="80" value="english preposition"/></p>
	<br/>
	
	<h3>Ghi chú</h3>
	<div><em>Mô tả tóm tắt lại những gì bạn đã sửa chữa</em></div>
	<p><input type="text" size="80" value="english preposition"/></p>
	<br/>
	<div>
		<button>Cập nhập</button> <button>Hủy bỏ</button>
	</div>
</div>
<br/>
[@tiles.insertTemplate template="/WEB-INF/layouts/questions/question-revision-and-comment.ftl"/]