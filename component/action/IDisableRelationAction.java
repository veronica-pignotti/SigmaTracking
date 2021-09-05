package com.sigmaspa.sigmatracking.component.action;

import com.sigmaspa.sigmatracking.model.Relation;

/**
 * Contratto che l'azione atomica di disabilitazione di una relazione deve rispettare.
 * Una relazione risulta disabilitata se il campo "active" dell'oggetto {@link Relation} è "false".
 * In tal modo le due etità non risulteranno associate.
 * */
public interface IDisableRelationAction extends IAction{
	
	/**
	 *  Disabilita la relazione avente snSigmaContainer come S/N Sigma dell'entità 
	 *  padre e snSigmaContent come S/N Sigma dell'entità figlia.
	 * La registrazione di questa operazione avrà data e ora passati e converititi in 
	 * millisecondi. 
	 * @param snSigmaContainer il S/N Sigma dell'entità padre.
	 * @param snSigmaContent il S/N Sigma dell'entità figlia.
	 * @param dateTime data e ora convertiti in millisecondi
	 * @return true se l'operazione è andata a buon fine, false altrimenti.
	 */
	public boolean disableRelation(
		String snSigmaContainer, 
		String snSigmaContent, 
		long dateTime
	);

}
