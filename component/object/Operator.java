package com.sigmaspa.sigmatracking.component.object;

import java.util.List;
import java.util.stream.Collectors;

import com.sigmaspa.sigmatracking.component.privilege.IPrivilege;
import com.sigmaspa.sigmatracking.component.process.IProcess;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rappresenta l'istanza dell'operatore corrente.
 * Contiene tutte le istanze delle implementazioni di {@link IProcess} e {@link IPrivilege} a lui assegnati
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operator {
	/**
	 * L'id dell'utente a cui si riferisce
	 * */
	private String id;
	
	/**
	 * Lista degli {@link IProcess} a lui abilitati
	 * */
	private List<IProcess> processes;
	/**
	 * Lista degli {@link IPrivilege} a lui abilitati
	 * */	
	private List<IPrivilege> privileges;
	
	/**
	 * Restituisce la lista degli id degli {@link IProcess} a lui abilitati.
	 * @return la lista degli id degli {@link IProcess} a lui abilitati.
	 * */
	public List<String> getIdProcesses(){
		return processes.stream().map(p->p.getId()).collect(Collectors.toList());
	}

	/**
	 * Restituisce la lista degli id degli {@link IPrivilege} a lui abilitati.
	 * @return la lista degli id degli {@link IPrivilege} a lui abilitati.
	 * */	
	public List<String> getIdPrivileges(){
		return privileges.stream().map(p->p.getId()).collect(Collectors.toList());
	}
	
}
