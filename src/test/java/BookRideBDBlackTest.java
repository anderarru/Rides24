import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

public class BookRideBDBlackTest {

   
    static DataAccess sut = new DataAccess();
  
    static TestDataAccess testDA = new TestDataAccess();

    @Test
    // Test 1: Traveler is null. The method must return false.
    public void test1() {
        String username = "nonexistentTraveler";
        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", new java.util.Date(), 2, 10.0, driver);


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
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(100.0); // Sufficient balance

        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", new java.util.Date(), 5, 10.0, driver);



        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 0.0); // Book 2 seats successfully
        sut.close();

        assertEquals(true, result);
        assertEquals(3, ride.getnPlaces());  // 2 seats booked, 3 remaining


    }

    @Test
    // Test 5: Ride is null. The method must return false.
    public void test5() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(100.0);



        sut.open();
        boolean result = sut.bookRide(username, null, 2, 0.0); // Null ride
        sut.close();

        assertEquals(false, result);


    }

    @Test
    // Test 6: Negative number of seats. The method must return false.
    public void test6() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(100.0);

        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", new java.util.Date(), 5, 10.0, driver);



        sut.open();
        boolean result = sut.bookRide(username, ride, -1, 0.0); // Negative seats
        sut.close();

        assertEquals(false, result);


    }

    @Test
    // Test 7: Discount is greater than the ride price. The method must return false.
    public void test7() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(100.0);

        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", new java.util.Date(), 5, 10.0, driver);



        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 15.0); // Discount is greater than price
        sut.close();

        assertEquals(false, result);


    }
}
