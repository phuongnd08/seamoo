package org.seamoo.daos;

import org.seamoo.entities.MemberQualification;

public interface MemberQualificationDao extends GenericDao<MemberQualification, Long> {
	MemberQualification findByMemberAndSubject(Long memberAutoId, Long subjectAutoId);
}
