import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

public class BookRideBDBlackTest {
	// sut:system under test
	static DataAccess sut = new DataAccess();

	// additional operations needed to execute the test
	static TestDataAccess testDA = new TestDataAccess();

	@Test
	// sut.bookRide: The traveler has introduced the appropriate number of seats and
	// has enough money to make the ride.
	public void test1() {
		// define parameters
		int availableSeats = 5;
		double price = 10;

		String driverUsername = "Driver Test";
		String driverPassword = "123";
		String travelerUserName = "Traveler Test";
		String travelerPassWord = "123";
		
		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		Driver driver = null;

		testDA.open();
		driver = testDA.createDriver(driverUsername, driverPassword);
		testDA.close();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;

		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Ride ride = null;

		try {
			testDA.open();
			ride = testDA.createRide(rideFrom, rideTo, rideDate, availableSeats, price, driver);
			testDA.close();
			System.out.println(ride);

			testDA.open();
			Traveler traveler = testDA.createTraveler(travelerUserName, travelerPassWord);
			traveler.setMoney(25);
			testDA.close();
			assertNotNull(traveler);
			assertNotNull(ride);
			assertNotNull(driver);
			
			testDA.open();
			boolean a = testDA.bookingComplete(travelerUserName);
			testDA.close();
			assertTrue(a);
			
			// invoke System Under Test (sut)
			sut.open();
			sut.bookRide(travelerUserName, ride, 2, 0.1);
			assertTrue(true);
			sut.close();
			

		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			fail("Unexpected exception: " + e.getMessage());
		} finally {
			// Remove the created objects in the database (cascade removing)
			testDA.open();
			testDA.removeTraveler(travelerUserName);
			testDA.removeRide(driverUsername, rideFrom, rideTo, rideDate);
			testDA.removeDriver(driverUsername);

			testDA.close();
		}
	}

	@Test
	// sut.bookRide: The Traveler is null. The test must return null. If an
	// Exception is returned the bookRide method is not well implemented.
	public void test2() {
		// define parameters
		int availableSeats = 5;
		double price = 10;

		String driverUsername = "Driver Test";
		String driverPassword = "123";
		String travelerUserName = null;

		Driver driver = null;

		testDA.open();
		driver = testDA.createDriver(driverUsername, driverPassword);
		testDA.close();

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		Ride ride = null;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;

		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		testDA.open();
		ride = testDA.createRide(rideFrom, rideTo, rideDate, availableSeats, price, driver);
		testDA.close();

		try {
			// invoke System Under Test (sut)
			sut.open();
			Boolean b = sut.bookRide(travelerUserName, ride, 2, 0.1);
			// verify the results
			assertFalse(b);
			sut.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			// Remove the created objects in the database (cascade removing)
			testDA.open();
			testDA.removeRide(driverUsername, rideFrom, rideTo, rideDate);
			testDA.removeDriver(driverUsername);

			testDA.close();
		}
	}

	@Test
	// sut.createRide: The ride is null. The test must return null. If an Exception
	// is returned the createRide method is not well implemented.
	public void test3() {
		// define parameters
		String travelerUserName = "Traveler Test";
		String travelerPassWord = "123";

		boolean travelerCreated = false;

		Ride ride = null;

		testDA.open();
		if (!testDA.existTraveler(travelerUserName)) {
			testDA.createTraveler(travelerUserName, travelerPassWord);
			travelerCreated = true;
		}
		testDA.close();

		try {
			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, 2, 0.1);
			// verify the results
			assertFalse(b);
			sut.close();
			assertNull(ride);

		} catch (NullPointerException e) {
			assertTrue(true);
		} finally {
			// Remove the created objects in the database (cascade removing)
			testDA.open();
			if (travelerCreated) {
				testDA.removeTraveler(travelerUserName);
			}
			testDA.close();
		}
	}

	@Test
	// sut.createRide: The ride seats is negative. The test must return null. If an
	// Exception is returned the createRide method is not well implemented.
	public void test4() {
		// define parameters
		int availableSeats = 5;
		double price = 10;

		String travelerUserName = "Traveler Test";
		String travelerPassWord = "123";

		String driverUsername = "Driver Test";
		String driverPassword = "123";

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		boolean travelerCreated = false;

		Driver driver = null;

		testDA.open();
		if (!testDA.existDriver(driverUsername)) {
			driver = testDA.createDriver(driverUsername, driverPassword);
		}
		testDA.close();

		Ride ride = null;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null; // verify the results

		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		testDA.open();
		if (!testDA.existRide(driverUsername, rideFrom, rideTo, rideDate)) {
			ride = testDA.createRide(rideFrom, rideTo, rideDate, availableSeats, price, driver);
		}
		testDA.close();

		testDA.open();
		if (!testDA.existTraveler(travelerUserName)) {
			testDA.createTraveler(travelerUserName, travelerPassWord);
			travelerCreated = true;
		}
		testDA.close();

		try {
			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, -2, 0.1);
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
	// sut.createRide: The ride desk is negative. The test must return null. If an
	// Exception is returned the createRide method is not well implemented.
	public void test5() {
		// define parameters
		int availableSeats = 5;
		double price = 10;

		String travelerUserName = "Traveler Test";
		String travelerPassWord = "123";

		String driverUsername = "Driver Test";
		String driverPassword = "123";

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		boolean travelerCreated = false;

		Driver driver = null;

		testDA.open();
		if (!testDA.existDriver(driverUsername)) {
			driver = testDA.createDriver(driverUsername, driverPassword);
		}
		testDA.close();

		Ride ride = null;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;

		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		testDA.open();
		if (!testDA.existRide(driverUsername, rideFrom, rideTo, rideDate)) {
			ride = testDA.createRide(rideFrom, rideTo, rideDate, availableSeats, price, driver);
		}
		testDA.close();

		testDA.open();
		if (!testDA.existTraveler(travelerUserName)) {
			testDA.createTraveler(travelerUserName, travelerPassWord);
			travelerCreated = true;
		}
		testDA.close();

		try {
			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, 2, -0.1);
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
	// sut.bookRide: The value of the seats introduced is higher than the seats
	// available on the ride.
	public void test6() {
		// define parameters
		int availableSeats = 5;
		double price = 10;

		String travelerUserName = "Traveler Test";
		String travelerPassWord = "123";

		String driverUsername = "Driver Test";
		String driverPassword = "123";

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		boolean travelerCreated = false;

		Driver driver = null;

		testDA.open();
		if (!testDA.existDriver(driverUsername)) {
			driver = testDA.createDriver(driverUsername, driverPassword);
		}
		testDA.close();

		Ride ride = null;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;

		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		testDA.open();
		ride = testDA.createRide(rideFrom, rideTo, rideDate, availableSeats, price, driver);
		testDA.close();

		testDA.open();
		if (!testDA.existTraveler(travelerUserName)) {
			testDA.createTraveler(travelerUserName, travelerPassWord);
			travelerCreated = true;
		}
		testDA.close();

		try {
			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, 6, 0.1);
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
}