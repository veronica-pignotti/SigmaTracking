package com.sigmaspa.sigmatracking.component.privilege.concreteprivilege;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sigmaspa.sigmatracking.component.manager.OperatorManager;
import com.sigmaspa.sigmatracking.component.privilege.IResearchRelation;
import com.sigmaspa.sigmatracking.service.IRelationService;

@Component
public class ResearchRelation implements IResearchRelation {
	@Autowired
	private IRelationService relationService;
	
	@Override
	public String getId() {
		return "Ricerca Relazione";
	}

	@Override
	public Map<String, List<String>> get(String snSigma) {
		OperatorManager.log(getId() + " dell'entità con S/N Sigma = " + snSigma);
		return relationService.getMapOf(snSigma);
	}

}