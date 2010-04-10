[#ftl/]
[#import "/spring.ftl" as spring/]
[#import "/common.ftl" as common/]
[@common.adminGwt module="league" params={"currentSubjectAutoId": currentSubject.autoId}/]
<div id="loading-message">
	Loading...
</div>

<div id="league-list">
</div>