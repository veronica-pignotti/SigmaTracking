package com.sigmaspa.sigmatracking.controller;

import java.util.Objects;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sigmaspa.sigmatracking.component.manager.OperatorManager;
import com.sigmaspa.sigmatracking.model.User;

@Controller
@RequestMapping("/")
public class WelcomePageController {
	
	@Autowired
	private OperatorManager operatorManager;
	
	@Autowired
	private HomePageController homePage;
	
	private ModelAndView mav;
	
	@GetMapping("/")
	public ModelAndView initPage() {
		if(Objects.nonNull(operatorManager.getOperator()))
			return homePage.home();
		
		mav = new ModelAndView("welcome");
		mav.addObject("operator", new User());
				
		return mav;
	}
	
	@PostMapping
	public ModelAndView login(@ModelAttribute User user) {
		String message= operatorManager.login(user);
		if(message.isEmpty()) return homePage.home();
		mav.addObject("message", message);
		return mav;
	}
	
	@PostMapping("/signin")
	@ResponseBody
	public String signin(@RequestBody String json) {
		JSONObject obj = new JSONObject(json);
		return operatorManager.signin(
			obj.getString("email"),
			obj.getString("password")
		);
	}
	
	@GetMapping("/logout")
	public ModelAndView logout() {
		operatorManager.logout();
		return new ModelAndView("redirect:/");
	}
	
	@GetMapping("/error")
	public ModelAndView errorPage() {
		return new ModelAndView("error");
	}

}
