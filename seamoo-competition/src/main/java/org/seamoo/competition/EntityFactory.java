package org.seamoo.competition;

import org.seamoo.entities.matching.Match;

/**
 * Provide a mockable way to instantiate object to get rid of 
 * the risk of messing byte code due to constructor manipulation
 * @author phuongnd08
 *
 */
public class EntityFactory {

	public static Match newMatch() {
		// TODO Auto-generated method stub
		return new Match();
	}

}
