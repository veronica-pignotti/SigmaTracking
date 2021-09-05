package com.sigmaspa.sigmatracking.component.factory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sigmaspa.sigmatracking.component.process.IProcess;
/**
 * Seguendo il design pattern Factory, raccoglie tutte le implementazioni di {@link IProcess} in un Set 
 * e le mette a disposizione mediante i loro id, forniti dal metodo {@link IProcess#getId()}
 * Le implementazioni vengono iniettate grazie all'uso delle annotazioni.
 * */
@Component
public class ProcessFactory {
	/**
	 * Il set contenente le implementazioni di {@IProcess}
	 */
	private Set<IProcess> processes;

	@Autowired
	public ProcessFactory(Set<IProcess> processes) {
		this.processes = processes;
	  }
	
	/**
	 * Restituisce l'implementazione di {@link IProcess} con l'id passato, se esiste;
	 * null altrimenti.
	 * @param id : l'id dell'implementazione di {@link IProcess} richiesto
	 * @return l'implementazione di {@link IProcess} con l'id passato, se esiste; false altrimenti
	 */
	public IProcess get(String id) {
		return processes.stream()
				.filter(p-> p.getId().equals(id)).findFirst()
				.orElse(null);
	}

	/**
	 * Restituisce la lista degli id delle implementazioni di {@link IProcess}
	 * */
	public List<String> getIds() {
		return processes.stream().map(p->p.getId()).collect(Collectors.toList());
	}

	/**
	 * Restituisce la lista {@link IProcess} i cui id sono contenuti nella lista ids passata.
	 * @param ids : la lista degli id che devono avere le implementazioni di {@link IProcess} richieste.
	 * @return la lista {@link IProcess} i cui id sono contenuti nella lista ids passata.
	 * */
	public List<IProcess> getAll(List<String> ids) {
		return processes.stream().filter(p-> ids.contains(p.getId())).collect(Collectors.toList());		
	}

}