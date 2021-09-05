package com.sigmaspa.sigmatracking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sigmaspa.sigmatracking.model.Entity;

@Repository("entityRepository")
public interface EntityRepository extends JpaRepository<Entity, String> {

	public Optional<List<Entity>> findAllByJdeCodeOrderBySnSigmaAsc(String jdeCode);
		
	public Optional<Entity> findAllBySnSigmaOrderBySnSigmaAsc(String snSigma);
	
	public Optional<List<Entity>> findAllBySnSigmaIn(List<String> snSigmas);
	
	public Optional<Entity> findAllBySnProducer(String snProducer);
	
	public Optional<Entity> findByJdeCodeAndSnProducer(String jdeCode, String snProducer);
	
	public Long countByJdeCode(String jdeCode);

	public Optional<List<Entity>> findAllByOrderTypeAndOrderNumberAndJdeCode(String orderType, long orderNumber,
			String jdeCode);
	
	
	
}
