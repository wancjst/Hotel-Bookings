package com.iquantex.samples.bookings.Service.impl;

import com.iquantex.samples.bookings.Service.OrderService;
import com.iquantex.samples.bookings.order.Order;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author quail
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Getter
	private static Set<Order> orderSet = new HashSet<>();

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
	public boolean cancelOrder(String orderNum) {
		return false;
	}

}
