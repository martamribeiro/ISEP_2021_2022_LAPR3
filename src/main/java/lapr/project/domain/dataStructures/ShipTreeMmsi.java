package lapr.project.domain.dataStructures;

import lapr.project.domain.model.Ship;
import lapr.project.domain.model.ShipSortMmsi;
import lapr.project.domain.shared.Constants;
import lapr.project.utils.ShipTotalMovementsComparator;
import lapr.project.utils.ShipTravelledDistanceComparator;

import java.util.*;

public class ShipTreeMmsi extends ShipTree{

    @Override
    public Ship createShip(PositionsBST positionsBST, int MMSI, String vesselName, String IMO, String callSign, int vesselTypeID, int length, int width, double draft, String cargo) {
        return new ShipSortMmsi(positionsBST, MMSI, vesselName, IMO, callSign, vesselTypeID, length, width, draft, cargo);
    }

    @Override
    public Ship getShip(String codShip) {
        Ship ship = find(new ShipSortMmsi(Integer.parseInt(codShip)));
        return ship;
    }


    /**
     * Method which calls its private method to obtain
     * the ship chosen by the user
     *
     * @param mmsiCode the MMSI code
     *
     * @return Ship chosen by the user
     */
    public Ship getShipByMmsiCode(int mmsiCode)  {
        return getShip(String.valueOf(mmsiCode));
    }


    /**
     * Method to get all the ships within the Base Date Time gap
     * @param initialDate initial Base Date Time
     * @param finalDate final Base Date Time
     */
    private void getShipsByDate(Node<Ship> node, Date initialDate, Date finalDate, List<Ship> shipList) {
        if(node==null) return;

        if (!shipList.contains(node.getElement())){
            if(node.getElement().getPositionsBST().getShipDate(node.getElement().getMmsi()).after(initialDate)
                    && node.getElement().getPositionsBST().getShipDate(node.getElement().getMmsi()).before(finalDate)){

                shipList.add(node.getElement());

            }
        }
        getShipsByDate(node.getLeft(), initialDate, finalDate, shipList);
        getShipsByDate(node.getRight(), initialDate, finalDate, shipList);

    }

    public List<Ship> getShipsByDate(Date initialDate, Date finalDate) {

        List<Ship> shipList = new ArrayList<>();
        getShipsByDate(root, initialDate, finalDate, shipList);
        return shipList;
    }

    /**
     * method to sort the ships with the most km travelled
     * @param shipList list with the ships belonging in the time gap
     */
    public List<Ship> sortNShips(List<Ship> shipList) {

        ShipTravelledDistanceComparator comparator = new ShipTravelledDistanceComparator();


        Collections.sort(shipList, comparator);
        return shipList;

    }


    /**
     * method to get the map with the ships associated by VesselType and sorted
     * @param listShip list with the sorted ships by most km travelled
     * @return map with the ships associated by VesselType and sorted
     */
    public Map<Integer, Map<Ship, Set<Double>>> getShipWithMean(List<Ship> listShip, int number) {
        Map<Integer,  Map<Ship, Set<Double>>> map = new HashMap<>();
        Map<Ship, Set<Double>> shipMap ;
        Set<Double> setter ;
        Integer vessel = null;

        listShip = sortNShips(listShip);

        for (Ship x: listShip) {
            if (!map.containsKey(x.getVesselTypeID())){
                shipMap = new HashMap<>();
                vessel = x.getVesselTypeID();
                setter = new HashSet<>();
                setter.add(x.getPositionsBST().getTotalDistance());
                setter.add(x.getPositionsBST().getMeanSog());

                shipMap.put(x, setter);
                map.put(vessel, shipMap);

            } else {
                shipMap = map.get(x.getVesselTypeID());
                setter = new HashSet<>();
                setter.add(x.getPositionsBST().getTotalDistance());
                setter.add(x.getPositionsBST().getMeanSog());
                if (shipMap.size() <= number) {
                    shipMap.put(x, setter);
                }
            }
        }

        return map;


    }

