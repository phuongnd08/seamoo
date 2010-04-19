package org.seamoo.daos.ofyImpl;

import org.seamoo.entities.League;
import org.seamoo.entities.SiteSetting;
import org.seamoo.entities.Subject;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.entities.question.QuestionRevision;
import org.seamoo.lookup.NumericBag;

import com.googlecode.objectify.ObjectifyService;

public class OfyModelRegistrar {
	public OfyModelRegistrar() {
		ObjectifyService.register(Subject.class);
		ObjectifyService.register(League.class);
		ObjectifyService.register(SiteSetting.class);
		ObjectifyService.register(Question.class);
		ObjectifyService.register(QuestionRevision.class);
		ObjectifyService.register(MultipleChoicesQuestionRevision.class);
		// ObjectifyService.register(QuestionChoice.class);
		ObjectifyService.register(Match.class);
		ObjectifyService.register(NumericBag.class);
	}
}
