package org.seamoo.webapp.client.uimocker;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.mockito.MockSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.seamoo.webapp.client.shared.ListenerMixin;
import org.seamoo.webapp.client.user.ui.QuestionRevisionView;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class GwtUiMocker {

	/*
	 * Mock field of object annotated with UiField and return a Widget that can be used for object (probably another Widget) to
	 * wrap around
	 */
	public static <T> T mockUiField(Object control, Class<T> clazz) {
		for (Field field : control.getClass().getDeclaredFields()) {
			if (field.getAnnotation(UiField.class) != null) {
				try {
					Object value = getMockedWidget(field.getType());
					field.setAccessible(true);
					field.set(control, value);
				} catch (Exception ex) {
					throw new RuntimeException(String.format("Error while setting %s over control of %s", field.getName(),
							control.getClass().getCanonicalName()), ex);
				}
			}
		}
		return mock(clazz);
	}

	public static <T> T getMockedWidget(Class<T> clazz) {
		if (!Widget.class.isAssignableFrom(clazz))
			throw new RuntimeException(String.format("Mocking for %s not supported because it doesnot sub-class %s",
					clazz.getCanonicalName(), Widget.class.getCanonicalName()));
		boolean isImage = clazz == Image.class;
		boolean hasText = HasText.class.isAssignableFrom(clazz);
		boolean hasValue = HasValue.class.isAssignableFrom(clazz);
		boolean clickable = HasClickHandlers.class.isAssignableFrom(clazz);
		boolean isQuestionView = QuestionRevisionView.class.isAssignableFrom(clazz);
		boolean hasChangeHandlers = HasChangeHandlers.class.isAssignableFrom(clazz);
		MockSettings mockSettings = withSettings();
		List<Class<?>> clazzes = new ArrayList<Class<?>>();
		if (clickable) {
			clazzes.add(MockedClickable.class);
		}
		if (hasChangeHandlers) {
			clazzes.add(MockedChangeable.class);
		}
		if (clazzes.size() > 0)
			mockSettings.extraInterfaces(clazzes.toArray(new Class<?>[clazzes.size()]));
		T widget = mock(clazz, mockSettings);
		final MockedWidgetHelper helper = new MockedWidgetHelper();
		mockVisibleAccessor(helper, (Widget) widget);
		if (clickable)
			mockClickHandler(helper, (HasClickHandlers) widget);
		if (isImage)
			mockUrlAccessor(helper, (Image) widget);
		if (hasText)
			mockTextAccessor(helper, (HasText) widget);
		if (hasValue)
			mockValueAccessor(helper, (HasValue) widget);
		if (isQuestionView)
			mockListenerMixin(helper, (QuestionRevisionView) widget);
		if (hasChangeHandlers)
			mockChangeHandlers(helper, (HasChangeHandlers) widget);
		return widget;
	}

	private static void mockValueAccessor(final MockedWidgetHelper helper, HasValue widget) {
		// TODO Auto-generated method stub
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return helper.getField("value");
			}
		}).when(widget).getValue();

		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				helper.setField("value", invocation.getArguments()[0]);
				return null;
			}
		}).when(widget).setValue(any());

	}

	private static void mockVisibleAccessor(final MockedWidgetHelper helper, Widget widget) {
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return helper.getField("visible");
			}
		}).when(widget).isVisible();

		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				helper.setField("visible", invocation.getArguments()[0]);
				return null;
			}
		}).when(widget).setVisible(anyBoolean());

	}

	private static void mockUrlAccessor(final MockedWidgetHelper helper, Image image) {
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return helper.getField("url");
			}
		}).when(image).getUrl();

		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				helper.setField("url", invocation.getArguments()[0]);
				return null;
			}
		}).when(image).setUrl(anyString());
	}

	private static void mockTextAccessor(final MockedWidgetHelper helper, HasText widget) {
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return helper.getField("text");
			}
		}).when(widget).getText();

		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				helper.setField("text", invocation.getArguments()[0]);
				return null;
			}
		}).when(widget).setText(anyString());
	}

	private static void mockClickHandler(final MockedWidgetHelper helper, HasClickHandlers widget) {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				helper.addClickHandler((ClickHandler) invocation.getArguments()[0]);
				return null;
			}
		}).when(widget).addClickHandler((ClickHandler) any());

		MockedClickable widgetAsClickable = (MockedClickable) widget;

		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				helper.click();
				return null;
			}
		}).when(widgetAsClickable).click();
	}

	private static void mockChangeHandlers(final MockedWidgetHelper helper, HasChangeHandlers widget) {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				helper.addHandler("change", invocation.getArguments()[0]);
				return null;
			}

		}).when(widget).addChangeHandler((ChangeHandler) any());

		MockedChangeable widgetAsChangeable = (MockedChangeable) widget;

		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				helper.change();
				return null;
			}
		}).when(widgetAsChangeable).change();
	}

	private static void mockListenerMixin(final MockedWidgetHelper helper, QuestionRevisionView widget) {
		when(widget.getListenerMixin()).thenAnswer(new Answer<ListenerMixin>() {

			@Override
			public ListenerMixin answer(InvocationOnMock invocation) throws Throwable {
				return (ListenerMixin) helper.getField("listenerMixin");
			}
		});

		helper.setField("listenerMixin", new ListenerMixin());
	}
}
