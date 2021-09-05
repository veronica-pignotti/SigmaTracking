package com.sigmaspa.sigmatracking.component.process.concreteprocess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.component.action.IRegisterRelationAction;
import com.sigmaspa.sigmatracking.component.manager.ConstraintManager;
import com.sigmaspa.sigmatracking.component.object.ResponseMap;
import com.sigmaspa.sigmatracking.component.process.ITestingProcess;
import com.sigmaspa.sigmatracking.service.IEventService;
import com.sigmaspa.sigmatracking.service.IRelationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestingProcess implements ITestingProcess {

	@Autowired
	private IRelationService iRelationService;
	
	@Autowired
	private ConstraintManager constraintManager;
		
	@Autowired
	private IRegisterRelationAction registerRelation;
	
	@Autowired
	private IEventService IEventService;

	@Override
	public ResponseMap testing(String snSigmaContainer, String snSigmaContent) {
		log.info("Collaudo tra entità {} e {}", snSigmaContainer, snSigmaContent);
		String existRel = constraintManager.existsRelation(
				snSigmaContainer, 
				snSigmaContent,
				""
		);
				
		ResponseMap response = new ResponseMap().setMessage(
			existRel.isEmpty()?
				"":
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
				testingEntities(snSigmaContainer, snSigmaContent):
				response;
		} catch (Exception e) {
			return response.setMessage(e.getMessage());
		}
		
	}
	
	private ResponseMap testingEntities(String snSigmaContainer, String snSigmaContent) {
		long dateTime = System.currentTimeMillis();
		boolean ok = registerRelation.registerRelation(snSigmaContainer, snSigmaContent, dateTime) &&
				IEventService.save(dateTime, snSigmaContainer, getId());
		
		return
			new ResponseMap()
			.setMessage(
				ok?
					"Collaudo tra l'entità con S/N Sigma " + snSigmaContainer + " e l'entità con S/N Sigma " + snSigmaContent + " confermato":
					"Collaudo tra l'entità con S/N Sigma " + snSigmaContainer + " e l'entità con S/N Sigma " + snSigmaContent + " non confermato"
			).setData(ok? iRelationService.getMapOf(snSigmaContainer):null);			
		
	}

	@Override
	public String getId() {
		return "Collaudo";
	}

}
