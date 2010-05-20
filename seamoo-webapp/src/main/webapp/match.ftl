[#ftl/]
[#--
	eventItem
	display an event of the match
--]
[#macro eventItem event]
	<tr>
		<td style="width: 20%">
			${event.happenTime?string("HH:mm:ss")} (UTC)
		</td>
		<td>
	[#switch event.type]
		[#case enums["org.seamoo.entities.matching.MatchEventType"].STARTED]
		Match started
		[#break/]
		[#case enums["org.seamoo.entities.matching.MatchEventType"].FINISHED]
		Match ended
		[#break/]
		[#case enums["org.seamoo.entities.matching.MatchEventType"].JOINED]
		<strong>${event.member.displayName}</strong> joined
		[#break/]
		[#case enums["org.seamoo.entities.matching.MatchEventType"].LEFT]
		<strong>${event.member.displayName}</strong> left
		[#break/]
		[#case enums["org.seamoo.entities.matching.MatchEventType"].FINISHED]
		<strong>${event.member.displayName}</strong> left
		[#break/]
	[/#switch]
	</td></tr>	
[/#macro]