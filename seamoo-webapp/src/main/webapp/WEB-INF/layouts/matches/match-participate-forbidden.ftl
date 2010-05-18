[#ftl/]
[#import "/match.ftl" as match/]
[#import "/common.ftl" as common/]
[#assign urlFactory=statics["org.seamoo.webapp.UrlFactory"]/]
<div class="description-box">
	Bạn không được phép tham gia giải đấu này. Vui lòng thử các giải đấu khác của <a href="${urlFactory.getSubjectViewUrl(subject)}">${subject.name}</a>
</div>