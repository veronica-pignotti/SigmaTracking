package com.sigmaspa.sigmatracking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sigmaspa.sigmatracking.model.Report;

@Repository("reportRepository")
public interface ReportRepository extends JpaRepository<Report, Long>{
		 
	public Optional<List<Report>> findAllByMessageIgnoreCaseContainingOrderByDateTimeAsc(String message);

	public Optional<List<Report>> findAllByDateTimeBetweenOrderByDateTimeAsc(Long from, Long to);
	
	public Optional<Report> findByDateTime(Long dateTime);
}
