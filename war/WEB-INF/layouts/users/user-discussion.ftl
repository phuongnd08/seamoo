[#ftl/]
[#import "/spring.ftl" as spring/]
[#import "/common.ftl" as common/]

<div id="subheader">
<div id="tabs">
    <a title="Hồ sơ của Mrc0ld" href="/users/244000/mrc0ld?tab=profile">hồ sơ</a>
	<a title="Your recent activity" href="/users/244000/mrc0ld?tab=discussion" class="youarehere">thảo luận</a>
</div>
<div class="cbt"></div>

[#macro discussionItem title time user]
<div class="description-box">
	<div>
		<a href="#">${title}</a>
		<div class="fr">
			<a href="#" title="Ngừng theo dõi câu hỏi" class="discussion-ignore-btn"></a>
		</div>
	</div>
	<div class="comments">
		<table>
		<tr class="comment">
			<td>
				<div>
					[#nested] - <a href="#" class="comment-user">${user}</a> <span class="comment-date">${time}</span> <a href="#">[chi tiết]</a>
				</div>
			</td>
		</tr>
		</table>
	</div>
</div>
[/#macro]
[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm nay, 8:30" user="mrcold"]
	Trong trường hợp này thì nội dung phù hợp lẽ ra nên là ....
[/@discussionItem]

[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm qua, 8:30" user="mrcold"]
	Đây là một sự lựa chọn không nhất thiết ....
[/@discussionItem]

[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm nay, 8:30" user="mrcold"]
	Cần sửa lại thành ....
[/@discussionItem]

[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm nay, 8:30" user="mrcold"]
	Trong trường hợp này thì nội dung phù hợp lẽ ra nên là ....
[/@discussionItem]

[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm qua, 8:30" user="mrcold"]
	Đây là một sự lựa chọn không nhất thiết ....
[/@discussionItem]

[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm nay, 8:30" user="mrcold"]
	Cần sửa lại thành ....
[/@discussionItem]

[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm nay, 8:30" user="mrcold"]
	Trong trường hợp này thì nội dung phù hợp lẽ ra nên là ....
[/@discussionItem]

[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm qua, 8:30" user="mrcold"]
	Đây là một sự lựa chọn không nhất thiết ....
[/@discussionItem]

[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm nay, 8:30" user="mrcold"]
	Cần sửa lại thành ....
[/@discussionItem]

[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm nay, 8:30" user="mrcold"]
	Trong trường hợp này thì nội dung phù hợp lẽ ra nên là ....
[/@discussionItem]

[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm qua, 8:30" user="mrcold"]
	Đây là một sự lựa chọn không nhất thiết ....
[/@discussionItem]

[@discussionItem title="What is the preposition that best suits to I'm looking ... a toy" time="Hôm nay, 8:30" user="mrcold"]
	Cần sửa lại thành ....
[/@discussionItem]

[@common.pagingControl "1,2,3,4,5"?split(",")]
	#
[/@common.pagingControl]
<div class="cbt"></div>