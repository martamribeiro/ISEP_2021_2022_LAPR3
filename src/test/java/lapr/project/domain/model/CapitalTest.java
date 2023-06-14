package lapr.project.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapitalTest {
    private Capital capital1;

    private final double [] lats = {-30.033056, -42.033006, -55.022056, 23.008721};
    private final double [] lons = {-51.230000, -47.223056, -46.233056, 24.092123};
    private String [] countryNames = {"Cyprus", "Malta", "Greece", "Portugal"};

    @BeforeEach
    void setUp() {
        capital1 = new Capital("Nicosia",lats[0],lons[0],countryNames[0], "Europe");

    }

    @Test
    void ensureNotNullNameIsAllowed() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Capital(null, lats[0],lons[0],countryNames[0], "Europe"));
        assertEquals("Name cannot be null.", thrown.getMessage());
    }
}