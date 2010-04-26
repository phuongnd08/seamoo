package org.seamoo.competition;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jbehave.scenario.annotations.Alias;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.cache.CacheWrapper;
import org.seamoo.cache.CacheWrapperFactory;
import org.seamoo.cache.memcacheImpl.MemcacheWrapperFactoryImpl;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchAnswer;
import org.seamoo.entities.matching.MatchCandidate;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.utils.converter.Converter;

public class MatchOrganizerSteps {
	List<Match> activeMatches;

	@Given("There is no active match")
	public void setUpNoActiveMatch() {
		activeMatches = new ArrayList<Match>();
	}

	MemberDao memberDao;
	List<Member> members;

	@Given("A list of $number users")
	public void initListOfUsers(int number) {
		members = new ArrayList<Member>();
		for (int i = 0; i < number; i++) {
			Member member = new Member();
			member.setAutoId(new Long(i + 1));
			member.setDisplayName("User #" + (i + 1));
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

	@Given("$number questions that have correct choices on #$correct")
	public void setUpQuestions(int number, int correct) {
		questions = new ArrayList<Question>();
		for (int i = 0; i < number; i++) {
			MultipleChoicesQuestionRevision revision = new MultipleChoicesQuestionRevision();
			for (int j = 0; j < 4; j++) {
				revision.addChoice(new QuestionChoice("xx", j == correct - 1));
			}
			Question q = new Question();
			q.addAndSetAsCurrentRevision(revision);
			q.setCurrentRevision(revision);
			questions.add(q);
		}
	}

	QuestionDao questionDao;

	@Given("A Question Dao")
	public void setUpQuestionDao() {
		questionDao = mock(QuestionDao.class);
		when(questionDao.getRandomQuestions(20)).thenReturn(questions);
	}

	MatchDao matchDao;

	@Given("A Match Dao")
	public void setUpMatchDao() {
		matchDao = mock(MatchDao.class);
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

	CacheWrapperFactory cacheWrapperFactory;
	MatchOrganizer organizer;

	@When("Organizer is created")
	public void initMatchOrganizer() {
		cacheWrapperFactory = new MemcacheWrapperFactoryImpl();
		organizer = new MatchOrganizer();
		reinstantiateMatchOrganizer();
	}

	@When("Match Organizer is recreated")
	public void reinstantiateMatchOrganizer() {
		organizer = new MatchOrganizer();
		organizer.memberDao = memberDao;
		organizer.questionDao = questionDao;
		organizer.matchDao = matchDao;
		organizer.cacheWrapperFactory = cacheWrapperFactory;
	}

	@Then("All Active Matches Are Loaded")
	public void assertActiveMatchesAreLoaded() {
		verify(matchDao).getAllActiveMatches();
	}

	private Date getDateFromHMS(int hour, int minute, int second) {
		return new Date(2010, 1, 1, hour, minute, second);
	}

	@Given("Current Time is $hour:$minute:$second")
	public void mockCurrentTime(int hour, int minute, int second) {
		Date d = getDateFromHMS(hour, minute, second);
		when(TimeStampProvider.getCurrentTimeMilliseconds()).thenReturn(d.getTime());
	}

	Match match;

	private int positionToNumber(String position) {
		return Converter.toInt(position.charAt(0));
	}

	@When("$position user request for Match")
	public void userRequestForMatch(String position) {
		int pos = positionToNumber(position) - 1;
		match = organizer.getMatchForUser(members.get(pos).getAutoId());
	}

	@Then("$position Match is created")
	public void assertMatchCreated(String position) {
		int times = positionToNumber(position);
		PowerMockito.verifyStatic(times(times));
		EntityFactory.newMatch();
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

	@Given("$position user last signal is $hour:$minute:$second")
	public void mockUserLastSignal(String position, int hour, int minute, int second) {
		int pos = positionToNumber(position) - 1;
		CacheWrapper<MatchCandidate> candidateWrapper = cacheWrapperFactory.createCacheWrapper(MatchCandidate.class, members.get(
				pos).getAutoId().toString());
		MatchCandidate candidate = candidateWrapper.getObject();
		candidate.setLastSeenMoment(getDateFromHMS(hour, minute, second).getTime());
		candidateWrapper.putObject(candidate);
	}

	@When("$position user submit #$choice for question $qFrom-$qTo")
	public void submitChoices(String position, int choice, int qFrom, int qTo) {
		int pos = positionToNumber(position) - 1;
		Member member = members.get(pos);
		for (int i = qFrom; i <= qTo; i++) {
			organizer.submitAnswer(member.getAutoId(), i, String.valueOf(choice));
		}
	}

	@When("$position user ignore question $qFrom-$qTo")
	public void userIgnoreQuestion(String position, int qFrom, int qTo) {
		int pos = positionToNumber(position) - 1;
		Member member = members.get(pos);
		for (int i = qFrom; i <= qTo; i++) {
			organizer.ignoreQuestion(member.getAutoId(), i);
		}
	}

	@Then("Question Dao is requested for $number random questions")
	public void assertRandomQuestionsRequested(int number) {
		verify(questionDao, times(1)).getRandomQuestions(eq(number));
	}

	@Then("Returned Match is assigned with $number questions")
	public void assertQuestionsAssignedToMatch(int number) {
		assertEquals(match.getQuestions().size(), number);
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

	@Then("Returned Match has $number events")
	@Alias("Returned Match has $number event")
	public void assertMatchPersisted(int number) {
		assertEquals(match.getEvents().size(), number);
	}

	@Then("$position user score is $score")
	public void assertScore(String position, double score) {
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

	@Given("Cache of notFullList is corrupted")
	public void corruptNotFullListCache() {
		CacheWrapper<List> notFullListCache = cacheWrapperFactory.createCacheWrapper(List.class,
				MatchOrganizer.NOT_FULL_WAITING_MATCHES_KEY);
		notFullListCache.putObject(null);
	}

	@Given("Cache of $position match is corrupted")
	public void corruptMatchCache(String position) {
		int pos = positionToNumber(position) - 1;
		match = bufferedMatches.get(pos);
		CacheWrapper<Match> matchWrapper = cacheWrapperFactory.createCacheWrapper(Match.class, match.getTemporalUUID());
		matchWrapper.putObject(null);
	}

	@Given("Cache of $position match candidate corrupted")
	public void corruptMatchCandidateCache(String position) {
		int pos = positionToNumber(position) - 1;
		Member member = members.get(pos);
		CacheWrapper<MatchCandidate> candidateWrapper = cacheWrapperFactory.createCacheWrapper(MatchCandidate.class,
				member.getAutoId().toString());
		candidateWrapper.putObject(null);
	}
}
