package org.seamoo.webapp.tiles;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seamoo.webapp.Site;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.tiles2.TilesView;

import freemarker.ext.beans.BeansWrapper;

public class ExtTilesView extends TilesView {

	private boolean exposeSpringMacroModel = false;

	public void setExposeSpringMacroModel(boolean exposeSpringMacroModel) {
		this.exposeSpringMacroModel = exposeSpringMacroModel;
	}

	@SuppressWarnings("unchecked")
	protected final void renderMergedOutputModel(Map model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (exposeSpringMacroModel) {
			if (model
					.containsKey(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE)) {
				throw new ServletException(
						"Cannot expose bind macro helper '"
								+ AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE
								+ "' because of an existing model object of the same name");
			}
			model
					.put(
							AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE,
							new RequestContext(request, model));
			model.put(Site.STATICS_ATTRIBUTE, BeansWrapper.getDefaultInstance().getStaticModels());
			model.put(Site.ENUMS_ATTRIBUTE, BeansWrapper.getDefaultInstance().getEnumModels());
		}

		super.renderMergedOutputModel(model, request, response);
	}
}
