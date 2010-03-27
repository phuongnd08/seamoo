package org.seamoo.webapp.client.admin;

import java.util.ArrayList;

import org.seamoo.webapp.client.admin.ui.SubjectBox;
import org.seamoo.webapp.client.admin.ui.SubjectBoxMode;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;

public class SubjectsManager implements EntryPoint {

	private ArrayList<SubjectBox> subjectBoxes;
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		// remove Loading-Message from page
		DOM.getElementById("loading-message").removeFromParent();
		Element[] elements = GQuery.$(".option-box").elements();
		subjectBoxes = new ArrayList<SubjectBox>();
		for (int i=0; i<elements.length; i++){
			subjectBoxes.add(new SubjectBox(elements[i], i==elements.length-1? SubjectBoxMode.CREATE: SubjectBoxMode.EDIT));
		}
	}

}
