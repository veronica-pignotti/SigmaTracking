package com.sigmaspa.sigmatracking.component.privilege;

import java.util.List;
import java.util.Map;

public interface IRegisterOperator extends IPrivilege {
	
	public String save(String idUser, List<String> processes, List<String> privileges);

	public Map<String, Map<String, Boolean>> get(String idUser, List<String> processes, List<String> privileges);

}
