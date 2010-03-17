package org.seamoo.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserController {
	@RequestMapping("/first-time")
	public ModelAndView welcomeFirstTime(@RequestParam("openId") String openId) {
		ModelAndView mav = new ModelAndView("users.first-time");
		mav.addObject("title", "Xác nhận Open ID");
		return mav;
	}

	@RequestMapping(value = { "/", "" })
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("users.list");
		mav.addObject("title", "Users");
		return mav;
	}

	@RequestMapping("/login")
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView("users.login");
		mav.addObject("title", "Đăng nhập với Open ID của bạn");
		return mav;
	}

//	@RequestMapping("/{userId}/{userName}")
//	public ModelAndView viewProfile(@PathVariable("userId") long userId,
//			@PathVariable("userName") String userName){
//		return viewProfileTab(userId, userName, "profile");
//	}
	@RequestMapping("/{userId}/{userName}")
	public ModelAndView viewProfileTab(@PathVariable("userId") long userId,
			@PathVariable("userName") String userName,
			@RequestParam(value="tab", required=false) String tab) {
		ModelAndView mav = null;
		if (tab != null && tab.equals("discussion")) {
			mav = new ModelAndView("user.discussion");
			mav.addObject("title", "Discussion");
		} else {
			mav = new ModelAndView("user.view");
			mav.addObject("userName", userName);
			mav.addObject("userId", new Long(userId));
			mav.addObject("title", userName);
		}
		return mav;
	}

	@RequestMapping("/edit/{userId}")
	public ModelAndView edit(@PathVariable("userId") long userId) {
		ModelAndView mav = new ModelAndView("user.edit");
		mav.addObject("title", String.format("Edit profile %d", userId));
		return mav;
	}
}
