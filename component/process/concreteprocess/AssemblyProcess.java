package com.sigmaspa.sigmatracking.component.process.concreteprocess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.component.action.IRegisterRelationAction;
import com.sigmaspa.sigmatracking.component.manager.ConstraintManager;
import com.sigmaspa.sigmatracking.component.object.ResponseMap;
import com.sigmaspa.sigmatracking.component.process.IAssemblyProcess;
import com.sigmaspa.sigmatracking.service.IEventService;
import com.sigmaspa.sigmatracking.service.IRelationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssemblyProcess implements IAssemblyProcess{
	
	@Autowired
	private IRelationService relationService;
	
	@Autowired
	private IEventService eventService;
	
	@Autowired
	private ConstraintManager constraintManager;
		
	@Autowired
	private IRegisterRelationAction registerRelation;
	
	@Override
	public ResponseMap assembly(String snSigmaContainer, String snSigmaContent) {
		log.info("Montaggio tra entità {} e {}", snSigmaContainer, snSigmaContent);
		
		ResponseMap response = new ResponseMap().setMessage(
			constraintManager.isValidContent(
				snSigmaContent, 
				snSigmaContainer,
				constraintManager.isValidEntity(
					snSigmaContent,		
					constraintManager.isValidEntity(snSigmaContainer, "")
				)
			)
		);
			
		try {
			return response.getMessage().isEmpty()?
					assemblyEntities(snSigmaContainer, snSigmaContent):
						response;
		} catch (Exception e) {
			return response.setMessage(e.getMessage());
		}
			
	}
	
	private ResponseMap assemblyEntities(String snSigmaContainer, String snSigmaContent) {
		long dateTime = System.currentTimeMillis();
		boolean ok = registerRelation.registerRelation(snSigmaContainer, snSigmaContent, dateTime) &&
				eventService.save(dateTime, snSigmaContainer, getId());
		
		return
			new ResponseMap()
			.setMessage(
				ok?
					"Montaggio tra l'entità con S/N Sigma " + snSigmaContainer + " e l'entità con S/N Sigma " + snSigmaContent + " confermato":
					"Montaggio tra l'entità con S/N Sigma " + snSigmaContainer + " e l'entità con S/N Sigma " + snSigmaContent + " non confermato"
			).setData(ok? relationService.getMapOf(snSigmaContainer):null);
		
	}

	@Override
	public String getId() {
		return "Montaggio";
	}
	
}
