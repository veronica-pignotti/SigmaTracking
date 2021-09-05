package com.sigmaspa.sigmatracking.service;

import java.util.List;

import com.sigmaspa.sigmatracking.model.Entity;
import com.sigmaspa.sigmatracking.model.Order;

public interface IEntityService {
	
	boolean save(String snSigma, String snProducer, String jdeCode, String description, String orderType,
			Long orderNumber) throws Exception;
	
	public List<Entity> getByAlias(Entity product);
	
	public Entity getBySnSigma(String snSigma);
	
	public List<Entity> getBySnSigmas(List<String> snSigmas);

	public List<Entity> getAllChangedToday();
	
	public List<Entity> getByOrder(Order order);

	public List<Entity> getByJdeCode(String jdeCode);
	
	public Entity getByJdeCodeAndSnProducer(String jdeCode, String snProducer);
	
	public boolean containsJdeCodeAndSnProducer(String jdeCode, String snProducer);
	
	public boolean containsSnSigma(String snSigma);

	public boolean isScrapped(String snSigma);

	public boolean scrap(String snSigma);

}
