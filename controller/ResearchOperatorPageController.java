package com.sigmaspa.sigmatracking.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sigmaspa.sigmatracking.component.factory.PrivilegeFactory;
import com.sigmaspa.sigmatracking.component.factory.ProcessFactory;
import com.sigmaspa.sigmatracking.component.manager.OperatorManager;
import com.sigmaspa.sigmatracking.component.privilege.IResearchOperator;
import com.sigmaspa.sigmatracking.model.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Controller
@CrossOrigin(origins = "http:/localhost:8080")
@RequestMapping("/operators")
public class ResearchOperatorPageController {
	
	@Autowired
	private OperatorManager operatorManager;
		
	@Autowired
	private ProcessFactory processFactory;
	
	@Autowired
	private PrivilegeFactory privilegeFactory;
	
	private Map<String, Object> model = new HashMap<String, Object>();
	
	protected void buildModel() {
		model.put("operator", operatorManager.getOperator());
		try {
			model.put("operatorObj", new ResearchOperatorObject());
			model.put("results", ((IResearchOperator) operatorManager.getPrivilege("Ricerca Operatore")).getAllActiveToday());
			model.put("error", false);
		} catch (Exception e) {
			model.put("results", new ArrayList<User>());
			model.put("error", true);
		}
		model.put("processes", processFactory.getIds());
		model.put("privileges", privilegeFactory.getIds());
	}
	
	@GetMapping
	public ModelAndView index() {
		return page();
	}
	
	@GetMapping("/")
	public ModelAndView page() {
		if(model.isEmpty()) buildModel();
		return new ModelAndView("researchoperator", model);
	}
	
	@PostMapping(value = "/")
	public ModelAndView searchOperator(@ModelAttribute ResearchOperatorObject operatorObj) {
		privateSearch(operatorObj);
		return new ModelAndView("redirect:/operators/");
	}
	
	protected void privateSearch(ResearchOperatorObject operatorObj) {
		model.put("operatorObj", operatorObj);
		
		try {
			model.put("results", 
				((IResearchOperator) operatorManager.getPrivilege("Ricerca Operatore")).get(
						operatorObj.getUser().getId(),
						operatorObj.getUser().getEmail(),
						operatorObj.getIdProcess(),
						operatorObj.getIdPrivilege()
				));
			model.put("error", false);
		} catch (Exception e) {
			model.put("results", new ArrayList<User>());
			model.put("error", true);
		}
	}
	
	
	
	@GetMapping("/{id}")
	public ModelAndView details(@PathVariable String id) {
		ModelAndView mav = new ModelAndView("operator");
		mav.addObject("operator", operatorManager.getOperator());
		try {
			mav.addAllObjects(((IResearchOperator) operatorManager.getPrivilege("Ricerca Operatore")).getDetails(id));
			return mav;
		} catch (Exception e) {
			return new ModelAndView("redirect:/error");
		}
	}
	
	@GetMapping("/idprocess/{idProcess}")
	public ModelAndView getByIdProcess(@PathVariable String idProcess) {
		buildModel();
		try {
			model.put("results", 
				((IResearchOperator) operatorManager.getPrivilege("Ricerca Operatore")).getByIdProcess(idProcess)
			);
			model.put("operatorObj", new ResearchOperatorObject().setIdProcess(idProcess));
			model.put("error", false);
		} catch (Exception e) {
			model.put("results", new ArrayList<User>());
			model.put("error", true);
		}
		return new ModelAndView("redirect:/operators/");
	}
	
	@GetMapping("/idprivilege/{idPrivilege}")
	public ModelAndView getByIdPrivilege(@PathVariable String idPrivilege) {
		buildModel();
		try {
			model.put("operatorObj", new ResearchOperatorObject().setIdPrivilege(idPrivilege));
			model.put(
					"results", 
					((IResearchOperator) operatorManager.getPrivilege("Ricerca Operatore")).getByIdPrivilege(idPrivilege));
			model.put("error", false);
		} catch (Exception e) {
			model.put("results", new ArrayList<User>());
			model.put("error", true);
		}
		return new ModelAndView("redirect:/operators/");
	}
	
	/**
	 * Oggetto utilizzato da {@link ResearchOperatorPageController} per raccogliere tutti i parametri necessari
	 * al fine di eseguire una ricerca più completa degli operatori.
	 * */
	@Data
	@NoArgsConstructor
	@Accessors(chain = true)
	public class ResearchOperatorObject {
		
		/**
		 * L'istanza di {@User} ricercata
		 * */
		private User user = new User();
		
		/**
		 * L'id di {@link IProcess} che devono avere abilitato i risultati mostrati.
		 * */	
		private String idProcess;
		
		/**
		 * L'id di {@link IPrivilege} che devono avere abilitato i risultati mostrati.
		 * */	
		private String idPrivilege;
	}

}
