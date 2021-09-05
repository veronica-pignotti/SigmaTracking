package com.sigmaspa.sigmatracking.service.concreteservice;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.model.Report;
import com.sigmaspa.sigmatracking.repository.ReportRepository;
import com.sigmaspa.sigmatracking.service.IReportService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Primary
@Service("LogicalEventConcreteService")
public class ReportService implements IReportService{

	@Autowired
	private ReportRepository reportRepository;
		

	@Override
	public boolean save(long date, String message) {
		Report saved = reportRepository.save(new Report(date, message));
		log.info(saved.toString());
		return saved!=null;
	}


	@Override
	public List<Report> getByDate(GregorianCalendar dateTime) {
		return getByDateRange(
			new GregorianCalendar(dateTime.get(GregorianCalendar.YEAR), dateTime.get(GregorianCalendar.MONTH), dateTime.get(GregorianCalendar.DAY_OF_MONTH)).getTimeInMillis(),
			new GregorianCalendar(dateTime.get(GregorianCalendar.YEAR), dateTime.get(GregorianCalendar.MONTH), dateTime.get(GregorianCalendar.DAY_OF_MONTH), 23, 59, 59).getTimeInMillis()
		);
	}


	@Override
	public Report getByDateTime(GregorianCalendar dateTime) {
		return reportRepository.findById(dateTime.getTimeInMillis()).orElse(null);
	}


	@Override
	public Report getByDateTime(long dateTime) {
		return reportRepository.findById(dateTime).orElse(null);
	}


	@Override
	public List<Report> getByMessage(String message) {
		return reportRepository.findAllByMessageIgnoreCaseContainingOrderByDateTimeAsc(message)
			.orElse(new ArrayList<Report>());
	}


	@Override
	public List<Report> getByDateRange(GregorianCalendar from, GregorianCalendar to) {
		return getByDateRange(from.getTimeInMillis(), to.getTimeInMillis());
	}


	@Override
	public List<Report> getByDateRange(long from, long to) {
		return reportRepository.findAllByDateTimeBetweenOrderByDateTimeAsc(
				from<=to? from:to, 
				from<=to? to:from
		).orElse(new ArrayList<Report>());

	}

}