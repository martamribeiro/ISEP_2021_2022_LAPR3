package lapr.project.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MassTest {

    @Test
    void getArea() {
        Mass m1 = new Mass(147.0, 294.0, 32.0);
        double expResult = 9408.0;
        double result = m1.getArea();
        assertEquals(expResult, result);
    }

    @Test
    void getX() {
        Mass m1 = new Mass(147.0, 294.0, 32.0);
        double expResult = 147.0;
        double result = m1.getX();
        assertEquals(expResult, result);
    }
}