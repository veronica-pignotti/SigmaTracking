package com.sigmaspa.sigmatracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sigmaspa.sigmatracking.component.factory.PrivilegeFactory;
import com.sigmaspa.sigmatracking.component.factory.ProcessFactory;
import com.sigmaspa.sigmatracking.component.manager.OperatorManager;
import com.sigmaspa.sigmatracking.component.privilege.IRegisterOperator;

import java.util.Map;
import java.util.stream.Collectors;

import org.json.*;
@Controller
@CrossOrigin(origins = "http:/localhost:8080")
@RequestMapping("/registeroperator")
public class RegisterOperatorPageController{
	
	@Autowired
	private OperatorManager operatorManager;
	
	@Autowired
	private ProcessFactory processFactory;
	
	@Autowired
	private PrivilegeFactory privilegeFactory;
	
	@Autowired
	private IRegisterOperator registerOperator;
	ModelAndView mav = new ModelAndView("registeroperator");
	
	@GetMapping("/")
	public ModelAndView page() {
		mav.addObject("operator", operatorManager.getOperator());	
		return mav;
	}
	
	@GetMapping("/{idUser}")
	@ResponseBody
	public Map<String, Map<String, Boolean>> getAllAbout(@PathVariable String idUser){
		return registerOperator.get(idUser, processFactory.getIds(), privilegeFactory.getIds());
	}
	
	@PostMapping("/save")
	@ResponseBody
	public String save(@RequestBody String json) {
		JSONObject obj = new JSONObject(json);
		
		String message = registerOperator.save(
			obj.getString("idUser"), 
			obj.getJSONArray("processes").toList().stream().map(p-> p.toString()).collect(Collectors.toList()), 
			obj.getJSONArray("privileges").toList().stream().map(p-> p.toString()).collect(Collectors.toList())
		);
		
		mav.addObject("operator", operatorManager.getOperator());	
		
		return message;
	}
	
}