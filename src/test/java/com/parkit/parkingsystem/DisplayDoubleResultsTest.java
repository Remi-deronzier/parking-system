package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.util.DisplayDoubleResults;

@ExtendWith(MockitoExtension.class)
@DisplayName("Réussir à retourner les frais de parking et la preuve de la mise à jour de la base de donnée")
public class DisplayDoubleResultsTest {

	private static DisplayDoubleResults result;

	@BeforeAll
	private static void setUp() {
		result = new DisplayDoubleResults(3, true);
	}

	@Test
	@DisplayName("Quand les frais valent 3€, alors le résultat retourné doit être 3")
	void getFeesTest() {
		assertEquals(3, result.getFees());
	}

	@Test
	@DisplayName("Quand la base de données a été mise à jour, alors le résultat retourné doit être vrai")
	void isDataBaseWellUpdatedTest() {
		assertEquals(true, result.isDataBaseWellUpdated());
	}
}
