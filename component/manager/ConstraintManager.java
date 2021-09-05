package com.sigmaspa.sigmatracking.component.manager;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sigmaspa.sigmatracking.service.IEntityService;
import com.sigmaspa.sigmatracking.service.IRelationService;


/**
 * Implementa dei controlli sui vincoli tra gli elementi dell'applicazione.
 * Se il vincolo definito dal nome del metodo è soddisfatto, il metodo ritornerà una stringa vuota, 
 * altrimenti un messaggio che dichiara un problema.
 * I metodi sono componibili, nel senso che ogni metodo può accettare, tra i suoi parametri, il risultato 
 * di un altro metodo e ritornarlo se non è una stringa vuota.
 */
@Component
public class ConstraintManager {
	
	@Autowired
	private IRelationService relationService;
	
	@Autowired
	private IEntityService entityService;
	
	/**
	 * Se message non è una stringa vuota la ritorna.
	 * Altrimenti controlla se l'entità avente il S/N passato è valida.
	 * Un'entità risulta valida se:
	 * - è stata registrata: esiste nel database;
	 * - non risulta rottamata (il suo campo scrapped è false).
	 * Ritorna una stringa vuota se è soddisfatto il vincolo dichiarato dal nome del metodo,
	 * altrimenti un messaggio che dichiara il problema.
	 * */
	public String isValidEntity(String snSigma, String message) {
		return !message.isEmpty()?
			message:
				!entityService.containsSnSigma(snSigma)?
					"\nL'entità con S/N Sigma " + snSigma + " non risulta registrata.":
					entityService.isScrapped(snSigma)?
							"\nL'entità con S/N Sigma " + snSigma + " risulta rottamata.":
								"";
	}
	
	/**
	 * Se message non è una stringa vuota la ritorna.	 
	 * Altrimenti controlla se l'entità con S/N Sigma content, può essere associato come figlio all'entita con S/N Sigma container.
	 * Ciò avviene se:
	 * - l'entità content non appartiene ad un altro padre, poiché un oggetto non può essere contemporaneamente presente in due componenti diversi;
	 * - le due entità passate non appartengono allo stesso componente, quindi alle stesso albero genealogico.
	 * Ritorna una stringa vuota se è soddisfatto il vincolo dichiarato dal nome del metodo,
	 * altrimenti un messaggio che dichiara il problema.
	 * */
	public String isValidContent(String content, String container, String message) {
		return !message.isEmpty()?
			message:
				relationService.isContent(content)?
				"\nL'entità con S/N Sigma " + content + " risulta già appartenere ad un'entità.":
					relationService.areRelatives(content, container)?
						"\nLe entità con S/N Sigma " + content +" e " + container + " fanno parte della medesima entità, avente S/N Sigma " + relationService.getRootOf(container):
						"";
	}
	
	/**
	 * Se message non è una stringa vuota la ritorna.
	 * Altrimenti controlla se l'entità con il S/N Sigma passato ha relazioni attive.
	 * Ritorna una stringa vuota se è soddisfatto il vincolo dichiarato dal nome del metodo,
	 * altrimenti un messaggio che dichiara il problema.
	 * */
	public String hasRelations(String snSigma, String message) {
		return !message.isEmpty()? 
			message:
			relationService.isActive(snSigma)?
				"":
				"\nL'entità con S/N Sigma " + snSigma + " non appartiene ad alcun componente.";	
	}
	
	/**
	 * Se message non è una stringa vuota la ritorna.	 
	 * Altrimenti controlla se esiste una relazione (relazione attiva) tra l'entità con il S/N Sigma snSigmaContainer e l'entità con il S/N Sigma snSigmaContent.
	 * Ritorna una stringa vuota se è soddisfatto il vincolo dichiarato dal nome del metodo,
	 * altrimenti un messaggio che dichiara il problema.
	 * */
	public String existsRelation(String snSigmaContainer, String snSigmaContent, String message) {
		return !message.isEmpty()? 
			message:
			Objects.nonNull(relationService.getContainerOf(snSigmaContent)) && relationService.getContainerOf(snSigmaContent).getSnSigmaContainer().equals(snSigmaContainer)?
				"":
				"Le entità con S/N Sigma " + snSigmaContainer + " e " + snSigmaContent + " non risultano assemblate.";	
	}

}
