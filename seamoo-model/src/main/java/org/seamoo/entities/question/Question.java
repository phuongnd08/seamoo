package org.seamoo.entities.question;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seamoo.entities.League;
import org.seamoo.entities.Member;
import org.seamoo.entities.Votable;

import com.vercer.engine.persist.annotation.Child;

public class Question extends Votable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7123889816881222646L;

	private Date addedTime;// used for faster access, can be deducted from first
	// revision

	private Date lastModifiedTime;// used for faster access, can be deducted
	// from last revision

	private QuestionRevision currentRevision;
	
	@Child
	private List<QuestionRevision> revisions;

	private Long leagueAutoId;
	private Member originator;

	private QuestionType type;

	private List<Tag> tags;

	private String alias;

	public Question() {
		this.revisions = new ArrayList<QuestionRevision>();
		this.tags = new ArrayList<Tag>();
	}

	public void setAddedTime(Date addedTime) {
		this.addedTime = addedTime;
	}

	public Date getAddedTime() {
		return addedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setCurrentRevision(QuestionRevision currentRevision) {
		this.currentRevision = currentRevision;
	}

	public QuestionRevision getCurrentRevision() {
		return currentRevision;
	}

	public void addAndSetAsCurrentRevision(QuestionRevision revision) {
		this.revisions.add(revision);
		this.setCurrentRevision(revision);
	}

	public List<QuestionRevision> getRevisions() {
		return revisions;
	}

	public void setOriginator(Member originator) {
		this.originator = originator;
	}

	public Member getOriginator() {
		return originator;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public QuestionType getType() {
		return type;
	}

	public void clearTags() {
		this.tags.clear();
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}

	/**
	 * Return 1-scale score of an answer If question is of multiple choices, then answer should be 1-based
	 * 
	 * @param answer
	 * @return
	 */
	public double getScore(String answer) {
		return currentRevision.getScore(answer);
	}

	public void setLeagueAutoId(Long leagueAutoId) {
		this.leagueAutoId = leagueAutoId;
	}

	public Long getLeagueAutoId() {
		return leagueAutoId;
	}
}
