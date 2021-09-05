package com.sigmaspa.sigmatracking.service;

import java.util.GregorianCalendar;
import java.util.List;

import com.sigmaspa.sigmatracking.model.Report;

public interface IReportService {
	
	public boolean save(long date, String message);
		
	public List<Report> getByDate(GregorianCalendar dateTime);

	public Report getByDateTime(GregorianCalendar dateTime);

	public Report getByDateTime(long dateTime);

	public List<Report> getByMessage(String message);
	
	public List<Report> getByDateRange(GregorianCalendar from, GregorianCalendar to);

	public List<Report> getByDateRange(long from, long to);


}
