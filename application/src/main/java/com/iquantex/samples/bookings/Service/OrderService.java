package com.iquantex.samples.bookings.Service;

import com.iquantex.samples.bookings.order.Order;

import java.util.List;
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
	 * 取消订单
	 */
	boolean cancelOrder(String orderNum);

}
