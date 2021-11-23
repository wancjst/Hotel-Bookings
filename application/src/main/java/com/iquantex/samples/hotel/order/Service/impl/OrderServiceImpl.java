package com.iquantex.samples.hotel.order.Service.impl;

import com.iquantex.samples.hotel.order.Order;
import com.iquantex.samples.hotel.order.Service.OrderService;
import com.iquantex.samples.hotel.order.listener.HotelBookingsEventListener;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author quail
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Getter
	private Set<Order> orderSet = new HashSet<>();

	@Override
	public Set<Order> queryAll() {
		return orderSet;
	}

	@Override
	public Order query2Order(String orderNum) {
		for (Order order : orderSet) {
			if (order.getOrderNum().equals(orderNum)) {
				return order;
			}
		}
		return null;
	}

	@Override
	public void add(Order order) {
		orderSet.add(order);
	}

	@Override
	public void delete(Order order) {
		orderSet.removeIf(v -> {
			if (v.getOrderNum().equals(order.getOrderNum())) {
				return true;
			}
			return false;
		});
	}

}
