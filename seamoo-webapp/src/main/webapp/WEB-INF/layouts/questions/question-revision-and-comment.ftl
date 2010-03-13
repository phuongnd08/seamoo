[#ftl/]
[#macro reversionItem revision moment author description active=false]
	[#assign selectedRev=revisionToShow!3 /]
	<tr[#if revision=selectedRev] class="revision-active"[/#if]><td>Rev ${revision?string("00")}</td><td><a href="#">${author}</a> <span class="date-description">${moment}</span>: ${description}</td><td><a href="#">Xem</a></td></tr>
[/#macro]
<div class="description-box">
	<h3>Lịch sử</h3>
	<div class="sc">
		<table class="fw">
			<tr><th class="revision-column">Phiên bản</th><th>Mô tả</th><th style="width: 50px;"></th></tr>
			[@reversionItem revision=3 moment="14 tháng 2, 2009 - 8:00" author="xuka" description="Sửa câu hỏi: Bổ sung giới từ vào trước câu"/]
			[@reversionItem revision=2 moment="13 tháng 2, 2009 - 8:00" author="mrcold" description="Sửa câu hỏi: Tinh gọn câu hỏi"/]
			[@reversionItem revision=1 moment="12 tháng 2, 2009 - 8:00" author="mrcold" description="Tạo ra câu hỏi"/]
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
				<tr>
                	<td class="comment-form">
                		<form id="add-comment-2326912">
                			<table>
                				<tr>
                					<td><textarea rows="3" cols="68" name="comment" tabindex="202"></textarea></td>
                					<td><input type="submit" value="Gửi" tabindex="203"></td>
            					</tr>
            					<tr>
            						<td colspan="2">
            							<span class="text-counter cool">nhập ít nhất 15 kí tự</span>
            							<span class="form-error"></span>
            						</td>
        						</tr>
    						</table>
						</form>
					</td>
            	</tr>
			</table>
		</div>
	</div>
</div>