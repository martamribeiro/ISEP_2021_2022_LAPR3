package lapr.project.genericDataStructures;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * Generic Class for Kd Tree
 *
 * @author Ana Albergaria <1201518.isep0.ipp.pt>
 */

public class KDTree<T> {

    /** Nested static class for a 2d tree node. */
    public static class Node<T> {
        /**
         * a point with x and y coordinates
         */
        private Point2D.Double coords;
        /**
         * a reference to the left child (if any)
         */
        private Node<T> left;
        /**
         * a reference to the right child (if any)
         */
        private Node<T> right;
        /**
         * an element stored at this node
         */
        private T element;
        /**
         * Constructs a node with the given element and coordinates x and y.
         *
         * @param element  the element to be stored
         * @param x   coordinate x
         * @param y coordinate y
         */
        public Node(T element, double x, double y) {
            this.coords = new Point2D.Double(x,y);
            this.element = element;
        }

        /**
         * Returns the element of node
         * @return the element of node
         */
        public T getElement() {
            return element;
        }

        /**
         * Sets the element of node
         * @param element the element of node
         */
        public void setElement(T element) {
            this.element = element;
        }
        /**
         * Returns x coordinate
         * @return x coordinate
         */
        public Double getX() {
            return coords.x;
        }

        /**
         * Returns y coordinate
         * @return y coordinate
         */
        public Double getY() {
            return coords.y;
        }

        /**
         * Modifies the Coordinates of the Point in the Node.
         * @param x x coordinate
         * @param y y coordinate
         */
        public void setCoords(Double x, Double y) {
            this.coords = new Point2D.Double(x,y); }

        /**
         * Method which verifies if two nodes are equal
         * @param otherObject object to be compared to node
         * @return true if nodes are equal; otherwise returns false
         */
        @Override
        public boolean equals(Object otherObject){
            if(this == otherObject)
                return true;

            if(otherObject == null || this.getClass() != otherObject.getClass())
                return false;

            Node<T> otherNode = (Node<T>) otherObject;

            return this.coords.equals(otherNode.coords);
        }
    }
    //----------- end of nested Node class -----------

    /**
     * Comparator for the x coordinates
     */
    private final Comparator<Node<T>> cmpX = new Comparator<Node<T>>() {

        @Override
        public int compare(Node<T> p1, Node<T> p2) {
            return Double.compare(p1.getX(), p2.getX());
        }
    };
    /**
     * Comparator for the y coordinates
     */
    private final Comparator<Node<T>> cmpY = new Comparator<Node<T>>() {

        @Override
        public int compare(Node<T> p1, Node<T> p2) {
            return Double.compare(p1.getY(), p2.getY());
        }
    };

    /**
     * Root of the tree
     */
    protected Node<T> root;
    /**
     * Constructs an empty KD Tree
     */
    public KDTree() {
        root = null;
    }

    /**
     * Constructs a balanced KD Tree
     * @param nodes the list of elements
     */
    public KDTree(List<Node<T>> nodes) {
        balanceTree(nodes);
    }

    /**
     * Method to balance the KD Tree
     * @param nodes the list of elements
     */
    public void balanceTree(List<Node<T>> nodes) {
        root = balanceTree(true, nodes);
    }

    private Node<T> balanceTree(boolean divX, List<Node<T>> nodes) {
        if (nodes == null || nodes.isEmpty())//1
            return null;
        nodes.sort(divX ? cmpX : cmpY); //nlogn
        int median = nodes.size() / 2; //1
        Node<T> node = new Node<>(nodes.get(median).element, nodes.get(median).getX(), nodes.get(median).getY());//1
        node.left = balanceTree(!divX, nodes.subList(0, median)); //logn
        if (median + 1 <= nodes.size() - 1)
            node.right = balanceTree(!divX, nodes.subList(median+1, nodes.size())); //logn
        insert(node.getElement(),node.getX(),node.getY());
        return node;
    }



    /**
     * Finds the nearest neighbour
     * @param x x coordinate
     * @param y y coordinate
     * @return the nearest neighbour from point(x,y)
     */
    public T findNearestNeighbour(double x, double y) {
        Node<T> closestNode = new Node(null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        return findNearestNeighbour(root, x, y, closestNode, true);
    }

    private T findNearestNeighbour(Node<T> node, final double x, final double y, Node<T> closestNode, boolean divX) {

        if (node == null)
            return null;

        double d = Point2D.distanceSq(node.coords.x, node.coords.y, x, y);
        double closestDist = Point2D.distanceSq(closestNode.coords.x,
                closestNode.coords.y, x, y);

        if (closestDist > d) {
            closestNode.setElement(node.getElement());
            closestNode.setCoords(node.getX(),node.getY());
        }
        double delta = divX ? x - node.coords.x : y - node.coords.y;
        double delta2 = delta * delta;
        Node<T> node1 = delta < 0 ? node.left : node.right;
        Node<T> node2 = delta < 0 ? node.right : node.left;
        findNearestNeighbour(node1, x, y, closestNode, !divX);
        if (delta2 < closestDist) {
            findNearestNeighbour(node2, x, y, closestNode, !divX);
        }
        return closestNode.getElement();
    }


    /**
     * Inserts an element in the tree
     * @param element the element to be inserted
     * @param x x coordinate
     * @param y y coordinate
     */
    public void insert(T element, double x, double y) {
        Node<T> node = new Node<>(element, x, y);
        if (root == null)
            root = node;
        else
            insert(node, root, true);
    }

    private void insert(Node<T> node, Node<T> currentNode, boolean divX) {
        if (node == null)
            return;
        if(node.coords.equals(currentNode.coords)) //added so that there aren't duplicates
            return;
        int cmpResult = (divX ? cmpX : cmpY).compare(node, currentNode);
        if (cmpResult == -1)
            if(currentNode.left == null)
                currentNode.left = node;
            else
                insert(node, currentNode.left, !divX);
        else
        if(currentNode.right == null)
            currentNode.right = node;
        else
            insert(node, currentNode.right, !divX);
    }


    public List<T> getAll() {
        List<T> result = new LinkedList<>();
        getAll(result, root);
        return result;
    }

    private void getAll(List<T> result, Node<T> node) {
        if(node == null)
            return;
        result.add(node.getElement());
        if(node.left != null)
            getAll(result, node.left);
        if(node.right != null)
            getAll(result, node.right);
    }


}

