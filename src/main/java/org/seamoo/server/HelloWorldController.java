package org.seamoo.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/basic")
public class HelloWorldController {
	@RequestMapping(value="/hello.do", method=RequestMethod.GET)
	public ModelAndView helloWorld() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("basic/helloWorld");
		mav.addObject("message", "Hello World From Phuong!");
		return mav;
	}
}
