package org.seamoo.webapp.client.admin;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.Dictionary;

public class AdminLoader implements EntryPoint {

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		// remove Loading-Message from page
		Dictionary dictionary = Dictionary.getDictionary("Vars");
		String module = dictionary.get("module");
		if (module.equals("subject")) {
			SubjectListPresenter subjectListPresenter = new SubjectListPresenter();
			subjectListPresenter.onModuleLoad();
		} else if (module.equals("league")){
			LeagueListPresenter leagueListPresenter = new LeagueListPresenter();
			leagueListPresenter.onModuleLoad(dictionary);
		}
	}
}
