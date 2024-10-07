import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import static org.mockito.Mockito.*;
import java.util.Collections;

public class GauzatuEragiketaMockWhiteTest {
	
static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	
	 @Mock
	    private TypedQuery<User> query;  // Simulamos la consulta
	 
	 private User existingUser;
	

	@Before
    public  void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);
	    existingUser = new User("Luken", "123", "Driver");
	    existingUser.setMoney(100);
    }
	@After
    public  void tearDown() {
		persistenceMock.close();
    }
	
	
	
	@Test
	//sut.createRide:  The User is null. The test must return false. If  an Exception is returned the createRide method is not well implemented.
		public void test1() {
			boolean ride;
			try {
				
				//define parameters
				String username = null;
				double amount = 100;
				boolean deposit = true;
				
	
				
				//Mockito.when(db.find(User.class, null)).thenReturn(null);
				Mockito.when(db.createQuery(anyString(), eq(User.class))).thenReturn(query);
	            // Configuramos el mock para que la consulta devuelva una lista vacía (usuario no encontrado)
				Mockito.when(query.setParameter(eq("username"), eq(username))).thenReturn(query);
				Mockito.when(query.getResultList()).thenReturn(Collections.emptyList());
				

				
				//invoke System Under Test (sut)  
				sut.open();
				ride=sut.gauzatuEragiketa(username, amount, deposit);
				sut.close();

				//verify the results
				assertFalse(ride);
				
			   } catch (Exception e) {
					e.toString();
				// TODO Auto-generated catch block
					fail();

				}
			
			   }
	
	@Test
	//sut.createRide:  The User is not in DB. The test must return false. If  an Exception is returned the createRide method is not well implemented.
		public void test2() {
			boolean ride;
			try {
				
				//define parameters
				String username = "Aimar";
				double amount = 100;
				boolean deposit = true;
				
	
				
				Mockito.when(db.createQuery(anyString(), eq(User.class))).thenReturn(query);
	            // Configuramos el mock para que la consulta devuelva una lista vacía (usuario no encontrado)
				Mockito.when(query.setParameter(eq("username"), eq(username))).thenReturn(query);
				Mockito.when(query.getResultList()).thenReturn(Collections.emptyList());
				

				
				//invoke System Under Test (sut)  
				sut.open();
				ride=sut.gauzatuEragiketa(username, amount, deposit);
				sut.close();

				//verify the results
				assertFalse(ride);
				
			   } catch (Exception e) {
					e.toString();
				// TODO Auto-generated catch block
					fail();

				}
			
			   }
	
	@Test
	//sut.createRide:  The User is in the DB and has put money. The test must return true. If  an Exception is returned the createRide method is not well implemented.
		public void test3() {
			boolean ride;
			try {
				
				//define parameters
				String username = "Luken";
				double amount = 100;
				boolean deposit = true;
				
	
				
				Mockito.when(db.createQuery(anyString(), eq(User.class))).thenReturn(query);
	            // Configuramos el mock para que la consulta devuelva una lista vacía (usuario no encontrado)
				Mockito.when(query.setParameter(eq("username"), eq(username))).thenReturn(query);
				Mockito.when(query.getResultList()).thenReturn(Collections.singletonList(existingUser)); // Devolvemos una lista que contiene el usuario existente

				

				
				//invoke System Under Test (sut)  
				sut.open();
				ride=sut.gauzatuEragiketa(username, amount, deposit);
				sut.close();

				//verify the results
				assertTrue(ride);
				
				assertEquals(200, existingUser.getMoney(), 0.01);
				
			   } catch (Exception e) {
					e.toString();
				// TODO Auto-generated catch block
					fail();

				}
			
			   }
	
	@Test
	//sut.createRide:  The User is in the DB and has taken money. The test must return true. If  an Exception is returned the createRide method is not well implemented.
		public void test4() {
			boolean ride;
			try {
				
				//define parameters
				String username = "Luken";
				double amount = 10;
				boolean deposit = false;
				
	
				
				Mockito.when(db.createQuery(anyString(), eq(User.class))).thenReturn(query);
	            // Configuramos el mock para que la consulta devuelva una lista vacía (usuario no encontrado)
				Mockito.when(query.setParameter(eq("username"), eq(username))).thenReturn(query);
				Mockito.when(query.getResultList()).thenReturn(Collections.singletonList(existingUser)); // Devolvemos una lista que contiene el usuario existente

				

				
				//invoke System Under Test (sut)  
				sut.open();
				ride=sut.gauzatuEragiketa(username, amount, deposit);
				sut.close();

				//verify the results
				assertTrue(ride);
				assertEquals(90, existingUser.getMoney(), 0.01);
				
			   } catch (Exception e) {
					e.toString();
				// TODO Auto-generated catch block
					fail();

				}
			
			   }
	
	@Test
	//sut.createRide:  The User is in the DB and has taken money but he hasn't enough. The test must return true. If  an Exception is returned the createRide method is not well implemented.
		public void test5() {
			boolean ride;
			try {
				
				//define parameters
				String username = "Luken";
				double amount = 100000;
				boolean deposit = false;
				
	
				
				Mockito.when(db.createQuery(anyString(), eq(User.class))).thenReturn(query);
	            // Configuramos el mock para que la consulta devuelva una lista vacía (usuario no encontrado)
				Mockito.when(query.setParameter(eq("username"), eq(username))).thenReturn(query);
				Mockito.when(query.getResultList()).thenReturn(Collections.singletonList(existingUser)); // Devolvemos una lista que contiene el usuario existente

				

				
				//invoke System Under Test (sut)  
				sut.open();
				ride=sut.gauzatuEragiketa(username, amount, deposit);
				sut.close();

				//verify the results
				assertTrue(ride);
				assertEquals(0, existingUser.getMoney(), 0.01);
				
			   } catch (Exception e) {
					e.toString();
				// TODO Auto-generated catch block
					fail();

				}
			
			   }

}
