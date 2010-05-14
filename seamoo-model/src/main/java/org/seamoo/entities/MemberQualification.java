package org.seamoo.entities;

import com.vercer.engine.persist.annotation.Key;

public class MemberQualification {
	@Key
	private Long autoId;
	private Long memberAutoId;
	private int level;
	private long subjectAutoId;
	private int updatedYear;
	private int updatedMonth;

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public Long getAutoId() {
		return autoId;
	}

	public void setMemberAutoId(Long memberAutoId) {
		this.memberAutoId = memberAutoId;
	}

	public Long getMemberAutoId() {
		return memberAutoId;
	}

	public void setSubjectAutoId(long subjectAutoId) {
		this.subjectAutoId = subjectAutoId;
	}

	public long getSubjectAutoId() {
		return subjectAutoId;
	}

	public void setUpdatedYear(int updatedYear) {
		this.updatedYear = updatedYear;
	}

	public int getUpdatedYear() {
		return updatedYear;
	}

	public void setUpdatedMonth(int updatedMonth) {
		this.updatedMonth = updatedMonth;
	}

	public int getUpdatedMonth() {
		return updatedMonth;
	}
}
