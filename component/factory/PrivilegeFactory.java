package com.sigmaspa.sigmatracking.component.factory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sigmaspa.sigmatracking.component.privilege.IPrivilege;

/**
 * Seguendo il design pattern Factory, raccoglie tutte le implementazioni di {@link IPrivilege} in un Set 
 * e le mette a disposizione mediante i loro id, forniti dal metodo {@link IPrivilege#getId()}.
 * Le implementazioni vengono iniettate grazie all'uso delle annotazioni.
 * */
@Component
public class PrivilegeFactory {
	
	/**
	 * Il set contenente le implementazioni di {@IPrivilege}
	 */
	private Set<IPrivilege> privileges;

	@Autowired
	public PrivilegeFactory(Set<IPrivilege> privileges) {
		this.privileges = privileges;
	  }
	
	
	/**
	 * Restituisce l'implementazione di {@link IPrivilege} con l'id passato, se esiste;
	 * null altrimenti.
	 * @param id : l'id dell'implementazione di {@link IPrivilege} richiesto
	 * @return l'implementazione di {@link IPrivilege} con l'id passato, se esiste; false altrimenti
	 */
	public IPrivilege get(String id) {
		return privileges.stream().filter(p-> p.getId().equals(id))
				.findFirst().orElse(null);
	}

	/**
	 * Restituisce la lista degli id delle implementazioni di {@link IPrivilege}
	 * */
	public List<String> getIds() {
		return privileges.stream().map(p->p.getId()).collect(Collectors.toList());
	}

	/**
	 * Restituisce la lista {@link IPrivilege} i cui id sono contenuti nella lista ids passata.
	 * @param ids : la lista degli id che devono avere le implementazioni di {@link IPrivilege} richieste.
	 * @return la lista {@link IPrivilege} i cui id sono contenuti nella lista ids passata.
	 * */
	public List<IPrivilege> getAll(List<String> ids) {
		return privileges.stream()
				.filter(p-> ids.contains(p.getId()))
				.collect(Collectors.toList());		
	}

}
