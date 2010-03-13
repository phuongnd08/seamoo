package org.seamoo.entities.question;

import java.util.Date;

import org.seamoo.entities.Member;

public class Report {
	private Member reporter;
	private Question question;
	private String content;
	private Date submittedTime;
	private ReportStatus status;
	private ReportJudgement judgement;
	public void setReporter(Member reporter) {
		this.reporter = reporter;
	}
	public Member getReporter() {
		return reporter;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public Question getQuestion() {
		return question;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setSubmittedTime(Date submittedTime) {
		this.submittedTime = submittedTime;
	}
	public Date getSubmittedTime() {
		return submittedTime;
	}
	public void setStatus(ReportStatus status) {
		this.status = status;
	}
	public ReportStatus getStatus() {
		return status;
	}
	public void setJudgement(ReportJudgement judgement) {
		this.judgement = judgement;
	}
	public ReportJudgement getJudgement() {
		return judgement;
	}
}
