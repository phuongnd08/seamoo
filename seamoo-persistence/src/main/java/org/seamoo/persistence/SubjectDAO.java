package org.seamoo.persistence;

import java.util.List;

import org.seamoo.entities.Subject;

public interface SubjectDAO extends GenericDAO<Subject, Long> {
	public List<Subject> getEnabledSubjects();
}
