[#ftl/]
[#import "/spring.ftl" as spring/]
[#macro subject name link img mode enabled=false]
	<div class="option-box">
		<form>
		<table class="fw">
			<tr>
				<td class="logo-cell tac">
					<img src="[@spring.url img/]" width="96" height="64"/><br/>
					<button style='display: [#if mode=="view"]none[/#if]'>Đổi ảnh</button>
				</td>
				<td>
					<div style='display: [#if mode="edit"]none[/#if]'>
						<div>
							<a href="${link}" id="name">${name}</a>
							<div class="fr">
								<a href="#" id="edit" class="edit-btn"></a>
								<a href="#" id="remove" class="remove-btn"></a>
								<a href="#" id="preview" class="preview-btn"></a>		
							</div>
						</div>
						<p id="description_label" >
						[#nested/] 
						</p>
						<div id="enabled_label">[#if enabled]Enabled[#else]Disabled[/#if]</div>
					</div>
					<div style='display: [#if mode="view"]none[/#if]'>
						<div><label for="name">Name</label></div>
						<div><input type="text" size="25" id="name" name="name" value="${name}"/></div>
						<div><label for="description">Description</label></div>
						<p><textarea id="description" name="description" rows="3" cols="25">[#nested/]</textarea></p> 
						<div><input type="checkbox" id="enabled" name="enabled" [#if enabled]checked[/#if]/> <label for="enabled">Enabled</label></div>
					</div>
				</td>
			</tr>
			<tr style='display: [#if mode="view"]none[/#if]' id="buttonContainer">
				<td colspan="2" class="tac">
					<span><input type="button" id="create-btn" value="Create"/></span>
					<span><input type="button" id="save-btn" value="Save"/></span>
					<span><input type="button" id="cancel-btn" value="Cancel"/></span>
				</td>
			</tr>
		</table>
		</form>
	</div>
[/#macro]
<script type="text/javascript" src="[@spring.url "/adminSubjects/adminSubjects.nocache.js"/]"></script>
<div id="loading-message">
	Loading...
</div>
[@subject name="English" link="#" img="/images/subjects/english.png" mode="view" enabled=true]Thể hiện vốn từ và khả năng nghe hiểu Anh ngữ của bạn[/@subject]
[@subject name="Toán học" link="#" img="/images/subjects/math.png" mode="view" enabled=true]Thể hiện hiểu biết và khả năng tư duy toán học của bạn[/@subject]
[@subject name="French" link="#" img="/images/subjects/english.png" mode="view"]Thể hiện vốn từ và khả năng nghe hiểu Pháp ngữ của bạn[/@subject]
[@subject name="Russia" link="#" img="/images/subjects/english.png" mode="edit"]Thể hiện vốn từ và khả năng nghe hiểu Nga ngữ của bạn[/@subject]