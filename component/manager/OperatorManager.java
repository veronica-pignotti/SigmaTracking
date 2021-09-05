package com.sigmaspa.sigmatracking.component.manager;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sigmaspa.sigmatracking.component.factory.PrivilegeFactory;
import com.sigmaspa.sigmatracking.component.factory.ProcessFactory;
import com.sigmaspa.sigmatracking.component.object.Operator;
import com.sigmaspa.sigmatracking.component.privilege.IPrivilege;
import com.sigmaspa.sigmatracking.component.process.IProcess;
import com.sigmaspa.sigmatracking.model.User;
import com.sigmaspa.sigmatracking.service.IUserService;

import lombok.extern.slf4j.Slf4j;


/**
 * Si occupa di gestire tutte le azioni strettamente legate all'operatore.
 * Infatti implementa operazioni come la registrazione alla pizttaforma, login e log out.
 * Inoltre costruisce l'istanza dell'operatore corrente e ricava dal database tutte le funzionalità ad esso associate. 
 *
 */
@Slf4j
@Component
public class OperatorManager{
	
	/**
	 * L'istanza dell'operatore corrente
	 * */
	private static Operator operator;
	
	@Autowired
	private ProcessFactory processFactory;
	
	@Autowired
	private PrivilegeFactory privilegeFactory;
	
	@Autowired
	private IUserService userService;
	
	/**
	 * Effettua alcune semplici controlli sulle credenziali passate, che non devono essere vuote.
	 * Inoltre email deve avere come suffisso "@sigmaspa.com".
	 * Genera un errore se non si rispettano questi vincoli.
	 * @param email l'email inserita dall'utente.
	 * @param password la password inserita dall'utente.
	 * */
	protected void checkCredentials(String email, String password) throws Exception{
		if(Objects.isNull(email) || email.isEmpty() || !email.endsWith("@sigmaspa.com")) 
			throw new Exception("Inserisci un'email SIGMA (@sigmaspa.com)");
		if(Objects.isNull(password) || password.isEmpty())
			throw new Exception("Inserisci una password");
	}
	
	/**
	 * Effettua i controlli sulle credenziali passate e registra il nuovo utente, ritornando una stringa 
	 * che descrive l'esito di tali operazioni.
	 * @param email l'email inserita dall'utente.
	 * @param password la password inserita dall'utente.
	 * @return una stringa che descrive l'esito dei controlli e dell'eventuale registrazione dell'utente.
	 * */
	public String signin(String email, String password) {
		try {
			checkCredentials(email, password);
			
			if(Objects.nonNull(userService.getByEmail(email))) 
				throw new Exception("L'email " + email + " risulta già registrata");
			
			log("Registrazione utente con email = " + email + ", password =  " + password);
			return signinImpl(email, password);
		}catch(Exception e) {
			log("Registrazione Utente con email = " + email + ", password =  " + password + " " + e.getMessage());
			return e.getMessage();
		}
		
	}
	
	/**
	 * Registra il nuovo utente, ritornando una stringa che ne descrive l'esito.
	 * @param email l'email inserita dall'utente.
	 * @param password la password inserita dall'utente.
	 * @return una stringa che descrive l'esito della registrazione dell'utente.
	 * */
	protected String signinImpl(String email, String password) throws Exception {
		if(userService.saveUser(
			email.split("@")[0], 
			email, 
			encodePassword(password)
		)) return "Registrazione confermata. Ora un amministratore può abilitarti le funzionalità che riguardano il tuo ruolo.";
		throw new Exception("Si è verificato un errore."); 	
	}
	
	/**
	 * Codifica, attraverso l'algoritmo di crittografia SHA 256 la password passata.
	 * @param password la password da codificare
	 * @return la password codificata
	 * */
	protected String encodePassword(String password) {
		String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(hash);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
	}
	
	/**
	 * Effettua i controlli sull'istanza di {@link User} passata, verificando la validità delle credenziali.
	 * Se non valide, ritorna una stringa che descrive il problema; altrimenti una stringa vuota.
	 * @param user l'istanza dell'utente che vuole effettuare l'accesso alla piattaforma.
	 * @return una stringa vuota se user è valido, altrimenti una stringa che descrive il problema riscontrato
	 * */
	public String login(User user){
		
		try {
			checkCredentials(user.getEmail(), user.getPassword());
			user = userService.getByEmailAndPassword(user.getEmail(), encodePassword(user.getPassword()));
			if(Objects.isNull(user)) throw new Exception("Credenziali errate");
			build(user.getId());
			log("Login con email = " + user.getEmail());
			return "";
		} catch (Exception e) {
			log("Login con email = " + user.getEmail() + " " + e.getMessage());
			return e.getMessage();
		}
	}
	
	/**
	 * Costruisce l'istanza dell'operatore corrispondente all'utente con l'id passato
	 * */
	protected void build(String id) {
	
		operator = new Operator(
			id, 
			processFactory.getAll(userService.getIdProcessesByIdUser(id)),
			privilegeFactory.getAll(userService.getIdPrivilegesByIdUser(id))
		);
		
	}
	
	/**
	 * Restituisce l'istanza dell'operatore corrente, se esiste; altrimenti un'istanza temporanea.
	 * */
	public Operator getOperator() {
		return Objects.nonNull(operator)? operator:
			new Operator("N/D", new ArrayList<IProcess>(), new ArrayList<IPrivilege>());
	}
	
	/**
	 * Ritorna l'implementazione di {@link IProcess}} assegnato con l'id passato. Se inesistente, genera un messaggio di errore.
	 * @param idProcess l'id dell'istanza {@link IProcess}} richiesta
	 * @return l'implementazione di {@link IProcess}} richiesta
	 * @throws Exception se non esiste alcun processo abilitato a tale operatore avente l'id passato.
	 * */
	public IProcess getProcess(String idProcess) throws Exception {
		try {
			return operator.getProcesses().stream().filter(p->p.getId().equals(idProcess)).findFirst().get();	
		}catch(Exception e) {
			throw new Exception("Non sei abilitato al processo " + idProcess);
		}		
	}


	/**
	 * Cancella l'istanza dell'operatore creata.
	 * */
	public void logout() {
		log("Logout");
		operator = null;		
	}

	/**
	 * Ritorna l'implementazione di {@link IPrivilege} assegnato all'operatore con l'id passato. Se inesistente, genera un messaggio di errore.
	 * @param idPrivilege l'id del processo richiesto
	 * @return l'implementazione del processo con l'id passato
	 * @throws Exception se non esiste alcun processo abilitato a tale operatore avente l'id passato.
	 * */
	public IPrivilege getPrivilege(String idPrivilege) throws Exception {
		try {
			return operator.getPrivileges().stream().filter(p->p.getId().equals(idPrivilege)).findFirst().get();	
		}catch(Exception e) {
			throw new Exception("Non sei abilitato al privilegio " + idPrivilege);
		}
	}
	
	/**
	 * Registra un log, composto dall'id dell'operatore corrente, se esiste e il messaggio passato.
	 * @param message
	 * */
	public static void log(String message) {
		log.info(
			"{} : {}", 
			Objects.isNull(operator)? "": operator.getId(), 
			message
		);
	}

	
}