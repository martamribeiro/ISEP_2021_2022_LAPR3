package lapr.project.genericDataStructures;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KDTreeTest {
    KDTree.Node<Object> node1;
    KDTree.Node<Object> node2;
    KDTree.Node<Object> node3;
    KDTree.Node<Object> node4;
    KDTree.Node<Object> node5;

    Object element1;
    Object element2;
    Object element3;
    Object element4;
    Object element5;

    @BeforeEach
    public void setUp() {
        element1 = new Object();
        element2 = new Object();
        element3 = new Object();
        element4 = new Object();
        element5 = new Object();

        node1 = new KDTree.Node<>(element1, 0, 0);
        node2 = new KDTree.Node<>(element2, 10, 10);
        node3 = new KDTree.Node<>(element3, 0, 10);
        node4 = new KDTree.Node<>(element4, 10, 0);
        node5 = new KDTree.Node<>(element5, 20, 20);
    }


    @Test
    void insert() {
        KDTree<Object> tree = new KDTree<>();
        tree.insert(element1, 0, 0);
        tree.insert(element2, 10, 10);
        tree.insert(element3, 0, 10);
        tree.insert(element4, 10, 0);
        tree.insert(element5, 20, 20);

        Object node = tree.findNearestNeighbour(0, 0);
        assertEquals(element1, node);

        node = tree.findNearestNeighbour(8, 8);
        assertEquals(element2, node);

        node = tree.findNearestNeighbour(0, 8);
        assertEquals(element3, node);

        node = tree.findNearestNeighbour(9, 0);
        assertEquals(element4, node);

        node = tree.findNearestNeighbour(20, 20);
        assertEquals(element5, node);
    }

    @Test
    public void testBalanceTree() {
        List<KDTree.Node<Object>> nodes = new ArrayList<>();

        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);

        KDTree<Object> tree = new KDTree<>(nodes);

        Object Object = tree.findNearestNeighbour(0, 0);
        assertEquals(node1.getElement(), Object);

        //errado
        Object = tree.findNearestNeighbour(8, 8);
        assertEquals(node2.getElement(), Object);

        /*Object = tree.findNearestNeighbour(0, 8);
        assertEquals(node3.getElement(), Object);

        Object = tree.findNearestNeighbour(9, 0);
        assertEquals(node4.getElement(), Object);

         */

        Object = tree.findNearestNeighbour(20, 20);
        assertEquals(node5.getElement(), Object);

    }




}