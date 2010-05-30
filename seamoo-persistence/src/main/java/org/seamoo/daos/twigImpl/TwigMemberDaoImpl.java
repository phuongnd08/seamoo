package org.seamoo.daos.twigImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.seamoo.daos.MemberDao;
import org.seamoo.entities.Member;
import org.seamoo.utils.SetBuilder;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigMemberDaoImpl extends TwigGenericDaoImpl<Member, Long> implements MemberDao {

	public static final int CACHE_PERIOD = 1 * 3600 * 1000;// 1 hour

	public TwigMemberDaoImpl() {
		super(SetBuilder.newSet("openId"), new FieldGetter<Member>() {

			@Override
			public Object getField(Member object, String field) {
				if (field.equals("openId"))
					return object.getOpenId();
				else
					throw new RuntimeException("Field not supported");
			}

			@Override
			public Object getKey(Member object) {
				return object.getAutoId();
			}
		}, CACHE_PERIOD);
	}

	public Member findByOpenId(String openId) {
		return findByField("openId", openId);
	}

}
