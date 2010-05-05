package org.seamoo.webapp.client.user;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.Dictionary;

public class UserLoader implements EntryPoint {
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		// remove Loading-Message from page
		Dictionary dictionary = Dictionary.getDictionary("Vars");
		String module = dictionary.get("module");
		if (module.equals("question.creator")) {
			QuestionCreator.Presenter questionCreatorPresenter = new QuestionCreator.Presenter();
			questionCreatorPresenter.onModuleLoad();
		} else if (module.equals("matching")) {
			MatchBoard.Presenter matchboardPresenter = new MatchBoard.Presenter();
			matchboardPresenter.onModuleLoad(dictionary);
		}
	}
}