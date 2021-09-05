package com.sigmaspa.sigmatracking.component.privilege.concreteprivilege;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sigmaspa.sigmatracking.component.manager.OperatorManager;
import com.sigmaspa.sigmatracking.component.privilege.IRegisterOperator;
import com.sigmaspa.sigmatracking.service.IUserService;

@Component
public class RegisterOperator implements IRegisterOperator {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private OperatorManager operatorManager;
		
	@Override
	public String getId() {
		return "Registra Operatore";
	}

	@Override
	public String save(String idUser, List<String> processes, List<String> privileges) {
		if(userService.getById(idUser)==null) return "Operatore inesistente.";
		
		userService.deleteProcesses(idUser);
		
		processes.forEach((p)->{
			userService.saveOperatorProcess(idUser, p);
		});
		
		userService.deletePrivileges(idUser);
		
		privileges.forEach((p)->{
			userService.saveOperatorPrivilege(idUser, p);
		});
		
		OperatorManager.log(getId() + " id = " + idUser + " processi abilitati = " + processes.toString() + " privilegi abilitati = " + privileges.toString());
		return "Salvato";
	}
	
	@Override
	public Map<String, Map<String, Boolean>> get(String idUser, List<String> processes, List<String> privileges){
		Map<String, Map<String, Boolean>> map = new HashMap<>();
		
		List<String> operatorProcesses = userService.getIdProcessesByIdUser(idUser);
		Map<String, Boolean> processesMap = new HashMap<String, Boolean>();
		processes.forEach(p-> processesMap.put(p, operatorProcesses.contains(p)));
		map.put("processes", processesMap );
		
		List<String> operatorPrivileges = userService.getIdPrivilegesByIdUser(idUser);
		Map<String, Boolean> privilegesMap = new HashMap<String, Boolean>();
		privileges.forEach(p-> privilegesMap.put(p, operatorPrivileges.contains(p)));
		map.put("privileges", privilegesMap);
		
		return map;
	}

}
