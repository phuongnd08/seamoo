package org.seamoo.entities.question;

import java.util.Date;

import org.seamoo.entities.Member;

public class Comment {
	private Member author;
	private String content;
	private Date postedTime;
	private Date lastEditTime;
	public void setAuthor(Member author) {
		this.author = author;
	}
	public Member getAuthor() {
		return author;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setPostedTime(Date postedTime) {
		this.postedTime = postedTime;
	}
	public Date getPostedTime() {
		return postedTime;
	}
}
