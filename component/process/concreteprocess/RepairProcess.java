package com.sigmaspa.sigmatracking.component.process.concreteprocess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.component.manager.ConstraintManager;
import com.sigmaspa.sigmatracking.component.process.IRepairProcess;
import com.sigmaspa.sigmatracking.service.IEventService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RepairProcess implements IRepairProcess{
	
	@Autowired
	private ConstraintManager constraintManager;

	@Autowired
	private IEventService IEventService;

	@Override
	public String repair(String snSigma) {
		log.info("Riparazione dell'entità con serial number Sigma {}", snSigma);

		String message = constraintManager.isValidEntity(snSigma, "");
		
		if(message.isEmpty())
				message = IEventService.save(snSigma,getId())? 
						"Riparazione registrata": 
						"Riparazione non registrata";
			
		log.info(message);
		return message;
	}

	@Override
	public String getId() {
		return "Riparazione";
	}

}
