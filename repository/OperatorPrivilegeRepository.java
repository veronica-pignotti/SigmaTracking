package com.sigmaspa.sigmatracking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sigmaspa.sigmatracking.model.OperatorPrivilege;

@Repository("operatorPrivilegeRepository")
public interface OperatorPrivilegeRepository extends JpaRepository<OperatorPrivilege, Long> {

	public Optional<List<OperatorPrivilege>> findAllByIdUser(String idUser);
	
	public Optional<List<OperatorPrivilege>> findAllByIdPrivilege(String idPrivilege);

}
