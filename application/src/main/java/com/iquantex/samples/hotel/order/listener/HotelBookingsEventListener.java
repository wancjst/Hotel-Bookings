package com.iquantex.samples.hotel.order.listener;

import com.iquantex.phoenix.core.message.Message;
import com.iquantex.phoenix.eventpublish.core.EventDeserializer;
import com.iquantex.phoenix.eventpublish.deserializer.DefaultMessageDeserializer;

import com.iquantex.samples.hotel.hotel.HotelCancelEvent;
import com.iquantex.samples.hotel.hotel.HotelCreateEvent;
import com.iquantex.samples.hotel.order.Order;
import com.iquantex.samples.hotel.order.Service.OrderService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.HashSet;
import java.util.Set;

/**
 * @author quail
 */
@Slf4j
public class HotelBookingsEventListener {

	@Autowired
	private OrderService orderService;

	// 使用提供的默认反序列化器，反序列化MQ中的字节数组，得到以Message封装的领域事件
	private EventDeserializer<byte[], Message> deserializer = new DefaultMessageDeserializer();

	// 使用注解标注事件处理的方法，在注解中声明订阅消息的topic和该listener的goupId
	@KafkaListener(topics = "order-event-pub", groupId = "order-event-sub")
	public void receive(byte[] eventBytes) {
		try {
			Message message = deserializer.deserialize(eventBytes);
			Object payload = message.getPayload();
			if (payload instanceof HotelCreateEvent) {
				HotelCreateEvent event = (HotelCreateEvent) payload;
				orderService.add(new Order(event.getSubNumber(), event.getHotelCode(), event.getRestType()));
			}
			else if (payload instanceof HotelCancelEvent) {
				HotelCancelEvent event = (HotelCancelEvent) payload;
				orderService.delete(
						new Order(event.getSubNumber(), event.getHotelCode(), event.getSubNumber().split("@")[0]));
			}
		}
		catch (Exception e) {
			log.error("handle event error", e);
		}
	}

}
