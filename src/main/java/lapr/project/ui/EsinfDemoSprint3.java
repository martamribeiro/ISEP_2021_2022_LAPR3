package lapr.project.ui;

import lapr.project.controller.*;
import lapr.project.data.dataControllers.CreateFreightNetworkController;
import lapr.project.domain.dataStructures.FreightNetwork;
import lapr.project.domain.model.*;
import lapr.project.dto.PortFileDTO;
import lapr.project.dto.ShipsFileDTO;
import lapr.project.utils.PortsFileUtils;
import lapr.project.utils.ShipsFileUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EsinfDemoSprint3 {
    public static void main(String[] args) throws IOException, ParseException {
        Company comp = App.getInstance().getCompany();
        CreateFreightNetworkController createFreightNetworkController = new CreateFreightNetworkController(comp);
        //US 301: PRINTING FREIGHT NETWORK TO FILE;
        createFreightNetworkController.createFreightNetworkFromDb();
        FreightNetwork freightNetwork = comp.getFreightNetwork();
        try(PrintWriter out = new PrintWriter("FreightNet.txt")){
            out.println(freightNetwork.getFreightNetwork());
        }

        System.out.println(freightNetwork.getFreightNetwork().numEdges());

        //US302
        ColorMapController controller = new ColorMapController(comp);
        Map<Capital, Integer> map = controller.colorMap();
        System.out.println("### ALL THE CAPITALS AND ITS COLORS ###");
        for(Capital c : map.keySet()){
            System.out.print("Capital: " + c.getName());
            System.out.print(" Color: " + map.get(c));
            System.out.println();
        }

        System.out.println("\n### EACH CAPITAL AND BORDERS AND THEIR COLORS ###");
        for (Capital c : map.keySet()) {
            System.out.println("Capital: " + c.getName() + "| Color: " + map.get(c));
            for (Capital cap : map.keySet()) {
                if(freightNetwork.getFreightNetwork().adjVertices(c).contains(cap)) {
                    System.out.println("Border: " + cap.getName() + " | Color: " + map.get(cap));
                }
            }
            System.out.println();
        }

        System.out.println();
        int colorsInMap = new HashSet<>(map.values()).size();
        System.out.println("NUMBERS OF COLORS USED: " + colorsInMap);


        //US303
        ClosenessPlacesController closenessPlacesController = new ClosenessPlacesController(comp);
        Map<String, List<Map.Entry<Location, Double>>> closenessMap = closenessPlacesController.getClosenessPlacesByContinent();
        System.out.println();
        for (String cont : closenessMap.keySet()){
            System.out.println(cont+":");
            System.out.println(closenessMap.get(cont));
        }
    }
}
