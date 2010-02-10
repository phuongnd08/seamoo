package org.seamoo.entities.question;

import org.seamoo.entities.Member;

public class QuestionTrack {
	private int newCommentCount;
	private int newModificationCount;
	private Question question;
	private Member member;

	public void setNewCommentCount(int newCommentCount) {
		this.newCommentCount = newCommentCount;
	}

	public int getNewCommentCount() {
		return newCommentCount;
	}

	public void setNewModificationCount(int newModificationCount) {
		this.newModificationCount = newModificationCount;
	}

	public int getNewModificationCount() {
		return newModificationCount;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Question getQuestion() {
		return question;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getMember() {
		return member;
	}
}
