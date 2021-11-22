package com.iquantex.samples.bookings.Service.impl;

import com.iquantex.samples.bookings.Service.OrderService;
import com.iquantex.samples.bookings.order.Order;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author quail
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Getter
    private static List<Order> orderList = new ArrayList<>();

    @Override
    public List<Order> queryAll() {
        return orderList;
    }

    @Override
    public Order query2Order(String orderNum) {
        AtomicReference<Order> order = null;
        orderList.forEach(v -> {
            if (v.getOrderNum().equals(orderNum)) {
                order.set(v);
            }
        });
        return order.get();
    }

    @Override
    public boolean cancelOrder(String orderNum) {
        return false;
    }
}
