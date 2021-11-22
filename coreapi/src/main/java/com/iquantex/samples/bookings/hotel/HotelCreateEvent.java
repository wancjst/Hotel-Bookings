package com.iquantex.samples.bookings.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author quail
 */
@Getter
@AllArgsConstructor
public class HotelCreateEvent implements Serializable {

	private static final long serialVersionUID = -2347396853431433998L;

	private String hotelCode;

	private String restType;

	private String subNumber;

}
