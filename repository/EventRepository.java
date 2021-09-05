package com.sigmaspa.sigmatracking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sigmaspa.sigmatracking.model.Event;

@Repository("eventRepository")
public interface EventRepository extends JpaRepository<Event, Long> {

	public Optional<List<Event>> findAllByidUserOrderByDateTimeAsc(String idUser);
 
	public Optional<List<Event>> findAllBySnSigmaOrderByDateTimeAsc(String snSigma);
	
	public Optional<List<Event>> findAllByDateTimeBetweenOrderByDateTimeAsc(long from, long to);

	public Optional<List<Event>> findAllByIdProcessOrderByDateTimeAsc(String idProcess);
	
}