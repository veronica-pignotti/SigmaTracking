package com.sigmaspa.sigmatracking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sigmaspa.sigmatracking.model.Order;
@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	public Optional<List<Order>> findAllByOrderTypeAndOrderNumber(String orderType, Long orderNumber); 

}
