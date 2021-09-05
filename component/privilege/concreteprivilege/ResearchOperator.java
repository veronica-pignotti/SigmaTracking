package com.sigmaspa.sigmatracking.component.privilege.concreteprivilege;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sigmaspa.sigmatracking.component.manager.OperatorManager;
import com.sigmaspa.sigmatracking.component.privilege.IResearchOperator;
import com.sigmaspa.sigmatracking.model.User;
import com.sigmaspa.sigmatracking.service.IUserService;

@Component
public class ResearchOperator implements IResearchOperator {

	@Autowired
	private IUserService userService;
	
	@Override
	public String getId() {
		return "Ricerca Operatore";
	}

	@Override
	public User getById(String idUser) {
		OperatorManager.log(getId() + " con id = " + idUser);
		return userService.getById(idUser);
	}

	@Override
	public List<User> getByIdProcess(String idProcess) {
		OperatorManager.log(getId() + " con idProcesso = " + idProcess);
		return userService.getByIdProcess(idProcess);
	}

	@Override
	public List<User> getByIdPrivilege(String idPrivilege) {
		OperatorManager.log(getId() + " con idPrivilegio = " + idPrivilege);
		return userService.getByIdPrivilege(idPrivilege);
	}

	@Override
	public User getByEmail(String email) {
		OperatorManager.log(getId() + " con email = " + email);
		return userService.getByEmail(email);
	}

	@Override
	public List<User> get(String idUser, String email, String idProcess, String idPrivilege) {
		OperatorManager.log(getId() + " con parametri: id = " + idUser + ", email = " + email + ", idProcesso = " + idProcess +", idPrivilegio = " + idPrivilege );

		List<User> results = new ArrayList<User>();
		
		if(!idProcess.isEmpty()) results.addAll(getByIdProcess(idProcess));
		
		if(!idPrivilege.isEmpty())
			if(results.isEmpty()) results.addAll(getByIdPrivilege(idPrivilege));
			else results = results.stream().filter(o-> getByIdPrivilege(idPrivilege).contains(o)).collect(Collectors.toList());
		
		if(!idUser.isEmpty())
			if(results.isEmpty()) results.add(getById(idUser));
			else results = results.stream().filter(o-> o.getId().equals(idUser)).collect(Collectors.toList());

		if(!email.isEmpty())
			if(results.isEmpty()) results.add(getByEmail(email));
			else results = results.stream().filter(o-> o.getEmail().equals(email)).collect(Collectors.toList());
		
		return results;
	}

	@Override
	public List<User> getAllActiveToday() {
		OperatorManager.log(getId() + " attivi oggi");
		return userService.getAllActiveToday();
	}

	@Override
	public Map<String, Object> getDetails(String id) {
		OperatorManager.log(getId() + " dettagli di " + id);
		return userService.getDetails(id);
	}

}
