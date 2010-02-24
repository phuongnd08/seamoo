[#ftl/]
<div class="description-box">
	<p>
	<a href="#">thinh_pro</a> <span class="report-time">12 tháng 2, 2009 - 8:00</span>: Câu trả lời phù hợp nên là <em>to</em> thay vì <em>at</em>
	</p>
	<p>Tình trạng: <strong>Đang xử lí</strong></p>
</div>
<br/>
<div class="description-box">
	<h3>Câu hỏi</h3>
	<div class="sc">
		<div>Điển vào chỗ trống</div>
		<p>
			<textarea rows="3" cols="80">(Dùng giới từ thích hợp) I looked ... her with curious eyes</textarea>
		</p>
	</div>
	<br/>
	<h3>Câu trả lời</h3>
	<div class="sc">
		<p><input type="text" size="80" value="at"/></p>
	</div>
	<br/>
	<h3>Từ khóa</h3>
	<div class="sc">
		<div><em>Nối từ-khóa-dài bằng dấu gạch, dùng dấu cách để nối tối đa 5 từ khóa (Ví dụ: <strong>tiếng-anh phát-âm</strong>)</em></div>
		<p><input type="text" size="80" value="english preposition"/></p>
	</div>
</div>
<div>
	<button>Cập nhập</button> <button>Hủy bỏ</button>
</div>
<br/>
[#macro reversionItem revision moment author description]
	<tr><td>${revision}</td><td><a href="#">${author}</a> <span class="date-description">${moment}</span>: ${description}</td><td><a href="#">Xem</a></td></tr>
[/#macro]
<div class="description-box">
	<h3>Lịch sử</h3>
	<div class="sc">
		<table class="fw">
			<tr><th class="revision-column">Phiên bản</th><th>Mô tả</th><th style="width: 50px;"></th></tr>
			[@reversionItem revision="Rev 03" moment="14 tháng 2, 2009 - 8:00" author="xuka" description="Sửa câu hỏi: Bổ sung giới từ vào trước câu"/]
			[@reversionItem revision="Rev 02" moment="13 tháng 2, 2009 - 8:00" author="mrcold" description="Sửa câu hỏi: Tinh gọn câu hỏi"/]
			[@reversionItem revision="Rev 01" moment="12 tháng 2, 2009 - 8:00" author="mrcold" description="Tạo ra câu hỏi"/]
		</table>
	</div>
</div>
<br/>
<div class="description-box">
	<h3>Thảo luận</h3>
	<div class="sc">
		[#macro discussionItem title time user]
			<tr class="comment">
				<td>
					<div>
						[#nested] - <a href="#" class="comment-user">${user}</a> <span class="comment-date">${time}</span>
					</div>
				</td>
			</tr>
		[/#macro]
		<div class="description-box">
			<div class="comments">
				<table>
					[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm nay, 8:30" user="mrcold"]
						Trong trường hợp này thì nội dung phù hợp lẽ ra nên là ....
					[/@discussionItem]
					
					[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm qua, 8:30" user="xuka"]
						Đây là một sự lựa chọn không nhất thiết ....
					[/@discussionItem]
					
					[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm nay, 8:30" user="mrcold"]
						Cần sửa lại thành ....
					[/@discussionItem]
				</table>
			</div>
		</div>
	</div>
</div>