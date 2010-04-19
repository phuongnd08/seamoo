package org.seamoo.daos;

import org.seamoo.entities.Member;

public interface MemberDao extends GenericDao<Member, Long> {
	public Member findByOpenId(String openId);
}
