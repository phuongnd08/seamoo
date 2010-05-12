package org.seamoo.entities.matching;

import java.io.Serializable;
import java.util.Date;

import org.seamoo.entities.Member;

import com.google.gwt.user.client.rpc.GwtTransient;
import com.vercer.engine.persist.annotation.Store;

public class MatchEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7625209665588405346L;
	private MatchEventType type;

	/**
	 * Member is a transient field. It is present to provide some support to
	 * Client MatchView. A loader should examine the memberAutoId And Assign
	 * this value before sending object to client
	 */
	private Member member;
	private int questionOrder;
	private Date happenTime;

	public MatchEvent() {
	}

	public MatchEvent(MatchEventType type) {
		this.setType(type);
	}

	public MatchEvent(MatchEventType type, Date happenTime, Member member) {
		this(type);
		this.member = member;
		this.happenTime = happenTime;
	}

	public MatchEvent(MatchEventType type, Date happenTime, Member member, int questionOrder) {
		this(type, happenTime, member);
		this.setQuestionOrder(questionOrder);
	}

	public MatchEvent(MatchEventType type, Date happenTime) {
		this(type);
		this.happenTime = happenTime;
	}

	public void setType(MatchEventType type) {
		this.type = type;
	}

	public MatchEventType getType() {
		return type;
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

	public void setHappenTime(Date happenTime) {
		this.happenTime = happenTime;
	}

	public Date getHappenTime() {
		return happenTime;
	}
}
