package com.sigmaspa.sigmatracking.service;

import java.util.List;
import java.util.Map;

import com.sigmaspa.sigmatracking.model.Relation;

public interface IRelationService {
	
	public boolean save(String snSigmaContainer, String snSigmaContent);
	
	public Relation getContainerOf(String snSigma);
	
	public List<Relation> getContentsOf(String snSigma);
	
	public boolean hasContents(String snSigma);
	
	public boolean isContent(String snSigma);
	
	public List<Relation> getAllContentsOf(String snSigma);

	public List<Relation> getTreeOf(String snSigma);
	
	public Map<String, List<String>> getMapOf(String snSigma);
	
	public String getRootOf(String snSigma);
	
	public boolean disable(String snSigmaContainer, String snSigmaContent);
	
	public List<Relation> getByAlias(Relation relation);
	
	public boolean isActive(String snSigma);
	
	public boolean areRelatives(String snSigmaOne, String snSigmaTwo);
		
}