import static org.junit.Assert.assertEquals;
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
    // Test 3: Traveler has insufficient balance. Expect false.
    public void test3() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(0.0); // Not enough balance

        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", date, 3, 15.0, driver); // Price is 15.0 per seat

        try {
            testDA.open();
            testDA.addTraveler(username, "password");
            testDA.createRide("Start", "End", date, 3, 15, "driver1");
            testDA.close();

            sut.open();
            boolean result = sut.bookRide(username, ride, 2, 0.0); // Trying to book 2 seats
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
    // Test 4: Successful booking. Expect true.
    public void test4() {
        String username = "traveler1";
        Driver driver = new Driver("driver1", "password");
        Ride ride = new Ride("Start", "End", date, 5, 10.0, driver);

        try {
            testDA.open();
            testDA.addTraveler(username, "password");
            testDA.getTraveler(username).setMoney(100.0);
            testDA.createRide("Start", "End", date, 5, 10, "driver1");
            testDA.close();

            sut.open();
            boolean result = sut.bookRide(username, ride, 2, 0.0); // Book 2 seats successfully
            sut.close();

            assertEquals(true, result);
            assertEquals(3, ride.getnPlaces());  // 2 seats booked, 3 remaining
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