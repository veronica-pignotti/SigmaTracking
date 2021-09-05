package com.sigmaspa.sigmatracking.service;

import java.util.List;

import com.sigmaspa.sigmatracking.model.Order;

public interface IOrderService {
	
	public List<Order> getOrders(String type, Long number);
	
}
