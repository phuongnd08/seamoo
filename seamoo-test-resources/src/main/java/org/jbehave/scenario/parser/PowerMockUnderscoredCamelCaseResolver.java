package org.jbehave.scenario.parser;

import static java.util.regex.Pattern.*;

import java.util.regex.Matcher;

import org.jbehave.scenario.RunnableScenario;

public class PowerMockUnderscoredCamelCaseResolver extends
		AbstractScenarioNameResolver {

	public static final String NUMBERS_AS_LOWER_CASE_LETTERS_PATTERN = "([A-Z].*?)([A-Z]|\\z|_\\$\\$)";
	public static final String NUMBERS_AS_UPPER_CASE_LETTERS_PATTERN = "([A-Z0-9].*?)([A-Z0-9]|\\z)";
	private static final String UNDERSCORE = "_";
	private final String resolutionPattern;

	public PowerMockUnderscoredCamelCaseResolver() {
		this(DEFAULT_EXTENSION);
	}

	public PowerMockUnderscoredCamelCaseResolver(String extension) {
		this(extension, NUMBERS_AS_LOWER_CASE_LETTERS_PATTERN);
	}

	public PowerMockUnderscoredCamelCaseResolver(String extension,
			String resolutionPattern) {
		super(extension);
		this.resolutionPattern = resolutionPattern;
	}

	@Override
	protected String resolveFileName(
			Class<? extends RunnableScenario> scenarioClass) {
		Matcher matcher = compile(resolutionPattern).matcher(
				scenarioClass.getSimpleName());
		int startAt = 0;
		StringBuilder builder = new StringBuilder();
		while (matcher.find(startAt)) {
			builder.append(matcher.group(1).toLowerCase());
			builder.append(UNDERSCORE);
			startAt = matcher.start(2);
		}
		return builder.substring(0, builder.length() - 1);
	}

}