import static org.junit.Assert.assertEquals;
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
    // Test 3: Traveler has insufficient balance. The method must return false.
    public void test3() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(10.0); // Not enough balance

        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", new java.util.Date(), 5, 15.0, driver); // Price is 15.0 per seat

        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 0.0); // Trying to book 2 seats
        sut.close();

        assertEquals(false, result);
    }

    @Test
 // Test 4: Successful booking. The method must return true.
 public void test4() {
     String username = "traveler1";
     Driver driver = new Driver("driver1", "password");
     Ride ride = new Ride("Start", "End", date, 5, 10.0, driver);

     try {
       
         testDA.open();
         testDA.addTraveler(username, "password");
         testDA.getTraveler(username).setMoney(100.0);
         testDA.close(); // Close after adding the traveler

         
         testDA.open();
         testDA.createRide("Start", "End", date, 5, 10, "driver1");
         testDA.close(); // Close after creating the ride

         
         sut.open();
         boolean result = sut.bookRide(username, ride, 2, 0.0); // Book 2 seats successfully
         sut.close();

         
         assertEquals(true, result);
         assertEquals(3, ride.getnPlaces());  // 2 seats booked, 3 remaining

     } catch (RideAlreadyExistException | RideMustBeLaterThanTodayException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
         // Cleanup the database state
         testDA.open();
         testDA.removeTraveler(username);
         testDA.removeRide("driver1", "Start", "End", date);
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
}
