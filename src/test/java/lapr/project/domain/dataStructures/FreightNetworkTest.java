package lapr.project.domain.dataStructures;

import lapr.project.domain.model.Capital;
import lapr.project.domain.model.Location;
import lapr.project.domain.model.Port;
import lapr.project.genericDataStructures.graphStructure.Algorithms;
import lapr.project.genericDataStructures.graphStructure.Edge;
import lapr.project.genericDataStructures.graphStructure.Graph;
import lapr.project.genericDataStructures.graphStructure.matrix.MatrixGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FreightNetworkTest {
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
        map = new FreightNetwork();

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

        map.addDistance(h, port1, 25.0);
        map.addDistance(j, port2, 23.0);

        map.addDistance(h, g, 1.0);
        map.addDistance(h, k, 1.0);
        map.addDistance(h, j, 4.0);
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

    }

    @Test
    void getOrderedCapitalsList() {

        Map<Capital, Integer> expMap = new LinkedHashMap<>();
        expMap.put(h, 5);
        expMap.put(k, 5);
        expMap.put(d, 4);
        expMap.put(g, 3);
        expMap.put(i, 3);
        expMap.put(j, 3);
        expMap.put(a, 2);
        expMap.put(b, 2);
        expMap.put(e, 2);
        expMap.put(f, 2);
        expMap.put(c, 1);

        List<Map.Entry<Capital, Integer>> expList = new ArrayList<>(expMap.entrySet());

        List<Map.Entry<Capital, Integer>> list = map.getOrderedCapitalsList();

        assertEquals(expList, list);

    }


    @Test
    void getCapitalsToColor() {
        Map<Capital, Integer> expMap = new LinkedHashMap<>();
        expMap.put(h, null);
        expMap.put(k, null);
        expMap.put(d, null);
        expMap.put(g, null);
        expMap.put(i, null);
        expMap.put(j, null);
        expMap.put(a, null);
        expMap.put(b, null);
        expMap.put(e, null);
        expMap.put(f, null);
        expMap.put(c, null);

        Map<Capital, Integer> capitalsToColor = new LinkedHashMap<>();
        map.fillCapitalsToColor(capitalsToColor);

        assertEquals(expMap, capitalsToColor);
    }

    @Test
    void colorMap() {
        Map<Capital, Integer> coloredCapitals = map.colorMap();

        Map<Capital, Integer> expMap = new LinkedHashMap<>();
        expMap.put(h, 0);
        expMap.put(k, 1);
        expMap.put(d, 0);
        expMap.put(g, 2);
        expMap.put(i, 1);
        expMap.put(j, 2);
        expMap.put(a, 1);
        expMap.put(b, 2);
        expMap.put(e, 0);
        expMap.put(f, 1);
        expMap.put(c, 1);

        assertEquals(expMap, coloredCapitals);

        int minimumColorsRequired = 3;
        int colorsInMap = new HashSet<>(coloredCapitals.values()).size();

        assertEquals(minimumColorsRequired, colorsInMap);

        //Testing capital H  | Borders: G, K, J, I, A
        Map<Capital, Integer> subMap = new LinkedHashMap<>(coloredCapitals);
        int colorCapitalH = coloredCapitals.get(h);
        List<Capital> subListCapitals = new ArrayList<>();
        subListCapitals.add(g);
        subListCapitals.add(k);
        subListCapitals.add(j);
        subListCapitals.add(i);
        subListCapitals.add(a);
        subMap.keySet().retainAll(subListCapitals);

        boolean containsColorH = subMap.containsValue(colorCapitalH);

        assertFalse(containsColorH);
        subListCapitals.clear();

        //Testing capital K | Borders: H, G, J, E, D
        subMap = new LinkedHashMap<>(coloredCapitals);
        int colorCapitalK = coloredCapitals.get(k);
        subListCapitals.add(h);
        subListCapitals.add(g);
        subListCapitals.add(j);
        subListCapitals.add(e);
        subListCapitals.add(d);
        subMap.keySet().retainAll(subListCapitals);

        boolean containsColorK = subMap.containsValue(colorCapitalK);

        assertFalse(containsColorK);
        subListCapitals.clear();

        //Testing capital D | Borders: C, B, I, K
        subMap = new LinkedHashMap<>(coloredCapitals);
        int colorCapitalD = coloredCapitals.get(d);
        subListCapitals.add(c);
        subListCapitals.add(b);
        subListCapitals.add(i);
        subListCapitals.add(k);
        subMap.keySet().retainAll(subListCapitals);

        boolean containsColorD = subMap.containsValue(colorCapitalD);

        assertFalse(containsColorD);
        subListCapitals.clear();

        //Testing capital G | Borders: K, H, F
        subMap = new LinkedHashMap<>(coloredCapitals);
        int colorCapitalG = coloredCapitals.get(g);
        subListCapitals.add(k);
        subListCapitals.add(h);
        subListCapitals.add(f);
        subMap.keySet().retainAll(subListCapitals);

        boolean containsColorG = subMap.containsValue(colorCapitalG);

        assertFalse(containsColorG);
        subListCapitals.clear();

        //Testing capital I | Borders: J, H, D
        subMap = new LinkedHashMap<>(coloredCapitals);
        int colorCapitalI = coloredCapitals.get(i);
        subListCapitals.add(j);
        subListCapitals.add(h);
        subListCapitals.add(d);
        subMap.keySet().retainAll(subListCapitals);

        boolean containsColorI = subMap.containsValue(colorCapitalI);

        assertFalse(containsColorI);
        subListCapitals.clear();

        //Testing capital J | Borders: I, K, H
        subMap = new LinkedHashMap<>(coloredCapitals);
        int colorCapitalJ = coloredCapitals.get(j);
        subListCapitals.add(i);
        subListCapitals.add(k);
        subListCapitals.add(h);
        subMap.keySet().retainAll(subListCapitals);

        boolean containsColorJ = subMap.containsValue(colorCapitalJ);

        assertFalse(containsColorJ);
        subListCapitals.clear();

        //Testing capital A | Borders: H, B
        subMap = new LinkedHashMap<>(coloredCapitals);
        int colorCapitalA = coloredCapitals.get(a);
        subListCapitals.add(h);
        subListCapitals.add(b);
        subMap.keySet().retainAll(subListCapitals);

        boolean containsColorA = subMap.containsValue(colorCapitalA);

        assertFalse(containsColorA);
        subListCapitals.clear();

        //Testing capital B | Borders: A, D
        subMap = new LinkedHashMap<>(coloredCapitals);
        int colorCapitalB = coloredCapitals.get(b);
        subListCapitals.add(a);
        subListCapitals.add(d);
        subMap.keySet().retainAll(subListCapitals);

        boolean containsColorB = subMap.containsValue(colorCapitalB);

        assertFalse(containsColorB);
        subListCapitals.clear();

        //Testing capital E | Borders: K, F
        subMap = new LinkedHashMap<>(coloredCapitals);
        int colorCapitalE = coloredCapitals.get(e);
        subListCapitals.add(k);
        subListCapitals.add(f);
        subMap.keySet().retainAll(subListCapitals);

        boolean containsColorE = subMap.containsValue(colorCapitalE);

        assertFalse(containsColorE);
        subListCapitals.clear();

        //Testing capital F | Borders: G, E
        subMap = new LinkedHashMap<>(coloredCapitals);
        int colorCapitalF = coloredCapitals.get(f);
        subListCapitals.add(g);
        subListCapitals.add(e);
        subMap.keySet().retainAll(subListCapitals);

        boolean containsColorF = subMap.containsValue(colorCapitalF);

        assertFalse(containsColorF);
        subListCapitals.clear();

        //Testing capital C | Borders: D
        subMap = new LinkedHashMap<>(coloredCapitals);
        int colorCapitalC = coloredCapitals.get(c);
        subListCapitals.add(d);
        subMap.keySet().retainAll(subListCapitals);

        boolean containsColorC = subMap.containsValue(colorCapitalC);

        assertFalse(containsColorC);
        subListCapitals.clear();
    }
    @Test
    public void getNetworkContinentsTest(){
        HashSet<String> exp = new HashSet<>();
        exp.add(continent1);
        exp.add(continent2);
        System.out.println(map.getNetworkContinents());
        assertEquals(exp, map.getNetworkContinents());
    }

    @Test
    public void getSubGraphByContinentTest(){
        map.addDistance(port1, port2, 12.0);
        map.addDistance(port3, port2, 12.0);
        map.addDistance(port4, port3, 12.0);
        map.addDistance(port1, port3, 12.0);
        map.addDistance(port1, port4, 12.0);

        Graph<Location, Double> expected = new MatrixGraph<>(false);
        expected.addVertex(port1);
        expected.addVertex(port2);
        expected.addVertex(port4);
        expected.addEdge(port1, port2, 12.0);
        expected.addEdge(port1, port4, 12.0);

        assertEquals(expected, map.getSubGraphByContinent(continent1));
    }

    @Test
    public void getClosenessPlacesByContinent(){
        Map<String, List<Map.Entry<Location, Double>>> result = map.closenessPlacesByContinent();
        Double expectedCapitalK=0.0;
        Graph<Location, Double> continentNetwork = Algorithms.minDistGraph(map.getSubGraphByContinent("Europe"), Double::compare, Double::sum);

        assert continentNetwork != null;
        for (Edge<Location, Double> edge : continentNetwork.incomingEdges(k)){
            expectedCapitalK += edge.getWeight();
        }

        System.out.println(map.closenessPlacesByContinent());
        expectedCapitalK = expectedCapitalK / (continentNetwork.vertices().size()-1);
        assertEquals(0, result.get("Europe").get(0).getValue(), "First value is 0 since port3 doesn't have any connection");
        assertEquals(expectedCapitalK, result.get("Europe").get(1).getValue(), "Second value should be capital k with 2.36 value");
    }

    @Test
    public void getCircuit(){
        Map<List<Location>, Double> resultMap = map.getTotalDistanceMinorCost();

        Map<List<Location>, Double> exptMap = new HashMap<>();

        List<Location> expList = new ArrayList<>();
        expList.add(i);
        expList.add(j);
        expList.add(h);
        expList.add(k);
        expList.add(d);
        expList.add(i);

        double expDistance= 8.0;
        exptMap.put(expList, expDistance);

        assertEquals(exptMap.size(), resultMap.size());

        for (List<Location> loc : exptMap.keySet()) {
            for (List<Location> local : resultMap.keySet()){
                assertEquals(loc.containsAll(local), local.containsAll(loc));
            }
        }

        for (Double weight: exptMap.values()){
            for (Double weight2: resultMap.values()) {
                assertEquals(weight, weight2);
            }
        }

    }
}