    /**
     * method to get all the ships in the BST
     * @return list with all the ships in the BST
     */
    public List<Ship> getAllShips() {
        List<Ship> allShip = new ArrayList<>();

        getAllShips(root, allShip);

        return allShip;
    }

    public void getAllShips(Node<Ship> node, List<Ship> list) {
        if (node == null) return;

        if (!list.contains(node.getElement())){
            list.add(node.getElement());
            getAllShips(node.getRight(), list);
            getAllShips(node.getLeft(), list);
        }
    }

    /**
     * method to get the MMSI, Total Travelled Distance, Delta Distance and Total Movements
     * of all the ships sorted By Total Travelled Distance
     * @return map with MMSI, Total Travelled Distance, Delta Distance and Total Movements
     * of all the ships sorted By Total Travelled Distance
     */
    public Map<Integer, Set<Double>> sortedByTravelledDistance() {
        Map<Integer, Set<Double>> mapByTravelled = new LinkedHashMap<>();
        List<Ship> list = getAllShips();
        sortedByTravelledDistance(mapByTravelled, list);
        return mapByTravelled;
    }

    private void sortedByTravelledDistance(Map<Integer, Set<Double>> map, List<Ship> list) {
        ShipTravelledDistanceComparator comparator = new ShipTravelledDistanceComparator();
        Collections.sort(list, comparator);

        for (Ship x : list) {
            if (!map.containsKey(x.getMmsi())) {
                Set<Double> setter = new LinkedHashSet<>();
                setter.add(x.getPositionsBST().getDeltaDistance());
                setter.add(x.getPositionsBST().getTotalDistance());
                setter.add((double) x.getPositionsBST().size());
                map.put(x.getMmsi(), setter);
            }
        }
    }

    /**
     * method to get the MMSI, Total Travelled Distance, Delta Distance and Total Movements
     * of all the ships sorted By Total Movements
     * @return map with MMSI, Total Travelled Distance, Delta Distance and Total Movements
     *  of all the ships sorted By Total Movements
     */
    public Map<Integer, Set<Double>> sortedByTotalMovements() {
        Map<Integer, Set<Double>> mapByMovements = new LinkedHashMap<>();
        List<Ship> list = getAllShips();
        sortedByTotalMovements(mapByMovements, list);
        return mapByMovements;
    }

    private void  sortedByTotalMovements(Map<Integer, Set<Double>> map, List<Ship> list) {
        ShipTotalMovementsComparator comparator = new ShipTotalMovementsComparator();
        Collections.sort(list, comparator);

        for (Ship x : list) {
            if (!map.containsKey(x.getMmsi())) {
                Set<Double> setter = new HashSet<>();
                setter.add(x.getPositionsBST().getDeltaDistance());
                setter.add(x.getPositionsBST().getTotalDistance());
                setter.add((double) x.getPositionsBST().size());
                map.put(x.getMmsi(), setter);
            }
        }
    }


    /**
     * Method to obtain the pairs of ships.
     *
     * @return the intended pairs of ships.
     */
    public List<TreeMap<Double, String>> getPairsOfShips() {
        List<TreeMap<Double, String>> listPairsOfShips = new ArrayList<>();

        List<Ship> listShipsWithIntendedTD = (List<Ship>) getShipsInOrderWithIntendedTD(); //O(n)

        for (Ship ship : listShipsWithIntendedTD) { //O(n)
            TreeMap<Double, String> infoPair = new TreeMap<>(Collections.reverseOrder());

            PositionsBST positionsBST = ship.getPositionsBST();
            Double travelledDistance = positionsBST.getTotalDistance(); //O(n)
            int indexShip = listShipsWithIntendedTD.indexOf(ship);

            fillTreeMapForEachShip(listShipsWithIntendedTD, infoPair, travelledDistance, positionsBST, ship.getMmsi(), indexShip); //O(n^2)

            if(!infoPair.isEmpty())
                listPairsOfShips.add(infoPair);
        }
        return listPairsOfShips;
    }

