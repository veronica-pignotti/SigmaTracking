package com.sigmaspa.sigmatracking.component.process.concreteprocess;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.sigmaspa.sigmatracking.component.action.IRegisterEntityAction;
import com.sigmaspa.sigmatracking.component.object.ResponseMap;
import com.sigmaspa.sigmatracking.component.process.IReceptionProcess;
import com.sigmaspa.sigmatracking.model.Entity;
import com.sigmaspa.sigmatracking.model.Order;
import com.sigmaspa.sigmatracking.service.IEntityService;
import com.sigmaspa.sigmatracking.service.IOrderService;
import com.sigmaspa.sigmatracking.service.IEventService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReceptionProcess implements IReceptionProcess{
	
	@Autowired
	private IEntityService entityService;
	
	@Autowired
	private IRegisterEntityAction registerEntityAction;
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IEventService organizationReportService;
	
	@Override
	public List<Order> getOrders(String orderType, long orderNumber) throws Exception{
		List<Order> orders = orderService.getOrders(orderType, orderNumber);
		if(orders.isEmpty()) throw new Exception("Ordine non trovato");
		orders = orders.stream().map(o->{ // sottraggo le quantità già registrate
			o.setQuantity(o.getQuantity() - entityService.getByOrder(o).size());
			return o;
		}).filter( // filtro gli ordini che presentano entità da registrare
				o->o.getQuantity()>0).collect(Collectors.toList()
		);
		
		if(orders.isEmpty()) throw new Exception("Ordine completato");
		orders.sort((a,b)-> a.getQuantity()>=b.getQuantity()?1:-1);
		return orders;
		
	}
	
	@Override
	public List<Entity> getEntitiesFromOrder(Order order) {
		log.info("reception process get entities from order = {}", order);
		List<Entity> list = new ArrayList<Entity>();
		List<Entity> exist = entityService.getByJdeCode(order.getJdeCode());
		exist.sort((a,b)-> {
			return a.getSnSigma().compareTo(b.getSnSigma());
		});
		String lastSnSigma = exist.isEmpty()?"":exist.get(exist.size()-1).getSnSigma();
		for(long i=1; i<=order.getQuantity(); i++) {
			lastSnSigma = generateSnSigma(order.getJdeCode(), lastSnSigma);
			list.add(new Entity(lastSnSigma, "", order.getJdeCode(), order.getDescription(), order.getOrderType(), order.getOrderNumber(), false));
		} 
		return list;
	}	
	
	@Override
	public ResponseMap register(String processType, String snSigma, String snProducer, String jdeCode, String description, String orderType,
			Long orderNumber) {
		long dateTime = System.currentTimeMillis();
		ResponseMap response;
		boolean ok;
		try {
			ok = registerEntityAction.registerEntity(snSigma, snProducer, jdeCode, description, orderType, orderNumber, dateTime) &&
			organizationReportService.save(dateTime, snSigma, processType);
			
			response = new ResponseMap(
				ok?
					"Registrazione dell'entità con S/N Sigma " + snSigma + " confermata":
					"Registrazione dell'entità con S/N Sigma " + snSigma + " non confermata",
					ok
			);
		} catch (Exception e) {
			
			response = new ResponseMap(
				e.getMessage(),
				false
			);
			
			
		}
		log.info(response.getMessage());
		return response;
	}
	
	@Override
	public String generateSnSigma(String jdeCode, String lastSnSigma) {
		return lastSnSigma == ""?
				jdeCode +"_00000000":
				incr(lastSnSigma.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
	}
		
	
	protected String incr(List<Character> characters) {
		char last = characters.get(characters.size()-1);
		if(last == 57) characters.set(characters.size()-1, 'a');//passaggio dal 9 alla a
		else if(last == 122) return incr(characters.subList(0, characters.size()-1)).concat("0");
		else characters.set(characters.size()-1, (char)(last+1));
		return StringUtils.join(characters, null);
	}
	
	
	/**
	 * Genera l'immagine del barcode di tipo Aztec corrispondente alla stringa @param text passata.
	 * @param text
	 * @return
	 */
	@Override
	public byte[] getAztecCodeImage(String snSigma) {
		BitMatrix bitMatrix;
		try {
			bitMatrix = new AztecWriter().encode(snSigma, BarcodeFormat.AZTEC, 100, 100);
			ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
			return pngOutputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	@Override
	public String getId() {
		return "Accettazione";
	}

}
