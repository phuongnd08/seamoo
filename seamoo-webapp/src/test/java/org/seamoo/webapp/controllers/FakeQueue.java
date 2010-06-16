package org.seamoo.webapp.controllers;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.TaskHandle;
import com.google.appengine.api.labs.taskqueue.TaskOptions;

public class FakeQueue implements Queue {
	List<TaskOptions> tasks;

	public FakeQueue() {
		tasks = new ArrayList<TaskOptions>();
	}

	@Override
	public String getQueueName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskHandle> add(Transaction arg0, Iterable<TaskOptions> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskHandle add(Transaction transaction, TaskOptions taskoptions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskHandle> add(Iterable<TaskOptions> taskoptionsList) {
		for (TaskOptions task : taskoptionsList)
			add(task);
		return null;
	}

	@Override
	public TaskHandle add(TaskOptions taskoptions) {
		tasks.add(taskoptions);
		return null;
	}

	@Override
	public TaskHandle add() {
		return null;
	}

	
	public List<TaskOptions> getTasks(){
		return tasks;
	}
}
