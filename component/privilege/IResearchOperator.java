package com.sigmaspa.sigmatracking.component.privilege;

import java.util.List;
import java.util.Map;

import com.sigmaspa.sigmatracking.model.User;

public interface IResearchOperator extends IPrivilege {
	
	public List<User> get(String idUser, String email, String idProcess, String idPrivilege);
	
	public User getById(String idUser);
	
	public User getByEmail(String email);
	
	public List<User> getByIdProcess(String processType);
	
	public List<User> getByIdPrivilege(String privilegeName);

	public List<User> getAllActiveToday();
	
	public Map<String, Object> getDetails(String id);

}
