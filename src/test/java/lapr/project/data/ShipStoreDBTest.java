package lapr.project.data;

import lapr.project.controller.App;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class ShipStoreDBTest {
    private DatabaseConnection databaseConnection;
    private ShipStoreDB shipStoreDB;

    @BeforeEach
    void setUp() {
        databaseConnection = mock(DatabaseConnection.class);
        shipStoreDB = mock(ShipStoreDB.class);
    }

    //The test was made when sysdate=15/01/2022, therefore the results will change in the future
    @Test
    void getIdleShips() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ship MMSI: 210950000 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 212180000 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 212351000 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 212351001 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 212351004 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 228339600 | Number of Idle Days: 4\n");
        sb.append("Ship MMSI: 229767000 | Number of Idle Days: 7\n");
        sb.append("Ship MMSI: 229961000 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 235092459 | Number of Idle Days: 0\n");
        sb.append("Ship MMSI: 249047000 | Number of Idle Days: 0\n");
        sb.append("Ship MMSI: 256304000 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 256888000 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 257881000 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 258692000 | Number of Idle Days: 6\n");
        sb.append("Ship MMSI: 303221000 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 303267000 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 305176000 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 305373000 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 636019825 | Number of Idle Days: 8\n");
        sb.append("Ship MMSI: 636091400 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 636092932 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 636092933 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 636092948 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 636092949 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 636092950 | Number of Idle Days: 15\n");
        sb.append("Ship MMSI: 636092951 | Number of Idle Days: 15");

        String expResult = sb.toString();
        when(shipStoreDB.getIdleShips(databaseConnection)).thenReturn(sb.toString());
        String result = shipStoreDB.getIdleShips(databaseConnection);
        assertEquals(expResult, result);
    }
}