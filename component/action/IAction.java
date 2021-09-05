package com.sigmaspa.sigmatracking.component.action;

/**
 * Definisce il contratto che ogni azione atomica deve rispettare.
 * Le azioni atomiche verranno inserite negli oggetti @Report e, per una loro maggiore chiarezza, 
 * dovranno mettere a disposizione un nome.
 * */
public interface IAction {
	
	/**
	 * Restituisce il nome dell'azione atomica
	 * */
	public String getType();

}
