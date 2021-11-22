package com.iquantex.samples.bookings.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author quail
 */
public class ConvertUtil {

	public static Map<String, Integer> Map2Map(Map<Integer, Integer> map) {
		Map<String, Integer> linkedHashMap = new LinkedHashMap<>();
		map.forEach((key, value) -> {
			if (key == 1) {
				linkedHashMap.put("大床房", value);
			}
			else if (key == 2) {
				linkedHashMap.put("标准间", value);
			}
			else if (key == 3) {
				linkedHashMap.put("情侣套房", value);
			}
			else if (key == 4) {
				linkedHashMap.put("总统套房", value);
			}
		});
		return linkedHashMap;
	}

}
