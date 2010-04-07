package org.seamoo.persistence.daos;

import java.util.List;

import org.seamoo.entities.Subject;

public interface SubjectDao extends GenericDao<Subject, Long> {
	public List<Subject> getEnabledSubjects();
}
