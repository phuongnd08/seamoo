package org.seamoo.daos.jdoImpl;

import org.seamoo.daos.MemberDao;
import org.seamoo.entities.Member;

public class JdoMemberDaoImpl extends JdoGenericDaoImpl<Member, Long> implements MemberDao {

	@Override
	public Member findByOpenId(String openId) {
		// TODO Auto-generated method stub
		return null;
	}

}
