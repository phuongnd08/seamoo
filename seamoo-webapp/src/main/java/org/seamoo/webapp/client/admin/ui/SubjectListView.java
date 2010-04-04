package org.seamoo.webapp.client.admin.ui;

import org.seamoo.webapp.client.admin.SubjectListPresenter.SubjectDisplay;
import org.seamoo.webapp.client.admin.SubjectListPresenter.SubjectListDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class SubjectListView extends Composite implements SubjectListDisplay {
	interface SubjectListViewUiBinder extends UiBinder<FlowPanel, SubjectListView> {
	}

	private static final SubjectListViewUiBinder binder = GWT.create(SubjectListViewUiBinder.class);

	@UiField
	FlowPanel mainPanel;

	public SubjectListView() {
		initWidget(binder.createAndBindUi(this));
	}

	@Override
	public void addSubjectDisplay(SubjectDisplay subjectDisplay) {
		// TODO Auto-generated method stub
		mainPanel.add((Widget) subjectDisplay);
	}

	@Override
	public void removeSubjectDisplay(SubjectDisplay subjectDisplay) {
		// TODO Auto-generated method stub
		mainPanel.remove((Widget) subjectDisplay);
	}

	@Override
	public SubjectDisplay createSubjectDisplay() {
		// TODO Auto-generated method stub
		return new SubjectView();
	}

}
