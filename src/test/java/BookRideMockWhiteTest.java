import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

public class BookRideMockWhiteTest {
	static DataAccess sut;
	protected MockedStatic<Persistence> persistenceMock;
	@Mock
	protected EntityManagerFactory entityManagerFactory;
	@Mock
	protected EntityManager db;
	@Mock
	protected EntityTransaction et;
	@Mock
	protected TypedQuery<Traveler> mockedQuery;

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
				.thenReturn(entityManagerFactory);
		Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
		sut = new DataAccess(db);

	}

	@After
	public void tearDown() {
		persistenceMock.close();
	}

	Driver driver;
	Traveler traveler;
	Ride ride;

	@Test
	// sut.bookRide: The Traveler is null. The test must return null. If an
	// Exception is returned the bookRide method is not well implemented.
	public void test1() {
		//define parameters
		int availableSeats = 5;
		double price = 10;

		String travelerUserName = null;

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		driver = new Driver("Driver Test", "123");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		
		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ride = new Ride(rideFrom, rideTo, rideDate, availableSeats, price, driver);

		Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Traveler.class))).thenReturn(mockedQuery);

		try {
			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, 2, 0.1);
			sut.close();
			// verify the results
			assertFalse(b);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			fail();
		}
	}

	@Test
	// The Traveler("Traveler Test") does not exist in the DB. The test must return null 
	//The test supposes that the "Driver Test" does not exist in the DB
	public void test2() {
		//define parameters
		int availableSeats = 5;
		double price = 10;

		String travelerUserName = "Traveler Test"; 

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		driver = new Driver("Driver Test", "123");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null; 

		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ride = new Ride(rideFrom, rideTo, rideDate, availableSeats, price, driver);

		Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Traveler.class))).thenReturn(mockedQuery);
		Mockito.when(mockedQuery.getResultList()).thenReturn(Collections.emptyList()); 

		try {
			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, 2, 0.1);
			sut.close();
			// verify the results
			assertFalse(b); 
		} catch (Exception e) {
			e.printStackTrace(); 
			fail();
		}
	}

	@Test
	// sut.bookRide: The value of the seats introduced is higher than the seats available on the ride.
	public void test3() {
		//define parameters
		int requestedSeats = 6;
		int availableSeats = 5;
		double price = 10;

		String travelerUserName = "Traveler Test"; 
		String travelerPassWord = "123";

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		driver = new Driver("Driver Test", "123");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null; 

		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ride = new Ride(rideFrom, rideTo, rideDate, availableSeats, price, driver);

		traveler = new Traveler(travelerUserName, travelerPassWord); 

		Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Traveler.class))).thenReturn(mockedQuery);
		Mockito.when(mockedQuery.getResultList()).thenReturn(Collections.singletonList(traveler)); 

		try {
			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, requestedSeats, 0.1);
			sut.close();
			// verify the results
			assertFalse(b);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	// sut.bookRide: The traveler does not have enough money to make the ride
	public void test4() {
		//define parameters
		int availableSeats = 5;
		double price = 10; 

		String travelerUserName = "Traveler Test"; 
		String travelerPassWord = "123";

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		driver = new Driver("Driver Test", "123");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;

		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ride = new Ride(rideFrom, rideTo, rideDate, availableSeats, price, driver);

		traveler = new Traveler(travelerUserName, travelerPassWord);
		traveler.setMoney(5);

		Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Traveler.class))).thenReturn(mockedQuery);
		Mockito.when(mockedQuery.getResultList()).thenReturn(Collections.singletonList(traveler)); 
		
		try {
			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, 2, 0.1);
			sut.close();
			// verify the results
			assertFalse(b);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	// sut.bookRide: The traveler has introduced the appropriate number of seats and has enough money to make the ride.
	public void test5() {
		//define parameters
		int availableSeats = 5;
		double price = 10; 

		String travelerUserName = "Traveler Test"; 
		String travelerPassWord = "123";

		String rideFrom = "Donostia";
		String rideTo = "Zarautz";

		driver = new Driver("Driver Test", "123");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;

		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ride = new Ride(rideFrom, rideTo, rideDate, availableSeats, price, driver);

		traveler = new Traveler(travelerUserName, travelerPassWord); // Supongamos que tiene 50 de dinero
		traveler.setMoney(20);
		
		Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Traveler.class))).thenReturn(mockedQuery);
		Mockito.when(mockedQuery.getResultList()).thenReturn(Collections.singletonList(traveler)); 

		try {
			// invoke System Under Test (sut)
			sut.open();
			boolean b = sut.bookRide(travelerUserName, ride, 2, 0.1);
			sut.close();
			//verify the results
			assertTrue(b);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}