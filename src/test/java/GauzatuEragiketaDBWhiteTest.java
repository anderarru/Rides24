import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.User;
import testOperations.TestDataAccess;

public class GauzatuEragiketaDBWhiteTest {

    // sut: system under test
    static DataAccess sut = new DataAccess();

    // additional operations needed to execute the test
    static TestDataAccess testDA = new TestDataAccess();

    @Test
    // Caso 1: TRY2(T) - Error transaction
    public void test1() {
        try {
        	sut.open();
            boolean result = sut.gauzatuEragiketa(null, 3.4, true);
            sut.close();
            assertFalse(result); // FALSE
        } catch (Exception e) {
            fail(); 
        }
    }

    @Test
    // Caso 2: TRY2(F)-IF5(F) - User not in DB
    public void test2() {
        String username = "EzNago"; // user not registered
        String pass ="a";
        try {
        	sut.open();
            boolean result = sut.gauzatuEragiketa(username, 40, true);
            sut.close();
            assertFalse(result); //FALSE
        } catch (Exception e) {
            fail(); 
        }
    }

    @Test
    // Caso 3: TRY2(F)-IF5(T)-IF7(T) - User exists and deposit
    public void test3() {
        String username = "proba";
        String pass ="a";
        double initialMoney = 100;
        double amount = 40;
        boolean deposit = true;

        try {

            testDA.open();
            testDA.createDriverWithMoney(username, pass,initialMoney);
            testDA.close();

            sut.open();
            boolean result = sut.gauzatuEragiketa(username, amount, deposit);
            sut.close();
            assertTrue(result); // TRUE

            // Money verify
            testDA.open();
            User user = testDA.getUser(username);
            testDA.close();
            assertEquals(initialMoney + amount, user.getMoney(), 0);
        } catch (Exception e) {
            fail(); 
        } finally {
            testDA.open();
            testDA.removeDriver(username);
            testDA.close();
        }
    }

    @Test
    // Caso 4: TRY2(F)-IF5(T)-IF7(F)-IF10(T) - User exists && amount>initialMoney
    public void test4() {
        String username = "proba";
        String pass="a";
        double initialMoney = 30;
        double amount = 40;
        boolean deposit = false;

        try {
            testDA.open();
            testDA.createDriverWithMoney(username, pass, initialMoney); 
            testDA.close();

            sut.open();
            boolean result = sut.gauzatuEragiketa(username, amount, deposit);
            sut.close();
            assertTrue(result); // TRUE


        } catch (Exception e) {
            fail(); 
        } finally {
            testDA.open();
            testDA.removeDriver(username);
            testDA.close();
        }
    }

    @Test
    // Caso 5: TRY2(F)-IF5(T)-IF7(F)-IF10(F) - User exists && amount<initialMoney
    public void test5() {
        String username = "proba";
        String pass="a";
        double initialMoney = 100;
        double amount = 40;
        boolean deposit = false;

        try {
            testDA.open();
            testDA.createDriverWithMoney(username, pass, initialMoney); 
            testDA.close();

            sut.open();
            boolean result = sut.gauzatuEragiketa(username, amount, deposit);
            sut.close();
            assertTrue(result); // TRUE


        } catch (Exception e) {
            fail(); 
        } finally {
            testDA.open();
            testDA.removeDriver(username);
            testDA.close();
        }
    }
}
