package com.iquantex.samples.bookings.domain;

import com.iquantex.phoenix.core.message.RetCode;
import com.iquantex.phoenix.server.aggregate.ActReturn;
import com.iquantex.phoenix.server.aggregate.CommandHandler;
import com.iquantex.phoenix.server.aggregate.EntityAggregateAnnotation;
import com.iquantex.phoenix.server.aggregate.QueryHandler;
import com.iquantex.samples.bookings.hotel.HotelCreateCmd;
import com.iquantex.samples.bookings.hotel.HotelCreateEvent;
import com.iquantex.samples.bookings.hotel.HotelCreateFailEvent;
import com.iquantex.samples.bookings.hotel.HotelQueryCmd;
import com.iquantex.samples.bookings.hotel.HotelQueryEvent;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author quail
 */
@EntityAggregateAnnotation(aggregateRootType = "HotelAggregate")
public class HotelAggregate implements Serializable {
    private static final long serialVersionUID = -4051924255577694209L;

    /**
     * 剩余房间<type,number>
     * 房间类型:
     * 1. 大床房
     * 2. 标准间
     * 3. 情侣套房
     * 4. 总统套房
     */
    private Map<Integer, Integer> restRoom = new HashMap<>();

    /**
     * 假定各类房间剩余10间
     */
    public HotelAggregate(){
        restRoom.put(1, 10);
        restRoom.put(2, 10);
        restRoom.put(3, 10);
        restRoom.put(4, 10);
    }

    /**
     * 查询剩余房间信息
     *
     * @param cmd
     * @return
     */
    @QueryHandler(aggregateRootId = "hotelCode")
    public ActReturn act(HotelQueryCmd cmd){
        return ActReturn.builder().retCode(RetCode.SUCCESS).event(new HotelQueryEvent(cmd.getHotelCode(),restRoom)).build();
    }

    /**
     * 预约流程
     *
     * @param cmd
     * @return
     */
    @CommandHandler(aggregateRootId = "hotelCode")
    public ActReturn act(HotelCreateCmd cmd){
        if (restRoom.get(cmd.getRestType()) > 0) {
            return ActReturn.builder().retCode(RetCode.SUCCESS).event(new HotelCreateEvent(cmd.getHotelCode(),cmd.getRestType(),cmd.getSubNumber())).build();
        }
        return ActReturn.builder().retCode(RetCode.FAIL).event(new HotelCreateFailEvent("There is no room left")).build();
    }

    public void on(HotelCreateEvent event){
        this.restRoom.put(event.getRestType(), restRoom.get(event.getRestType()) - 1);
    }

    public void on(HotelCreateFailEvent event){}

}
