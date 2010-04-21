package org.seamoo.entities.matching;

import java.io.Serializable;

import org.seamoo.entities.Member;

public class MatchEvent implements Serializable{
	private MatchEventType type;
	private Member member;
	private int questionOrder;

	public MatchEvent() {
	}

	public MatchEvent(MatchEventType type) {
		this.setType(type);
	}

	public MatchEvent(MatchEventType type, Member member) {
		this(type);
		this.setMember(member);
	}

	public MatchEvent(MatchEventType type, Member member, int questionOrder) {
		this(type, member);
		this.setQuestionOrder(questionOrder);
	}

	public void setType(MatchEventType type) {
		this.type = type;
	}

	public MatchEventType getType() {
		return type;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getMember() {
		return member;
	}

	public void setQuestionOrder(int questionOrder) {
		this.questionOrder = questionOrder;
	}

	public int getQuestionOrder() {
		return questionOrder;
	}
}
