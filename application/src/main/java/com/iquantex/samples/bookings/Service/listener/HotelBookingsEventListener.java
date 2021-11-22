package com.iquantex.samples.bookings.Service.listener;

import com.iquantex.phoenix.core.message.Message;
import com.iquantex.phoenix.eventpublish.core.EventDeserializer;
import com.iquantex.phoenix.eventpublish.deserializer.DefaultMessageDeserializer;
import com.iquantex.samples.bookings.Service.OrderService;
import com.iquantex.samples.bookings.Service.impl.OrderServiceImpl;
import com.iquantex.samples.bookings.hotel.HotelCreateEvent;
import com.iquantex.samples.bookings.order.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.weaver.ast.Or;
import org.omg.PortableInterceptor.ObjectReferenceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author quail
 */
@Component
public class HotelBookingsEventListener implements BatchAcknowledgingMessageListener<String, byte[]> {

    // phoenix反序列化工具
    private final EventDeserializer<byte[], Message> deserializer = new DefaultMessageDeserializer();

    @Override
    @KafkaListener(topics = "${quantex.phoenix.event-publish.event-task.topic}")
    public void onMessage(List<ConsumerRecord<String, byte[]>> data, Acknowledgment acknowledgment) {
        // 处理事件
        for (ConsumerRecord<String, byte[]> record : data) {
            Message message = deserializer.deserialize(record.value());
            if (message.getPayload() instanceof HotelCreateEvent) {
                HotelCreateEvent event = message.getPayload();
                OrderServiceImpl.getOrderList().add(new Order(event.getSubNumber(), event.getHotelCode(), event.getRestType()));
            }
        }

        // 提交消费位点
        acknowledgment.acknowledge();
    }
}
