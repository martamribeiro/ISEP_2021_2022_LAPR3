package lapr.project.data.dataControllers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuditTrailControllerTest {

    @Disabled
    @Test
    void getContainerAuditTrail() {
        String expected = "Audit trail:\n" +
                "Register number 1\n" +
                "Audit id: 307,User: JOAO,Date: 2022-01-01,Operation performed: INSERT,Container id: 8950208,Cargo manifest id: 11031,Temperature kept: -24,000000\n" +
                "Register number 2\n" +
                "Audit id: 505,User: JOAO,Date: 2022-01-01,Operation performed: UPDATE,Container id: 8950208,Cargo manifest id: 11031,Temperature kept: 2,000000\n";

        AuditTrailController auditTrailController = new AuditTrailController();
        String resutl = auditTrailController.getContainerAuditTrail(8950208, 11031);
        System.out.println(resutl);
        assertEquals(expected, resutl);

        String result2 = auditTrailController.getContainerAuditTrail(0, 0);
        assertEquals("No registers were found", result2, "If the container with the cargo manifest is not found the result is the string of no registers.");
    }
}