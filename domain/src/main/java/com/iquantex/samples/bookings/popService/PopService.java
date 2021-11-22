package com.iquantex.samples.bookings.popService;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author quail
 */
public interface PopService {

	/**
	 * 查询酒店房间的热度
	 */
	Map<String, Integer> queryPop();

}
