package com.sigmaspa.sigmatracking.component.process;

import com.sigmaspa.sigmatracking.component.object.ResponseMap;

public interface IAssemblyProcess extends IProcess {

	/**
	 * Registra il montaggio tra le entità con i S/N Sigma specificati, creando una relazione e l'azione inerente.
	 * Tali operazioni vengono effettuate solo se:
	 * 1- Entrambi i S/N Sigma specificati appartengono a entità esistenti (registrate nel database);
	 * 2- Entrambi i S/N Sigma specificati appartengono a entità non rottamate;
	 * 3- Il S/N Sigma del contenuto non risulta già appartenere ad un'altra entità, poiché ogni entità deve avere un solo padre.
	 * 4- Le entità con i S/N Sigma specificati non devono far parte della stessa entità. 
	 * Se la creazione della relazione va a buon fine, la @ResponseMap ritornata conterrà il nuovo albero genealogico.
	 * @param name nome del processo
	 * @param container S/N Sigma del contenitore
	 * @param content S/N Sigma del contenuto
	 * @return un'istanza di @ResponseMap contenente un messaggio che comunica il risultato di tale operazione e l'eventuale albero genealogico aggiornato.
	 */
	public ResponseMap assembly(String snSigmaContainer, String snSigmaConten);
}
