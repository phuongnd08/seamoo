package org.seamoo.entities.question;

import org.seamoo.entities.Member;

public abstract class QuestionRevision {
	private Member creator;
	private int revisionNumber;
	private String editSummary;

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

	public void setEditSummary(String editSummary) {
		this.editSummary = editSummary;
	}

	public String getEditSummary() {
		return editSummary;
	}

	/**
	 * Return 1-scale score given for an answer. If question is of multiple
	 * choices, then answer should be 1-based
	 * 
	 * @param answer
	 * @return
	 */
	public abstract double getScore(String answer);
}
