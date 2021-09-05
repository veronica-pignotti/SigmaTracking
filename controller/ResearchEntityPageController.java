package com.sigmaspa.sigmatracking.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sigmaspa.sigmatracking.component.manager.OperatorManager;
import com.sigmaspa.sigmatracking.component.privilege.IResearchEntity;
import com.sigmaspa.sigmatracking.component.process.IReceptionProcess;
import com.sigmaspa.sigmatracking.model.Entity;
import com.sigmaspa.sigmatracking.service.IEntityService;
import com.sigmaspa.sigmatracking.service.IEventService;
import com.sigmaspa.sigmatracking.service.IRelationService;

@Controller
@CrossOrigin(origins = "http:/localhost:8080")
@RequestMapping("/entities")
public class ResearchEntityPageController {

	@Autowired
	private OperatorManager operatorManager;
		
	@Autowired
	private IEventService eventService;
	
	@Autowired
	private IEntityService entityService;
	
	@Autowired
	private IRelationService relationService;
	
	@Autowired
	private IReceptionProcess receptionProcess;
				
	private Entity entity;
	
	private Map<String, Object> model = new HashMap<String, Object>();
		
	
	protected void buildModel() {
		model.put("name", "entities");
		model.put("operator", operatorManager.getOperator());
		model.put("entity", new Entity());
		try {
			model.put("results", ((IResearchEntity) operatorManager.getPrivilege("Ricerca Entità")).getAllChangedToday());
			model.put("error", false);
		} catch (Exception e) {
			model.put("results", new ArrayList<Entity>());
			model.put("error", true);
		}
	}
	
	@GetMapping
	public ModelAndView index() {
		return page();
	}
	
	@GetMapping("/")
	public ModelAndView page() {
		if(model.isEmpty()) buildModel();

		return new ModelAndView("researchentity", model);
	}
	
	protected void privateSearch(Entity entity) {
		model.put("entity", entity);
		try {
			model.put("results", ((IResearchEntity) operatorManager.getPrivilege("Ricerca Entità")).getByAlias(entity));
			model.put("error", false);
		} catch (Exception e) {
			model.put("results", new ArrayList<Entity>());
			model.put("error", true);
		}
	}
	
	
	@PostMapping("/")
	public ModelAndView search(@ModelAttribute Entity entity) {
		privateSearch(entity);
		return new ModelAndView("redirect:/entities/");
	}
	
	@GetMapping("/snproducer/{snProducer}")
	public ModelAndView getBySnProducer(@PathVariable String snProducer) {
		return search(new Entity().setSnProducer(snProducer));
	}
	
	@GetMapping("/ordertype/{orderType}")
	public ModelAndView getByOrderType(@PathVariable String orderType) {
		return search(new Entity().setOrderType(orderType));
	}
	
	@GetMapping("/ordernumber/{orderNumber}")
	public ModelAndView getByOrderNumber(@PathVariable String orderNumber) {
		return search(new Entity().setOrderNumber(Long.parseLong(orderNumber)));
	}
	
	@GetMapping("/jdecode/{jdeCode}")
	public ModelAndView getByJdeCode(@PathVariable String jdeCode) {
		return search(new Entity().setJdeCode(jdeCode));
	}
	
	@RequestMapping(value = "/{snSigma}")
	public ModelAndView details(@PathVariable String snSigma) {
		ModelAndView mav = new ModelAndView("entity");
		mav.addObject("operator", operatorManager.getOperator());
		entity = entityService.getBySnSigma(snSigma);
		mav.addObject("events", eventService.getBySnSigma(snSigma));
		mav.addObject("entity", entity);	
		mav.addObject("tree", relationService.getMapOf(snSigma));
		mav.addObject("barcode",Base64.getEncoder().encodeToString(receptionProcess.getAztecCodeImage(snSigma)));
		return mav;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/relations")
	public Map<String, List<String>> getRelations(){
		return relationService.getMapOf(entity.getSnSigma());
	}
}
