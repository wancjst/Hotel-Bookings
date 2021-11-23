package com.iquantex.samples.bookings.Service.listener;

import com.iquantex.phoenix.core.message.Message;
import com.iquantex.phoenix.eventpublish.core.EventDeserializer;
import com.iquantex.phoenix.eventpublish.deserializer.DefaultMessageDeserializer;
import com.iquantex.samples.bookings.Service.impl.OrderServiceImpl;
import com.iquantex.samples.bookings.hotel.HotelCreateEvent;
import com.iquantex.samples.bookings.order.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author quail
 */
@Slf4j
public class HotelBookingsEventListener {

	// 使用提供的默认反序列化器，反序列化MQ中的字节数组，得到以Message封装的领域事件
	private EventDeserializer<byte[], Message> deserializer = new DefaultMessageDeserializer();

	// 使用注解标注事件处理的方法，在注解中声明订阅消息的topic和该listener的goupId
	@KafkaListener(topics = "order-event-pub", groupId = "order-event-sub")
	public void receive(byte[] eventBytes) {
		try {
			Message message = deserializer.deserialize(eventBytes);
			if (message.getPayload() instanceof HotelCreateEvent) {
				HotelCreateEvent event = message.getPayload();
				OrderServiceImpl.getOrderSet()
						.add(new Order(event.getSubNumber(), event.getHotelCode(), event.getRestType()));
			}
		}
		catch (Exception e) {
			log.error("handle event error", e);
		}
	}

}
