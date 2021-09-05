package com.sigmaspa.sigmatracking.component.action;
/**
 * Contratto che l'azione atomica della registrazione di una relazione deve rispettare.
 * */
public interface IRegisterRelationAction extends IAction {
	
	/**
	 * Registra una relazione tra i S/N Sigma passati.
	 * Tale registrazione avrà data e ora passate convertite in millisecondi
	 * @param snSigmaContainer il S/N Sigma dell'oggetto padre
	 * @param snSigmaContent il S/N Sigma dell'oggetto figlio
	 * @param date data e ora della registrazioni, convertiti in millisecondi
	 * @return true se l'operazione è andata a buon fine, false altrimenti
	 */
	public boolean registerRelation(
		String snSigmaContainer, 
		String snSigmaContent, 
		long date
	);
}
