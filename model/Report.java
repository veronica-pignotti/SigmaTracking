package com.sigmaspa.sigmatracking.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = "report", schema = "sigmatracking_schema")
public class Report {
	
	@Id
	private Long dateTime = System.currentTimeMillis();
	
	@NotNull
	private String message;
	
	public Report(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return
			getDateString() + " " + message;
				
	}
	
	public String getDateString() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Timestamp(dateTime));
	}
	
}
