[#ftl/]
[#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]]
[#import "/spring.ftl" as spring/]
[#import "/common.ftl" as common/]
	<p>
		<a href="#">English</a> 
		&gt; <a href="#">Giải nghiệp dư</a> 
		&gt; <strong>Câu hỏi[#if revisionToShow?exists]-<em>Rev ${revisionToShow?string("00")}</em>[/#if]</strong>
	</p>
	<div class="cbt"></div>
<br/>
<table class="fw">
	<tr>
		<td class="votecell">
	        
			<div class="vote">
			    <input type="hidden" value="2326912">
			    <a href="#" class="vote-up-btn"></a>
			    <span class="vote-count">
			        0
			    </span>
			    <a href="#" class="vote-down-btn"></a>
				
			    <a href="#" class="question-favorite-off-btn"></a>
			    <a href="#" class="question-add-btn-big"></a>
			</div>
	
	    </td>
	    <td>
			<div class="description-box">
				<p><div class="fr"><a href="#" class="edit-btn"></a></div><h3>Câu hỏi</h3></p>
				[@common.tagList tags=["grammar", "english"]/]
				<p>Cho biết nội dung trong đoạn hội thoại sau: <a href="#">play</a> <a href="#">pause</a></p>
				<h3>Trả lời</h3>
				<p>Hello, I'm Micheal</p>
			</div>
		</td>
	</tr>
</table>
[@tiles.insertTemplate template="/WEB-INF/layouts/questions/question-revision-and-comment.ftl"/]