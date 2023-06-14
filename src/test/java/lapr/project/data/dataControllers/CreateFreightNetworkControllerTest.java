package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.dataControllers.CreateFreightNetworkController;
import lapr.project.domain.model.*;
import lapr.project.genericDataStructures.graphStructure.Edge;
import lapr.project.genericDataStructures.graphStructure.Graph;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CreateFreightNetworkControllerTest {

    @Test
    public void createFreightNetworkTest(){
        CreateFreightNetworkController createFreightNetworkController = spy(CreateFreightNetworkController.class);

        doNothing().when(createFreightNetworkController).importDataFromDatabase();

        //creating reduced scales ports and country for testing graph edge rules

        List<Country> ptSpList = new ArrayList<>();
        Capital lisb = new Capital("Lisbon", 38.71666667, -9.133333, "Portugal", "Europe");
        Port leix = new Port(12345,"Leixoes", "Europe", "Portugal", 41.18, -8.7);
        Map<Integer, Double> setubDists = new HashMap<>();
        setubDists.put(leix.getIdentification(), 298.6);
        Port setub = new Port(13122,"Setubal", "Europe", "Portugal", 38.5, -8.9166, setubDists);
        List<String> borderPt = new ArrayList<>();
        borderPt.add("Spain");
        Country pt = new Country("Europe", "Portugal", lisb, borderPt);
        ptSpList.add(pt);

        List<Port> portsPt = new ArrayList<>();
        portsPt.add(leix);
        portsPt.add(setub);

        App.getInstance().getCompany().getPortStore().importPorts(portsPt);

        Capital madrid = new Capital("Madrid", 40.4, -3.683333, "Spain", "Europe");
        List<String> borderSpain = new ArrayList<>();
        Country spain = new Country("Europe", "Spain", madrid, borderSpain);
        ptSpList.add(spain);

        App.getInstance().getCompany().getCountryStore().importCountriesList(ptSpList);

        createFreightNetworkController.createFreightNetworkFromDb();
        Graph<Location, Double> freightNetwork = App.getInstance().getCompany().getFreightNetwork().getFreightNetwork();

        for(Edge<Location, Double> edge : freightNetwork.incomingEdges(madrid)) {
            assertEquals(lisb, edge.getVOrig(), "Madrid should only be connected with lisbon");
        }

        List<Location> expLisb = new ArrayList<>();
        expLisb.add(madrid);
        expLisb.add(setub);

        Double distLisbSet = 30.5791; // calculated with http://www.movable-type.co.uk/scripts/latlong.html

        List<Location> resultList = new ArrayList<>();
        for (Edge<Location, Double> edge  : freightNetwork.incomingEdges(lisb)){
            resultList.add(edge.getVOrig());
        }
        assertEquals(distLisbSet, Math.round(freightNetwork.edge(lisb, setub).getWeight()), 4);
        assertEquals(expLisb, resultList, "Lisbon should be connected with Madrid and with setubal(closest port from capital");


        for(Edge<Location, Double> edge : freightNetwork.incomingEdges(leix)) {
            assertEquals(setub, edge.getVOrig(), "Leixoes should onyl connect with setubal");
        }

    }

    @Disabled("Database test")
    @Test
    public void createFreightNetworkFromDbTest(){
        CreateFreightNetworkController createFreightNetworkController = new CreateFreightNetworkController();
        assertTrue(createFreightNetworkController.createFreightNetworkFromDb());
        System.out.println(App.getInstance().getCompany().getFreightNetwork().getFreightNetwork());
    }

}