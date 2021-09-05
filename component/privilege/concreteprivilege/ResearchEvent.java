package com.sigmaspa.sigmatracking.component.privilege.concreteprivilege;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sigmaspa.sigmatracking.component.manager.OperatorManager;
import com.sigmaspa.sigmatracking.component.privilege.IResearchEvent;
import com.sigmaspa.sigmatracking.model.Event;
import com.sigmaspa.sigmatracking.model.Report;
import com.sigmaspa.sigmatracking.service.IEventService;
import com.sigmaspa.sigmatracking.service.IReportService;

@Component
public class ResearchEvent implements IResearchEvent {
	
	@Autowired
	private IEventService eventService;
	
	@Autowired
	private IReportService reportService;
	
	@Override
	public String getId() {
		return "Ricerca Evento";
	}

	@Override
	public List<Event> get(String snSigma, String idUser, String idProcess, long from, long to){
		
		return snSigma == null && idUser == null && idProcess==null?
			getByDataRange(from, to):
			
			get(
				new Event(snSigma, idUser, idProcess),
				from, to
			);
	}
	
	
	
	@Override
	public List<Event> get(Event event) {
		OperatorManager.log(getId() + " " + event.toString());
		return eventService.getByAlias(event.setDateTime(null));
	}

	@Override
	public List<Event> get(Event event, long from, long to) {
		OperatorManager.log(getId() +" "+ event.toString() + " da " + new Date(from) + " a " + new Date(to));
		List<Event> results = getByDataRange(from, to);
		if(event.getSnSigma()!=null)
			results = results.stream().filter(e->e.getSnSigma().equals(event.getSnSigma())).collect(Collectors.toList());
		if(event.getIdUser()!=null)
			results = results.stream().filter(e->e.getIdUser().equals(event.getIdUser())).collect(Collectors.toList());
		if(event.getIdProcess()!=null)
			results = results.stream().filter(e->e.getIdProcess().equals(event.getIdProcess())).collect(Collectors.toList());
		
		return results;
	}

	@Override
	public List<Event> getByDataRange(long from, long to) {
		OperatorManager.log(getId() + " da " + new Date(from) + " a " + new Date(to));
		return eventService.getByDateRange(from, to);
	}

	@Override
	public List<Event> getAllToday() {
		OperatorManager.log(getId() + " oggi");
		return eventService.getByDateRange(System.currentTimeMillis(), System.currentTimeMillis());
	}

	@Override
	public Report getReportOf(Event event) {
		return getReportOf(event.getDateTime());
	}

	@Override
	public Report getReportOf(long dateTime) {
		OperatorManager.log(getId() + " report con data " + new Date(dateTime));
		return reportService.getByDateTime(dateTime);
	}

}
