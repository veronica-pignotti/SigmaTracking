package com.sigmaspa.sigmatracking.service.concreteservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.model.User;
import com.sigmaspa.sigmatracking.model.OperatorPrivilege;
import com.sigmaspa.sigmatracking.model.OperatorProcess;
import com.sigmaspa.sigmatracking.repository.OperatorPrivilegeRepository;
import com.sigmaspa.sigmatracking.repository.OperatorProcessRepository;
import com.sigmaspa.sigmatracking.repository.UserRepository;
import com.sigmaspa.sigmatracking.service.IEventService;
import com.sigmaspa.sigmatracking.service.IUserService;

@Service("operatorService")
public class UserService implements IUserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OperatorProcessRepository operatorProcessRepository;
	
	@Autowired
	private OperatorPrivilegeRepository operatorPrivilegeRepository;
	
	@Autowired
	private IEventService eventService;
	
	@Override
	public User getById(String id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public boolean saveUser(String idUser, String email, String password) {
		return userRepository.save(new User(idUser, email, password)) != null;
	}

	@Override
	public List<User> getByIds(List<String> ids) {
		return userRepository.findAllById(ids);
	}

	@Override
	public boolean saveOperatorPrivilege(String idUser, String idPrivilege) {
		return operatorPrivilegeRepository.save(new OperatorPrivilege(idUser, idPrivilege))!=null;
	}

	@Override
	public List<String> getIdPrivilegesByIdUser(String idUser) {
		return operatorPrivilegeRepository.findAllByIdUser(idUser)
				.orElse(new ArrayList<OperatorPrivilege>())
				.stream()
				.map(op->op.getIdPrivilege())
				.collect(Collectors.toList());
	}

	@Override
	public List<User> getByIdPrivilege(String idPrivilege) {
		return getByIds(
			operatorPrivilegeRepository.findAllByIdPrivilege(idPrivilege)
			.orElse(new ArrayList<OperatorPrivilege>())
			.stream()
			.map(op->op.getIdUser())
			.collect(Collectors.toList())
		);
	}

	@Override
	public boolean saveOperatorProcess(String idUser, String idProcess) {
		return operatorProcessRepository.save(new OperatorProcess(idUser, idProcess))!=null;
	}

	@Override
	public List<String> getIdProcessesByIdUser(String idUser) {
		return operatorProcessRepository.findAllByIdUser(idUser)
				.orElse(new ArrayList<OperatorProcess>())
				.stream()
				.map(op->op.getIdProcess())
				.collect(Collectors.toList());
	}

	@Override
	public List<User> getByIdProcess(String idProcess) {
		return getByIds(
				operatorProcessRepository.findAllByIdProcess(idProcess)
				.orElse(new ArrayList<OperatorProcess>())
				.stream()
				.map(op->op.getIdUser())
				.collect(Collectors.toList())
			);
	}

	@Override
	public List<User> getAllActiveToday() {
		return getByIds(
			eventService.getByDate(System.currentTimeMillis())
			.stream().map(e->e.getIdUser()).distinct().collect(Collectors.toList())
		);
	}

	@Override
	public User getByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}
	
	@Override
	public User getByEmailAndPassword(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password).orElse(null);
	}

	@Override
	public void deleteProcesses(String idUser) {
		List<OperatorProcess> operators = operatorProcessRepository.findAllByIdUser(idUser).orElse(new ArrayList<OperatorProcess>());
		if(!operators.isEmpty())
			operatorProcessRepository.deleteInBatch(operators);
	}

	@Override
	public void deletePrivileges(String idUser) {
		List<OperatorPrivilege> operators = operatorPrivilegeRepository.findAllByIdUser(idUser).orElse(new ArrayList<OperatorPrivilege>());
		if(!operators.isEmpty())
			operatorPrivilegeRepository.deleteInBatch(operators);
	}

	@Override
	public Map<String, Object> getDetails(String id) {

		Map<String, Object> map = new HashMap<>();
		map.put("op", getById(id));
		map.put("events", eventService.getByIdUser(id));
		map.put("processes", getIdProcessesByIdUser(id));
		map.put("privileges", getIdPrivilegesByIdUser(id));
		return map;

	}

}
