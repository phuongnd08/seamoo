package org.seamoo.entities.question;

import org.seamoo.entities.Member;

public abstract class QuestionRevision {
	private Member creator;
	private int revisionNumber;
	public void setCreator(Member creator) {
		this.creator = creator;
	}
	public Member getCreator() {
		return creator;
	}
	public void setRevisionNumber(int revisionNumber) {
		this.revisionNumber = revisionNumber;
	}
	public int getRevisionNumber() {
		return revisionNumber;
	}
}
