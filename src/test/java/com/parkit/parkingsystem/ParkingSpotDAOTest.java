package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;

//@Disabled("Stoppé car le test ne fonctionne pas")
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
		ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(any(String.class))).thenReturn(stmt);
		doNothing().when(stmt).setString(anyInt(), anyString());
		when(stmt.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getInt(anyInt())).thenReturn(1);
		int nextAvailableSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
		assertEquals(nextAvailableSlot, 1);
	}

}
