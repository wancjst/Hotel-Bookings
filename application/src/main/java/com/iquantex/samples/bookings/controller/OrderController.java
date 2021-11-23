package com.iquantex.samples.bookings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iquantex.phoenix.client.RpcResult;
import com.iquantex.samples.bookings.Service.OrderService;
import com.iquantex.samples.bookings.hotel.HotelQueryCmd;
import com.iquantex.samples.bookings.hotel.HotelQueryEvent;
import com.iquantex.samples.bookings.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author quail
 */
@RestController
@RequestMapping("order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping("/queryAll")
	public String queryAll() {
		try {
			return objectMapper.writeValueAsString(orderService.queryAll());
		}
		catch (JsonProcessingException e) {
			return "order query fail";
		}
	}

	@GetMapping("/query/{orderCode}")
	public String queryRestRoom(@PathVariable String orderCode) {
		try {
			if (orderService.query2Order(orderCode) != null) {
				return objectMapper.writeValueAsString(orderService.query2Order(orderCode));
			}
			return "Please check your order number";
		}
		catch (JsonProcessingException e) {
			return "order query fail";
		}
	}

}
