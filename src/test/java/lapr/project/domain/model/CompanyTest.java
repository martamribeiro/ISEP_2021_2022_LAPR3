package lapr.project.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {



    @Test
    public void TestCompanyInstance(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Company(""));
        assertEquals("Designation cannot be blank.", thrown.getMessage());

    }
}