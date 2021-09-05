package com.sigmaspa.sigmatracking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operator_process", schema = "sigmatracking_schema")
public class OperatorProcess {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotNull
	private String idUser;
	
	@NotNull
	private String idProcess;
	
	public OperatorProcess(String idUser, String idProcess) {
		this.idUser = idUser;
		this.idProcess = idProcess;
	}
}
