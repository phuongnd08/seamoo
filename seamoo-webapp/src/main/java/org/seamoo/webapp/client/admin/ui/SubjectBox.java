package org.seamoo.webapp.client.admin.ui;

import org.seamoo.entities.Subject;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;

public class SubjectBox {
	private Element baseElement;

	public SubjectBox(Element element, SubjectBoxMode mode) {
		this.baseElement = element;
		setMode(mode);
		GQuery.$(getEditButton()).click(new Function() {
			@Override
			public void f(Element e) {
				// TODO Auto-generated method stub
				setMode(SubjectBoxMode.EDIT);
			}
		});
	}

	private InputElement getAutoIdInput() {
		return (InputElement) GQuery.$("input#autoId", baseElement).get(0);
	}

	private LabelElement getNameLabel() {
		return (LabelElement) GQuery.$("label#name", baseElement).get(0);
	}

	private ParagraphElement getDescriptionParagraph() {
		return (ParagraphElement) GQuery.$("p#description", baseElement).get(0);
	}

	private LabelElement getEnabledLabel() {
		return (LabelElement) GQuery.$("label#enabled", baseElement).get(0);
	}

	private InputElement getNameInput() {
		return (InputElement) GQuery.$("input#name", baseElement).get(0);
	}

	private TextAreaElement getDescriptionTextArea() {
		return (TextAreaElement) GQuery.$("textarea#description", baseElement)
				.get(0);
	}

	private InputElement getEnabledCheckbox() {
		return (InputElement) GQuery.$("input#enabled", baseElement).get(0);
	}

	private InputElement getCreateButton() {
		return (InputElement) GQuery.$("input#create", baseElement).get(0);
	}

	private InputElement getSaveButton() {
		return (InputElement) GQuery.$("input#save", baseElement).get(0);
	}

	private InputElement getCancelButton() {
		return (InputElement) GQuery.$("input#cancel", baseElement).get(0);
	}

	private TableRowElement getButtonContainer() {
		return (TableRowElement) GQuery.$("tr#buttonContainer", baseElement)
				.get(0);
	}

	private AnchorElement getEditButton() {
		return (AnchorElement) GQuery.$("a#edit", baseElement).get(0);
	}

	private AnchorElement getDeleteButton() {
		return (AnchorElement) GQuery.$("a#edit", baseElement).get(0);
	}

	/*
	 * Change the nearest div/span/tr parent of the element CSS to make it
	 * visible/invisible
	 */
	private void setElementWrapperVisible(Element element, boolean visible) {
		if (element.getNodeName().toLowerCase().matches("^(div|span|tr)$")) {
			if (visible)
				GQuery.$(element).show();
			else
				GQuery.$(element).hide();
		}
	}

	private void setMode(SubjectBoxMode mode) {
		setElementWrapperVisible(getButtonContainer(),
				mode != SubjectBoxMode.VIEW);
		setElementWrapperVisible(getSaveButton(), mode == SubjectBoxMode.EDIT);
		setElementWrapperVisible(getCancelButton(), true);
		setElementWrapperVisible(getCreateButton(),
				mode == SubjectBoxMode.CREATE);

		// show/hide input panel
		setElementWrapperVisible(getNameInput().getParentElement()
				.getParentElement(), mode != SubjectBoxMode.VIEW);

		// show/hide display panel
		setElementWrapperVisible(getNameLabel().getParentElement()
				.getParentElement(), mode == SubjectBoxMode.VIEW);
	}

	public Subject getSubject() {
		return new Subject(Long.parseLong(getAutoIdInput().getValue()),
				getNameInput().getValue(), null, getDescriptionTextArea()
						.getValue(), getEnabledCheckbox().isChecked());
	}

	public void setSubject(Subject subject) {
		getAutoIdInput().setValue(
				subject.getAutoId() != null ? subject.getAutoId().toString()
						: "");
		getNameInput().setValue(subject.getName());
		getNameLabel().setInnerText(subject.getName());
		getDescriptionTextArea().setValue(subject.getDescription());
		getDescriptionParagraph().setInnerText(subject.getDescription());
		getEnabledCheckbox().setChecked(subject.isEnabled());
		getEnabledLabel().setInnerText(
				subject.isEnabled() ? "Enabled" : "Disabled");
	}
}
