package com.sigmaspa.sigmatracking.component.process;

import com.sigmaspa.sigmatracking.component.object.ResponseMap;

public interface ITestingProcess extends IProcess {
	
	public ResponseMap testing(String snSigmaContainer, String snSigmaContent);

}
