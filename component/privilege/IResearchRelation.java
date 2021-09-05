package com.sigmaspa.sigmatracking.component.privilege;

import java.util.List;
import java.util.Map;

public interface IResearchRelation extends IPrivilege{
	
	public Map<String, List<String>> get(String snSigma);

}
