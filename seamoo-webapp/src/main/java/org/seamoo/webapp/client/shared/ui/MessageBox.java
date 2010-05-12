package org.seamoo.webapp.client.shared.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class MessageBox extends Composite {

	public static interface EventListener {
		void select(String buttonName);
	}

	public interface MessageBoxUiBinder extends UiBinder<Widget, MessageBox> {
	}

	private static MessageBoxUiBinder binder = GWT.create(MessageBoxUiBinder.class);
	@UiField
	Label labelMessage;
	@UiField
	FlowPanel panelButtons;
	List<EventListener> listeners;

	public MessageBox() {
		initWidget(binder.createAndBindUi(this));
		listeners = new ArrayList<EventListener>();
	}

	public void setMessage(String message) {
		this.labelMessage.setText(message);
	}

	public void addButton(String name, String caption) {
		Button button = new Button();
		button.setText(caption);
		button.addClickHandler(getSelectTrigger(name));
		panelButtons.add(button);
	}

	private ClickHandler getSelectTrigger(final String name) {
		final MessageBox me = this;
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent clickevent) {
				for (EventListener l : listeners)
					l.select(name);
			}
		};
	}

	public void addEventListener(EventListener listener) {
		listeners.add(listener);
	}

	/**
	 * Show a message box and trigger a select event when user click on a button
	 * 
	 * @param message
	 * @param buttonNames
	 *            Name of button that will be passed to select event
	 * @param buttonCaptions
	 * @param listener
	 *            The listener that will receive the select event
	 */
	public static PopupPanel showPopupPanel(String message, String[] buttonNames, String[] buttonCaptions, EventListener listener) {
		final PopupPanel panel = new PopupPanel();
		MessageBox box = new MessageBox();
		box.addEventListener(listener);
		box.addEventListener(new EventListener() {

			@Override
			public void select(String buttonName) {
				// TODO Auto-generated method stub
				panel.hide();
			}
		});
		box.setMessage(message);
		for (int i = 0; i < buttonNames.length; i++) {
			box.addButton(buttonNames[i], buttonCaptions[i]);
		}
		panel.setWidget(box);
		panel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = (Window.getClientWidth() - offsetWidth) / 2;
				int top = (Window.getClientHeight() - offsetHeight) / 2;
				panel.setPopupPosition(left, top);
			}
		});

		return panel;
	}
}
