package org.seamoo.daos.twigImpl;

import java.util.HashMap;
import java.util.Map;

import org.seamoo.daos.MemberDao;
import org.seamoo.entities.Member;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigMemberDaoImpl extends TwigGenericDaoImpl<Member, Long> implements MemberDao {

	private Map<Long, Member> memberByKeys;
	private Map<String, Member> memberByOpenIds;
	Map<Long, Long> memberLastCacheByKeys;
	Map<String, Long> memberLastCacheByOpenIds;
	public static final long CACHE_PERIOD = 1 * 3600 * 1000;// 1 hour

	public TwigMemberDaoImpl() {
		memberByKeys = new HashMap<Long, Member>();
		memberByOpenIds = new HashMap<String, Member>();
		memberLastCacheByKeys = new HashMap<Long, Long>();
		memberLastCacheByOpenIds = new HashMap<String, Long>();
	}

	private Member internalFindByOpenId(String openId) {
		// TODO Auto-generated method stub
		RootFindCommand<Member> fc = getOds().find().type(Member.class).addFilter("openId", FilterOperator.EQUAL, openId);
		if (fc.countResultsNow() == 1)
			return fc.returnResultsNow().next();
		return null;
	}

	public Member findByOpenId(String openId) {
		if (!memberByOpenIds.containsKey(openId) || isExpiredByOpenId(openId)) {
			refreshByOpenId(openId);
		}
		return memberByOpenIds.get(openId);
	}

	@Override
	public Member findByKey(Long key) {
		// prefer in-memory cached over datastore
		if (!memberByKeys.containsKey(key) || isExpiredByKey(key))
			refreshByKey(key);
		return memberByKeys.get(key);
	}

	private void refreshByKey(Long key) {
		// TODO Auto-generated method stub
		Member member = super.findByKey(key);
		if (member != null) {
			getOds().refresh(member);
			refreshMember(key, member.getOpenId(), member);
		}
	}

	private void refreshByOpenId(String openId) {
		Member member = internalFindByOpenId(openId);
		if (member != null) {
			getOds().refresh(member);
			refreshMember(member.getAutoId(), openId, member);
		}
	}

	private void refreshMember(Long autoId, String openId, Member member) {
		memberByKeys.put(autoId, member);
		memberByOpenIds.put(openId, member);
		memberLastCacheByKeys.put(autoId, System.currentTimeMillis());
		memberLastCacheByOpenIds.put(openId, System.currentTimeMillis());
	}

	private boolean isExpiredByKey(Long key) {
		// TODO Auto-generated method stub
		return memberLastCacheByKeys.get(key) + CACHE_PERIOD <= System.currentTimeMillis();
	}

	private boolean isExpiredByOpenId(String openId) {
		// TODO Auto-generated method stub
		return memberLastCacheByOpenIds.get(openId) + CACHE_PERIOD <= System.currentTimeMillis();
	}
}
