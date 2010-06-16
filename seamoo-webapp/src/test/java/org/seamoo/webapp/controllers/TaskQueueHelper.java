package org.seamoo.webapp.controllers;

import static org.testng.Assert.*;

import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import com.google.appengine.api.labs.taskqueue.TaskOptions;

public class TaskQueueHelper {

	public static void assertParam(Object param, String name, String value) {
		assertEquals(ReflectionTestUtils.getField(param, "name"), name);
		assertEquals(ReflectionTestUtils.getField(param, "value"), value);
	}

	public static List getParams(TaskOptions taskOptions) {
		return (List) ReflectionTestUtils.invokeGetterMethod(taskOptions, "params");

	}
}
