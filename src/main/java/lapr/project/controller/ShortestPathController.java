package lapr.project.controller;

import lapr.project.domain.dataStructures.FreightNetwork;
import lapr.project.domain.model.Capital;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Location;
import lapr.project.domain.model.Port;
import lapr.project.genericDataStructures.graphStructure.Edge;
import lapr.project.genericDataStructures.graphStructure.Graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ShortestPathController {

    /**
     * Company instance of the session.
     */
    private final Company company;

    /**
     * Constructor for the controller.
     */
    public ShortestPathController(){
        this(App.getInstance().getCompany());
    }

    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public ShortestPathController(Company company){
        this.company=company;
    }

    /**
     * Get shortest path, given an origin, destination and path type.
     *
     * @param beg beginning location
     * @param end ending location
     * @param path type of path
     * @return a list with the locals of the path
     */
    public List<String> getShortestPath(Location beg, Location end, int path){
        //1: land path | 2: maritime path | 3: land or sea path
        if (path==1){
            return getShortestLandPath(beg,end);
        } else if (path==2){
            return getShortestMaritimePath(beg,end);
        } else if (path==3){
            return getShortestLandOrSeaPath(beg,end);
        }
        return null;
    }

    /**
     * Get the shortest land path given a beginning and ending location.
     * @param beg beginning location
     * @param end ending location
     * @return a list with the locals of the path
     */
    public List<String> getShortestLandPath(Location beg, Location end){
        FreightNetwork freightNetwork = this.company.getFreightNetwork();
        if (beg==end || beg==null || end==null){ //O(1)
            return null;
        }
        Graph<Location,Double> noPortsEdges = freightNetwork.getFreightNetwork().clone();
        Collection<Edge<Location,Double>> outgoingEdges;
        Collection<Edge<Location,Double>> incomingEdges;
        for (Location loc: noPortsEdges.vertices()) { //O(v)
            if (loc instanceof Port){ //O(1)
                incomingEdges=noPortsEdges.incomingEdges(loc);
                outgoingEdges=noPortsEdges.outgoingEdges(loc);
                for (Edge<Location,Double> edge:outgoingEdges) { //O(E)
                    if (edge.getVOrig() instanceof Port && edge.getVDest() instanceof Port)  //O(1)
                        noPortsEdges.removeEdge(edge.getVOrig(),edge.getVDest()); //O(1)
                }
                for (Edge<Location,Double> edge:incomingEdges) { //O(E)
                    if (edge.getVOrig() instanceof Port && edge.getVDest() instanceof Port)
                        noPortsEdges.removeEdge(edge.getVOrig(),edge.getVDest());
                }
            }
        }
        LinkedList<Location> result;
        List<String> strings = new ArrayList<>();
        String toAdd;
        result=freightNetwork.getShortestPath(noPortsEdges,beg,end); //Dijkstra O(V^2)
        if (!result.contains(beg) || !result.contains(end)){
            return null;
        }
        for (Location loc:result) {
            if (loc instanceof Port){
                toAdd="Port: " + ((Port) loc).getName();
                strings.add(toAdd);
            } else if (loc instanceof Capital){
                toAdd="Capital: " + ((Capital) loc).getName();
                strings.add(toAdd);
            }
        }
        return strings;
    }

    /**
     * Get the shortest maritime path given a beginning and ending location.
     * @param beg beginning location
     * @param end ending location
     * @return a list with the locals of the path
     */
    public List<String> getShortestMaritimePath(Location beg, Location end){
        FreightNetwork freightNetwork = this.company.getFreightNetwork();
        if (beg==null || end==null || beg==end || beg instanceof Capital || end instanceof Capital){
            return null;
        }
        Graph<Location,Double> onlyPorts = freightNetwork.getFreightNetwork().clone();
        Collection<Edge<Location,Double>> outgoingEdges;
        Collection<Edge<Location,Double>> incomingEdges;
        for (Location loc: onlyPorts.vertices()) {
            if (loc instanceof Capital){
                incomingEdges=onlyPorts.incomingEdges(loc);
                outgoingEdges=onlyPorts.outgoingEdges(loc);
                for (Edge<Location,Double> edge:outgoingEdges) {
                    onlyPorts.removeEdge(edge.getVOrig(),edge.getVDest());
                }
                for (Edge<Location,Double> edge:incomingEdges) {
                    onlyPorts.removeEdge(edge.getVOrig(),edge.getVDest());
                }
                onlyPorts.removeVertex(loc);
            }
        }
        LinkedList<Location> result;
        List<String> strings = new ArrayList<>();
        result=freightNetwork.getShortestPath(onlyPorts,beg,end);
        if (!result.contains(beg) || !result.contains(end)){
            return null;
        }
        String toAdd;
        for (Location loc:result) {
            toAdd="Port: " + ((Port) loc).getName();
            strings.add(toAdd);
        }
        return strings;
    }

    /**
     * Get the shortest land or sea path given a beginning and ending location.
     * @param beg beginning location
     * @param end ending location
     * @return a list with the locals of the path
     */
    public List<String> getShortestLandOrSeaPath(Location beg, Location end){
        FreightNetwork freightNetwork = this.company.getFreightNetwork();
        LinkedList<Location> result;
        List<String> strings = new ArrayList<>();
        String toAdd;
        if (beg==end || beg==null || end==null){
            return null;
        }
        result=freightNetwork.getShortestPath(freightNetwork.getFreightNetwork(),beg,end); //O(V^2)
        if (!result.contains(beg) || !result.contains(end)){
            return null;
        }
        for (Location loc:result) {
            if (loc instanceof Port){
                toAdd="Port: " + ((Port) loc).getName();
                strings.add(toAdd);
            } else if (loc instanceof Capital){
                toAdd="Capital: " + ((Capital) loc).getName();
                strings.add(toAdd);
            }
        }
        return strings;
    }

}
