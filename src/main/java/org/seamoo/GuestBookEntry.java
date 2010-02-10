package org.seamoo;

import java.util.Date;
import javax.jdo.annotations.*;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class GuestBookEntry {
	@PrimaryKey()
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String author;
	@Persistent
	private String content;
	@Persistent
	private Date postedDate;

	public GuestBookEntry(String author, String content) {
		this.setAuthor(author);
		this.setContent(content);
		this.setPostedDate(new Date());
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	public Date getPostedDate() {
		return postedDate;
	}
}
