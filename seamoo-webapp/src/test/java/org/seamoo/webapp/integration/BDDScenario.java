package org.seamoo.webapp.integration;

import org.jbehave.scenario.JUnitScenario;
import org.jbehave.scenario.MostUsefulConfiguration;
import org.jbehave.scenario.errors.PendingErrorStrategy;
import org.jbehave.scenario.parser.ClasspathScenarioDefiner;
import org.jbehave.scenario.parser.PatternScenarioParser;
import org.jbehave.scenario.parser.ScenarioDefiner;
import org.jbehave.scenario.parser.UnderscoredCamelCaseResolver;
import org.jbehave.scenario.steps.StepsFactory;

public abstract class BDDScenario extends JUnitScenario {
	public BDDScenario(Object... stepsInstance) {
		super(new MostUsefulConfiguration() {
			public ScenarioDefiner forDefiningScenarios() {
				return new ClasspathScenarioDefiner(
						new UnderscoredCamelCaseResolver(".scenario"),
						new PatternScenarioParser(keywords()));
			}

			public PendingErrorStrategy forPendingSteps() {
				return PendingErrorStrategy.FAILING;
			}
		});
		addSteps(new StepsFactory().createCandidateSteps(stepsInstance));
	}

}
