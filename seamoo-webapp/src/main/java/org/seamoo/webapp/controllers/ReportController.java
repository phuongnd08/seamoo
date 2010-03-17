package org.seamoo.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/reports")
public class ReportController {
	@RequestMapping("/process/{reportId}")
	public ModelAndView process(@PathVariable("reportId") long reportId){
		ModelAndView mav = new ModelAndView("reports.process");
		mav.addObject("title", "Xử lí báo cáo");
		return mav;
	}
	
	@RequestMapping(value={"/", ""})
	public ModelAndView list(){
		ModelAndView mav = new ModelAndView("reports.list");
		mav.addObject("title", "Danh sách báo cáo");
		return mav;
	}
	
	@RequestMapping("/{reportId}/{reportDescription}")
	public ModelAndView view(@PathVariable("reportId") long reportId, @PathVariable("reportDescription") String reportDescription){
		ModelAndView mav = new ModelAndView("reports.detail");
		mav.addObject("title", "Xem báo cáo");
		return mav;
	}
}
