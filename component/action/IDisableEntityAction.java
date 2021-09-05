package com.sigmaspa.sigmatracking.component.action;

/**
 * Contratto che l'azione atomica di disabilitazione di un'entità deve rispettare.
 * Un'entità disabilitata, se pur presente del database, non è utilizzabile, poiché 
 * rottamata. Verranno disabilitate tutte le sue componenti insieme alle relazioni che forma.
 * */
public interface IDisableEntityAction extends IAction {

	/**
	 * Disabilita l'entità con il S/N Sigma passato.
	 * Disabilita:
	 * - ogni relazione attiva coinvolta, ponendo a false il campo "active" di @Relation.
	 * - ogni entità coinvolta nelle relazioni, compresa l'entità con il S/N Sigma passato, ponendo il 
	 * loro campo "scrapped" a true.
	 * In tal modo, tutte le entità coinvolte non saranno utilizzabili.
	 * La registrazione di questa operazione avrà data e ora passati e converitit in millisecondi. 
	 * @param snSigma il S/N Sigma dell'entità da disabilitare.
	 * @param dateTime data e ora dell'attuale registrazione, convertiti in millisecondi
	 * @return true se l'operazione è andata a buon fine, false altrimenti.
	 */
	public boolean disableEntity(String snSigma, long dateTime);

}
