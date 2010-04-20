package org.seamoo.daos.twigImpl;

import org.seamoo.daos.MemberDao;
import org.seamoo.entities.Member;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.googlecode.objectify.Query;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigMemberDaoImpl extends TwigGenericDaoImpl<Member, Long> implements MemberDao {

	public Member findByOpenId(String openId) {
		// TODO Auto-generated method stub
		RootFindCommand<Member> fc = getOds().find().type(Member.class).addFilter("openId", FilterOperator.EQUAL, openId);
		if (fc.countResultsNow() == 1)
			return fc.returnResultsNow().next();
		return null;
	}

}
