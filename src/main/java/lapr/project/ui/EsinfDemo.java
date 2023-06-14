package lapr.project.ui;

import lapr.project.controller.*;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Port;
import lapr.project.domain.model.Ship;
import lapr.project.dto.PortFileDTO;
import lapr.project.dto.ShipsFileDTO;
import lapr.project.utils.PortsFileUtils;
import lapr.project.utils.ShipsFileUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EsinfDemo {
    public static void main(String[] args) throws IOException, ParseException {
        Company comp = App.getInstance().getCompany();
        ImportShipsController importShipsController = new ImportShipsController(comp);
        ShipsFileUtils shipsFileUtils = new ShipsFileUtils();
        List<ShipsFileDTO> shipsOfFile = shipsFileUtils.getShipsDataToDto("data-ships&ports/bships.csv");
        for(ShipsFileDTO ship : shipsOfFile){
            importShipsController.importShipFromFile(ship);
        }

        //Search for a ships details by any code
        SearchShipController searchShipController = new SearchShipController();
        System.out.println(searchShipController.getShipInfoByAnyCode("211331640"));
        System.out.println(searchShipController.getShipInfoByAnyCode("IMO9193305"));
        System.out.println(searchShipController.getShipInfoByAnyCode("DHBN"));

        //Show the Positional Messages of a Ship
        //ATTENTION: The results will be put in a file named: "us103PositionalMessages"
        ShowPositionalMessagesController showMessagesController = new ShowPositionalMessagesController();
        boolean selectedShip = showMessagesController.isValidShip(366904940);
        boolean wantsPeriod = false;
        if(selectedShip) {
            Date initialDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("31/12/2020 01:16:00");
            if(wantsPeriod) {
                Date finalDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("31/12/2020 01:19:00");
                if(!finalDate.after(initialDate))
                    throw new UnsupportedEncodingException("The final date can't be before or equal to initial Date!");
                showMessagesController.showPositionalMessages(initialDate, finalDate);
            } else {
                showMessagesController.showPositionalMessages(initialDate, initialDate);
            }
        }

        //Make a summary of the ships movement:
        MovementSummaryController movementSummaryController = new MovementSummaryController();
        System.out.println(movementSummaryController.getShipMovementsSummary("303296000"));
        System.out.println(movementSummaryController.getShipMovementsSummary("367439390"));
        System.out.println(movementSummaryController.getShipMovementsSummary("211331640"));


        //For all ships return total movements, travelled distantance and delta distance ordered by traveledDistance and total number of movements
        AllShipMMSIController allShipMMSIController = new AllShipMMSIController();
        Map<Integer, Set<Double>> map = allShipMMSIController.sortedByTotalMovements();
        System.out.println();
        System.out.println("----------------------Sorted by Total Movements-------------------");
        for(Integer mmsi : map.keySet()){
            System.out.println(mmsi+":");
            System.out.println(map.get(mmsi));
            System.out.println();
        }

        Map<Integer, Set<Double>> mapTDistance = allShipMMSIController.sortedByTravelledDistance();
        System.out.println("----------------------Sorted by Travelled Distance----------------");
        for(Integer mmsi : mapTDistance.keySet()){
            System.out.println(mmsi+":");
            System.out.println(mapTDistance.get(mmsi));
            System.out.println();
        }

        //Get top N ships with most km made and its mean speed
        TopNController topNController = new TopNController();
        List<Ship> list = topNController.getShipsByDate(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("31/12/2020 1:00"),
                new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("31/12/2020 10:00"));

        Map<Integer, Map<Ship,Set<Double>>> topNMap = topNController.getShipWithMean(list, 5);
        for(Integer pos : topNMap.keySet()){
            System.out.println();
            System.out.println("VesselTypeId - " +pos+":");
            Map<Ship,Set<Double>> x = topNMap.get(pos);
            System.out.println("[Travelled Distance, MeanSOG]");
            for (Ship shi : x.keySet()) {
                System.out.println("MMSI - "+ shi.getMmsi());
                System.out.println(x.get(shi));

           }
        }

        //Get pair of ships to file.
        //ATTENTION: The results will be put in a file named: "us107ShowPairsOfShips"
        ShowPairsOfShipsController showPairsOfShipsController = new ShowPairsOfShipsController(comp);
        showPairsOfShipsController.getPairsOfShips();


        //import Ports from a file
        ImportPortsController importPortsController = new ImportPortsController(comp);
        PortsFileUtils portsFileUtils = new PortsFileUtils();
        List<PortFileDTO> portsOfFile = portsFileUtils.getPortsDataToDto("data-ships&ports/bports.csv");
        for(PortFileDTO port : portsOfFile){
            importPortsController.importPortFromFile(port);
        }

        importPortsController.balancePorts2DTree();
        List<Port> listOfBalancedPorts = comp.getPortStore().getPorts2DTree().getAll();
        System.out.println();
        for (Port port : listOfBalancedPorts) {
            System.out.println(port);
        }

        //find nearest Port to a ship
        NearestPortController nearestPortController = new NearestPortController(comp);
        Port nearesPort = nearestPortController.findClosestPort("DHBN", new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("31/12/2020 06:57"));
        System.out.println("--------------------------");
        System.out.println("Nearest Port:" + nearesPort);
    }
}
