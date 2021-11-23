package com.iquantex.samples.hotel.bookings.domain;

import com.iquantex.phoenix.core.message.RetCode;
import com.iquantex.phoenix.server.aggregate.ActReturn;
import com.iquantex.phoenix.server.aggregate.CommandHandler;
import com.iquantex.phoenix.server.aggregate.EntityAggregateAnnotation;
import com.iquantex.samples.hotel.hotel.HotelCancelCmd;
import com.iquantex.samples.hotel.hotel.HotelCancelEvent;
import com.iquantex.samples.hotel.hotel.HotelCancelFailEvent;
import com.iquantex.samples.hotel.hotel.HotelCreateCmd;
import com.iquantex.samples.hotel.hotel.HotelCreateEvent;
import com.iquantex.samples.hotel.hotel.HotelCreateFailEvent;
import com.iquantex.samples.hotel.hotel.HotelQueryCmd;
import com.iquantex.samples.hotel.hotel.HotelQueryEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author quail
 */
@EntityAggregateAnnotation(aggregateRootType = "HotelAggregate")
public class HotelAggregate implements Serializable {

	private static final long serialVersionUID = -4051924255577694209L;

	/**
	 * 已被预订的房间的预定号
	 */
	private List<String> bookedRoom = new ArrayList<>();

	/**
	 * 剩余房间<type,number> 房间类型: 1. 大床房 2. 标准间 3. 情侣套房 4. 总统套房
	 */
	private Map<String, Integer> restRoom = new HashMap<>();

	/**
	 * 假定各类房间剩余10间
	 */
	public HotelAggregate() {
		restRoom.put("1", 10);
		restRoom.put("2", 10);
		restRoom.put("3", 10);
		restRoom.put("4", 10);
	}

	/**
	 * 查询剩余房间信息
	 * @param cmd
	 * @return
	 */
	@CommandHandler(aggregateRootId = "hotelCode")
	public ActReturn act(HotelQueryCmd cmd) {
		return ActReturn.builder().retCode(RetCode.SUCCESS).event(new HotelQueryEvent(cmd.getHotelCode(), restRoom))
				.build();
	}

	public void on(HotelQueryEvent event) {
	}

	/**
	 * 预约流程
	 * @param cmd
	 * @return
	 */
	@CommandHandler(aggregateRootId = "hotelCode")
	public ActReturn act(HotelCreateCmd cmd) {
		if (restRoom.get(cmd.getRestType()) > 0) {
			return ActReturn.builder().retCode(RetCode.SUCCESS)
					.event(new HotelCreateEvent(cmd.getHotelCode(), cmd.getRestType(), cmd.getSubNumber())).build();
		}
		return ActReturn.builder().retCode(RetCode.FAIL).event(new HotelCreateFailEvent("There is no room left"))
				.build();
	}

	public void on(HotelCreateEvent event) {
		this.bookedRoom.add(event.getSubNumber());
		this.restRoom.put(event.getRestType(), restRoom.get(event.getRestType()) - 1);
	}

	public void on(HotelCreateFailEvent event) {
	}

	/**
	 * 取消预约
	 * @param cmd
	 * @return
	 */
	@CommandHandler(aggregateRootId = "hotelCode")
	public ActReturn act(HotelCancelCmd cmd) {
		if (!bookedRoom.contains(cmd.getSubNumber())) {
			return ActReturn.builder().retCode(RetCode.FAIL)
					.event(new HotelCancelFailEvent("Please check your order number")).build();
		}
		return ActReturn.builder().retCode(RetCode.SUCCESS)
				.event(new HotelCancelEvent(cmd.getHotelCode(), cmd.getSubNumber())).build();
	}

	public void on(HotelCancelEvent event) {
		bookedRoom.removeIf(v -> v.contains(event.getSubNumber()));
		String s = event.getSubNumber().split("@")[0];
		if (restRoom.containsKey(s)) {
			restRoom.put(s, restRoom.get(s) + 1);
		}
	}

}
