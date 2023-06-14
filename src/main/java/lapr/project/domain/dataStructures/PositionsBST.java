package lapr.project.domain.dataStructures;

import lapr.project.genericDataStructures.BST;
import lapr.project.domain.model.ShipPosition;
import lapr.project.domain.shared.Constants;
import lapr.project.utils.DistanceUtils;

import java.util.*;

public class PositionsBST extends BST<ShipPosition> {

    public Date getStartDate(){
        if(isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }
        return smallestElement().getBaseDateTime();
    }

    public Date getEndDate(){
        if(isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }
        return biggestElement().getBaseDateTime();
    }

    public Double getMaxSog(){
        if(isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }
        List<ShipPosition> allPos = (List<ShipPosition>) inOrder();
        double max = allPos.get(0).getSog();
        for(ShipPosition pos : allPos){
            if(pos.getSog() > max)
                max=pos.getSog();
        }
        return max;
    }

    public Double getMeanCog(){
        if(isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }
        List<ShipPosition> allPos = (List<ShipPosition>) inOrder();
        double mean=0;
        for(ShipPosition pos : allPos){
            mean += pos.getCog();
        }
        return (mean / (double) allPos.size());
    }

    public Double getMeanSog(){
        if(isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }
        List<ShipPosition> allPos = (List<ShipPosition>) inOrder();
        double mean=0;
        for(ShipPosition pos : allPos){
            mean += pos.getSog();
        }
        return (mean / (double) allPos.size());
    }

    public Double getDepartLatitude(){
        if(isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }
        return smallestElement().getLat();
    }

    public Double getDepartLongitude(){
        if(isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }
        return smallestElement().getLon();
    }

    public Double getArrivalLatitude(){
        if(isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }
        return biggestElement().getLat();
    }

    public Double getArrivalLongitude(){
        if(isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }
        return biggestElement().getLon();
    }

    public Double getDeltaDistance(){
        if(isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }
        ShipPosition start = smallestElement();
        ShipPosition end = biggestElement();
        return DistanceUtils.distanceBetweenInKm(start.getLat(), end.getLat(), start.getLon(), end.getLon());
    }

    public Double getTotalDistance(){
        if(isEmpty()){
            throw new IllegalArgumentException("List is empty");
        }
        List<ShipPosition> allPos = (List<ShipPosition>) inOrder();
        double totalDist = 0;
        for(int i = 0; i<allPos.size()-1; i++){
            double latA = allPos.get(i).getLat();
            double latB = allPos.get(i+1).getLat();
            double lonA = allPos.get(i).getLon();
            double lonB = allPos.get(i+1).getLon();
            totalDist += DistanceUtils.distanceBetweenInKm(latA, latB, lonA, lonB);
        }
        return totalDist;
    }


    /**
     * Method for getting the biggest element of the tree
     * @return the shipPosition element with the biggest(newest) date
     */
    public ShipPosition biggestElement(){
        return biggestElement(root);
    }

    /**
     * inner recursive method for getting the biggest element of the bst
     * @param node node of the tree to iterate
     * @return the biggest node in the tree
     */
    protected ShipPosition biggestElement(Node<ShipPosition> node){
        if(node == null){
            return null;
        }
        if(node.getRight() == null){
            return node.getElement();
        }
        return biggestElement(node.getRight());
    }

    /**
     * Method which call its private method in order to
     * obtain the list of positional messages of the ship chosen by the user.
     *
     * @param initialDate initial date
     * @param finalDate final date
     *
     * @return the list of positional messages of the ship chosen by the user
     */
    public List<String> getPositionalMessages(Date initialDate, Date finalDate) {
        List<String> listPositionalMessages = new ArrayList<>();

        getPositionalMessages(root, listPositionalMessages, initialDate, finalDate);

        return listPositionalMessages;
    }

    /**
     * Method for returning the positional messages of the ship
     * chosen by the user in the wished period of time.
     *
     * @param node the node of the Positions' Tree
     * @param listPositionalMessages list containing the positional messages
     * @param initialDate the initial Date
     * @param finalDate the final Date
     *
     */
    private void getPositionalMessages(Node<ShipPosition> node,
                                       List<String> listPositionalMessages,
                                       Date initialDate,
                                       Date finalDate) {

        if(node == null)
            return;

        getPositionalMessages(node.getLeft(), listPositionalMessages, initialDate, finalDate);

        Date currentBaseDateTime = node.getElement().getBaseDateTime();

        if( !(currentBaseDateTime.before(initialDate) || currentBaseDateTime.after(finalDate)) ) {
            listPositionalMessages.add(node.getElement().toString());
        }

        getPositionalMessages(node.getRight(), listPositionalMessages, initialDate, finalDate);

    }

    /**
     * method to get the Base Date Time of a ship by it's MMSI
     * @param shipMMSI ship's MMSI
     * @return Base Date Time
     */
    public Date getShipDate(int shipMMSI){
        Date shipDate = null;
        List<ShipPosition> allPos = (List<ShipPosition>) inOrder();

        for (ShipPosition pos : allPos) {
            if (pos.getMMSI() == shipMMSI) {
                shipDate = pos.getBaseDateTime();
            }
        }
        return shipDate;
    }

    /**
     * Method to obtain the arrival distance between two ships
     *
     * @param positionsBST2 positions of the 2nd ship
     * @return arrival distance between two ships
     */
    public Double getArrivalDistance(PositionsBST positionsBST2) {
        Double arrivalLat = this.getArrivalLatitude(); //Time Complexity to obtain the Latitudes and Longitudes: O(h)
        Double arrivalLog = this.getArrivalLongitude();

        Double arrivalLat2 = positionsBST2.getArrivalLatitude();
        Double arrivalLog2 = positionsBST2.getArrivalLongitude();

        return DistanceUtils.distanceBetweenInKm(arrivalLat, arrivalLat2, arrivalLog, arrivalLog2); //O(1)
    }

    /**
     * Method to obtain the departure distance between two ships
     *
     * @param positionsBST2 positions of the 2nd ship
     * @return departure distance between two ships
     */
    public Double getDepartureDistance(PositionsBST positionsBST2) {
        Double depLat = this.getDepartLatitude();
        Double depLog = this.getDepartLongitude();

        Double depLat2 = positionsBST2.getDepartLatitude();
        Double depLog2 = positionsBST2.getDepartLongitude();

        return DistanceUtils.distanceBetweenInKm(depLat, depLat2, depLog, depLog2);
    }

    /**
     * Method to check if a shipPosition already exists in the PositionsBST.
     * @param shipPosition the shipPosition to search in the PositionsBST.
     * @return true if the shipPosition already exists in the PositionsBST;
     * false if the shipPosition doesn't exist in the PositionsBST.
     */
    public boolean hasPosition(ShipPosition shipPosition){
        List<ShipPosition> allPos = (List<ShipPosition>) inOrder();
        for (ShipPosition pos : allPos) {
            if (pos.equals(shipPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to get the coordinates of a certain Ship given a Base Date Time
     * @param dateTime Base Date Time
     * @return List with latitude and longitude
     */
    public List<Double> getPosInDateTime(Date dateTime) {

        List<ShipPosition> allPos = (List<ShipPosition>) inOrder();
        List<Double> coordinates = new LinkedList<>();
        ShipPosition posi = new ShipPosition(dateTime);
        posi.setBaseDateTime(dateTime);

        ShipPosition coor = find(root, posi).getElement();
        coordinates.add(coor.getLat());
        coordinates.add(coor.getLon());

        return coordinates;
    }
}
