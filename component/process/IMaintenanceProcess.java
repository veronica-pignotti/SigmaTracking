package com.sigmaspa.sigmatracking.component.process;

import com.sigmaspa.sigmatracking.component.object.ResponseMap;

public interface IMaintenanceProcess extends IProcess {

	public ResponseMap replacement(String container, String oldContent, String newContent);

}
