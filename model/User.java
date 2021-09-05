package com.sigmaspa.sigmatracking.model;

import javax.persistence.Entity;
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
@Table(name = "User", schema = "sigmatracking_schema")
public class User {

	@Id
	@NotNull
	private String id;
	
	@NotNull
	private String email;
	
	@NotNull
	private String password;
}
