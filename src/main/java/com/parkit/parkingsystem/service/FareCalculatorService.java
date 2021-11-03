package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		double inHour = ticket.getInTime().getTime() / (1000 * 60); // Convert milliseconds into minutes
		double outHour = ticket.getOutTime().getTime() / (1000 * 60); // Convert milliseconds into minutes

		double duration = (outHour - inHour) / 60; // Convert minutes into hours

		if (duration <= Fare.THIRTY_MINUTES_IN_HOURS) { // No parking fees when the duration is less than 30 minutes
			ticket.setPrice(0);
		} else {
			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				if (!ticket.isRecurringUser()) {
					ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
				} else {
					ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_REDUCTION_COEFFICIENT); // 5%
																													// discount
																													// for
																													// recurring
																													// user
				}
				break;
			}
			case BIKE: {
				if (!ticket.isRecurringUser()) {
					ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
				} else {
					ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_REDUCTION_COEFFICIENT); // 5%
																													// discount
																													// for
																													// recurring
																													// user
				}
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
		}

	}
}