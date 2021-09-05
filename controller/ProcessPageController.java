package com.sigmaspa.sigmatracking.controller;

import java.util.ArrayList;
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
import com.sigmaspa.sigmatracking.component.object.ResponseMap;
import com.sigmaspa.sigmatracking.component.process.IAssemblyProcess;
import com.sigmaspa.sigmatracking.component.process.IMaintenanceProcess;
import com.sigmaspa.sigmatracking.component.process.IProcess;
import com.sigmaspa.sigmatracking.component.process.IReceptionProcess;
import com.sigmaspa.sigmatracking.component.process.IRepairProcess;
import com.sigmaspa.sigmatracking.component.process.IScrappingProcess;
import com.sigmaspa.sigmatracking.component.process.ITestingProcess;
import com.sigmaspa.sigmatracking.model.Entity;
import com.sigmaspa.sigmatracking.model.Order;

@Controller
@CrossOrigin(origins = "http:/localhost:8080")
@RequestMapping("/register")
public class ProcessPageController{
	
	@Autowired
	private OperatorManager operatorManager;
	
	@Autowired
	private IReceptionProcess receptionProcess;
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	private List<Order> orders= new ArrayList<Order>();;
	
	private Order selected;
		
	private IProcess process;
		
	//ModelAndView mav;
	
	Map<String, Object> model;
	
	private void page(String name) {
		System.out.println("Apertura pagina " + name);
		model = new HashMap<String, Object>();
		model.put("operator", operatorManager.getOperator());
		
		try {
			this.process = operatorManager.getProcess(name);
			model.put("process", name);			
		}catch(Exception e) {
			model.put("processError", e.getMessage());
			model.put("process", "");
		}
	}
	
	private void pageWithOrder() {
		model.put("order", new Order());
		model.put("selected", new Order());
		model.put("entity", new Entity());
	}
	
	@GetMapping(value = "/reception")
	public ModelAndView receptionPage() {
		page("Accettazione");
		pageWithOrder();
		return new ModelAndView("process", model);
	}
	
	@PostMapping("/reception/selectorder")
	public ModelAndView selectOrder(@ModelAttribute Order order) {
		try {
			this.orders = receptionProcess.getOrders(order.getOrderType().toUpperCase(), order.getOrderNumber());
		}catch(Exception e) {
			model.put("message", e.getMessage());
		}
		model.put("order", order);
		model.put("orders", this.orders);
		model.put("selected", order);
		return new ModelAndView("redirect:");
	}
	
	@PostMapping("/reception/selectjde")
	public ModelAndView selectJde(@ModelAttribute Order selected) {
		
		this.selected = orders.stream().filter(o->o.getJdeCode().equals(selected.getJdeCode())).findFirst().get();
		
		this.entities = receptionProcess.getEntitiesFromOrder(this.selected);
		model.put("entities", this.entities);
		model.put("entity", buildEntity(this.selected));
		model.put("count", "0 / " + this.selected.getQuantity());
		return new ModelAndView("redirect:");
	}
	
	
	
	
	@PostMapping(value = "/reception/save")
	public ModelAndView save(@ModelAttribute Entity entity) {
		
		if(
				entity.getSnSigma()== null || entity.getSnProducer()== null ||
				entity.getSnSigma().isEmpty() || entity.getSnProducer().isEmpty()
			)
			return new ModelAndView("redirect:");
		Entity toSave = entities.stream().filter(e->
			e.getSnSigma().equals(entity.getSnSigma())
		).findFirst().orElse(null);
		if(toSave!= null) privateSave(toSave.setSnProducer(entity.getSnProducer()));
		else model.put("message", "S/N Sigma "+ entity.getSnSigma() + " non valido");
		entity.setSnSigma(null);
		entity.setSnProducer(null);
		model.put("entity", entity);
		model.put("entities", this.entities);
		return save(entity);
	}
	
