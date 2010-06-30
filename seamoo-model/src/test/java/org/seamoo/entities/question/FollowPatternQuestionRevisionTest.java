package org.seamoo.entities.question;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class FollowPatternQuestionRevisionTest {
	@Test
	public void getCorrectAnswerShouldReturnSimpleText() {
		FollowPatternQuestionRevision rev = new FollowPatternQuestionRevision();
		rev.setPattern("Wo[w]");
		assertEquals(rev.getCorrectAnswer(), "Wow");
	}
	
	@Test
	public void getScoreShouldReturnCorrectScore(){
		FollowPatternQuestionRevision rev = new FollowPatternQuestionRevision();
		rev.setPattern("Wo[w]");
		assertEquals(rev.getScore("Wow"), 1.0);
		assertEquals(rev.getScore("WOW"), 1.0);
		assertEquals(rev.getScore("Woy"), 0.0);
	}
}
