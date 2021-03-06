package org.seamoo.daos.twigImpl;

import org.seamoo.daos.MemberQualificationDao;
import org.seamoo.entities.MemberQualification;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigMemberQualificationDaoImpl extends TwigGenericDaoImpl<MemberQualification, Long> implements
		MemberQualificationDao {

	@Override
	public MemberQualification findByMemberAndSubject(Long memberAutoId, Long subjectAutoId) {
		RootFindCommand<MemberQualification> fc = getOds().find().type(MemberQualification.class).addFilter("memberAutoId",
				FilterOperator.EQUAL, memberAutoId).addFilter("subjectAutoId", FilterOperator.EQUAL, subjectAutoId);
		if (fc.countResultsNow() == 0)
			return null;
		return fc.returnResultsNow().next();
	}
}
