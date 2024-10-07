import static org.junit.Assert.assertEquals;
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
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import testOperations.TestDataAccess;
import domain.Driver;
public class GauzatuEragiketaBDBlackTest {
	
	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();
 

	@Test
	//sut.createRide:  The User("Driver Test") is null 

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
	//sut.createRide:  The User("Driver Test") HAS  NOT to be in the DB
	public void test2() {
		boolean ride;
		try {
			
			String username = "Aimar";
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
	//sut.createRide:  The User("Driver Test") has put minus money. The test must return false 
	//The test supposes that the "Driver Test" does exist in the DB
	public void test3() {
		boolean ride;	
		try {
			testDA.open();
			Driver a =testDA.createDriver("Luken", "123");
			a.setMoney(100);
			testDA.close();
				
			//define parameters
			String username="Luken";

			double amount = -100;
			boolean deposit = true;		
				
			//invoke System Under Test (sut)  
			sut.open();
			ride=sut.gauzatuEragiketa(username, amount, deposit);
			sut.close();
				
			assertFalse(ride);
				
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
	//sut.createRide:  The User("Driver Test") has put money. The test must return true 
	//The test supposes that the "Driver Test" does exist in the DB
	public void test4() {
		boolean ride;	
		try {
			testDA.open();
			Driver a =testDA.createDriver("Luken", "123");
			a.setMoney(100);
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
	public void test5() {
		boolean ride;	
		try {
			
			testDA.open();
			Driver a =testDA.createDriver("Luken", "123");
			a.setMoney(100);
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
	public void test6() {
		boolean ride;	
		try {
			
			testDA.open();
			Driver a =testDA.createDriver("Luken", "123");
			a.setMoney(100);
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
