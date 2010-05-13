package org.seamoo.daos;

import org.seamoo.entities.MemberQualification;

public interface MemberQualificationDao extends GenericDao<MemberQualification, Long> {
	MemberQualification findByMember(Long memberAutoId);
}
