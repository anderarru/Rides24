import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

public class BookRideBDWhiteTest {

    // sut: system under test
    static DataAccess sut = new DataAccess();

    // additional operations needed to execute the test
    static TestDataAccess testDA = new TestDataAccess();

    @Test
    // Test 1: Traveler is null. The method must return false.
    public void test1() {

        // This traveler does not exist
        String username = "nonexistentTraveler";
        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", new java.util.Date(), 2, 10.0, driver);

        /*
        testDA.open();
        //testDA.createDriver(driver.getName(), driver.getPassword());
        sut.createRide();
        testDA.close();

         */

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

        /*
        testDA.open();
        testDA.createTraveler(traveler);
        testDA.createDriver(driver.getName(), driver.getPassword());
        testDA.createRide(ride);
        testDA.close();

         */

        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 0.0); // Requesting 2 seats
        sut.close();

        assertEquals(false, result);

        /*
        testDA.open();
        testDA.removeTraveler(username);
        testDA.removeDriver(driver.getName());
        testDA.close();

         */
    }

    @Test
    // Test 3: Traveler has insufficient balance. The method must return false.
    public void test3() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(10.0); // Not enough balance

        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", new java.util.Date(), 5, 15.0, driver); // Price is 15.0 per seat

        /*
        testDA.open();
        testDA.createTraveler(traveler);
        testDA.createDriver(driver.getName(), driver.getPassword());
        testDA.createRide(ride);
        testDA.close();

         */

        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 0.0); // Trying to book 2 seats
        sut.close();

        assertEquals(false, result);

        /*
        testDA.open();
        testDA.removeTraveler(username);
        testDA.removeDriver(driver.getName());
        testDA.close();

         */
    }

    @Test
    // Test 4: Successful booking. The method must return true.
    public void test4() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(100.0); // Sufficient balance

        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", new java.util.Date(), 5, 10.0, driver);

        /*
        testDA.open();
        testDA.createTraveler(traveler);
        testDA.createDriver(driver.getName(), driver.getPassword());
        testDA.createRide(ride);
        testDA.close();

         */

        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 0.0); // Book 2 seats successfully
        sut.close();

        assertEquals(true, result);
        assertEquals(3, ride.getnPlaces());  // 2 seats booked, 3 remaining
        /*


        testDA.open();
        testDA.removeTraveler(username);
        testDA.removeDriver(driver.getName());
        testDA.close();
         */

    }
}
