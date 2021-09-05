package com.sigmaspa.sigmatracking.component.object;

import com.sigmaspa.sigmatracking.model.Event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Oggetto utilizzato da {@link ResearchEventPageController} per raccogliere tutti i parametri necessari
 * al fine di eseguire una ricerca più completa degli eventi.
 * */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ResearchEventObject {
	
	/**
	 * L'istanza di {@link Event} ricercata
	 * */
	private Event event = new Event();
	
	/**
	 * La data di inizio che devono avere gli eventi mostrati, sottoforma di stringa
	 * */
	private String from;
	
	/**
	 * La data di fine che devono avere gli eventi mostrati, sottoforma di stringa
	 * */		
	private String to;

}
