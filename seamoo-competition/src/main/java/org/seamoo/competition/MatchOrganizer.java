package org.seamoo.competition;

import org.seamoo.entities.matching.MatchState;

public class MatchOrganizer {
	/**
	 * A Member try to get his current match state
	 * @param userAutoId
	 * @return
	 */
	public MatchState getMatchState(Long userAutoId) {
		return null;
	}

	/**
	 * A Member trying to escape his current match. He may want to return to his match later
	 * @param userAutoId
	 */
	void escapeCurrentMatch(Long userAutoId) {
		
	}
	
	
	void submitAnswer(Long userAutoId, int questionOrder, String answer){
		
	}
	
	
}
