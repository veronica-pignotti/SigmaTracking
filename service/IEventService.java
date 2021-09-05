package com.sigmaspa.sigmatracking.service;

import java.util.List;

import com.sigmaspa.sigmatracking.model.Event;

public interface IEventService {

	public boolean save(String snSigma, String idProcess);
	
	public boolean save(long date, String snSigma, String idProcess);
	
	public List<Event> getByAlias(Event Event);

	public List<Event> getBySnSigma(String snSigma);

	public Event getById(long id);

	public List<Event> getByIdUser(String id);

	public List<Event> getByIdProcess(String idProcess);

	public List<Event> getByDateRange(long from, long to);

	public List<Event> getByDate(long date);

}
