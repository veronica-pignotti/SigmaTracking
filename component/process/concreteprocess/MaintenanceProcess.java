package com.sigmaspa.sigmatracking.component.process.concreteprocess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.component.action.IDisableRelationAction;
import com.sigmaspa.sigmatracking.component.action.IRegisterRelationAction;
import com.sigmaspa.sigmatracking.component.manager.ConstraintManager;
import com.sigmaspa.sigmatracking.component.object.ResponseMap;
import com.sigmaspa.sigmatracking.component.process.IMaintenanceProcess;
import com.sigmaspa.sigmatracking.model.Relation;
import com.sigmaspa.sigmatracking.service.IEventService;
import com.sigmaspa.sigmatracking.service.IRelationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MaintenanceProcess implements IMaintenanceProcess {

	@Autowired
	private IRelationService relationService;
	
	@Autowired
	private ConstraintManager constraintManager;

	@Autowired
	private IDisableRelationAction disableRelationAction;
	
	@Autowired
	private IRegisterRelationAction registerRelationAction;
	
	@Autowired
	private IEventService eventService; 
	
	public ResponseMap replacement(String container, String oldContent, String newContent) {

		ResponseMap response = new ResponseMap();
		log.info("Sostituzione sull'entità con S/N Sigma {} dell'entità con S/N Sigma {} con l'entità con S/N Sigma {}", container, oldContent, newContent);

		response.setMessage(
			constraintManager.isValidContent(newContent, oldContent,
				constraintManager.existsRelation(container, oldContent,
					constraintManager.hasRelations(oldContent,
						constraintManager.isValidEntity(
								newContent,
								constraintManager.isValidEntity(oldContent, "")
						)
					)
				)
			)
		);		
		
		return response.getMessage().isEmpty()?
			replacementEntity(container, oldContent, newContent):
			response;
	}
	
	private ResponseMap replacementEntity(String container, String oldContent, String newContent) {
		List<Relation> relations = relationService.getContentsOf(oldContent);
		boolean ok = true;
		long date;
		if (relationService.isContent(oldContent))
			relations.add(relationService.getContainerOf(oldContent));

		for (Relation r : relations) {
			date = System.currentTimeMillis();
			if (r.getSnSigmaContainer().equals(oldContent))  // oldContent è il padre

				ok = disableRelationAction.disableRelation(oldContent, r.getSnSigmaContent(), date) &&
						registerRelationAction.registerRelation(newContent, r.getSnSigmaContent(), date);

			 else {  // oldContent è il figlio
				
				ok = disableRelationAction.disableRelation(container, oldContent, date) &&
						registerRelationAction.registerRelation(container, newContent, date);
			 
			 }
			if(!ok) break;

		}
		
		if(ok) eventService.save(container, this.getId());

		return new ResponseMap(
			ok
				? "Sostituzione sull'entità con S/N Sigma " + container + " dell'entità con S/N Sigma " + oldContent + " con l'entità con S/N Sigma " + newContent +" confermata."
				: "Sostituzione sull'entità con S/N Sigma " + container + " dell'entità con S/N Sigma " + oldContent + " con l'entità con S/N Sigma " + newContent + " non confermata.",
				ok? relationService.getMapOf(newContent) : null
			);

	}

	@Override
	public String getId() {
		return "Manutenzione";
	}

}
