package com.sigmaspa.sigmatracking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sigmaspa.sigmatracking.component.manager.OperatorManager;
import com.sigmaspa.sigmatracking.component.privilege.IResearchRelation;

@Controller
@CrossOrigin(origins = "http:/localhost:8080")
@RequestMapping("/relations")
public class ResearchRelationPageController {
	
	@Autowired
	private OperatorManager operatorManager;
	
	private IResearchRelation researchRelation;
		
	@RequestMapping("/")
	public ModelAndView page() {
		ModelAndView mav = new ModelAndView("relation");
		mav.addObject("operator", operatorManager.getOperator());
		try {
			researchRelation = (IResearchRelation) operatorManager.getPrivilege("Ricerca Relazione");
		} catch (Exception e) {
			mav.addObject("message", e.getMessage());
		}
		return mav;
	}
	
	@RequestMapping("/{sns}")
	@ResponseBody
	public Map<String, List<String>> getRelations(@PathVariable String sns){
		return researchRelation.get(sns);
	}
	
	/*@GetMapping("/getContainer/{content}")
	@ResponseBody
	public String getContainerOf(@PathVariable String content) {
		Relation container = iRelationService.getContainerOf(content); 
		return container == null? "": container.getSnSigmaContainer();
	}*/
}
	
	
