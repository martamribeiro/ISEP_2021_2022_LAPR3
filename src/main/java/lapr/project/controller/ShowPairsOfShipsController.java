package lapr.project.controller;

import lapr.project.domain.dataStructures.ShipTreeMmsi;
import lapr.project.domain.model.Company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Controller class for showing the positional messages of a ship temporally organized
 * and associated with each of the ships
 *
 *  @author Ana Albergaria <1201518@isep.ipp.pt>
 *
 */
public class ShowPairsOfShipsController {
    /**
     * The company associated to the Controller.
     */
    private Company company;

    /**
     * Builds an empty constructor for having the actual instance of the company when instantiated.
     */
    public ShowPairsOfShipsController() {
        this(App.getInstance().getCompany());
    }


    /**
     * Builds a Show Pairs Of Ship's instance receiving the company.
     *
     * @param company company associated to the Controller.
     */
    public ShowPairsOfShipsController(Company company) {
        this.company = company;
    }

    public void getPairsOfShips() throws IOException {
        ShipTreeMmsi shipsBST = this.company.getShipStore().getShipsBstMmsi();
        List<TreeMap<Double, String>> listPairsOfShips = shipsBST.getPairsOfShips();

        String header = String.format("%-15s%-15s%-15s%-15s%-14s%-15s%-15s%-20s%n",
                "Ship1 MMSI", "Ship2 MMSI", "distOrig", "distDest","Movs1", "TravelDist1", "Movs2", "TravelDist2");

        File file = new File("us107ShowPairsOfShips.txt");
        if (!file.exists())
            file.createNewFile();

        FileWriter fw = new FileWriter(file, false);
        BufferedWriter bw = new BufferedWriter(fw);
        try {

            bw.write(header);
            for (TreeMap<Double, String> item : listPairsOfShips) {
                for (Map.Entry<Double, String> entry : item.entrySet()) {
                    bw.write(entry.getValue());
                }
            }

        } finally {
            bw.close();
            fw.close();
        }
    }




}
