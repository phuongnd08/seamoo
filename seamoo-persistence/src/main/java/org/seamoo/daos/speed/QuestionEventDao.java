package org.seamoo.daos.speed;

import java.util.List;

import org.seamoo.daos.GenericDao;
import org.seamoo.speed.QuestionEvent;

public interface QuestionEventDao extends GenericDao<QuestionEvent, Long>{
	/**
	 * Return all question event that has timestamp no smaller than minimumTimeStamp ignore first certain element to cope better with 
	 * questions saved in batches.
	 * @param leagueAutoId
	 * @param minimumTimeStamp
	 * @param skip TODO
	 * @param count
	 * @return
	 */
	List<QuestionEvent> getByMinimumTimeStamp(Long leagueAutoId, long minimumTimeStamp, int skip, int count); 
}
