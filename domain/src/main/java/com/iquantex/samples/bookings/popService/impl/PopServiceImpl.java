package com.iquantex.samples.bookings.popService.impl;

import com.iquantex.samples.bookings.listener.PopPublishHandler;
import com.iquantex.samples.bookings.popService.PopService;
import com.iquantex.samples.bookings.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author quail
 */
@Service
public class PopServiceImpl implements PopService {

	@Autowired
	private PopPublishHandler popPublishHandler;

	@Override
	public Map<String, Integer> queryPop() {
		return ConvertUtil.Map2Map(popPublishHandler.getPopList());
	}

}
