package com.sigmaspa.sigmatracking.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@Accessors(chain = true)
@Table(name = "Entity", schema = "sigmatracking_schema", 
uniqueConstraints={
		@UniqueConstraint(columnNames = {"snSigma", "snProducer"})
	})
public class Entity {
	
	@Id
	private String snSigma;

	@NotNull
	private String snProducer;
	
	@NotNull
	private String jdeCode;
	
	@NotNull
	private String description;
	
	@Size(min = 2, max = 2)
	private String orderType;
	
	private long orderNumber;
	
	@NotNull
    @Column(columnDefinition = "boolean default false")
	private Boolean scrapped;
	
}
