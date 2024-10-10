/*

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import org.junit.Test;
import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

import java.util.Date;

public class BookRideDBWhiteTest {

    static DataAccess sut = new DataAccess(); // System Under Test
    static TestDataAccess testDA = new TestDataAccess();

    Date date = new Date("2025/12/12");

    @Test
    // Test 1: Traveler is null in the database. Expect false.
    public void test1() {
        String username = "nonexistentTraveler";
        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", date, 2, 10.0, driver);

        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 0.0); // Traveler is null
        sut.close();

        assertEquals(false, result);
    }

    @Test
    // Test 2: Ride has fewer seats than requested. Expect false.
    public void test2() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(100.0);

        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", date, 1, 10.0, driver); // Only 1 seat available

        try {
            testDA.open();
            testDA.addTraveler(username, "password");
            testDA.getTraveler(username).setMoney(100.0);
            testDA.createRide("Start", "End", date, 1, 10, "driver1"); // Ride has only 1 seat
            testDA.close();

            sut.open();
            boolean result = sut.bookRide(username, ride, 2, 0.0); // Requesting 2 seats
            sut.close();

            assertEquals(false, result);
        } catch (RideAlreadyExistException | RideMustBeLaterThanTodayException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            fail(); 
        } finally {
            testDA.open();
            testDA.removeTraveler(username);
            testDA.removeRide("driver1", "Start", "End", date);
            testDA.close();
        }
    }

    @Test
    public void test3() {
        String username = "traveler1";
        String driverUsername = "driver1";
        Date rideDate = new Date("2025/12/12"); // Use current date
        
        try {
            // Add a traveler and set money
            testDA.open();
            testDA.addTraveler(username, "password");
            Traveler traveler = testDA.getTraveler(username);
            traveler.setMoney(10.0); // Not enough balance
            testDA.updateTraveler(traveler);
            testDA.close();
            System.out.println("Traveler created with balance: " + traveler.getMoney());

            // Create a driver
            testDA.open();
            testDA.createDriver(driverUsername, "password");
            testDA.close();
            System.out.println("Driver created: " + driverUsername);

            // Create a ride for the driver
            testDA.open();
            Ride createdRide = testDA.createRide("Start", "End", rideDate, 5, 10, driverUsername);
            testDA.close();
            System.out.println("Ride created: " + createdRide);

            // Perform the booking operation
            sut.open();
            boolean result = sut.bookRide(username, createdRide, 2, 0.0); // Trying to book 2 seats
            sut.close();
            System.out.println("Booking result: " + result);

            // Check the results
            assertFalse("Booking should fail due to insufficient balance", result);
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown: " + e.getMessage());
        } finally {
           
            testDA.open();
            testDA.removeTraveler(username);
            testDA.removeDriver(driverUsername);
            testDA.close();
        }
    }
    @Test
    // Test 4: Successful booking. Expect true.
    public void test4() {
        String username = "traveler1";
        String driverUsername = "driver1";
        Date rideDate = new Date("2025/12/12"); // Use current date or set a specific date
        
        try {
            // Add a traveler and set money
            testDA.open();
            testDA.addTraveler(username, "password");
            Traveler traveler = testDA.getTraveler(username);
            traveler.setMoney(100.0);
            testDA.updateTraveler(traveler);
            testDA.close();
            System.out.println("Traveler created: " + traveler);

            // Create a driver
            testDA.open();
            testDA.createDriver(driverUsername, "password");
            testDA.close();
            System.out.println("Driver created: " + driverUsername);

            // Create a ride for the driver
            testDA.open();
            Ride createdRide = testDA.createRide("Start", "End", rideDate, 5, 10, driverUsername);
            testDA.close();
            System.out.println("Ride created: " + createdRide);

            // Perform the booking operation
            sut.open();
            boolean result = sut.bookRide(username, createdRide, 2, 0.0); // Book 2 seats successfully
            sut.close();
            System.out.println("Booking result: " + result);

            // Check the results
            assertTrue("Booking should be successful", result);

            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown: " + e.getMessage());
        } finally {
        	testDA.open();
            testDA.removeTraveler(username);
            testDA.removeDriver(driverUsername);
            testDA.close();
        }
    }

    @Test
    // Test 5: Traveler not found in the database. Expect false.
    public void test5() {
        String username = "nonexistentTraveler";
        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", date, 2, 10.0, driver);

        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 0.0); // Traveler does not exist in DB
        sut.close();

        assertEquals(false, result);
    }
}
*/