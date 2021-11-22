package com.iquantex.samples.bookings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iquantex.samples.bookings.popService.PopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quail
 */
@RestController
@RequestMapping("pop")
public class PopController {

	@Autowired
	private PopService popService;

	@GetMapping("/query")
	public String queryRestRoom() {
		try {
			return new ObjectMapper().writeValueAsString(popService.queryPop());
		}
		catch (JsonProcessingException e) {
			return "query fail: " + e.getMessage();
		}
	}

}
