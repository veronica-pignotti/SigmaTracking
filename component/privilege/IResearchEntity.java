package com.sigmaspa.sigmatracking.component.privilege;

import java.util.List;

import com.sigmaspa.sigmatracking.model.Entity;

public interface IResearchEntity extends IPrivilege{
	
	public List<Entity> getByAlias(Entity entity);

	public List<Entity> getAllChangedToday();

}
