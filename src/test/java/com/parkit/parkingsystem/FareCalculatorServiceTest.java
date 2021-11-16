package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

@DisplayName("Réussir à calculer les bons tarifs pour le stationnement")
public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Nested
	@Tag("CarTest")
	@DisplayName("Réussir à calculer les bons tarifs pour un automobiliste qui stationne")
	class CarTest {
		@Test
		@DisplayName("Quand une voiture se gare 1 h, alors elle doit payer le prix d'une place voiture pour une heure")
		public void calculateFareCar() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
		}

		@Test
		@DisplayName("Quand une voiture se gare pour une durée inférieure à 30 minutes, alors le prix du stationnement doit être gratuit")
		public void calculateFareCarWithLessThanThirtyMinutesParkingTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (15 * 60 * 1000));// 25 minutes parking time should be free
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals(0, ticket.getPrice());
		}

		@Test
		@DisplayName("Quand une voiture se gare pour une durée inférieure à 1h et supérieure à 30 minutes, alors le prix du stationnement doit couter 3/4 du prix du ticket pour 1h pour une voiture")
		public void calculateFareCarWithLessThanOneHourAndMoreThanThirtyMinutesParkingTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
																			// parking fare
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
		}

		@Test
		@DisplayName("Quand une voiture se gare pour une durée supérieure ou égale à une journée, alors le prix du stationnement doit correspondre à la bonne valeur")
		public void calculateFareCarWithMoreThanADayParkingTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																				// parking fare per hour
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
		}

		@Test
		@DisplayName("Quand un automobiliste récurrent se gare, alors il doit bénéficier d'une réduction de 5%")
		public void calculateFareCarForRecurringUser() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (22 * 60 * 60 * 1000));// 22 hours parking time should give 22 *
																				// 0.95 (5% discount) *
																				// parking fare per hour
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			ticket.setRecurringUser(true);
			fareCalculatorService.calculateFare(ticket);
			assertEquals((22 * Fare.CAR_RATE_PER_HOUR * 0.95), ticket.getPrice());
		}
	}

	@Nested
	@Tag("BikeTest")
	@DisplayName("Réussir à calculer les bons tarifs pour un cycliste qui stationne")
	class BikeTest {
		@Test
		@DisplayName("Quand un vélo se gare 1 h, alors il doit payer le prix d'une place vélo pour une heure")
		public void calculateFareBike() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
		}

		@Test
		@DisplayName("Quand un vélo indique une heure d'entrée chronologiquement après son heure d'arrivée, alors une erreur doit se déclarer")
		public void calculateFareBikeWithFutureInTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("Quand un vélo se gare pour une durée inférieure à 30 minutes, alors le prix du stationnement doit être gratuit")
		public void calculateFareBikeWithLessThanThirtyMinutesParkingTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (15 * 60 * 1000));// 15 minutes parking time should be free
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals(0, ticket.getPrice());
		}

		@Test
		@DisplayName("Quand un vélo se gare pour une durée inférieure à 1h et supérieure à 30 minutes, alors le prix du stationnement doit couter 3/4 du prix du ticket pour 1h pour un vélo")
		public void calculateFareBikeWithLessThanOneHourAndMoreThanThirtyMinutesParkingTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
																			// parking fare
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
		}

		@Test
		@DisplayName("Quand un cycliste récurrent se gare, alors il doit bénéficier d'une réduction de 5%")
		public void calculateFareBikeForRecurringUser() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (22 * 60 * 60 * 1000));// 22 hours parking time should give 22 *
																				// 0.95 (5% discount) *
																				// parking fare per hour
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			ticket.setRecurringUser(true);
			fareCalculatorService.calculateFare(ticket);
			assertEquals((22 * Fare.BIKE_RATE_PER_HOUR * 0.95), ticket.getPrice());
		}
	}

	@Nested
	@Tag("ErrorTest")
	@DisplayName("Vérifier que le service de calcul des tarifs gére bien les erreurs")
	class ErrorFareCalculatorServiceTest {
		@Test
		@DisplayName("Quand un véhicule de type non déclaré se gare, alors une erreur doit se déclarer")
		public void calculateFareUnkownType() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
		}
	}

}
