package com.sigmaspa.sigmatracking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sigmaspa.sigmatracking.model.Relation;

@Repository("relationRepository")
public interface RelationRepository extends JpaRepository<Relation, Long> {

	public Optional<List<Relation>> findAllBySnSigmaContainerAndActive(String snSigmaContainer, boolean active);
	public Optional<List<Relation>> findAllBySnSigmaContainerAndSnSigmaContent(String snSigmaContainer, String snSigmaContent);
	public Optional<Relation> findBySnSigmaContentAndActive(String snSigmaContent, boolean active);
	
}
