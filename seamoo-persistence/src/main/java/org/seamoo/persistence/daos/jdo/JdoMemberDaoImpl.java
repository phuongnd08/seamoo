package org.seamoo.persistence.daos.jdo;

import org.seamoo.entities.Member;
import org.seamoo.persistence.daos.MemberDao;

public class JdoMemberDaoImpl extends JdoGenericDaoImpl<Member, String> implements MemberDao {

	public Member findByAutoId(Long autoId) {
		// TODO Auto-generated method stub
		return null;
	}

}
