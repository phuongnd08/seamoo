package org.seamoo.entities.training;

import java.util.List;

import org.seamoo.entities.Member;

public class TrainingBook {
	private Member member;
	private List<TrainingTag> trainingTags;
	public void setMember(Member member) {
		this.member = member;
	}
	public Member getMember() {
		return member;
	}
	public void setTrainingTags(List<TrainingTag> trainingTags) {
		this.trainingTags = trainingTags;
	}
	public List<TrainingTag> getTrainingTags() {
		return trainingTags;
	}
}
