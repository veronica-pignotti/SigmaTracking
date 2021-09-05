package com.sigmaspa.sigmatracking.component.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ResponseMap {
	
	private String message;
	private Object data;
	
	public ResponseMap setMessage(String message) {
		if(!message.isEmpty()) log.info(message);
		this.message = message;
		return this;
	}
	
	public ResponseMap setData(Object data) {
		this.data = data;
		return this;
	}

}
