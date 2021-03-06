package org.seamoo.entities.question;

import java.io.Serializable;

import javax.persistence.Id;

import org.seamoo.entities.Member;

import com.vercer.engine.persist.annotation.Key;

public abstract class QuestionRevision implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2192950758622290277L;
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
	
	public abstract String getCorrectAnswer();
	
	public abstract String getTranslatedAnswer(String userAnswer);

	/**
	 * Return 1-scale score given for an answer. If question is of multiple
	 * choices, then answer should be 1-based
	 * 
	 * @param answer
	 * @return
	 */
	public abstract double getScore(String answer);
}
