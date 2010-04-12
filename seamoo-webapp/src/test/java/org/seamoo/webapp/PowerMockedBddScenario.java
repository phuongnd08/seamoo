package org.seamoo.webapp;

import org.jbehave.scenario.JUnitScenario;
import org.jbehave.scenario.MostUsefulConfiguration;
import org.jbehave.scenario.errors.PendingErrorStrategy;
import org.jbehave.scenario.parser.ClasspathScenarioDefiner;
import org.jbehave.scenario.parser.PatternScenarioParser;
import org.jbehave.scenario.parser.PowerMockUnderscoredCamelCaseResolver;
import org.jbehave.scenario.parser.ScenarioDefiner;
import org.jbehave.scenario.steps.StepsFactory;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;

import com.google.gwt.junit.GWTMockUtilities;

public abstract class PowerMockedBddScenario extends JUnitScenario {

	/*
	 * For PowerMock to work with TestNG See <a
	 * href="http://code.google.com/p/powermock/wiki/TestNG_usage"/>
	 */
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	public PowerMockedBddScenario(Object... stepsInstance) {
		super(new MostUsefulConfiguration() {
			public ScenarioDefiner forDefiningScenarios() {
				return new ClasspathScenarioDefiner(
						new PowerMockUnderscoredCamelCaseResolver(".scenario"),
						new PatternScenarioParser(keywords()));
			}

			public PendingErrorStrategy forPendingSteps() {
				return PendingErrorStrategy.FAILING;
			}
		});
		addSteps(new StepsFactory().createCandidateSteps(stepsInstance));
	}
}
