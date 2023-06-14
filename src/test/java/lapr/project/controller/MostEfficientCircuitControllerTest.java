package lapr.project.controller;

import lapr.project.domain.dataStructures.FreightNetwork;
import lapr.project.domain.model.Capital;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Location;
import lapr.project.domain.model.Port;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MostEfficientCircuitControllerTest {
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

    private String [] mapCapitals = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R"};

    private Capital a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r;

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
        l = new Capital(mapCapitals[11],lats[0],lats[1],countryNames[7], continent2);
        m = new Capital(mapCapitals[12],lats[0],lats[1],countryNames[7], continent2);
        n = new Capital(mapCapitals[13],lats[0],lats[1],countryNames[7], continent2);
        o = new Capital(mapCapitals[14],lats[0],lats[1],countryNames[7], continent2);
        p = new Capital(mapCapitals[15],lats[0],lats[1],countryNames[7], continent2);
        q = new Capital(mapCapitals[16],lats[0],lats[1],countryNames[7], continent2);
        r = new Capital(mapCapitals[17],lats[0],lats[1],countryNames[7], continent2);

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
        map.addLocation(l);
        map.addLocation(m);
        map.addLocation(n);
        map.addLocation(o);
        map.addLocation(p);
        map.addLocation(q);
        map.addLocation(r);



        map.addDistance(a, c, 2.0);
        map.addDistance(b, c, 5.0);
        map.addDistance(c, h, 1.0);
        map.addDistance(d, e, 3.0);
        map.addDistance(e, f, 1.0);
        map.addDistance(f, g, 13.0);
        map.addDistance(h, i, 6.0);
        map.addDistance(i, j, 3.0);
        map.addDistance(g, j, 5.0);
        map.addDistance(j, k, 7.0);
        map.addDistance(k, l, 12.0);
        map.addDistance(j, n, 2.0);
        map.addDistance(n, o, 2.0);
        map.addDistance(n, m, 3.0);
        map.addDistance(n, p, 1.0);
        map.addDistance(p, q, 9.0);
        map.addDistance(q, r, 8.0);
        map.addDistance(q, h, 1.0);
        map.addDistance(r, m, 7.0);
        map.addDistance(m, e, 4.0);

    }

    @Test
    public void getCircuit(){
        MostEfficientCircuitController ctrl = new MostEfficientCircuitController(company);
        Map<List<Location>, Double> test = new HashMap<>();
        double expDistance = map.getDistance(f, e) + map.getDistance(e, m) + map.getDistance(m, n) + map.getDistance(n, p) + map.getDistance(p, q) + map.getDistance(q, h) + map.getDistance(h, i) + map.getDistance(i, j) + map.getDistance(j, g) + map.getDistance(g, f);

        List<Location> result = new LinkedList<>();

        result.add(n);
        result.add(p);
        result.add(q);
        result.add(h);
        result.add(i);
        result.add(j);
        result.add(g);
        result.add(f);
        result.add(e);
        result.add(m);
        result.add(n);
        test.put(result, expDistance);

        Map<List<Location>, Double> mapResult = ctrl.getMostEfficientCircuit();

        for (List<Location> list: test.keySet()) {
            for (List<Location> list2 : mapResult.keySet()) {
                assertEquals(list.size(), list2.size());

                assertEquals(list.containsAll(list2), list2.containsAll(list));
            }
        }
    }


}
