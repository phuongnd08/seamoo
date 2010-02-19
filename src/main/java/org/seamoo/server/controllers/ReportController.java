package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportController {
	@RequestMapping("/reports/process")
	public ModelAndView process(@RequestParam("reportId") long reportId){
		ModelAndView mav = new ModelAndView("reports.process");
		return mav;
	}
	
	@RequestMapping("/reports/list")
	public ModelAndView list(){
		ModelAndView mav = new ModelAndView("reports.list");
		return mav;
	}
	
	@RequestMapping("/reports/view")
	public ModelAndView view(@RequestParam("reportId") long reportId){
		ModelAndView mav = new ModelAndView("reports.detail");
		return mav;
	}
}
