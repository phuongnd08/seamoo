package org.seamoo.webapp.client.user.ui;

import java.util.List;

import org.seamoo.entities.Member;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.webapp.client.shared.GwtUrlFactory;
import org.seamoo.webapp.client.user.MatchBoard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ProgressBar;

public class CompetitorView extends Composite {

	public interface CompetitorViewUiBinder extends UiBinder<Widget, CompetitorView> {
	}

	private static final CompetitorViewUiBinder binder = GWT.create(CompetitorViewUiBinder.class);
	@UiField
	Image imageAvatar;
	@UiField
	Anchor anchorDisplayName;
	@UiField
	Label labelQuote;
	@UiField
	ProgressBar barAnsweredQuestionsIndicator;
	MatchCompetitor competitor;

	List<MatchBoard.Display.EventListener> listeners;

	public CompetitorView() {
		initWidget(binder.createAndBindUi(this));
		barAnsweredQuestionsIndicator.setMinProgress(0);
		barAnsweredQuestionsIndicator.setTextVisible(false);
	}

	public static final int AVATAR_SIZE = 32;

	public void setCompetitor(MatchCompetitor competitor) {
		Member m = competitor.getMember();
		String avatarUrl = GwtUrlFactory.getAvatarUrl(m.getEmailHash(), AVATAR_SIZE);
		imageAvatar.setUrl(avatarUrl);
		anchorDisplayName.setText(m.getDisplayName());
		anchorDisplayName.setHref(GwtUrlFactory.getUserViewUrl(m));
		labelQuote.setText(m.getQuote());
		barAnsweredQuestionsIndicator.setVisible(competitor.getPassedQuestionCount() > 0);
		barAnsweredQuestionsIndicator.setProgress(competitor.getPassedQuestionCount());
		this.competitor = competitor;
	}

	public MatchCompetitor getCompetitor() {
		return this.competitor;
	}

	public void setTotalQuestionsCount(int count) {
		barAnsweredQuestionsIndicator.setMaxProgress(count);
	}
}
