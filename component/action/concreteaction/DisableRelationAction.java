package com.sigmaspa.sigmatracking.component.action.concreteaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.service.IReportService;
import com.sigmaspa.sigmatracking.component.action.IDisableRelationAction;
import com.sigmaspa.sigmatracking.service.IRelationService;

@Primary
@Service
public class DisableRelationAction implements IDisableRelationAction{
	@Autowired
	private IReportService reportService;
	
	@Autowired
	private IRelationService relationService;
	
	public boolean disableRelation(String snSigmaContainer, String snSigmaContent, long date) {
		return relationService.disable(snSigmaContainer, snSigmaContent) &&
				reportService.save(date, getType() + " " + snSigmaContainer + " -> " + snSigmaContent);	
		}
	
	@Override
	public String getType() {
		return "DISABLE_RELATION";
	}

}
