package org.seamoo.daos.twigImpl.speed;

import static org.testng.Assert.*;

import java.util.List;

import org.seamoo.persistence.test.LocalAppEngineTest;
import org.seamoo.speed.QuestionEvent;
import org.seamoo.speed.QuestionEventType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TwigQuestionEventDaoImplTest extends LocalAppEngineTest {

	TwigQuestionEventDaoImpl questionEventDaoImpl;

	@BeforeMethod
	public void setUp() {
		super.setUp();
		questionEventDaoImpl = new TwigQuestionEventDaoImpl();
	}

	Object[] toIdArray(List<QuestionEvent> questionEvents) {
		Object[] results = new Long[questionEvents.size()];
		int i = -1;
		for (QuestionEvent qe : questionEvents)
			results[++i] = qe.getAutoId();
		return results;
	}

	@Test
	public void getAllByMinimumTimeStampShouldReportItemAccordingly() {
		QuestionEvent e1 = new QuestionEvent(QuestionEventType.CREATE, 1L, 1L, 100);
		QuestionEvent e2 = new QuestionEvent(QuestionEventType.CREATE, 2L, 1L, 100);
		QuestionEvent e3 = new QuestionEvent(QuestionEventType.CREATE, 4L, 1L, 125);
		QuestionEvent e4 = new QuestionEvent(QuestionEventType.CREATE, 3L, 1L, 120);
		QuestionEvent e5 = new QuestionEvent(QuestionEventType.CREATE, 4L, 2L, 120);
		questionEventDaoImpl.persist(new QuestionEvent[] { e1, e2, e3, e4, e5 });
		assertEquals(toIdArray(questionEventDaoImpl.getByMinimumTimeStamp(1L, 99, 0, 5)), new Object[] { e1.getAutoId(),
				e2.getAutoId(), e4.getAutoId(), e3.getAutoId() });
		assertEquals(toIdArray(questionEventDaoImpl.getByMinimumTimeStamp(1L, 100, 1, 5)), new Object[] { e2.getAutoId(),
				e4.getAutoId(), e3.getAutoId() });
		assertEquals(toIdArray(questionEventDaoImpl.getByMinimumTimeStamp(1L, 100, 2, 5)), new Object[] { e4.getAutoId(),
				e3.getAutoId() });
		assertEquals(toIdArray(questionEventDaoImpl.getByMinimumTimeStamp(1L, 120, 2, 5)), new Object[] {});
	}

}
