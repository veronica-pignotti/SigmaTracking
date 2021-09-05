package com.sigmaspa.sigmatracking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Orders", schema = "sigmatracking_schema",
uniqueConstraints={
		@UniqueConstraint(columnNames = {"orderType", "orderNumber", "JDECode"})
	})
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Size(min = 2, max = 2)
	private String orderType;
	
	private long orderNumber;
	
	private String jdeCode;
	
	private String description;
	
	private Integer quantity;

}
