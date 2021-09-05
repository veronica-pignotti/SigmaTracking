package com.sigmaspa.sigmatracking.component.process;

import java.util.List;

import com.sigmaspa.sigmatracking.component.object.ResponseMap;
import com.sigmaspa.sigmatracking.model.Entity;
import com.sigmaspa.sigmatracking.model.Order;

public interface IReceptionProcess extends IProcess {
	
	public List<Order> getOrders(String orderType, long orderNumber) throws Exception;
			
	public ResponseMap register(String processType, String snSigma, String snProducer, String jdeCode, String description, String orderType,
			Long orderNumber);

	public List<Entity> getEntitiesFromOrder(Order order);

	public String generateSnSigma(String jdeCode, String lastSnSigma);

	/**
	 * Genera l'immagine del barcode di tipo Aztec corrispondente alla stringa @param text passata.
	 * @param text
	 * @return
	 */
	byte[] getAztecCodeImage(String snSigma);

}
