package com.sigmaspa.sigmatracking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
		name = "Relation", schema = "sigmatracking_schema",
		uniqueConstraints={
				@UniqueConstraint(columnNames = {"snSigmaContainer", "snSigmaContent", "active"})
			}
		)
public class Relation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@NotNull
	private String snSigmaContainer;
	
	@NotNull
	private String snSigmaContent;
	
	@NotNull
	private Boolean active;
	
	public Relation(String snSigmaContainer, String snSigmaContent) {
		this.snSigmaContainer = snSigmaContainer;
		this.snSigmaContent = snSigmaContent;
		this.active = true;
	}



}
