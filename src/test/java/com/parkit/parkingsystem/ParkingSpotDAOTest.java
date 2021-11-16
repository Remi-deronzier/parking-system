package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;

@Disabled
@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

	@Mock
	private DataBaseConfig ds;

	@Mock
	private Connection c;

	@Mock
	private PreparedStatement stmt;

	@Mock
	private ResultSet rs;

	@Test
	public void getNextAvailableSlotTest() throws SQLException, ClassNotFoundException {
		assertNotNull(ds);
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(any(String.class))).thenReturn(stmt);
		when(stmt.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getInt(1)).thenReturn(2);
		ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
		int nextAvailableSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		assertEquals(nextAvailableSlot, 2);
	}

}
