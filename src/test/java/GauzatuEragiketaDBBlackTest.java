import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.User;
import testOperations.TestDataAccess;

public class GauzatuEragiketaDBBlackTest {

    // sut: system under test
    static DataAccess sut = new DataAccess();

    // additional operations needed to execute the test
    static TestDataAccess testDA = new TestDataAccess();

    @Test
    // Caso 1: username != null, amount > 0, deposit != null, user ∈ BD, currentMoney >= 0
    public void test1() {
        String username = "user1";
        double initialMoney = 100.0;
        double amount = 100.0;
        boolean deposit = true;

        try {
            testDA.open();
            testDA.createDriverWithMoney(username, "password", initialMoney);
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
    // Caso 2: username != null, amount > 0, deposit != null, user ∈ BD, currentMoney >= 0
    public void test2() {
        String username = "user2";
        double initialMoney = 100.0;
        double amount = 50.0;
        boolean deposit = false;

        try {
            testDA.open();
            testDA.createDriverWithMoney(username, "password", initialMoney);
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
    // Caso 3: username == null
    public void test3() {
        try {
            sut.open();
            boolean result = sut.gauzatuEragiketa(null, 100.0, true);
            sut.close();
            assertFalse(result); // FALSE
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    // Caso 4: amount <= 0
    public void test4() {
        String username = "user3";
        double initialMoney = 100.0;
        double amount = -50.0;
        boolean deposit = true;

        try {
            testDA.open();
            testDA.createDriverWithMoney(username, "password", initialMoney);
            testDA.close();

            sut.open();
            boolean result = sut.gauzatuEragiketa(username, amount, deposit);
            sut.close();
            assertFalse(result); // FALSE

        } catch (Exception e) {
            fail();
        } finally {
            testDA.open();
            testDA.removeDriver(username);
            testDA.close();
        }
    }

    /*
    @Test
    // Caso 5: deposit == null
    public void test5() {
        String username = "user4";
        double initialMoney = 100.0;

        try {
            testDA.open();
            testDA.createDriverWithMoney(username, "password", initialMoney);
            testDA.close();

            sut.open();
            boolean result = sut.gauzatuEragiketa(username, 50.0, null);
            sut.close();
            assertFalse(result); // FALSE

        } catch (Exception e) {
            fail();
        } finally {
            testDA.open();
            testDA.removeDriver(username);
            testDA.close();
        }
    }
*/
    @Test
    // Caso 6: user == null (no user in the DB)
    public void test6() {
        String username = "user5";

        try {
            sut.open();
            boolean result = sut.gauzatuEragiketa(username, 100.0, true);
            sut.close();
            assertFalse(result); // FALSE

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    // Caso 7: currentMoney < 0 (invalid state)
    public void test7() {
        String username = "user6";
        double initialMoney = -100.0; // Invalid state, should be handled in DB design
        double amount = 50.0;
        boolean deposit = true;

        try {
            testDA.open();
            testDA.createDriverWithMoney(username, "password", initialMoney); 
            testDA.close();

            sut.open();
            boolean result = sut.gauzatuEragiketa(username, amount, deposit);
            sut.close();
            assertFalse(result); // FALSE

        } catch (Exception e) {
            fail();
        } finally {
            testDA.open();
            testDA.removeDriver(username);
            testDA.close();
        }
    }

    @Test
    // Caso 8: amount > currentMoney
    public void test8() {
        String username = "user7";
        double initialMoney = 100.0;
        double amount = 200.0;
        boolean deposit = false;

        try {
            testDA.open();
            testDA.createDriverWithMoney(username, "password", initialMoney);
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
    // Caso 9: amount <= currentMoney
    public void test9() {
        String username = "user8";
        double initialMoney = 100.0;
        double amount = 50.0;
        boolean deposit = false;

        try {
            testDA.open();
            testDA.createDriverWithMoney(username, "password", initialMoney);
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
    // Caso 10: successful deposit (amount > 0, valid username)
    public void test10() {
        String username = "user9";
        double initialMoney = 100.0;
        double amount = 100.0;
        boolean deposit = true;

        try {
            testDA.open();
            testDA.createDriverWithMoney(username, "password", initialMoney);
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
