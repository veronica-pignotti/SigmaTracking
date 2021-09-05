package com.sigmaspa.sigmatracking.controller;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sigmaspa.sigmatracking.component.factory.ProcessFactory;
import com.sigmaspa.sigmatracking.component.manager.OperatorManager;
//import com.sigmaspa.sigmatracking.component.object.ResearchEventObject;
import com.sigmaspa.sigmatracking.component.privilege.IResearchEvent;
import com.sigmaspa.sigmatracking.model.Event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Controller
@CrossOrigin(origins = "http:/localhost:8080")
@RequestMapping("/events")
public class ResearchEventPageController {
	
	@Autowired
	private OperatorManager operatorManager;
	
	@Autowired
	private ProcessFactory processFactory;
	
	private Map<String, Object> model = new HashMap<String, Object>();
		
	protected void buildModel() {
		model.putIfAbsent("operator", operatorManager.getOperator());
		model.putIfAbsent("processes", processFactory.getIds());
		model.putIfAbsent("eventObj", new ResearchEventObject());
		
		try {
			model.putIfAbsent("results", ((IResearchEvent) operatorManager.getPrivilege("Ricerca Evento")).getAllToday());
			model.put("error", false);		
		}catch (Exception e) {
			model.put("results", new ArrayList<Event>());
			model.put("error", true);		
		}
		
	} 
	
	@GetMapping
	public ModelAndView index() {
		return page();
	}
	
	@GetMapping("/")
	public ModelAndView page() {
		buildModel();
		return new ModelAndView("researchevent", model);
	}
	
	@PostMapping("/")
	public ModelAndView search(@ModelAttribute ResearchEventObject eventObj) {
		privateSearch(eventObj);
		return new ModelAndView("redirect:/events/");
	}
	
	protected void privateSearch(ResearchEventObject eventObj) {
		model.put("eventObj", eventObj);
		
		if(Objects.nonNull(eventObj.getEvent().getSnSigma()) && eventObj.getEvent().getSnSigma().isEmpty()) 
			eventObj.setEvent(eventObj.getEvent().setSnSigma(null));
		
		if(Objects.nonNull(eventObj.getEvent().getIdUser()) && eventObj.getEvent().getIdUser().isEmpty()) 
			eventObj.setEvent(eventObj.getEvent().setIdUser(null));
		
		if(Objects.nonNull(eventObj.getEvent().getIdProcess()) && eventObj.getEvent().getIdProcess().isEmpty()) 
			eventObj.setEvent(eventObj.getEvent().setIdProcess(null));
		
		try {
			model.put("results", 
			 ((IResearchEvent) operatorManager.getPrivilege("Ricerca Evento")).get(
					 eventObj.getEvent(),
					 eventObj.getFrom() == null || eventObj.getFrom().isEmpty()? 0: stringToMillisec(eventObj.getFrom()),
					 eventObj.getTo() == null || eventObj.getTo().isEmpty()? System.currentTimeMillis(): stringToMillisec(eventObj.getTo())
			));
			model.put("error", false);		
		} catch (Exception e) {
			model.put("results", new ArrayList<Event>());
			model.put("error", true);		
		}
	}
	
	
	
	private long stringToMillisec(String str) {
		String[] arr = str.split("-");
		return new GregorianCalendar(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])-1, Integer.parseInt(arr[2])).getTimeInMillis();
	}

	@GetMapping("/idprocess/{idProcess}")
	public ModelAndView getByIdProcess(@PathVariable String idProcess) {
		if(model.isEmpty()) buildModel();
		privateSearch(
			new ResearchEventObject().setEvent(new Event().setIdProcess(idProcess))
		);
		return new ModelAndView("redirect:/events/");
	}
	
	@GetMapping("/{dateTime}")
	public ModelAndView getDetails(@PathVariable String dateTime) {
		ModelAndView details = new ModelAndView("event");
		details.addObject("operator", operatorManager.getOperator());
		try {
			details.addObject("event", ((IResearchEvent) operatorManager.getPrivilege("Ricerca Evento")).getByDataRange(Long.parseLong(dateTime),Long.parseLong(dateTime)).get(0));
			details.addObject("report", ((IResearchEvent) operatorManager.getPrivilege("Ricerca Evento")).getReportOf(Long.parseLong(dateTime)));
			model.put("error", false);		
		} catch (Exception e) {
			model.put("results", new ArrayList<Event>());
			model.put("error", true);		
		}
		return details;
	}
	
	
	/**
	 * Oggetto utilizzato da {@link ResearchEventPageController} per raccogliere tutti i parametri necessari
	 * al fine di eseguire una ricerca più completa degli eventi.
	 * */
	@Data
	@NoArgsConstructor
	@Accessors(chain = true)
	protected class ResearchEventObject {
		
		/**
		 * L'istanza di {@link Event} ricercato
		 * */
		private Event event = new Event();
		
		/**
		 * La data di inizio che devono avere gli eventi mostrati, sottoforma di stringa
		 * */
		private String from;
		
		/**
		 * La data di fine che devono avere gli eventi mostrati, sottoforma di stringa
		 * */		
		private String to;

	}
}
