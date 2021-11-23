package com.iquantex.samples.hotel.bookings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iquantex.phoenix.client.PhoenixClient;
import com.iquantex.phoenix.client.RpcResult;
import com.iquantex.samples.hotel.utils.ConvertUtil;
import com.iquantex.samples.hotel.hotel.HotelCancelCmd;

import com.iquantex.samples.hotel.hotel.HotelCancelEvent;
import com.iquantex.samples.hotel.hotel.HotelCancelFailEvent;
import com.iquantex.samples.hotel.hotel.HotelCreateCmd;
import com.iquantex.samples.hotel.hotel.HotelCreateEvent;
import com.iquantex.samples.hotel.hotel.HotelCreateFailEvent;
import com.iquantex.samples.hotel.hotel.HotelQueryCmd;
import com.iquantex.samples.hotel.hotel.HotelQueryEvent;
import com.iquantex.samples.hotel.bookings.listener.PopPublishHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("hotel")
public class HotelController {

	@Autowired
	private PhoenixClient client;

	@Autowired
	private PopPublishHandler popPublishHandler;

	@PutMapping("/bookings/{hotelCode}/{roomType}")
	public String bookings(@PathVariable String hotelCode, @PathVariable String roomType) {
		// 生成预约号: roomType@UUID
		String subNumber = roomType + "@" + UUID.randomUUID().toString();
		HotelCreateCmd cmd = new HotelCreateCmd(hotelCode, roomType, subNumber);
		Future<RpcResult> future = client.send(cmd, "hotel-bookings", UUID.randomUUID().toString());
		try {
			Object data = future.get(10, TimeUnit.SECONDS).getData();
			if (data instanceof HotelCreateEvent) {
				return ((HotelCreateEvent) data).getSubNumber();
			}
			return ((HotelCreateFailEvent) data).getMsg();
		}
		catch (InterruptedException | ExecutionException | TimeoutException e) {
			return "rpc error: " + e.getMessage();
		}
	}

	@GetMapping("/query/{hotelCode}")
	public String queryRestRoom(@PathVariable String hotelCode) {
		HotelQueryCmd hotelQueryCmd = new HotelQueryCmd(hotelCode);
		Future<RpcResult> future = client.send(hotelQueryCmd, "hotel-bookings", UUID.randomUUID().toString());
		try {
			HotelQueryEvent event = (HotelQueryEvent) future.get(10, TimeUnit.SECONDS).getData();
			return new ObjectMapper().writeValueAsString(ConvertUtil.Map2Map(event.getRestRoom()));
		}
		catch (InterruptedException | ExecutionException | TimeoutException | JsonProcessingException e) {
			return "rpc error: " + e.getMessage();
		}
	}

	@GetMapping("/queryPop")
	public String queryRestRoom() {
		try {
			return new ObjectMapper().writeValueAsString(ConvertUtil.Map2Map(popPublishHandler.getPopList()));
		}
		catch (JsonProcessingException e) {
			return "query fail: " + e.getMessage();
		}
	}

	@PutMapping("/cancel/{hotelCode}/{subNumber}")
	public String cancel(@PathVariable String hotelCode, @PathVariable String subNumber) {
		HotelCancelCmd hotelCancelCmd = new HotelCancelCmd(hotelCode, subNumber);
		Future<RpcResult> future = client.send(hotelCancelCmd, "hotel-bookings", UUID.randomUUID().toString());
		try {
			Object data = future.get(10, TimeUnit.SECONDS).getData();
			if (data instanceof HotelCancelEvent) {
				return "cancel ok";
			}
			return ((HotelCancelFailEvent) data).getMsg();
		}
		catch (InterruptedException | ExecutionException | TimeoutException e) {
			return "rpc error: " + e.getMessage();
		}
	}

}
