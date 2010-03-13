[#ftl]
[#import "/spring.ftl" as spring/]
[#--
 * optionBox
 *
 * Macro to create an option-box (used to display a subject for selection).
 --]
[#macro optionBox link title img]
<div class="option-box">
<table class="fw">
	<tr>
		<td><a href="[@spring.url link/]"><img src="[@spring.url img/]" width="96" height="64" /></a></td>
		<td>
		<div><a href="${link}">${title}</a></div>
		<div>[#nested/]</div>
		</td>
	</tr>
</table>
</div>
[/#macro]

<div>
[@optionBox link="/subjects/view/1" title="English" img="/images/subjects/english.png"]
	Thể hiện kĩ năng Anh ngữ của bạn về từ vựng, ngữ pháp và phát âm
[/@optionBox]
[@optionBox link="/subjects/view/2" title="Toán học" img="/images/subjects/math.png"]
	Thể hiện sự hiểu biết cùng khả năng suy luận toán học của bạn
[/@optionBox]
<div class="cbt"></div>
<script type="text/javascript" language="javascript" src="[@spring.url "/seamoo_2/seamoo_2.nocache.js"/]"></script>
<div>
    <table align="center">
      <tr>
        <td colspan="2" style="font-weight:bold;">Please enter your name:</td>        
      </tr>
      <tr>
        <td id="nameFieldContainer"></td>
        <td id="sendButtonContainer"></td>
      </tr>
    </table>
</di>