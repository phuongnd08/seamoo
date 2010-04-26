package org.seamoo.entities.matching;

import java.io.Serializable;

import org.seamoo.entities.Member;

import com.vercer.engine.persist.annotation.Store;

public class MatchEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7625209665588405346L;
	private MatchEventType type;
	private Long memberAutoId;

	@Store(false)
	/**
	 * Member is a transient field. It is present to provide some support
	 * to Client MatchView. A loader should examine the memberAutoId
	 * And Assign this value before sending object to client
	 */
	private Member member;
	private int questionOrder;

	public MatchEvent() {
	}

	public MatchEvent(MatchEventType type) {
		this.setType(type);
	}

	public MatchEvent(MatchEventType type, Long memberAutoId) {
		this(type);
		this.setMemberAutoId(memberAutoId);
	}

	public MatchEvent(MatchEventType type, Long memberAutoId, int questionOrder) {
		this(type, memberAutoId);
		this.setQuestionOrder(questionOrder);
	}

	public void setType(MatchEventType type) {
		this.type = type;
	}

	public MatchEventType getType() {
		return type;
	}

	public void setMemberAutoId(Long autoId) {
		this.memberAutoId = autoId;
	}

	public Long getMemberAutoId() {
		return memberAutoId;
	}

	public void setQuestionOrder(int questionOrder) {
		this.questionOrder = questionOrder;
	}

	public int getQuestionOrder() {
		return questionOrder;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getMember() {
		return member;
	}
}
