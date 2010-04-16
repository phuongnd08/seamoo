package org.seamoo.persistence.daos;

import org.seamoo.entities.Member;

public interface MemberDao extends GenericDao<Member, String> {
	public Member findByAutoId(Long autoId);
}
