[#ftl/]
[#import "/spring.ftl" as spring/]
[#import "/common.ftl" as common/]
[#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]]

<div>
	<button>Cập nhập lại danh sách</button> <span class="loading-indicator"></span> Đang cập nhập lại danh sách
	<div class="fr"><input type="checkbox"/>Hiện cả những báo cáo đã bị xóa</div>
</div>
<br/>

[@tiles.insertTemplate template="/WEB-INF/layouts/reports/report-list-unprocessed.ftl"/]
[@tiles.insertTemplate template="/WEB-INF/layouts/reports/report-list-processing.ftl"/]
[@tiles.insertTemplate template="/WEB-INF/layouts/reports/report-list-processed.ftl"/]