	protected void privateSave(Entity entity) {
		
		ResponseMap resp = receptionProcess.register(
			process.getId(), 
			entity.getSnSigma(), 
			entity.getSnProducer(), 
			selected.getJdeCode(), 
			selected.getDescription(), 
			selected.getOrderType(), 
			selected.getOrderNumber()
		);
		
		model.put(
			"message",
			resp.getMessage()
		);
		
		if((boolean) resp.getData()) {
			this.entities.removeIf(e-> e.getSnSigma().equals(entity.getSnSigma()));
			model.put("count", this.selected.getQuantity()-this.entities.size() + " / " + this.selected.getQuantity());
		}
		
	}
	
	
	protected Entity buildEntity(Order order) {
		Entity entity = new Entity();
		entity.setOrderType(order.getOrderType());
		entity.setOrderNumber(order.getOrderNumber());
		entity.setJdeCode(order.getJdeCode());
		entity.setDescription(order.getDescription());
		return entity;
	}
	
	/*
	@GetMapping(value = "/reception/getOrders/{type}:{number}")
	@ResponseBody
	public List<Order> getOrders(@PathVariable String type, @PathVariable Long number){
		this.orders = receptionProcess.getOrders(type.toUpperCase(), number);
		this.orders.sort((a,b)-> a.getQuantity()>=b.getQuantity()?1:-1);
		return this.orders;
	}
	
	
	
	@GetMapping(value = "/reception/getEntities/{jde}")
	@ResponseBody
	public List<Entity> getEntities(@PathVariable String jde) {
		this.orderSelected = orders.stream().filter(o->o.getJdeCode().equals(jde)).findFirst().get();
		this.entities = receptionProcess.getEntitiesFromOrder(this.orderSelected);
		return this.entities;
	}
	
	@RequestMapping(value = "/reception/register/{snSigma}:{snProducer}")
	@ResponseBody
	public ResponseMap save(@PathVariable String snSigma, @PathVariable String snProducer ) {
		entities.forEach(e->{
			if(e.getSnSigma().equals(snSigma)) e.setSnProducer(snProducer);
		});
		Entity entity = entities.stream().filter(e->(e.getSnSigma().equals(snSigma))).findFirst().get();
		return receptionProcess.register(process.getId(), snSigma, snProducer, entity.getJdeCode(), entity.getDescription(), entity.getOrderType(), entity.getOrderNumber());
	}*/
	
	@GetMapping(value = "/assembly")
	public ModelAndView assemblyPage() {
		page("Montaggio");
		pageWithOrder();
		return new ModelAndView("process", model);
	}
	
	@PostMapping("/assembly/{container}:{content}")
	@ResponseBody
	public ResponseMap assembly(@PathVariable String container, @PathVariable String content) {
		return ((IAssemblyProcess) process).assembly(container, content);
	}
	
	@GetMapping(value = "/repair")
	public ModelAndView repairPage() {
		page("Riparazione");
		pageWithOrder();
		return new ModelAndView("process", model);
	}
	
	@PostMapping("/repair/{content}")
	@ResponseBody
	public String repair(@PathVariable String content) {
		return ((IRepairProcess) process).repair(content);
	}
	
	@GetMapping(value = "/maintenance")
	public ModelAndView maintenancePage() {
		page("Manutenzione");
		return new ModelAndView("process", model);
	}
	
	@PostMapping("/maintenance/{container}:{oldContent}:{newContent}")
	@ResponseBody
	public ResponseMap maintenance(@PathVariable String container,@PathVariable String oldContent, @PathVariable String newContent) {
		return ((IMaintenanceProcess) process).replacement(container, oldContent, newContent);
	}
	
	@GetMapping(value = "/scrapping")
	public ModelAndView scrappingPage() {
		page("Rottamazione");
		return new ModelAndView("process", model);
	}
	
	@PostMapping("/scrapping/{snSigma}")
	@ResponseBody
	public String scrapping(@PathVariable String snSigma) {
		return ((IScrappingProcess) process).scrapping(snSigma);
	}
	
	@GetMapping(value = "/testing")
	public ModelAndView testingPage() {
		page("Collaudo");
		return new ModelAndView("process", model);
	}
	
	@PostMapping("/testing/{container}:{content}")
	@ResponseBody
	public ResponseMap testing(@PathVariable String container, @PathVariable String content) {
		return ((ITestingProcess) process).testing(container, content);
	}
	
}
