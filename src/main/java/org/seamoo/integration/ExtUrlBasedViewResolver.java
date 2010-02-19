package org.seamoo.integration;

import java.util.Locale;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

public class ExtUrlBasedViewResolver extends UrlBasedViewResolver implements
		Ordered {

	private boolean exposeSpringMacroModel = false;

	public void setExposeSpringMacroModel(boolean exposeSpringMacroModel) {
		this.exposeSpringMacroModel = exposeSpringMacroModel;
	}

	protected View loadView(String viewName, Locale locale) throws Exception {
		AbstractUrlBasedView view = buildView(viewName);
		View viewObj = (View) getApplicationContext()
				.getAutowireCapableBeanFactory().initializeBean(view, viewName);

		if (viewObj instanceof ExtTilesView) {
			ExtTilesView tilesView = (ExtTilesView) viewObj;
			tilesView.setExposeSpringMacroModel(exposeSpringMacroModel);
		}

		return viewObj;
	}
}