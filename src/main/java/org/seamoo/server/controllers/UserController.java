package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
	@RequestMapping("/users/first-time")
	public ModelAndView welcomeFirstTime(@RequestParam("openId") String openId){
		ModelAndView mav  = new ModelAndView("users.first-time");
		return mav;
	}
	
	@RequestMapping("/users/list")
	public ModelAndView list(){
		ModelAndView mav = new ModelAndView("users.list");
		return mav;
	}
	
	@RequestMapping("/users/login")
	public ModelAndView login(){
		ModelAndView mav = new ModelAndView("users.login");
		return mav;
	}
	
}
