package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAO {

	private static final Logger logger = LogManager.getLogger("TicketDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	public boolean saveTicket(Ticket ticket) {
		Connection con = null;
		try {
			boolean isTicketSavedInDB = false;
			int ticketId = 0;
			con = dataBaseConfig.getConnection();
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
			dataBaseConfig.closeConnection(con);
		}
	}

	public Ticket getTicket(String vehicleRegNumber) {
		Connection con = null;
		Ticket ticket = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(rs.getTimestamp(4));
				ticket.setOutTime(rs.getTimestamp(5));
				ticket.setRecurringUser(rs.getBoolean(7));
			}
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}
		return ticket;
	}

	public boolean updateTicket(Ticket ticket) {
		Connection con = null;
		Connection conCheck = null;
		boolean isFareWellPopulatedInDB = false;
		boolean isOutTimeWellPopulatedInDB = false;
		try {
			con = dataBaseConfig.getConnection();
			Timestamp outTime = new Timestamp(ticket.getOutTime().getTime());
			outTime.setTime(1000 * (long) Math.floor(outTime.getTime() / 1000)); // remove milliseconds to be able to
																					// compare with the data store in
																					// the DB
			double price = ticket.getPrice();
			PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
			ps.setDouble(1, price);
			ps.setTimestamp(2, outTime);
			ps.setInt(3, ticket.getId());
			ps.execute();

			// Check that the fare generated and out time are populated correctly in the
			// database
			conCheck = dataBaseConfig.getConnection();
			PreparedStatement psCheck = conCheck.prepareStatement(DBConstants.GET_TICKET_UPDATED);
			psCheck.setInt(1, ticket.getId());
			ResultSet rsCheck = psCheck.executeQuery();
			if (rsCheck.next()) {
				isFareWellPopulatedInDB = rsCheck.getInt("PRICE") == ticket.getPrice();
				isOutTimeWellPopulatedInDB = rsCheck.getTimestamp("OUT_TIME").equals(outTime);
			}
			dataBaseConfig.closeResultSet(rsCheck);
			dataBaseConfig.closePreparedStatement(psCheck);
			return isFareWellPopulatedInDB && isOutTimeWellPopulatedInDB;
		} catch (Exception ex) {
			logger.error("Error saving ticket info", ex);
			return false;
		} finally {
			dataBaseConfig.closeConnection(con);
		}
	}

	public Ticket getRecurringUser(String vehicleRegNumber) {
		Connection con = null;
		Ticket ticket = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET_OF_RECURRING_USER);
			// VEHICLE_REG_NUMBER
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ticket.setVehicleRegNumber(vehicleRegNumber);
			}
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}
		return ticket;
	}
}
