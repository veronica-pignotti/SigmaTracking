package com.sigmaspa.sigmatracking.service;

import java.util.List;
import java.util.Map;

import com.sigmaspa.sigmatracking.model.User;

public interface IUserService {
	
	public boolean saveUser(String idUser, String email, String password);
	
	public User getById(String id);
	
	public List<User> getByIds(List<String> ids);
	
	public boolean saveOperatorPrivilege(String idUser, String idPrivilege);

	public List<String> getIdPrivilegesByIdUser(String idUser);

	public List<User> getByIdPrivilege(String idPrivilege);
	
	public boolean saveOperatorProcess(String idUser, String idProcess);
	
	public List<String> getIdProcessesByIdUser(String idUser);
	
	public List<User> getByIdProcess(String idProcess);
	
	public List<User> getAllActiveToday();

	public User getByEmail(String email);

	public User getByEmailAndPassword(String email, String password);

	public void deleteProcesses(String idUser);

	public void deletePrivileges(String idUser);

	public Map<String, Object> getDetails(String id);
	
}
