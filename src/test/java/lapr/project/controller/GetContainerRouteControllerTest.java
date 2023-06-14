package lapr.project.controller;

import lapr.project.data.dataControllers.GetContainerRouteController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GetContainerRouteControllerTest {
    private GetContainerRouteController ctrl;

    @BeforeEach
    void setUp() {
        ctrl = new GetContainerRouteController();
    }

    //FIRST SITUATION: the container is at the a location (route 7)
    @Disabled
    @Test
    void getContainerPathForContainerAtLocation() {
        StringBuilder sb = new StringBuilder();
        sb.append("Departure Location: Port Larnaca, Departure Date: 21.03.04, Arrival Location: Port Dublin, Arrival Date: 21.05.12, Transport: Ship\n");
        sb.append("Departure Location: Port Dublin, Departure Date: 21.05.14, Arrival Location: Port Bahia Blanca, Arrival Date: 21.05.29, Transport: Ship\n");
        sb.append("Departure Location: Port Bahia Blanca, Departure Date: 21.05.29, Arrival Location: Port Cartagena, Arrival Date: 21.06.01, Transport: Ship\n");
        sb.append("Departure Location: Port Cartagena, Departure Date: 21.06.04, Arrival Location: Port Izola, Arrival Date: 21.06.26, Transport: Ship\n");
        sb.append("  > Current Container Location: Port Izola < ");

        try {
            String route = ctrl.getContainerPath(144449, 10);
            assertEquals(sb.toString(), route);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //SECOND SITUATION: the container is in the middle of the ocean (route 8)
    @Disabled
    @Test
    void getContainerPathForContainerMiddleOcean() {
        StringBuilder sb = new StringBuilder();
        sb.append("Departure Location: Port Constantza, Departure Date: 21.03.04, Arrival Location: Port Batumi, Arrival Date: 21.05.12, Transport: Ship\n");
        sb.append("Departure Location: Port Batumi, Departure Date: 21.05.14, Arrival Location: Port New Jersey, Arrival Date: 21.05.29, Transport: Ship\n");
        sb.append("Departure Location: Port New Jersey, Departure Date: 21.05.29, Arrival Location: Port Mersin, Arrival Date: 21.06.01, Transport: Ship\n");
        sb.append("Departure Location: Port Mersin, Departure Date: 21.06.04\n");
        sb.append("  > The Ship is between Port Mersin and Port Vlissingen < ");

        try {
            String route = ctrl.getContainerPath(1962396, 9);
            assertEquals(sb.toString(), route);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //THIRD SITUATION: the container is in a truck (route 9)
    @Disabled
    @Test
    void getContainerPathForContainerInTruck() {
        StringBuilder sb = new StringBuilder();
        sb.append("Departure Location: Port Bahia Blanca, Departure Date: 21.03.04, Arrival Location: Port Horta, Arrival Date: 21.05.12, Transport: Ship\n");
        sb.append("Departure Location: Port Horta, Departure Date: 21.05.14, Arrival Location: Port Veracruz, Arrival Date: 21.05.29, Transport: Ship\n");
        sb.append("Departure Location: Port Veracruz, Departure Date: 21.05.29, Arrival Location: Port Bourgas, Arrival Date: 21.06.01, Transport: Ship\n");
        sb.append("Departure Location: Warehouse WarehouseA, Departure Date: 21.06.04\n");
        sb.append("  > The Truck is between Warehouse WarehouseA and Warehouse WarehouseC < ");

        try {
            String route = ctrl.getContainerPath(7560668, 8);
            assertEquals(sb.toString(), route);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //FOURTH SITUATION: the container has arrived its final destination (route 10)
    @Disabled
    @Test
    void getContainerPathForContainerInFinalDestination() {
        StringBuilder sb = new StringBuilder();
        sb.append("Departure Location: Port Batumi, Departure Date: 21.03.04, Arrival Location: Port Split, Arrival Date: 21.05.12, Transport: Ship\n");
        sb.append("Departure Location: Port Split, Departure Date: 21.05.14, Arrival Location: Port San Vicente, Arrival Date: 21.05.29, Transport: Ship\n");
        sb.append("Departure Location: Port San Vicente, Departure Date: 21.05.29, Arrival Location: Port Genoa, Arrival Date: 21.06.01, Transport: Ship\n");
        sb.append("Departure Location: Port Genoa, Departure Date: 21.06.04, Arrival Location: Warehouse WarehouseD, Arrival Date: 21.06.26, Transport: Truck\n");
        sb.append("Departure Location: Warehouse WarehouseD, Departure Date: 21.06.19, Arrival Location: Warehouse WarehouseE, Arrival Date: 21.06.21, Transport: Truck\n");
        sb.append("  > Container has arrived at its final destination at Warehouse WarehouseE < ");

        try {
            String route = ctrl.getContainerPath(5655299, 7);
            assertEquals(sb.toString(), route);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //EXCEPTION 1: the container id is invalid, therefore raises ex_invalid_container_id exception
    @Disabled
    @Test
    void getRouteIdExceptionInvalidContainerID() {
        try {
            String expectedException = "10 – invalid container id";
            String route = ctrl.getContainerPath(12,2);
            assertEquals(expectedException, route);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //EXCEPTION 2: the container id is valid, but it was not leased by client, therefore raises ex_not_leased_client exception
    @Disabled
    @Test
    void getRouteIdExceptionNotLeasedByClient() {
        try {
            String expectedException = "11 – container is not leased by client";
            String routeId = ctrl.getContainerPath(9803333,2);
            assertEquals(expectedException, routeId);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}