package com.sigmaspa.sigmatracking.component.action.concreteaction;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.component.action.IDisableEntityAction;
import com.sigmaspa.sigmatracking.component.action.IDisableRelationAction;
import com.sigmaspa.sigmatracking.service.IEntityService;
import com.sigmaspa.sigmatracking.service.IReportService;
import com.sigmaspa.sigmatracking.service.IRelationService;


@Service
public class DisableEntityAction implements IDisableEntityAction {
	
	@Autowired
	private IReportService reportService;
	
	@Autowired
	private IRelationService relationService;
	
	@Autowired
	private IEntityService entityService;
	
	@Autowired
	private IDisableRelationAction disableRelationAction;
	
	public boolean disableEntity(String snSigma, long dateTime) {
		Set<String> snSigmas = new HashSet<String>(), relations = new HashSet<String>();
		
		relationService.getTreeOf(snSigma)
			.forEach(r->{
				disableRelationAction.disableRelation(r.getSnSigmaContainer(), r.getSnSigmaContent(), dateTime);
				entityService.scrap(r.getSnSigmaContainer());
				entityService.scrap(r.getSnSigmaContent());
				
				snSigmas.add(r.getSnSigmaContainer());
				snSigmas.add(r.getSnSigmaContent());
				
				relations.add(r.getSnSigmaContainer() + " -> " + r.getSnSigmaContent());	
			});

		return reportService.save(
				dateTime, 
				"\n" + getType() + ": " + snSigmas.toString() + "\n" + 
				disableRelationAction.getType() + ": " + relations.toString());
	}

	@Override
	public String getType() {
		return "DISABLE_ENTITY";
	}

}
