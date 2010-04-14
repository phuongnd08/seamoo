package org.seamoo.entities.question;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.seamoo.entities.Member;
import org.seamoo.entities.Votable;

@PersistenceCapable
public class Question extends Votable {
	@Persistent
	private Date addedTime;// used for faster access, can be deducted from first
							// revision
	@Persistent
	private Date lastModifiedTime;// used for faster access, can be deducted
									// from last revision
	@Persistent
	private QuestionRevision currentRevision;
	@Persistent
	private List<QuestionRevision> revisions;
	@Persistent
	private Member originator;
	@Persistent
	private QuestionType type;
	@Persistent
	private List<Tag> tags;
	
	@Persistent
	private String alias;

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

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}

}
