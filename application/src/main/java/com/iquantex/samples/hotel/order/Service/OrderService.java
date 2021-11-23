package com.iquantex.samples.hotel.order.Service;

import com.iquantex.samples.hotel.hotel.HotelCreateEvent;
import com.iquantex.samples.hotel.order.Order;

import java.util.Set;

/**
 * @author quail
 */
public interface OrderService {

	/**
	 * 查看全部订单
	 */
	Set<Order> queryAll();

	/**
	 * 查找订单详情
	 */
	Order query2Order(String orderNum);

	/**
	 * 添加订单
	 */
	void add(Order order);

	/**
	 * 删除订单
	 */
	void delete(Order order);

}
