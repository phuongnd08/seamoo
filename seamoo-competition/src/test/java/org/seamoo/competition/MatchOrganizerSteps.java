package org.seamoo.competition;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.jbehave.scenario.annotations.Alias;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.cache.RemoteObjectFactory;
import org.seamoo.cache.memcacheImpl.MemcacheRemoteObjectFactoryImpl;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchAnswer;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.test.MockedTimeProvider;
import org.seamoo.utils.converter.Converter;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class MatchOrganizerSteps {
	List<Match> activeMatches;
	public static final long TEST_LEAGUE_ID = 1L;
	public static final long TEST_LEAGUE2_ID = 2L;

	MemberDao memberDao;
	List<Member> members;
	MockedTimeProvider timeProvider = new MockedTimeProvider();
	MatchOrganizer.EventListener listener = mock(MatchOrganizer.EventListener.class);

	@Given("A list of $number users")
	public void initListOfUsers(int number) {
		members = new ArrayList<Member>();
		for (int i = 0; i < number; i++) {
			Member member = new Member();
			member.setAutoId(new Long(i + 1));
			member.setDisplayName("User #" + (i + 1));
			member.setAlias("user" + (i + 1));
			members.add(member);
		}
		memberDao = mock(MemberDao.class);
		when(memberDao.findByKey(anyLong())).thenAnswer(new Answer<Member>() {

			@Override
			public Member answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				Long autoId = (Long) invocation.getArguments()[0];
				return members.get((int) (autoId - 1));
			}
		});
	}

	List<Question> questions;
	List<Long> questionKeys;

	@Given("$number questions that have correct choices on #$correct")
	public void setUpQuestions(int number, int correct) {
		questions = new ArrayList<Question>();
		questionKeys = new ArrayList<Long>();
		for (int i = 0; i < number; i++) {
			MultipleChoicesQuestionRevision revision = new MultipleChoicesQuestionRevision();
			for (int j = 0; j < 4; j++) {
				revision.addChoice(new QuestionChoice("xx", j == correct - 1));
			}
			Question q = new Question();
			q.addAndSetAsCurrentRevision(revision);
			q.setCurrentRevision(revision);
			q.setAutoId(new Long(i + 1));
			questions.add(q);
			questionKeys.add(q.getAutoId());
		}
	}

	QuestionDao questionDao;

	@Given("A Question Dao")
	public void setUpQuestionDao() {
		questionDao = mock(QuestionDao.class);
		when(questionDao.getRandomQuestionKeys(anyLong(), eq(20))).thenReturn(questionKeys);
		when(questionDao.findByKey(anyLong())).thenAnswer(new Answer<Question>() {

			@Override
			public Question answer(InvocationOnMock invocation) throws Throwable {
				Long key = (Long) invocation.getArguments()[0];
				return questions.get((int) (key - 1));
			}
		});
	}

	MatchDao matchDao;

	@Given("A Match Dao")
	public void setUpMatchDao() {
		matchDao = mock(MatchDao.class);
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Match m = (Match) invocation.getArguments()[0];
				m.setAutoId(1L);
				return null;
			}
		}).when(matchDao).persist((Match) any());
	}

	List<Match> bufferedMatches;
	int invocationTimes = 0;

	@Given("Match Are Observered with buffer of $buffered objects")
	public void setUpMatchObserver(int buffered) throws Exception {
		bufferedMatches = new ArrayList<Match>();
		for (int i = 0; i < buffered; i++) {
			Match m = new Match();
			m.setAutoId(new Long(i + 1));
			m.setDescription("Match #" + (i + 1));
			bufferedMatches.add(m);
		}
		PowerMockito.mockStatic(EntityFactory.class);
		when(EntityFactory.newMatch()).thenAnswer(new Answer<Match>() {

			@Override
			public Match answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return bufferedMatches.get(invocationTimes++);
			}
		});

	}

	RemoteObjectFactory cacheWrapperFactory;
	MatchOrganizer organizer;

	@When("Organizer is created")
	public void initMatchOrganizer() {
		cacheWrapperFactory = new MemcacheRemoteObjectFactoryImpl();
		reinstantiateMatchOrganizer();
	}

	MatchOrganizer organizer2;

	@When("Organizer of league 2 is created")
	public void initLeague2MatchOrganizer() {
		MatchOrganizerSettings settings = new MatchOrganizerSettings();
		settings.setCandidateActivePeriod(5000);// use 5000 for testing purpose
		organizer2 = new MatchOrganizer(TEST_LEAGUE2_ID, settings);
		organizer2.memberDao = memberDao;
		organizer2.questionDao = questionDao;
		organizer2.matchDao = matchDao;
		organizer2.timeProvider = timeProvider;
		organizer2.cacheWrapperFactory = new MemcacheRemoteObjectFactoryImpl();
	}

	@When("Match Organizer is recreated")
	public void reinstantiateMatchOrganizer() {
		MatchOrganizerSettings settings = new MatchOrganizerSettings();
		settings.setCandidateActivePeriod(5000);// use 5000 for testing purpose
		organizer = new MatchOrganizer(TEST_LEAGUE_ID, settings);
		organizer.memberDao = memberDao;
		organizer.questionDao = questionDao;
		organizer.matchDao = matchDao;
		organizer.timeProvider = timeProvider;
		organizer.cacheWrapperFactory = cacheWrapperFactory;
		organizer.addEventListener(listener);
	}

	private Date getDateFromHMS(int hour, int minute, int second) {
		return new Date(2010, 1, 1, hour, minute, second);
	}

	@Given("Current Time is $hour:$minute:$second")
	public void mockCurrentTime(int hour, int minute, int second) {
		Date d = getDateFromHMS(hour, minute, second);
		timeProvider.setCurrentTimeStamp(d.getTime());
	}

	Match match;

	private int positionToNumber(String position) {
		return Converter.toInt(position.charAt(0));
	}

	@When("$position user request for Match")
	public void userRequestForMatch(String position) throws TimeoutException {
		int pos = positionToNumber(position) - 1;
		match = organizer.getMatchForUser(members.get(pos).getAutoId());
	}

	@When("$position user request for Match on league 2")
	public void userRequestForMatchOnLeague2(String position) throws TimeoutException {
		int pos = positionToNumber(position) - 1;
		match = organizer2.getMatchForUser(members.get(pos).getAutoId());
	}

	@Then("Returned Match Phase is $phase")
	public void assertMatchPhase(String phase) {
		MatchPhase p = MatchPhase.valueOf(phase);
		assertEquals(match.getPhase(), p);
	}

	@Then("Returned Match is $position match")
	public void assertMatchReturning(String position) {
		int pos = positionToNumber(position);
		assertEquals(match.getDescription(), "Match #" + pos);

	}

	@Then("Returned Match has $number players")
	@Alias("Returned Match has $number player")
	public void assertNumberOfPlayersInMatch(int number) {
		assertEquals(match.getCompetitors().size(), number);
	}

	@When("$position user submit #$choice for question $qFrom-$qTo")
	public void submitChoices(String position, int choice, int qFrom, int qTo) throws TimeoutException {
		int pos = positionToNumber(position) - 1;
		Member member = members.get(pos);
		for (int i = qFrom; i <= qTo; i++) {
			organizer.submitAnswer(member.getAutoId(), i, String.valueOf(choice));
		}
	}

	@When("$position user ignore question $qFrom-$qTo")
	public void userIgnoreQuestion(String position, int qFrom, int qTo) throws TimeoutException {
		int pos = positionToNumber(position) - 1;
		Member member = members.get(pos);
		for (int i = qFrom; i <= qTo; i++) {
			organizer.ignoreQuestion(member.getAutoId(), i);
		}
	}

	@Then("Question Dao is requested for $number random questions")
	public void assertRandomQuestionsRequested(int number) {
		verify(questionDao, times(1)).getRandomQuestionKeys(anyLong(), eq(number));
	}

	@Then("Question Dao is requested for $number random questions on league $leagueId")
	public void assertRandomQuestionsRequested(int number, long leagueId) {
		verify(questionDao, times(1)).getRandomQuestionKeys(eq(leagueId), eq(number));
	}

	@Then("Returned Match is assigned with $number questions")
	public void assertQuestionsAssignedToMatch(int number) {
		assertEquals(match.getQuestionIds().size(), number);
	}

	@Then("$position Match is persisted")
	public void assertMatchPersisted(String position) {
		final int pos = positionToNumber(position);
		verify(matchDao).persist(argThat(new ArgumentMatcher<Match>() {

			@Override
			public boolean matches(Object argument) {
				// TODO Auto-generated method stub
				Match m = (Match) argument;
				return m.getDescription().equals("Match #" + pos);
			}
		}));
	}

	@Then("$position Match is not persisted")
	public void assertMatchNotPersisted(String position) {
		final int pos = positionToNumber(position);
		verify(matchDao, never()).persist(argThat(new ArgumentMatcher<Match>() {

			@Override
			public boolean matches(Object argument) {
				// TODO Auto-generated method stub
				Match m = (Match) argument;
				return m.getDescription().equals("Match #" + pos);
			}
		}));
	}

	@Given("matchDao forgot its interaction")
	public void resetMatchDao() {
		reset(matchDao);
	}

	@Then("$position user score is $score")
	public void assertScore(String position, double score) throws TimeoutException {
		int pos = positionToNumber(position) - 1;
		Long memberAutoId = members.get(pos).getAutoId();
		Match m = organizer.getMatchForUser(memberAutoId);
		MatchCompetitor competitor = m.getCompetitorForMember(members.get(pos).getAutoId());
		assertEquals(competitor.getTotalScore(), score);
	}

	@Then("$position user ranked $rank")
	public void assertRank(String position, int rank) {
		int pos = positionToNumber(position) - 1;
		MatchCompetitor competitor = match.getCompetitorForMember(members.get(pos).getAutoId());
		assertEquals(competitor.getRank(), rank);
	}

	@Then("$position user has $count answers")
	@Alias("$position user has $count answer")
	public void assertUserQuestionsCount(String position, int count) {
		int pos = positionToNumber(position) - 1;
		Long memberAutoId = members.get(pos).getAutoId();
		Match m = organizer.getMatchForUser(memberAutoId);
		MatchCompetitor competitor = m.getCompetitorForMember(members.get(pos).getAutoId());
		assertEquals(competitor.getAnswers().size(), count);
		for (MatchAnswer a : competitor.getAnswers())
			assertNotNull(a);
	}

	@Then("$position user finished moment assigned")
	public void assertFinishedMomentAssigned(String position) {
		int pos = positionToNumber(position) - 1;
		MatchCompetitor competitor = match.getCompetitorForMember(members.get(pos).getAutoId());
		assertNotSame(competitor.getFinishedMoment(), 0L);
	}

	@Given("Cache is corrupted")
	public void corruptCache() {
		LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig(),
				new LocalDatastoreServiceTestConfig());
		helper.setUp();
	}

	@Then("event finishMatch is triggered")
	public void assertEventFinishMatchTriggered() {
		verify(listener).finishMatch((Match) any());
	}

	@Then("Returned Match League Id is $num")
	public void assertMatchLeagueAutoId(long num) {
		assertEquals(match.getLeagueAutoId(), new Long(num));
	}

	@Then("Returned Match AutoId is $id")
	public void assertMatchAutoId(long id) {
		assertEquals(match.getAutoId(), new Long(id));
	}

	@Then("Returned Match has no AutoId")
	public void assertMatchHasNoAutoId() {
		assertNull(match.getAutoId());
	}

	@Then("Returned Match Alias is \"$alias\"")
	public void assertMatchAlias(String alias) {
		assertEquals(match.getAlias(), alias);
	}
}
