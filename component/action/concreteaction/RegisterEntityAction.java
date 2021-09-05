package com.sigmaspa.sigmatracking.component.action.concreteaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.component.action.IRegisterEntityAction;
import com.sigmaspa.sigmatracking.service.IEntityService;
import com.sigmaspa.sigmatracking.service.IReportService;

@Service
public class RegisterEntityAction implements IRegisterEntityAction {
	
	@Autowired
	private IEntityService entityService;
	
	@Autowired
	private IReportService reportService;
	
	public boolean registerEntity(String snSigma, String snProducer, String jdeCode, String description, String orderType, Long orderNumber, long dateTime) throws Exception {
		return entityService.save(snSigma, snProducer,jdeCode, description, orderType, orderNumber) &&
				reportService.save(dateTime, getType() + " " + snSigma);
	}

	@Override
	public String getType() {
		return "REGISTER_ENTITY";
	}

}
