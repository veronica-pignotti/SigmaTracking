package com.sigmaspa.sigmatracking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OperatorPrivilege", schema = "sigmatracking_schema")
public class OperatorPrivilege {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private String idUser;
	
	private String idPrivilege;
	
	public OperatorPrivilege(String idUser, String idPrivilege) {
		this.idUser = idUser;
		this.idPrivilege = idPrivilege;
	}
}
