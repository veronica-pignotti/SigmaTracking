package com.sigmaspa.sigmatracking.service.concreteservice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.model.Entity;
import com.sigmaspa.sigmatracking.model.Order;
import com.sigmaspa.sigmatracking.repository.EntityRepository;
import com.sigmaspa.sigmatracking.service.IEventService;
import com.sigmaspa.sigmatracking.service.IEntityService;

@Primary
@Service("entityService")
public class EntityService implements IEntityService {

	@Autowired
	private EntityRepository entityRepository;
	
	@Autowired 
	private IEventService eventService;

	@Override
	public boolean save(String snSigma, String snProducer, String jdeCode, String description, String orderType, Long orderNumber) throws Exception{
		return entityRepository.save(
				check(
					new Entity(snSigma, snProducer,jdeCode, description, orderType, orderNumber, false)
				)
		)!=null;
	} 

	@Override
	public List<Entity> getByAlias(Entity product) {
		return entityRepository.findAll(Example.of(product, ExampleMatcher.matchingAny()));
	}
	
	@Override
	public List<Entity> getBySnSigmas(List<String> snSigmas){
		return snSigmas.isEmpty()?
			new ArrayList<Entity>():
			entityRepository.findAllBySnSigmaIn(snSigmas)
			.orElse(new ArrayList<Entity>());
	}

	@Override
	public Entity getBySnSigma(String snSigma) {
		return entityRepository.findAllBySnSigmaOrderBySnSigmaAsc(snSigma)
		 .orElse(null);
	}
	
	@Override
	public List<Entity> getByJdeCode(String jdeCode) {
		return entityRepository.findAllByJdeCodeOrderBySnSigmaAsc(jdeCode)
			.orElse(new ArrayList<Entity>());
	}

	@Override
	public List<Entity> getAllChangedToday() {
		return getBySnSigmas(
				eventService.getByDate(System.currentTimeMillis())
					.stream().map((a)->a.getSnSigma()).collect(Collectors.toList())
		);
	}

	@Override
	public boolean containsSnSigma(String snSigma) {
		return entityRepository.existsById(snSigma);
	}

	@Override
	public boolean isScrapped(String snSigma) {
		return getBySnSigma(snSigma).getScrapped();
	}

	@Override
	public boolean scrap(String snSigma) {
		Entity entity = getBySnSigma(snSigma);
		entityRepository.delete(entity);
		entity.setScrapped(true);
		entityRepository.save(entity);	
		return true;
	}

	@Override
	public List<Entity> getByOrder(Order order) {
		return entityRepository.findAllByOrderTypeAndOrderNumberAndJdeCode(order.getOrderType(), order.getOrderNumber(), order.getJdeCode())
			.orElse(new ArrayList<Entity>());
	}

	@Override
	public Entity getByJdeCodeAndSnProducer(String jdeCode, String snProducer) {
		return entityRepository.findByJdeCodeAndSnProducer(jdeCode, snProducer)
			.orElse(null);
	}

	@Override
	public boolean containsJdeCodeAndSnProducer(String jdeCode, String snProducer) {
		return getByJdeCodeAndSnProducer(jdeCode, snProducer) != null;
	}
	
	protected Entity check(Entity entity) throws Exception{
		if(containsSnSigma(entity.getSnSigma())) 
			throw new Exception("Il S/N Sigma " + entity.getSnSigma() + " è stato già registrato");
		if(containsJdeCodeAndSnProducer(entity.getJdeCode(), entity.getSnProducer()))
			throw new Exception("L'entità con codice JDE = " + entity.getJdeCode() + " e S/N fornitore = " + entity.getSnProducer() + " è stata già registrata");
		return entity;
	}

}
