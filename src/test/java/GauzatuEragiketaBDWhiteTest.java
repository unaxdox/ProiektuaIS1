import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import testOperations.TestDataAccess;

public class GauzatuEragiketaBDWhiteTest {
	// sut:system under test
	static DataAccess sut = new DataAccess();

	// additional operations needed to execute the test
	static TestDataAccess testDA = new TestDataAccess();
	
	@Test
	// sut.bookRide: The User is null. The test must return false. If an
	// Exception is returned the bookRide method is not well implemented.
	public void test1() {
		boolean ride;
		try {
			
			String username = null;
			double amount = 100;
			boolean deposit = true;
			
			
			//invoke System Under Test (sut)  
			sut.open();
		    ride=sut.gauzatuEragiketa(username, amount, deposit);

			//verify the results
			assertFalse(ride);
			
			} catch (Exception e) {
			// TODO Auto-generated catch block
				fail();
				
			} finally {
				sut.close();
			}
		
	}
	
	@Test
	//sut.createRide:  The User("Aimar") does not exist in the DB. The test must return false 
	//The test supposes that the "Driver Test" does not exist in the DB
	public void test2() {
		boolean ride;	
		try {
				
			//define parameters
			String username="Aimar";

			double amount = 100;
			boolean deposit = true;		
				
			//invoke System Under Test (sut)  
			sut.open();
			ride=sut.gauzatuEragiketa(username, amount, deposit);
			sut.close();
				
			assertFalse(ride);
				
			} catch (Exception e) {
			// TODO Auto-generated catch block
			fail();
		} 
	}
	
	@Test
	//sut.createRide:  The User("Driver Test") has put money. The test must return true 
	//The test supposes that the "Driver Test" does exist in the DB
	public void test3() {
		boolean ride;	
		try {
			
			testDA.open();
			if (!testDA.existDriver("Luken")) {
				Driver a =testDA.createDriver("Luken", "123");
			}
			testDA.close();
			
			//define parameters
			String username="Luken";

			double amount = 100;
			boolean deposit = true;		
				
			//invoke System Under Test (sut)  
			sut.open();
			ride=sut.gauzatuEragiketa(username, amount, deposit);
			sut.close();
				
			assertTrue(ride);
				
			} catch (Exception e) {
			// TODO Auto-generated catch block
			fail();
			} finally {
				testDA.open();
				testDA.removeDriver("Luken");
				testDA.close();
			}
	}
	
	@Test
	//sut.createRide:  The Driver("Driver Test") has taken money in the DB. The test must return true 
	//The test supposes that the "Driver Test" does exist in the DB
	public void test4() {
		boolean ride;	
		try {
			
			testDA.open();
			Driver a =testDA.createDriver("Luken", "123");
			testDA.close();
				
			//define parameters
			String username="Luken";

			double amount = 100;
			boolean deposit = false;		
				
			//invoke System Under Test (sut)  
			sut.open();
			ride=sut.gauzatuEragiketa(username, amount, deposit);
			sut.close();
				
			assertTrue(ride);
				
			} catch (Exception e) {
			// TODO Auto-generated catch block
			fail();
			} finally {
				testDA.open();
				testDA.removeDriver("Luken");
				testDA.close();
			}
	}
	
	@Test
	//sut.createRide:  The Driver("Driver Test") has taken money in the DB but he hasn't enough. The test must return true 
	//The test supposes that the "Driver Test" does exist in the DB
	public void test5() {
		boolean ride;	
		try {
			
			testDA.open();
			Driver a =testDA.createDriver("Luken", "123");
			testDA.close();
				
			//define parameters
			String username="Luken";

			double amount = 100000;
			boolean deposit = false;		
				
			//invoke System Under Test (sut)  
			sut.open();
			ride=sut.gauzatuEragiketa(username, amount, deposit);
			sut.close();
				
			assertTrue(ride);
				
			} catch (Exception e) {
			// TODO Auto-generated catch block
			fail();
			} finally {
				testDA.open();
				testDA.removeDriver("Luken");
				testDA.close();
			}
	}
}
