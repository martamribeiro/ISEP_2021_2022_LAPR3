package lapr.project.domain.dataStructures;

import lapr.project.genericDataStructures.KDTree;
import lapr.project.domain.model.Port;

import java.util.ArrayList;
import java.util.List;

public class Ports2DTree extends KDTree<Port> {
    private final List<Node<Port>> listOfPortNodes = new ArrayList<>();

    public Ports2DTree() {
        root = null;
    }

    /**
     * Constructs a balanced Port 2D Tree
     * @param nodes the list of elements
     */
    public Ports2DTree(List<Node<Port>> nodes) {
        balanceTree(nodes);
    }

    /**
     * Add node to the list of nodes
     * @param port port
     * @param lat latitude of the port
     * @param lon longitude of the port
     */
    public boolean addNode(Port port, double lat, double lon) {
        Node<Port> node = new Node<>(port, lat, lon);
        if(!listOfPortNodes.contains(node)) {
            listOfPortNodes.add(node);
            return true;
        }
        return false;
    }

    /**
     * Returns the list of valid nodes to be inserted to to the balanced KD Tree.
     *
     * @return list of valid nodes to be inserted to to the balanced KD Tree
     */
    public List<Node<Port>> getListOfPortNodes() {
        return listOfPortNodes;
    }

    /**
     * Returns the nearest Port from a given Ship's coordinates
     * @param coordinates List with the latitude and longitude of a certain Ship
     * @return nearest Port
     */
    public Port findClosestPort(List<Double> coordinates) {
        double x = coordinates.get(0);
        double y = coordinates.get(1);
        return findNearestNeighbour(x, y);
    }
}
