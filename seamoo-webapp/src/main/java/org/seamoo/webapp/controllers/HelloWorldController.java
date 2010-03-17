package org.seamoo.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
//@RequestMapping("/basic")
public class HelloWorldController {
	@RequestMapping(value="/basic/hello")
	public ModelAndView sayHello(@RequestParam("id") int id) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("home.index");
		mav.addObject("title", "Hi World");
		mav.addObject("message", "Hello World (From Phuong)!");
		mav.addObject("id", new Integer(id));
		return mav;
	}
}