package com.sigmaspa.sigmatracking.component.action.concreteaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.service.IReportService;
import com.sigmaspa.sigmatracking.component.action.IRegisterRelationAction;
import com.sigmaspa.sigmatracking.service.IRelationService;

@Service
public class RegisterRelationAction implements IRegisterRelationAction {
	@Autowired
	private IReportService reportService;
	
	@Autowired
	private IRelationService relationService;
		
	public boolean registerRelation(String snSigmaContainer, String snSigmaContent, long date) {
		return relationService.save(snSigmaContainer, snSigmaContent) &&
			reportService.save(date, getType() + snSigmaContainer + " -> " + snSigmaContent);
		
	}

	@Override
	public String getType() {
		return "REGISTER_RELATION";
	}

}
