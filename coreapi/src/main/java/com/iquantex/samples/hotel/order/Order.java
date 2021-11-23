package com.iquantex.samples.hotel.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author quail
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	private String orderNum;

	private String hotelCode;

	private String roomType;

}
