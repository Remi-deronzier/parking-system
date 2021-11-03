package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.util.DisplayDoubleResults;

@ExtendWith(MockitoExtension.class)
public class DisplayDoubleResultsTest {

	private static DisplayDoubleResults result;

	@BeforeAll
	private static void setUp() {
		result = new DisplayDoubleResults(3, true);
	}

	@Test
	void getFeesTest() {
		assertEquals(3, result.getFees());
	}

	@Test
	void isDataBaseWellUpdatedTest() {
		assertEquals(true, result.isDataBaseWellUpdated());
	}
}
