package com.sigmaspa.sigmatracking.service.concreteservice;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.model.Relation;
import com.sigmaspa.sigmatracking.repository.RelationRepository;
import com.sigmaspa.sigmatracking.service.IRelationService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Primary
@Service("relationService")
public class RelationService implements IRelationService{

	@Autowired
	private RelationRepository repository;
	

	@Override
	public boolean save(String snSigmaContainer, String snSigmaContent) {
		Relation relation = new Relation(snSigmaContainer, snSigmaContent); 
		log.info("Salvataggio della relazione: {}", relation);
		return repository.exists(Example.of(relation)) || repository.save(relation)!=null;
	}

	@Override
	public Relation getContainerOf(String snSigma) {
		return repository.findBySnSigmaContentAndActive(snSigma, true).orElse(null);
	}

	@Override
	public List<Relation> getContentsOf(String snSigma){
		if(snSigma == "") return new ArrayList<Relation>();
		return  repository.findAllBySnSigmaContainerAndActive(snSigma, true)
				.orElse(new ArrayList<Relation>());
	}

	@Override
	public boolean hasContents(String snSigma) {
		return !getContentsOf(snSigma).isEmpty();
	}

	@Override
	public boolean isContent(String snSigma) {
		return getContainerOf(snSigma)!= null;
	}

	@Override
	public List<Relation> getAllContentsOf(String snSigma) {
		List<Relation> map = new ArrayList<Relation>();
		List<String> elements = new ArrayList<String>();
		elements.add(snSigma);
		return recursiveGetAllActiveContentsOf(0, elements, map);
	}
	
	private List<Relation> recursiveGetAllActiveContentsOf(int index, List<String> elements, List<Relation> list){
		String father = elements.get(index);
		List<Relation> sons = getContentsOf(father);
		if(!sons.isEmpty()) {
			list.addAll(sons);
			elements.addAll(sons.stream().map(r->r.getSnSigmaContent()).collect(Collectors.toList()));
		}
		index +=1;
		return index == elements.size()? list 
			: recursiveGetAllActiveContentsOf(index, elements, list);
	}

	public String getRootOf(String snSigma) {
		Relation container;
		do{
			container = getContainerOf(snSigma);
			if(container != null) snSigma = container.getSnSigmaContainer();
		}while(container!=null);
		return snSigma;
	}
	
	@Override
	public List<Relation> getTreeOf(String snSigma) {	
		return getAllContentsOf(getRootOf(snSigma));
	}

	@Override
	public Map<String, List<String>> getMapOf(String snSigma) {
		
		log.info("Creazione mappa a partire da {}", snSigma);
		List<String> elements = new ArrayList<String>();
		elements.add(getRootOf(snSigma));
		
		return recursiveGetMap(0, elements, new LinkedHashMap<String, List<String>>());
		
	}
	
	private Map<String, List<String>> recursiveGetMap(int index, List<String> elements, Map<String, List<String>> map){
		String father = elements.get(index);
		List<String> sons = getContentsOf(father).stream().map((c)->c.getSnSigmaContent()).collect(Collectors.toList());
		map.put(father, sons);
		if(!sons.isEmpty()) {
			elements.addAll(sons);
		}
		index +=1;
		return index == elements.size()? map 
			: recursiveGetMap(index, elements, map);
	}

	@Override
	public boolean disable(String snSigmaContainer, String snSigmaContent) {
		Relation relation = repository.findBySnSigmaContentAndActive(snSigmaContent, true).get();
		log.info("Disabilitazione relazione {}", relation);
		repository.delete(relation);
		relation.setActive(false);
		
		return repository.save(relation)!= null;
	}
	
	@Override
	public List<Relation> getByAlias(Relation relation) {
		return repository.findAll(Example.of(relation, ExampleMatcher.matchingAny()));
	}
	
	@Override
	public boolean isActive(String snSigma) {
		return isContent(snSigma) || hasContents(snSigma);
	}
	
	@Override
	public boolean areRelatives(String snSigmaOne, String snSigmaTwo) {
		
		return getRootOf(snSigmaOne).equals(getRootOf(snSigmaTwo));
	}	
}