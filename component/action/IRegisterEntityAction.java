package com.sigmaspa.sigmatracking.component.action;

/**
 * Contratto che l'azione atomica della registrazione di un'entità deve rispettare.
 * */
public interface IRegisterEntityAction extends IAction {
	
	
	/**
	 * Crea un'entità con i parametri passati e la registra nel database.
	 * Questa registrazione avrà data e ora passati, convertiti in millisecondi.
	 * @param snSigma il S/N Sigma dell'entità da creare
	 * @param snProducer il S/N del fornitore dell'entità da creare
	 * @param jdeCode il codice JDE dell'entità da creare
	 * @param description la descrizione dell'entità da creare
	 * @param orderType il tipo dell'ordine con cui tale entità viene registrata
	 * @param orderNumber il numero dell'ordine con cui tale entità viene registrata
	 * @param dateTime data e ora di tale registrazione, convertite in millisecondi
	 * @return true se l'operazione è andata a buon fine; false altrimenti.
	 * @throws Exception se il salvataggio non va a buon fine
	 */
	public boolean registerEntity(
			String snSigma, 
			String snProducer, 
			String jdeCode, 
			String description, 
			String orderType, 
			Long orderNumber, 
			long dateTime
	) throws Exception;
}
