import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import exceptions.RideMustBeLaterThanTodayException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CancelRideMockBlackTest {

    static DataAccess sut;

    protected MockedStatic<Persistence> persistenceMock;

    @Mock
    protected EntityManagerFactory entityManagerFactory;
    @Mock
    protected EntityManager db;
    @Mock
    protected EntityTransaction et;

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

    @Test
    public void test1() {
        // Test cancelling a ride that has bookings
        Driver driver = new Driver("Driver1", "password");
        Ride ride1 = new Ride("A", "B", new Date(System.currentTimeMillis() + 86400000), 2, 50.0, driver);
        Traveler traveler = new Traveler("Traveler1", "password");
        Booking booking = new Booking(ride1, traveler, 1);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        ride1.setBookings(bookings);

     
        Mockito.when(db.find(Ride.class, ride1.getRideNumber())).thenReturn(ride1);

      
        sut.open();
        sut.cancelRide(ride1);
        sut.close();

    
        assertEquals(false, ride1.isActive());
        assertEquals(Booking.STATUS_REJECTED, booking.getStatus());
    }

    @Test
    public void test2() {
        // Test cancelling a ride that does not belong to the driver
        Driver driver1 = new Driver("Driver1", "password");
        Driver driver2 = new Driver("Driver2", "password");
        Ride ride2 = new Ride("C", "D", new Date(System.currentTimeMillis() + 86400000), 2, 50.0, driver1);

    
        Mockito.when(db.find(Ride.class, ride2.getRideNumber())).thenReturn(ride2);

      
        sut.open();
        try {
            sut.cancelRide(ride2); 
            fail("Expected exception for unauthorized cancellation not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        sut.close();
    }

    @Test
    public void test3() {
        // Test cancelling a ride in the past
        Driver driver = new Driver("Driver1", "password");
        Ride ride3 = new Ride("E", "F", new Date(System.currentTimeMillis() - 86400000), 2, 50.0, driver);

     
        Mockito.when(db.find(Ride.class, ride3.getRideNumber())).thenReturn(ride3);

       
        sut.open();
        try {
            sut.cancelRide(ride3);
            fail("Expected RideMustBeLaterThanTodayException not thrown");
        } catch (Exception e) {
            // Expected
        }
        sut.close();
    }

    @Test
    public void test4() {
        // Test cancelling a null ride
        sut.open();
        try {
            sut.cancelRide(null);
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected
        }
        sut.close();
    }

    @Test
    public void test5() {
        // Test cancelling a ride with multiple bookings
        Driver driver = new Driver("Driver1", "password");
        Ride ride4 = new Ride("G", "H", new Date(System.currentTimeMillis() + 86400000), 2, 50.0, driver);
        Traveler traveler1 = new Traveler("Traveler1", "password");
        Traveler traveler2 = new Traveler("Traveler2", "password");
        Booking booking1 = new Booking(ride4, traveler1, 1);
        Booking booking2 = new Booking(ride4, traveler2, 1);
      
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);
        ride4.setBookings(bookings);
        
        Mockito.when(db.find(Ride.class, ride4.getRideNumber())).thenReturn(ride4);

    
        sut.open();
        sut.cancelRide(ride4);
        sut.close();

   
        assertEquals(false, ride4.isActive());
        assertEquals(Booking.STATUS_REJECTED, booking1.getStatus());
        assertEquals(Booking.STATUS_REJECTED, booking2.getStatus());
    }

    @Test
    public void test6() {
        // Test cancelling a ride that has no bookings
        Driver driver = new Driver("Driver1", "password");
        Ride ride5 = new Ride("I", "J", new Date(System.currentTimeMillis() + 86400000), 2, 50.0, driver);

      
        Mockito.when(db.find(Ride.class, ride5.getRideNumber())).thenReturn(ride5);

       
        sut.open();
        sut.cancelRide(ride5);
        sut.close();

        
        assertEquals(false, ride5.isActive());
    }
}
