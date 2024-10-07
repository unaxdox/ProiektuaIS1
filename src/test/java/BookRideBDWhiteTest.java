import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

public class BookRideBDWhiteTest {
	// sut:system under test
	static DataAccess sut = new DataAccess();

	// additional operations needed to execute the test
	static TestDataAccess testDA = new TestDataAccess();

	@Test
	// sut.bookRide: The Traveler is null. The test must return null. If an
	// Exception is returned the bookRide method is not well implemented.
	public void test1() {
		// define parameters
		int availableSeats = 5;
		double price = 10;

		String driverUsername = "Driver Test";
		String driverPassword = "123";

		boolean driverCreated = false;

		String travelerUserName = null;

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		Date rideDate = null;

		try {
			Driver driver = null;

			testDA.open();
			driver = testDA.createDriver(driverUsername, driverPassword);
			driverCreated = true;
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			try {
				rideDate = sdf.parse("05/10/2026");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Ride ride = null;

			testDA.open();
			if (!testDA.existRide(driverUsername, rideFrom, rideTo, rideDate)) {
				ride = testDA.createRide(rideFrom, rideTo, rideDate, availableSeats, price, driver);
			}
			testDA.close();

			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, 2, 0.1);
			sut.close();
			// verify the results
			assertFalse(b);
		} catch (Exception e) {
			e.toString();
			// TODO Auto-generated catch block
			fail();
		} finally {
			// Remove the created objects in the database (cascade removing)
			testDA.open();
		
			testDA.removeRide(driverUsername, rideFrom, rideTo, rideDate);
			if (driverCreated) {
				testDA.removeDriver(driverUsername);
			}
			testDA.close();
		}
	}

	@Test
	// The Traveler("Traveler Test") does not exist in the DB. The test must return
	// null
	// The test supposes that the "Driver Test" does not exist in the DB
	public void test2() {
		// define parameters
		int availableSeats = 5;
		double price = 10;

		String driverUsername = "Driver Test";
		String driverPassword = "123";

		boolean driverCreated = false;

		String travelerUserName = "Traveler Test";

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		Date rideDate = null;

		try {
			Driver driver = null;

			testDA.open();
			driver = testDA.createDriver(driverUsername, driverPassword);
			driverCreated = true;
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			try {
				rideDate = sdf.parse("05/10/2026");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Ride ride = null;

			testDA.open();
			ride = testDA.createRide(rideFrom, rideTo, rideDate, availableSeats, price, driver);
			testDA.close();

			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, 2, 0.1);
			sut.close();
			// verify the results
			assertFalse(b);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			// Remove the created objects in the database (cascade removing)
			testDA.open();
		
			testDA.removeRide(driverUsername, rideFrom, rideTo, rideDate);
			if (driverCreated) {
				testDA.removeDriver(driverUsername);
			}
			testDA.close();
		}
	}

	@Test
	// sut.bookRide: The value of the seats introduced is higher than the seats
	// available on the ride.
	public void test3() {
		// define parameters
		int requestedSeats = 6;
		int availableSeats = 5;
		double price = 10;

		String driverUsername = "Driver Test";
		String driverPassword = "123";

		boolean travelerCreated = false;

		String travelerUserName = "Traveler Test";
		String travelerPassWord = "123";

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		Date rideDate = null;

		try {
			Driver driver = null;

			testDA.open();
			driver = testDA.createDriver(driverUsername, driverPassword);
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			try {
				rideDate = sdf.parse("05/10/2026");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Ride ride = null;

			testDA.open();
			ride = testDA.createRide(rideFrom, rideTo, rideDate, availableSeats, price, driver);
			testDA.close();

			// define parameters
			testDA.open();
			if (!testDA.existTraveler(travelerUserName)) {
				testDA.createTraveler(travelerUserName, travelerPassWord);
				travelerCreated = true;
			}
			testDA.close();

			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, requestedSeats, 0.1);
			sut.close();
			// verify the results
			assertFalse(b);
		} catch (Exception e) {
			fail();
		} finally {
			// Remove the created objects in the database (cascade removing)
			testDA.open();
			if (travelerCreated) {
				testDA.removeTraveler(travelerUserName);
			}
			testDA.removeRide(driverUsername, rideFrom, rideTo, rideDate);
			testDA.removeDriver(driverUsername);

			testDA.close();
		}
	}

	@Test
	// sut.bookRide: The traveler does not have enough money to make the ride
	public void test4() {
		// define parameters
		int availableSeats = 5;
		double price = 10;

		String driverUsername = "Driver Test";
		String driverPassword = "123";

		boolean travelerCreated = false;

		String travelerUserName = "Traveler Test";
		String travelerPassWord = "123";

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		Date rideDate = null;

		try {
			Driver driver = null;

			testDA.open();
			driver = testDA.createDriver(driverUsername, driverPassword);
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			try {
				rideDate = sdf.parse("05/10/2026");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Ride ride = null;

			testDA.open();
			ride = testDA.createRide(rideFrom, rideTo, rideDate, availableSeats, price, driver);
			testDA.close();
			// define parameters
			testDA.open();
			Traveler traveler = testDA.createTraveler(travelerUserName, travelerPassWord);
			traveler.setMoney(5);
			travelerCreated = true;
			testDA.close();
			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, 2, 0.1);
			sut.close();
			// verify the results
			assertFalse(b);
		} catch (Exception e) {
			fail();
		} finally {
			// Remove the created objects in the database (cascade removing)
			testDA.open();
			if (travelerCreated) {
				testDA.removeTraveler(travelerUserName);
			}
			testDA.removeRide(driverUsername, rideFrom, rideTo, rideDate);
			testDA.removeDriver(driverUsername);

			testDA.close();
		}
	}

	@Test
	// sut.bookRide: The traveler has introduced the appropriate number of seats and
	// has enough money to make the ride.
	public void test5() {
		// define parameters
		int availableSeats = 5;
		double price = 10;

		String driverUsername = "Driver Test";
		String driverPassword = "123";

		boolean travelerCreated = false;

		String travelerUserName = "Traveler Test";
		String travelerPassWord = "123";

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		Date rideDate = null;

		try {
			Driver driver = null;

			testDA.open();
			driver = testDA.createDriver(driverUsername, driverPassword);
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			try {
				rideDate = sdf.parse("05/10/2026");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Ride ride = null;

			testDA.open();
			ride = testDA.createRide(rideFrom, rideTo, rideDate, availableSeats, price, driver);
			testDA.close();

			// define parameters
			testDA.open();
			Traveler traveler = testDA.createTraveler(travelerUserName, travelerPassWord);
			traveler.setMoney(25);
			travelerCreated = true;
			testDA.close();
			
			testDA.open();
			boolean a = testDA.bookingComplete(travelerUserName);
			testDA.close();
			assertTrue(a);

			assertNotNull(driver);
			assertNotNull(ride);
			// invoke System Under Test (sut)
			sut.open();
			sut.bookRide(travelerUserName, ride, 2, 0.1);
			assertTrue(true);
			sut.close();
			
			// verify the results

		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			// Remove the created objects in the database (cascade removing)
			testDA.open();
			if (travelerCreated) {
				testDA.removeTraveler(travelerUserName);
			}
			testDA.removeRide(driverUsername, rideFrom, rideTo, rideDate);
			testDA.removeDriver(driverUsername);

			testDA.close();
		}
	}
}