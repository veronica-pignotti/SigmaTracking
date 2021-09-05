package com.sigmaspa.sigmatracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sigmaspa.sigmatracking.component.manager.OperatorManager;


@Controller
@CrossOrigin(origins = "http:/localhost:8080")
@RequestMapping("/home")
public class HomePageController{

	@Autowired
	private OperatorManager operatorManager;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView("home");
		mav.addObject("operator", operatorManager.getOperator());			
		return mav;
	}
}