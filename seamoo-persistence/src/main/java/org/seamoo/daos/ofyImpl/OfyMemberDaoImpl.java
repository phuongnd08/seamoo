package org.seamoo.daos.ofyImpl;

import org.seamoo.daos.MemberDao;
import org.seamoo.entities.Member;

import com.googlecode.objectify.Query;

public class OfyMemberDaoImpl extends OfyGenericDaoImpl<Member, Long> implements MemberDao {

	public Member findByOpenId(String openId) {
		// TODO Auto-generated method stub
		Query<Member> q = getOfy().query(Member.class).filter("openId =", openId);
		return q.get();
	}

}
