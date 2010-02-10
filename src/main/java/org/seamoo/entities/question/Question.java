package org.seamoo.entities.question;

import java.util.Date;
import java.util.List;

import org.seamoo.entities.Member;
import org.seamoo.entities.Votable;

public class Question extends Votable{
	private Date addedTime;//used for faster access, can be deducted from first revision
	private Date lastModifiedTime;//used for faster access, can be deducted from last revision
	private QuestionRevision currentRevision;
	private List<QuestionRevision> revisions;
	private Member originator;
	private QuestionType type;
	private List<Tag> tags;
	
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
	public void setRevisions(List<QuestionRevision> revisions) {
		this.revisions = revisions;
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
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	public List<Tag> getTags() {
		return tags;
	}
	
}
