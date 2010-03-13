[#ftl/]
[#import "/spring.ftl" as spring/]
[#macro subject name link img]
	<div class="option-box">
		<table class="fw">
			<tr>
				<td class="logo-cell"><img src="[@spring.url img/]" width="96" height="64"/></td>
				<td>
					<div><a href="${link}">${name}</a>
						<div class="fr">
							<a href="#" class="edit-btn"></a>
							<a href="#" class="remove-btn"></a>
							<a href="#" class="preview-btn"></a>		
						</div>
					</div>
					<p>
						[#nested/] 
					</p>
				</td>
			</tr>
		</table>
	</div>
[/#macro]

[#macro subjectCreator name img]
	<div class="option-box">
		<table class="fw">
			<tr>
				<td class="logo-cell tac">
					<img src="[@spring.url img/]" width="96" height="64"/><br/>
					<button>Đổi ảnh</button>
				</td>
				<td>
					<div>
						<input type="text" size="25" value="${name}"/>
					</div>
					<p>
						<textarea rows="3" cols="25">[#nested/]</textarea> 
					</p>
				</td>
			</tr>
		</table>
	</div>
[/#macro]

[@subject name="English" link="#" img="/images/subjects/english.png"]Thể hiện vốn từ và khả năng nghe hiểu Anh ngữ của bạn[/@subject]
[@subject name="Toán học" link="#" img="/images/subjects/math.png"]Thể hiện hiểu biết và khả năng tư duy toán học của bạn[/@subject]
[@subject name="French" link="#" img="/images/subjects/english.png"]Thể hiện vốn từ và khả năng nghe hiểu Pháp ngữ của bạn[/@subject]
[@subjectCreator name="Russia" img="/images/subjects/english.png"]Thể hiện vốn từ và khả năng nghe hiểu Nga ngữ của bạn[/@subjectCreator]