package org.seamoo.daos.twigImpl.speed;

import java.util.List;

import org.seamoo.daos.speed.QuestionEventDao;
import org.seamoo.daos.twigImpl.TwigGenericDaoImpl;
import org.seamoo.speed.QuestionEvent;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigQuestionEventDaoImpl extends TwigGenericDaoImpl<QuestionEvent, Long> implements QuestionEventDao {

	@Override
	public List<QuestionEvent> getByMinimumTimeStamp(Long leagueAutoId, long minimumTimeStamp, int skip, int count) {
		RootFindCommand<QuestionEvent> fc = getOds().find().type(QuestionEvent.class).addFilter("leagueAutoId",
				FilterOperator.EQUAL, leagueAutoId).addFilter("timeStamp", FilterOperator.GREATER_THAN_OR_EQUAL, minimumTimeStamp).addSort(
				"timeStamp", SortDirection.ASCENDING).startFrom(skip);
		return getSegmentedList(fc, count);
	}
}
