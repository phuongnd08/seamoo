package org.seamoo.entities.matching;

public enum MatchPhase {
	/**
	 * Only one user has requested and joined the match
	 */
	NOT_FORMED,
	/**
	 * More than one user has requested and joined the match
	 */
	FORMED,
	/**
	 * The match is started
	 */
	PLAYING,
	/**
	 * Current requesting user has finished the match
	 */
	YOU_FINISHED,
	/**
	 * All user has finished the match or the match is time out
	 */
	FINISHED,
	/**
	 * The match is about to started but doesn't have enough active member
	 */
	CANCELLED
}
