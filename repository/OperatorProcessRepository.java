package com.sigmaspa.sigmatracking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sigmaspa.sigmatracking.model.OperatorProcess;

@Repository("operatorProcessRepository")
public interface OperatorProcessRepository extends JpaRepository<OperatorProcess, Long> {
		
	public Optional<List<OperatorProcess>> findAllByIdProcess(String idProcess);
	
	public Optional<List<OperatorProcess>> findAllByIdProcessIn(List<String> ids);

	public Optional<List<OperatorProcess>> findAllByIdUser(String idUser);

	public Optional<List<OperatorProcess>> findAllByIdUserIn(List<String> ids );

}