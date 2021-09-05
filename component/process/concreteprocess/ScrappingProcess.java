package com.sigmaspa.sigmatracking.component.process.concreteprocess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.component.action.IDisableEntityAction;
import com.sigmaspa.sigmatracking.component.manager.ConstraintManager;
import com.sigmaspa.sigmatracking.component.process.IScrappingProcess;
import com.sigmaspa.sigmatracking.service.IEventService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScrappingProcess implements IScrappingProcess {
	
	@Autowired
	private ConstraintManager constraintManager;
	
	@Autowired
	private IDisableEntityAction iDisableEntityAction;
	
	@Autowired
	private IEventService IEventService;

	@Override
	public String scrapping(String snSigma) {
		log.info("Rottamazione dell'entità con S/N Sigma {}", snSigma);
		
		String message = constraintManager.isValidEntity(snSigma, "");
		if(message.isEmpty()) {
			long dateTime = System.currentTimeMillis();
			message = iDisableEntityAction.disableEntity(snSigma, dateTime) &&
					IEventService.save(dateTime, snSigma, getId())?
					"Rottamazione dell'entità con S/N Sigma "+ snSigma + " confermata.":
					"Rottamazione dell'entità con S/N Sigma "+ snSigma + " non confermata.";
		}
				
			
		log.info(message);
		return message;
	}
	
	@Override
	public String getId() {
		return "Rottamazione";
	}

}
