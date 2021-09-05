package com.sigmaspa.sigmatracking.component.object;

import com.sigmaspa.sigmatracking.model.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Oggetto utilizzato da {@link ResearchEventPageController} per raccogliere tutti i parametri necessari
 * al fine di eseguire una ricerca più completa degli operatori.
 * */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ResearchOperatorObject {
	
	/**
	 * L'istanza di {@User} ricercata
	 * */
	private User user = new User();
	
	/**
	 * L'id di {@link IProcess} che devono avere abilitato i risultati mostrati.
	 * */	
	private String idProcess;
	
	/**
	 * L'id di {@link IPrivilege} che devono avere abilitato i risultati mostrati.
	 * */	
	private String idPrivilege;
}