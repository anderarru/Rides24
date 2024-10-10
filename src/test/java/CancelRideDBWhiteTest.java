

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CancelRideDBWhiteTest {

	static DataAccess sut=new DataAccess();
	 
	
	 static TestDataAccess testDA=new TestDataAccess();

   
    @Test
    public void test1() {
        // Test successful cancellation of a ride with one booking
        Driver driver = new Driver("Driver1", "password");
        Ride ride1 = new Ride("A", "B", new Date(System.currentTimeMillis() + 86400000), 2, 50.0, driver);
        Traveler traveler = new Traveler("Traveler1", "password");
        Booking booking = new Booking(ride1, traveler, 1);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        ride1.setBookings(bookings);

       
      

       
        sut.cancelRide(ride1);

       
        assertFalse(ride1.isActive());
        assertEquals(Booking.STATUS_REJECTED, booking.getStatus());
    }

    @Test
    public void test2() {
        // Test cancellation with no bookings
        Driver driver = new Driver("Driver1", "password");
        Ride ride2 = new Ride("C", "D", new Date(System.currentTimeMillis() + 86400000), 2, 50.0, driver);

    
      

     
        sut.cancelRide(ride2);

     
        assertFalse(ride2.isActive());
    }

    @Test
    public void test3() {
        // Test cancellation of a ride that has a past date
        Driver driver = new Driver("Driver1", "password");
        Ride ride3 = new Ride("E", "F", new Date(System.currentTimeMillis() - 86400000), 2, 50.0, driver);

      
       

       
        try {
            sut.cancelRide(ride3);
            fail("Expected RideMustBeLaterThanTodayException not thrown");
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test
    public void test4() {
        // Test cancellation of a ride with bookings that have different statuses
        Driver driver = new Driver("Driver1", "password");
        Ride ride4 = new Ride("G", "H", new Date(System.currentTimeMillis() + 86400000), 2, 50.0, driver);
        Traveler traveler1 = new Traveler("Traveler1", "password");
        Booking bookingAccepted = new Booking(ride4, traveler1, 1);
        bookingAccepted.setStatus(Booking.STATUS_ACCEPTED);
      

        Traveler traveler2 = new Traveler("Traveler2", "password");
        Booking bookingNotDefined = new Booking(ride4, traveler2, 1);
        bookingNotDefined.setStatus("NotDefined");
        
        List<Booking> bookings = new ArrayList<>();
        bookings.add(bookingNotDefined);
        bookings.add(bookingAccepted);
        ride4.setBookings(bookings);
  
        

   
        sut.cancelRide(ride4);


        assertFalse(ride4.isActive());
        assertEquals(Booking.STATUS_REJECTED, bookingAccepted.getStatus());
        assertEquals(Booking.STATUS_REJECTED, bookingNotDefined.getStatus());
    }

    
    
    @Test
    public void test5() {
        // Test cancelling a ride that is null
        try {
            sut.cancelRide(null);
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}

