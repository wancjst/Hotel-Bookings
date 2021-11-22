package com.iquantex.samples.bookings.listener;

import com.iquantex.phoenix.core.message.Message;
import com.iquantex.phoenix.core.message.protobuf.Phoenix;
import com.iquantex.phoenix.eventpublish.core.CommittableEventBatchWrapper;
import com.iquantex.phoenix.eventpublish.core.EventDeserializer;
import com.iquantex.phoenix.eventpublish.core.EventHandler;
import com.iquantex.phoenix.eventpublish.deserializer.DefaultMessageDeserializer;
import com.iquantex.phoenix.server.eventstore.EventStoreRecord;
import com.iquantex.samples.bookings.hotel.HotelCreateEvent;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author quail
 */
@Component
public class PopPublishHandler implements EventHandler<Phoenix.Message, Phoenix.Message> {

	/** 流行度排行内容 */
	@Getter
	private Map<Integer, Integer> popList = new HashMap<>();

	/** 使用提供的默认反序列化器，反序列化MQ中的字节数组，得到以Message封装的领域事件 */
	private EventDeserializer<byte[], Message> deserializer = new DefaultMessageDeserializer();

	@Override
	public String getInfo() {
		return null;
	}

	@Override
	public CommittableEventBatchWrapper handleBatch(CommittableEventBatchWrapper batchWrapper) {
		List<EventStoreRecord<Phoenix.Message>> events = batchWrapper.getEvents();
		Iterator<EventStoreRecord<Phoenix.Message>> iterator = events.iterator();
		while (iterator.hasNext()) {
			Message message = deserializer.deserialize(iterator.next().getContent().toByteArray());
			if (message.getPayload() instanceof HotelCreateEvent) {
				int restType = ((HotelCreateEvent) message.getPayload()).getRestType();
				if (popList.containsKey(restType)) {
					popList.put(restType, popList.get(restType) + 1);
				}
				else {
					popList.put(restType, 1);
				}
				this.popList = sortMap(popList);
			}
		}
		return batchWrapper;
	}

	@Override
	public int getOrder() {
		return 0;
	}

	private static Map sortMap(Map oldMap) {
		ArrayList<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(oldMap.entrySet());
		list.sort(Comparator.comparingInt(Map.Entry::getValue));
		Map newMap = new LinkedHashMap();
		for (int i = list.size() - 1; i >= 0; i--) {
			newMap.put(list.get(i).getKey(), list.get(i).getValue());
		}
		return newMap;
	}

}
