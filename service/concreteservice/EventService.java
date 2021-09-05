package com.sigmaspa.sigmatracking.service.concreteservice;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.component.manager.OperatorManager;
import com.sigmaspa.sigmatracking.model.Event;
import com.sigmaspa.sigmatracking.repository.EventRepository;
import com.sigmaspa.sigmatracking.service.IEventService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Primary
@Service("eventService")
public class EventService implements IEventService {
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private OperatorManager operatorManager;

	@Override
	public boolean save(String snSigma, String processType) {
		return save(System.currentTimeMillis(), snSigma, processType);
	}
	
	@Override
	public boolean save(long date, String snSigma, String processType) {
		Event saved = eventRepository.save(new Event(date, snSigma, operatorManager.getOperator().getId(), processType));
		log.info("Event: \n {}", saved);
		return saved !=null;
	}

	@Override
	public Event getById(long id) {
		return eventRepository.findById(id).orElse(null);
	}
	
	@Override
	public List<Event> getByAlias(Event event) {
		return eventRepository.findAll().stream().filter(
				e-> 
					e.getSnSigma().equals(event.getSnSigma()) || 
					e.getIdUser().equals(event.getIdUser()) ||
					e.getIdProcess().equals(event.getIdProcess())
			).collect(Collectors.toList());
	}
	
	@Override
	public List<Event> getBySnSigma(String snSigma) {
		return eventRepository.findAllBySnSigmaOrderByDateTimeAsc(snSigma)
				.orElse(new ArrayList<Event>());
	}

	@Override
	public List<Event> getByDate(long date) {
		if(date>System.currentTimeMillis()) return new ArrayList<Event>();
		GregorianCalendar gcDate = new GregorianCalendar();
		gcDate.setTimeInMillis(date);
		return getByDateRange( date, date);
	}


	@Override
	public List<Event> getByIdUser(String id) {
		return eventRepository.findAllByidUserOrderByDateTimeAsc(id).get();
	}

	@Override
	public List<Event> getByIdProcess(String processType) {
		return eventRepository.findAllByIdProcessOrderByDateTimeAsc(processType)
				.orElse(new ArrayList<Event>());
	}

	@Override
	public List<Event> getByDateRange(long from, long to) {
		return eventRepository.findAllByDateTimeBetweenOrderByDateTimeAsc(
				getDate(Long.min(from, to), true), 
				getDate(Long.max(from, to), false)
		).orElse(new ArrayList<Event>());
	}
	
	private long getDate(long millsec, boolean start) {
		GregorianCalendar date = new GregorianCalendar();
		date.setTimeInMillis(millsec);
		return start? 
			new GregorianCalendar(
				date.get(GregorianCalendar.YEAR), 
				date.get(GregorianCalendar.MONTH), 
				date.get(GregorianCalendar.DAY_OF_MONTH)
			).getTimeInMillis():
			 new GregorianCalendar(
				date.get(GregorianCalendar.YEAR), 
				date.get(GregorianCalendar.MONTH), 
				date.get(GregorianCalendar.DAY_OF_MONTH), 
				23,59,59
			).getTimeInMillis();
	}
			
}