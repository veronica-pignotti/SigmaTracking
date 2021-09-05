package com.sigmaspa.sigmatracking.service.concreteservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.sigmaspa.sigmatracking.model.Order;
import com.sigmaspa.sigmatracking.repository.OrderRepository;
import com.sigmaspa.sigmatracking.service.IOrderService;

@Primary
@Service("orderService")
public class OrderService implements IOrderService{
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public List<Order> getOrders(String type, Long number) {
		List<Order> results = orderRepository.findAllByOrderTypeAndOrderNumber(type, number).orElse(new ArrayList<Order>());
		if(results.isEmpty()) return results;
		Map<String, Order> map = new HashMap<String, Order>();
		results.forEach((o)->{
			String jde = o.getJdeCode();
			if(map.containsKey(jde)) 
				map.replace(o.getJdeCode(), new Order(o.getId(), type, number,  jde, o.getDescription(), map.get(jde).getQuantity() + o.getQuantity()));
			else map.put(jde, o);
		});
		
		return List.copyOf(map.values());
	}

}
