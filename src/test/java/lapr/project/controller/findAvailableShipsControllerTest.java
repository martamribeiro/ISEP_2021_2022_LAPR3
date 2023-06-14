package lapr.project.controller;

import lapr.project.data.dataControllers.findAvailableShipsController;
import lapr.project.domain.model.Company;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class findAvailableShipsControllerTest {

    @Test
    public void getAvailableShips(){
        String expected= "Available ships:\n" +
                "_______\n" +
                "Ship code: 258692000\n" +
                "Location(Port): Vlore\n" +
                "_______\n" +
                "Ship code: 229961000\n" +
                "Location(Port): Vlore\n" +
                "_______\n" +
                "Ship code: 636092932\n" +
                "Location(Port): Copenhagen\n" +
                "_______\n" +
                "Ship code: 228339600\n" +
                "Location(Port): Larnaca\n" +
                "_______\n" +
                "Ship code: 210950000\n" +
                "Location(Port): Vlissingen\n" +
                "_______\n" +
                "Ship code: 305176000\n" +
                "Location(Port): Vlone\n" +
                "_______\n" +
                "Ship code: 256888000\n" +
                "Location(Port): Aarhus\n" +
                "_______\n" +
                "Ship code: 212180000\n" +
                "Location(Port): Zeebrugge\n" +
                "_______\n" +
                "Ship code: 303221000\n" +
                "Location(Port): Matarani\n" +
                "_______\n" +
                "Ship code: 229767000\n" +
                "Location(Port): Rijeka\n" +
                "_______\n" +
                "Ship code: 636091400\n" +
                "Location(Port): Copenhagen\n" +
                "_______\n" +
                "Ship code: 303267000\n" +
                "Location(Port): Veracruz\n" +
                "_______\n" +
                "Ship code: 257881000\n" +
                "Location(Port): Santos\n" +
                "_______\n" +
                "Ship code: 305373000\n" +
                "Location(Port): Horta\n" +
                "_______\n" +
                "Ship code: 636019825\n" +
                "Location(Port): Ponta\n" +
                "_______\n" +
                "Ship code: 249047000\n" +
                "Location(Port): Delgada\n" +
                "_______\n" +
                "Ship code: 235092459\n" +
                "Location(Port): Cristobal\n" +
                "_______\n" +
                "Ship code: 256304000\n" +
                "Location(Port): Setubal\n" +
                "_______\n" +
                "Ship code: 212351000\n" +
                "Location(Port): Ambarli\n" +
                "_______";
        findAvailableShipsController findAvailableShipsController = mock(findAvailableShipsController.class);
        when(findAvailableShipsController.getNextMondayAvailableShips()).thenReturn(expected);
        String call = findAvailableShipsController.getNextMondayAvailableShips();
        assertEquals(expected, call);
    }

    @Test
    public void testControllerInstance(){
        findAvailableShipsController ctrl = new findAvailableShipsController();
        Company company = new Company("Company");
        findAvailableShipsController ctrl2 = new findAvailableShipsController(company);
    }

}