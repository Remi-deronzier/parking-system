package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

	private static ParkingService parkingService;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	private static String vehicleRegNumber = "ABCDEF";

	@BeforeEach
	private void setUpPerTest() {
		try {
			lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
			lenient().when(inputReaderUtil.readSelection()).thenReturn(1);

			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			Ticket ticket = new Ticket();
			ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
			ticket.setParkingSpot(parkingSpot);
			ticket.setVehicleRegNumber(vehicleRegNumber);
			lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
			lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
			lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
			lenient().when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	@Test
	public void processIncomingVehicleTest() {
		parkingService.processIncomingVehicle();
		verify(ticketDAO, Mockito.times(1)).getRecurringUser(vehicleRegNumber);
	}

	@Test
	public void processExitingVehicleTest() {
		parkingService.processExitingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
	}

	@Test
	public void checkNonRecurringUserTest() {
		// GIVEN
		Ticket ticketNonRecurringUser = null;

		// WHEN
		when(ticketDAO.getRecurringUser(vehicleRegNumber)).thenReturn(ticketNonRecurringUser);
		boolean isRecurringUser = parkingService.checkRecurringUser(vehicleRegNumber);

		// THEN
		verify(ticketDAO, Mockito.times(1)).getRecurringUser(vehicleRegNumber);
		assertEquals(isRecurringUser, false);
	}

	@Test
	public void checkRecurringUserTest() {
		// GIVEN
		Ticket ticketRecurringUser = new Ticket();
		ticketRecurringUser.setVehicleRegNumber(vehicleRegNumber);

		// WHEN
		when(ticketDAO.getRecurringUser(any(String.class))).thenReturn(ticketRecurringUser);
		boolean isRecurringUser = parkingService.checkRecurringUser(vehicleRegNumber);

		// THEN
		verify(ticketDAO, Mockito.times(1)).getRecurringUser(vehicleRegNumber);
		assertEquals(isRecurringUser, true);
	}

}
