

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import testOperations.TestDataAccess;

import java.util.Date;

public class BookRideBDBlackTest {
/*
    static DataAccess sut = new DataAccess();
    static TestDataAccess testDA = new TestDataAccess();
    Date date = new Date("2025/12/12");

    @Test
    // Test 1: Traveler is null. The method must return false.
    public void test1() {
        String username = "nonexistentTraveler";
        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", new java.util.Date(), 2, 10.0, driver);

        //the user with that username won't exist
        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 0.0);
        sut.close();

        assertEquals(false, result);
    }

    @Test
    // Test 2: Ride has fewer seats than requested. The method must return false.
    public void test2() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(100.0);

        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", new java.util.Date(), 1, 10.0, driver); // Only 1 seat available

        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 0.0); // Requesting 2 seats
        sut.close();

        assertEquals(false, result);
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
    // Test 5: Ride is null. The method must return false.
    public void test5() {
        String username = "traveler1";

        sut.open();
        boolean result = sut.bookRide(username, null, 2, 0.0); // Null ride
        sut.close();

        assertEquals(false, result);
    }

    @Test
    // Test 6: Error during database operation (Rollback Handling).
    public void test6() {
        String username = "traveler1";
        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", new java.util.Date(), 5, 10.0, driver);

        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(100.0);

        testDA.open();
        testDA.addTraveler(username, "password");
        testDA.close();

       
        sut.open();
        boolean result = false;
        try {
           
            result = sut.bookRide(username, null, 2, 0.0);
        } catch (Exception e) {
            System.out.println("Rollback handled: " + e.getMessage());
        } finally {
            sut.close();
        }
        assertEquals(false, result);

     
        testDA.open();
        testDA.removeTraveler(username);
        testDA.close();
    }
    

    @Test
    // Test 7: Error during commit (Transaction Commit Exception Handling).
    public void test7_TransactionCommitException() {
        String username = "traveler1";
        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", new java.util.Date(), 5, 10.0, driver);

        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(100.0);

        testDA.open();
        testDA.addTraveler(username, "password");
        testDA.close();

        sut.open();
        boolean result = false;
        try {
            result = sut.bookRide(username, ride, 2, 0.0);
            // Simulate commit exception (e.g., force error in db.getTransaction().commit())
            throw new RuntimeException("Commit failed.");
        } catch (Exception e) {
            System.out.println("Commit exception handled: " + e.getMessage());
        } finally {
            sut.close();
        }
        assertEquals(false, result);

        // Clean up
        testDA.open();
        testDA.removeTraveler(username);
        testDA.close();
    }
    */
}
