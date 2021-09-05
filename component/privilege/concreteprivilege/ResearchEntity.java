package com.sigmaspa.sigmatracking.component.privilege.concreteprivilege;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sigmaspa.sigmatracking.component.manager.OperatorManager;
import com.sigmaspa.sigmatracking.component.privilege.IResearchEntity;
import com.sigmaspa.sigmatracking.model.Entity;
import com.sigmaspa.sigmatracking.service.IEntityService;

@Component
public class ResearchEntity implements IResearchEntity {

	@Autowired
	private IEntityService entityService;
	
	@Override
	public String getId() {
		return "Ricerca Entità";
	}

	@Override
	public List<Entity> getByAlias(Entity entity) {
		OperatorManager.log(getId() + " "+ entity);
		return entityService.getByAlias(entity);
	}

	@Override
	public List<Entity> getAllChangedToday() {
		return entityService.getAllChangedToday();
	}

}
