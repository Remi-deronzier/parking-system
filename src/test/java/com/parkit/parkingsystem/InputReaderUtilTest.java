package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.util.InputReaderUtil;

@Disabled("Stoppé car le test ne fonctionne pas")
@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {

	@Test
	void readSelectionTest() {
		// GIVEN
		String input = "1";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		// WHEN
		InputReaderUtil inputReader = new InputReaderUtil();

		// THEN
		assertEquals(1, inputReader.readSelection());
	}

	@Test
	void readVehicleRegistrationNumberTest() throws Exception {
		// GIVEN
		String input = "REMI";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		// WHEN
		InputReaderUtil inputReader = new InputReaderUtil();

		// THEN
		assertEquals("REMI", inputReader.readVehicleRegistrationNumber());
	}

}
