package com.sigmaspa.sigmatracking.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Event", schema = "sigmatracking_schema")
@Accessors(chain = true)
public class Event {

	@Id
	private Long dateTime = System.currentTimeMillis();
	
	@NotNull
	private String snSigma;
		
	@NotNull
	private String idUser;
	
	@NotNull
	private String idProcess;
	
	public Event(String snSigma, String idUser, String idProcess) {
		this.snSigma = snSigma;
		this.idUser = idUser;
		this.idProcess = idProcess;
	}
	
	public String getDateString() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Timestamp(dateTime));
	}
}
