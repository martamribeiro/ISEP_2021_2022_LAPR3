package lapr.project.controller;

import lapr.project.data.dataControllers.GetContainerSituationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetContainerSituationControllerTest {
    private GetContainerSituationController ctrl;

    @BeforeEach
    void setUp() {
        ctrl = new GetContainerSituationController();
    }

    //FIRST SITUATION: the container is at the a location (route 7)
    @Disabled
    @Test
    void getContainerPathForContainerAtLocation() {
        try {
            String expectedLocation = "PORT, Izola";
            String location = ctrl.getLocation(144449, 10);
            assertEquals(expectedLocation, location);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //SECOND SITUATION: the container is in the middle of the ocean (route 8)
    @Disabled
    @Test
    void getContainerPathForContainerMiddleOcean() {
        try {
            String expectedLocation = "SHIP, VARAMO";
            String location = ctrl.getLocation(1962396, 9);
            assertEquals(expectedLocation, location);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //THIRD SITUATION: the container is in a truck (route 9)
    @Disabled
    @Test
    void getContainerPathForContainerInTruck() {
        try {
            String expectedLocation = "TRUCK, Blaze";
            String location = ctrl.getLocation(7560668, 8);
            assertEquals(expectedLocation, location);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //FOURTH SITUATION: the container has arrived its final destination (route 10)
    @Disabled
    @Test
    void getContainerPathForContainerInFinalDestination() {
        try {
            String expectedLocation = "WAREHOUSE, WarehouseE";
            String location = ctrl.getLocation(5655299, 7);
            assertEquals(expectedLocation, location);
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
            String location = ctrl.getLocation(12,2);
            assertEquals(expectedException, location);
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
            String location = ctrl.getLocation(9803333,2);
            assertEquals(expectedException, location);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}