    /**
     * Auxiliar method for getPairsOfShips to fill the ships with whom the current ship
     * is close to for Us107
     *
     * @param listShipsWithIntendedTD list of ships with TD >= 10
     * @param infoPair a map containing the info about the pair to be added to the final list
     * @param travelledDistance travelled distance of the 1st ship
     * @param positionsBST positions of the 1st ship
     * @param ship1MMSI mmsi code of the 1st ship
     * @param indexShip index of the first ship
     */
    public void fillTreeMapForEachShip(List<Ship> listShipsWithIntendedTD,
                                       TreeMap<Double, String> infoPair,
                                       Double travelledDistance,
                                       PositionsBST positionsBST,
                                       int ship1MMSI, int indexShip) {

        for (int j = indexShip+1; j < listShipsWithIntendedTD.size(); j++) { //O(n^2)

            Ship ship2 = listShipsWithIntendedTD.get(j);

            PositionsBST positionsBST2 = ship2.getPositionsBST();
            Double travelledDistance2 = positionsBST2.getTotalDistance(); //O(n)

            if(!Objects.equals(travelledDistance, travelledDistance2)) {
                Double arrivalDistance = positionsBST.getArrivalDistance(positionsBST2); //O(h)

                if(arrivalDistance <= Constants.LIMIT_COORDINATES) {
                    Double depDistance = positionsBST.getDepartureDistance(positionsBST2); //O(h)

                    if(depDistance <= Constants.LIMIT_COORDINATES) {
                        int numMovs = positionsBST.size()-1, numMovs2 = positionsBST2.size()-1; //O(n)
                        double diffTravDist = Math.abs(travelledDistance - travelledDistance2);
                        String allInfo = String.format("%-15d%-15d%-15.3f%-15.3f%-15d%-15.3f%-15d%-15.3f%n",
                                ship1MMSI, ship2.getMmsi(), arrivalDistance, depDistance, numMovs, travelledDistance, numMovs2, travelledDistance2);
                        infoPair.put(diffTravDist, allInfo);
                    }
                }
            }
        }
    }

    /**
     * Method which calls its private method to
     * obtain the list of ships in ascending order by the mmsi code
     * and with Travelled Distance >= 10
     *
     * @return the list of ships in ascending order by the mmsi code
     * and with Travelled Distance >= 10
     */
    public Iterable<Ship> getShipsInOrderWithIntendedTD() {
        List<Ship> listShipsWithIntendedTD = new ArrayList<>();
        getShipsInOrderWithIntendedTD(root, listShipsWithIntendedTD);
        return listShipsWithIntendedTD;
    }

    /**
     * Method to obtain the list of ships in ascending order by the mmsi code
     * and with Travelled Distance >= 10
     *
     * @return the list of ships in ascending order by the mmsi code
     * and with Travelled Distance >= 10
     */
    private void getShipsInOrderWithIntendedTD(Node<Ship> node, List<Ship> listShipsWithIntendedTD) {
        if(node == null) {
            return;
        }

        getShipsInOrderWithIntendedTD(node.getLeft(), listShipsWithIntendedTD);

        Double currentTravelledDistance = node.getElement().getTravelledDistance();

        if(currentTravelledDistance >= Constants.LIMIT_TRAVELLED_DISTANCE)
            listShipsWithIntendedTD.add(node.getElement());

        getShipsInOrderWithIntendedTD(node.getRight(), listShipsWithIntendedTD);
    }

    public boolean hasShip(Ship ship){
        List<Ship> allShip = (List<Ship>) inOrder();
        for (Ship ship1 : allShip) {
            if (ship1.equals(ship)){
                return true;
            }
        }
        return false;
    }
}
