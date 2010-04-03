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
						<p id="description" >
						[#nested/] 
						</p>
						<div><label id="enabled">[#if enabled]Enabled[#else]Disabled[/#if]</label></div>
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
					<span><input type="button" id="create" value="Create"/></span>
					<span><input type="button" id="save" value="Save"/></span>
					<span><input type="button" id="cancel" value="Cancel"/></span>
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

<div id="subject-list">
</div>