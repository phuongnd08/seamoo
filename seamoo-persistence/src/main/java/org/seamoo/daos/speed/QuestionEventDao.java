package org.seamoo.daos.speed;

import java.util.List;

import org.seamoo.daos.GenericDao;
import org.seamoo.speed.QuestionEvent;

public interface QuestionEventDao extends GenericDao<QuestionEvent, Long>{
	List<QuestionEvent> getAllByMinimumTimeStamp(Long leagueAutoId, long minimumTimeStamp, int count); 
}
