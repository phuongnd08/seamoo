package org.seamoo.webapp.client.admin.ui;

import java.util.ArrayList;
import java.util.List;

import org.seamoo.entities.League;
import org.seamoo.webapp.client.admin.LeagueListPresenter.LeagueDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LeagueView extends Composite implements LeagueDisplay {
	interface LeagueViewUiBinder extends UiBinder<Widget, LeagueView> {
	}

	private static final LeagueViewUiBinder binder = GWT.create(LeagueViewUiBinder.class);

	@UiField
	Hidden hiddenAutoId;

	@UiField
	Image imgLogo;
	@UiField
	Button buttonSelectLogo;

	@UiField
	HTMLPanel divView;
	@UiField
	Anchor anchorName;
	@UiField
	Anchor buttonEdit;
	@UiField
	Anchor buttonRemove;
	@UiField
	Anchor buttonPreview;
	@UiField
	Label labelDescription;
	@UiField
	Label labelLevel;
	@UiField
	Label labelEnabled;

	@UiField
	HTMLPanel divEdit;
	@UiField
	TextBox textboxName;
	@UiField
	TextArea textareaDescription;
	@UiField
	TextBox textboxLevel;
	@UiField
	CheckBox checkboxEnabled;
	@UiField
	Button buttonCreate;
	@UiField
	Button buttonSave;
	@UiField
	Button buttonCancel;
	@UiField
	HTMLPanel buttonContainer;

	List<LeagueDisplayEventListener> listeners;

	private League league;

	public LeagueView() {
		initWidget(binder.createAndBindUi(this));
		listeners = new ArrayList<LeagueDisplayEventListener>();
		final LeagueView me = this;
		buttonEdit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent clickevent) {
				// TODO Auto-generated method stub
				for (LeagueDisplayEventListener l : listeners)
					l.edit(me);
			}
		});

		buttonCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent clickevent) {
				// TODO Auto-generated method stub
				for (LeagueDisplayEventListener l : listeners)
					l.cancelEdit(me);
			}
		});

		buttonSave.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent clickevent) {
				// TODO Auto-generated method stub
				for (LeagueDisplayEventListener l : listeners)
					l.update(me, me.getEditedLeague());
			}
		});

		buttonCreate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent clickevent) {
				// TODO Auto-generated method stub
				for (LeagueDisplayEventListener l : listeners)
					l.create(me, me.getEditedLeague());
			}
		});

		buttonRemove.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent clickevent) {
				// TODO Auto-generated method stub
				for (LeagueDisplayEventListener l : listeners)
					l.delete(me, me.getLeague());
			}
		});

		buttonSelectLogo.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent clickevent) {
				// TODO Auto-generated method stub
				for (LeagueDisplayEventListener l : listeners)
					l.selectLogo(me, me.getLogoUrl());
			}
		});
	}

	public League getEditedLeague() {
		Long autoId = null;
		String autoIdStr = hiddenAutoId.getValue();
		if (autoIdStr != null && !autoIdStr.equals(""))
			autoId = Long.parseLong(hiddenAutoId.getValue());

		return new League(autoId, textboxName.getText(), imgLogo.getUrl(), textareaDescription.getText(),
				Integer.parseInt(textboxLevel.getText()), checkboxEnabled.getValue());

	}

	public void setMode(LeagueDisplayMode mode) {
		boolean isView = mode == LeagueDisplayMode.VIEW;
		boolean isCreate = mode == LeagueDisplayMode.CREATE;
		boolean isEdit = mode == LeagueDisplayMode.EDIT;
		buttonContainer.setVisible(isEdit || isCreate);
		buttonCreate.setVisible(isCreate);
		buttonSave.setVisible(isEdit);
		buttonCancel.setVisible(isEdit);
		divView.setVisible(isView);
		divEdit.setVisible(isEdit || isCreate);
		buttonSelectLogo.setVisible(isCreate || isEdit);
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
		hiddenAutoId.setValue(league.getAutoId() != null ? league.getAutoId().toString() : "");
		textboxName.setText(league.getName());
		anchorName.setText(league.getName());
		imgLogo.setUrl(league.getLogoUrl());
		textareaDescription.setText(league.getDescription());
		labelDescription.setText(league.getDescription());
		labelLevel.setText("Level " + league.getLevel());
		textboxLevel.setText(String.valueOf(league.getLevel()));
		labelEnabled.setText(league.isEnabled() ? "Enabled" : "Disabled");

		checkboxEnabled.setValue(league.isEnabled());
	}

	@Override
	public void addEventListener(LeagueDisplayEventListener listener) {
		// TODO Auto-generated method stub
		listeners.add(listener);
	}

	@Override
	public String getLogoUrl() {
		// TODO Auto-generated method stub
		return imgLogo.getUrl();
	}

	@Override
	public void setLogoUrl(String url) {
		// TODO Auto-generated method stub
		imgLogo.setUrl(url);
	}
}
