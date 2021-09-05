package com.sigmaspa.sigmatracking.component.privilege;

import java.util.List;

import com.sigmaspa.sigmatracking.model.Event;
import com.sigmaspa.sigmatracking.model.Report;

public interface IResearchEvent extends IPrivilege{
	
	public List<Event> get(Event entity);
	
	public List<Event> get(String snSigma, String idUser, String idProcess, long from, long to);
	
	public List<Event> get(Event entity, long from, long to);
	
	public List<Event> getByDataRange(long from, long to);

	public List<Event> getAllToday();

	//public List<Event> get(String paramsJson);
	
	public Report getReportOf(Event event);
	
	public Report getReportOf(long dateTime);

}
