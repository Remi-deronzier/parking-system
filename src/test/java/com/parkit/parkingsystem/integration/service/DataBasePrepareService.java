package com.parkit.parkingsystem.integration.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class DataBasePrepareService {

	private static final Logger logger = LogManager.getLogger("TicketDAO");

	DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
	private String vehicleRegNumber = "ABCDEF";
	private boolean isUserARecurringUser = false;

	public void clearDataBaseEntries() {
		Connection connection = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			// set parking entries to available
			connection.prepareStatement("update parking set available = true").execute();

			// clear ticket entries;
			connection.prepareStatement("truncate table ticket").execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closeConnection(connection);
		}
	}

	public void setUpDataBaseForRecurringUser() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (48 * 60 * 60 * 1000));// User arrived 48 hours ago
		Ticket ticket = new Ticket();
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber(vehicleRegNumber);
		ticket.setPrice(0);
		ticket.setInTime(inTime);
		ticket.setOutTime(null);
		ticket.setRecurringUser(isUserARecurringUser);

		saveTicket(ticket);
	}

	public boolean saveTicket(Ticket ticket) {
		Connection con = null;
		try {
			boolean isTicketSavedInDB = false;
			int ticketId = 0;
			con = dataBaseTestConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET, Statement.RETURN_GENERATED_KEYS);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			// ps.setInt(1,ticket.getId());
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
			ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
			ps.setBoolean(6, ticket.isRecurringUser());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				ticketId = rs.getInt(1); // I consider that the ticket is well saved in the DB from the moment that
											// ticketId = 1
			}
			isTicketSavedInDB = ticketId == 1;
			return isTicketSavedInDB;
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
			return false;
		} finally {
			dataBaseTestConfig.closeConnection(con);
		}
	}

}
