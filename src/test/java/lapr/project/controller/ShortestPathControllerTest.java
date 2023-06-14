package lapr.project.controller;

import lapr.project.domain.dataStructures.FreightNetwork;
import lapr.project.domain.model.Capital;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Port;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShortestPathControllerTest {

    Company company;
    FreightNetwork map;

    private final int[] identifications = {123456789, 987654321, 369258147, 741852963};

    private final String [] names = {"Port1", "Port2", "Port3", "Port4"};

    private String continent1, continent2;

    private final double [] lats = {-30.033056, -42.033006, -55.022056, 23.008721};
    private final double [] lons = {-51.230000, -47.223056, -46.233056, 24.092123};

    private Port port1, port2, port3, port4;

    private Map<Integer, Double> seaDists;

    private String [] countryNames = {"Cyprus", "Malta", "Greece", "Portugal", "Turkey", "Armenia", "Spain", "Albania"};

    private String [] mapCapitals = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"};

    private Capital a, b, c, d, e, f, g, h, i, j, k;

    @BeforeEach
    void setUp() {
        company = new Company("Cmp");

        map = company.getFreightNetwork();

        continent1 = "America";
        continent2 = "Europe";

        seaDists = new HashMap<>();
        seaDists.put(123456789, 1200.0);
        seaDists.put(369258147, 500.0);

        port1 = new Port(identifications[0], names[0], continent1, countryNames[0], lats[0], lons[0], seaDists);
        port2 = new Port(identifications[1], names[1], continent1, countryNames[1], lats[1], lons[1], seaDists);
        port3 = new Port(identifications[2], names[2], continent2, countryNames[2], lats[2], lons[2], seaDists);
        port4 = new Port(identifications[3], names[3], continent1, countryNames[3], lats[3], lons[3], seaDists);

        a = new Capital(mapCapitals[0], lats[0],lats[1],countryNames[0], continent2);
        b = new Capital(mapCapitals[1],lats[0],lats[1],countryNames[1], continent2);
        c = new Capital(mapCapitals[2],lats[0],lats[1],countryNames[2], continent2);
        d = new Capital(mapCapitals[3],lats[0],lats[1],countryNames[3], continent2);
        e = new Capital(mapCapitals[4],lats[0],lats[1],countryNames[4], continent2);
        f = new Capital(mapCapitals[5],lats[0],lats[1],countryNames[5], continent2);
        g = new Capital(mapCapitals[6],lats[0],lats[1],countryNames[6], continent2);
        h = new Capital(mapCapitals[7],lats[0],lats[1],countryNames[7], continent2);
        i = new Capital(mapCapitals[8],lats[0],lats[1],countryNames[7], continent2);
        j = new Capital(mapCapitals[9],lats[0],lats[1],countryNames[7], continent2);
        k = new Capital(mapCapitals[10],lats[0],lats[1],countryNames[7], continent2);

        map.addLocation(a);
        map.addLocation(b);
        map.addLocation(c);
        map.addLocation(d);
        map.addLocation(e);
        map.addLocation(f);
        map.addLocation(g);
        map.addLocation(h);
        map.addLocation(i);
        map.addLocation(j);
        map.addLocation(k);

        map.addLocation(port1);
        map.addLocation(port2);
        map.addLocation(port3);
        map.addLocation(port4);

        //addDistance com capitais
        map.addDistance(h, g, 1.0);
        map.addDistance(h, k, 1.0);
        map.addDistance(h, i, 1.0);
        map.addDistance(h, a, 6.0);
        map.addDistance(k, g, 1.0);
        map.addDistance(k, e, 5.0);
        map.addDistance(k, j, 13.0);
        map.addDistance(k, d, 1.0);
        map.addDistance(d, i, 1.0);
        map.addDistance(d, c, 1.0);
        map.addDistance(d, b, 3.0);
        map.addDistance(g, f, 1.0);
        map.addDistance(i, j, 1.0);
        map.addDistance(a, b, 7.0);
        map.addDistance(e, f, 1.0);

        //addDistance com portos
        map.addDistance(port1,port2,2.0);
        map.addDistance(port2,port3,2.0);
        map.addDistance(port3,port4,2.0);
        map.addDistance(port1,port4,4.0);
        map.addDistance(port1,port3,5.0);

        //addDistance com portos e capitais
        map.addDistance(port1,a,1.0);
        map.addDistance(port1,b,14.0);
        map.addDistance(a,port3,1.0);
        map.addDistance(b,port2,8.0);
        map.addDistance(c,port4,12.0);

    }

    @Test
    public void getShortestLandPathSameBegEndTest(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(port1,port1,1);
        Assertions.assertNull(actual);
    }

    @Test
    public void getShortestMaritimePathSameBegEndTest(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(port1,port1,2);
        Assertions.assertNull(actual);
    }

    @Test
    public void getShortestLandOrSeaPathSameBegEndTest(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(port1,port1,3);
        Assertions.assertNull(actual);
    }

    @Test
    public void getShortestLandPathNonExBegTest(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(null,port1,1);
        Assertions.assertNull(actual);
    }

    @Test
    public void getShortestMaritimePathNonExBegTest(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(null,port1,2);
        Assertions.assertNull(actual);
    }

    @Test
    public void getShortestLandOrSeaPathNonExBegTest(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(null,port1,3);
        Assertions.assertNull(actual);
    }

    @Test
    public void getShortestLandPathNonExEndTest(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(port1,null,1);
        Assertions.assertNull(actual);
    }

    @Test
    public void getShortestMaritimePathNonExEndTest(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(port1,null,2);
        Assertions.assertNull(actual);
    }

    @Test
    public void getShortestLandOrSeaPathNonExEndTest(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(port1,null,3);
        Assertions.assertNull(actual);
    }

    @Test
    public void getShortestPathInvalidPathTypeBelow1(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(port1,port2,0);
        Assertions.assertNull(actual);
    }

    @Test
    public void getShortestPathInvalidPathTypeAbove3(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(port1,port2,4);
        Assertions.assertNull(actual);
    }

    @Test
    public void testLandPath1(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(h,j,1);
        List<String> expected = new ArrayList<>();
        expected.add("Capital: H");
        expected.add("Capital: I");
        expected.add("Capital: J");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void testLandPath2(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(port1,b,1);
        List<String> expected = new ArrayList<>();
        expected.add("Port: Port1");
        expected.add("Capital: A");
        expected.add("Capital: B");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void testMaritimePath1(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(port1,port3,2);
        List<String> expected = new ArrayList<>();
        expected.add("Port: Port1");
        expected.add("Port: Port2");
        expected.add("Port: Port3");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void testMaritimePath2(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(port1,port4,2);
        List<String> expected = new ArrayList<>();
        expected.add("Port: Port1");
        expected.add("Port: Port4");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void testLandOrSeaPath1(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(port1,port3,3);
        List<String> expected = new ArrayList<>();
        expected.add("Port: Port1");
        expected.add("Capital: A");
        expected.add("Port: Port3");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void testLandOrSeaPath2(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(b,port1,3);
        List<String> expected = new ArrayList<>();
        expected.add("Capital: B");
        expected.add("Capital: A");
        expected.add("Port: Port1");
        Assertions.assertEquals(expected,actual);
    }

    @Disabled
    @Test
    public void checkActualResults(){
        ShortestPathController shortestPathController = new ShortestPathController(company);
        List<String> actual = shortestPathController.getShortestPath(h,j,1);
        System.out.println(actual);
    }

}
