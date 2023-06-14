package lapr.project.genericDataStructures.graphStructure;

import lapr.project.genericDataStructures.graphStructure.matrix.MatrixGraph;

import java.util.*;
import java.util.function.BinaryOperator;

/**
 *
 * @author DEI-ISEP
 *
 */
public class Algorithms {

    /** Performs breadth-first search of a Graph starting in a vertex
     *
     * @param g Graph instance
     * @param vert vertex that will be the source of the search
     * @return a LinkedList with the vertices of breadth-first search
     */
    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vert) {
        if(!g.validVertex(vert))
            return null;

        LinkedList<V> quaux = new LinkedList<>();
        LinkedList<V> qbls = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];
        quaux.add(vert); qbls.add(vert);
        int vkey = g.key(vert);
        visited[vkey] = true;

        while(!quaux.isEmpty()) {
            vert = quaux.remove();
            for(V vAdj : g.adjVertices(vert)) {
                vkey = g.key(vAdj);
                if(!visited[vkey]) {
                    quaux.add(vAdj);
                    visited[vkey] = true;
                    qbls.add(vAdj);
                }
            }
        }
        return qbls;
    }

    /** Performs depth-first search starting in a vertex
     *
     * @param g Graph instance
     * @param vOrig vertex of graph g that will be the source of the search
     * @param visited set of previously visited vertices
     * @param qdfs return LinkedList with vertices of depth-first search
     */
    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vOrig, boolean[] visited, LinkedList<V> qdfs) {
        visited[g.key(vOrig)] = true;

        for (V vertex : g.adjVertices(vOrig)) {
            if (! visited[g.key(vertex)]) {
                qdfs.add(vertex);
                DepthFirstSearch(g, vertex, visited, qdfs);
            }
        }
    }

    /** Performs depth-first search starting in a vertex
     *
     * @param g Graph instance
     * @param vert vertex of graph g that will be the source of the search

     * @return a LinkedList with the vertices of depth-first search
     */
    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {
        if (!g.validVertex(vert)) {
            return null;
        }

        LinkedList<V> resultQueue = new LinkedList<>();
        resultQueue.add(vert);
        boolean[] knownVertices = new boolean[g.numVertices()];

        DepthFirstSearch(g, vert, knownVertices, resultQueue);

        return resultQueue;
    }
    
    
    /** Returns all paths from vOrig to vDest
     *
     * @param g       Graph instance
     * @param vOrig   Vertex that will be the source of the path
     * @param vDest   Vertex that will be the end of the path
     * @param visited set of discovered vertices
     * @param path    stack with vertices of the current path (the path is in reverse order)
     * @param paths   ArrayList with all the paths (in correct order)
     */
    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest, boolean[] visited,
                                        LinkedList<V> path, ArrayList<LinkedList<V>> paths) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** Returns all paths from vOrig to vDest
     *
     * @param g     Graph instance
     * @param vOrig information of the Vertex origin
     * @param vDest information of the Vertex destination
     * @return paths ArrayList with all paths from vOrig to vDest
     */
    public static <V, E> ArrayList<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with non-negative edge weights
     * This implementation uses Dijkstra's algorithm
     *
     * @param g        Graph instance
     * @param vOrig    Vertex that will be the source of the path
     * @param visited  set of previously visited vertices
     * @param pathKeys minimum path vertices keys
     * @param dist     minimum distances
     */
    //resolução professora
    private static <V, E> void shortestPathDijkstra(Graph<V, E> g, V vOrig,
                                                    Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                                    boolean[] visited, V [] pathKeys, E [] dist) {
        int vKey = g.key(vOrig);
        dist[vKey] = zero; //vetor dos custos
        pathKeys[vKey] = vOrig; //vetor dos antecessores

        while(vOrig != null) {
            vKey = g.key(vOrig);
            visited[vKey] = true;
            for (Edge<V, E> edge : g.outgoingEdges(vOrig)) { //outgoingEdges(vOrig) -> ramos que partem do vértice origem
                int vKeyAdj = g.key(edge.getVDest());   //para obter cada vértice adjacente de cada ramo
                if(!visited[vKeyAdj]) {
                    E s = sum.apply(dist[vKey], edge.getWeight());
                    if(dist[vKeyAdj] == null || ce.compare(dist[vKeyAdj], s) > 0) {
                        dist[vKeyAdj] = s;
                        pathKeys[vKeyAdj] = vOrig;
                    }
                }

            }
            E minDist = null;   //next vertice, that has minimum dist
            vOrig = null;
            for (V vert : g.vertices()) {
                int i = g.key(vert);
                if(!visited[i] && dist[i] != null && (minDist == null || ce.compare(dist[i], minDist) < 0)) {
                    minDist = dist[i];
                    vOrig = vert;
                }
            }
        }
        //throw new UnsupportedOperationException("Not supported yet.");
    }


    /** Shortest-path between two vertices
     *
     * @param g graph
     * @param vOrig origin vertex
     * @param vDest destination vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param shortPath returns the vertices which make the shortest path
     * @return if vertices exist in the graph and are connected, true, false otherwise
     */
    //resolução professora
    public static <V, E> E shortestPath(Graph<V, E> g, V vOrig, V vDest,
                                        Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                        LinkedList<V> shortPath) {
        if(!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return null;
        }

        shortPath.clear();
        int numVerts = g.numVertices();
        boolean[] visited = new boolean[numVerts];
        @SuppressWarnings("unchecked")
        V[] pathkeys = (V[]) new Object[numVerts];
        @SuppressWarnings("unchecked")
        E[] dist = (E[]) new Object[numVerts];

        for (int i = 0; i < numVerts; i++) {
            dist[i] = null;
            pathkeys[i] = null;
        }

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathkeys, dist);

        E lengthPath = dist[g.key(vDest)]; //para saber se conseguimos alcançar o vértice destino

        if(lengthPath == null)
            return null;

        getPath(g, vOrig, vDest, pathkeys, shortPath);
        return lengthPath;
    }

    /** Shortest-path between a vertex and all other vertices
     *
     * @param g graph
     * @param vOrig start vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param paths returns all the minimum paths
     * @param dists returns the corresponding minimum distances
     * @return if vOrig exists in the graph true, false otherwise
     */
    //resolução professora
    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig,
                                               Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                               ArrayList<LinkedList<V>> paths, ArrayList<E> dists) {
        if(!g.validVertex(vOrig)) {
            return false;
        }

        dists.clear();
        paths.clear();

        int numVerts = g.numVertices();

        for (int i = 0; i < numVerts; i++) {
            dists.add(null);
            paths.add(null);
        }

        boolean[] visited = new boolean[numVerts];
        @SuppressWarnings("unchecked")
        V[] pathkeys = (V[]) new Object[numVerts];
        @SuppressWarnings("unchecked")
        E[] dist = (E[]) new Object[numVerts];

        for (int i = 0; i < numVerts; i++) {
            dist[i] = null;
            pathkeys[i] = null;
        }
        //dists -> cada índice é o vetor destino e conteúdo é o custo até esse vértice

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathkeys, dist);

        for (V vDest : g.vertices()) {
            int v = g.key(vDest);
            if(dist[v] != null) {
                LinkedList<V> shortPath = new LinkedList<>();
                getPath(g, vOrig, vDest, pathkeys, shortPath);
                paths.set(v, shortPath);
                dists.set(v, dist[v]);
            }
        }
        return true;
    }

    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf
     * The path is constructed from the end to the beginning
     *
     * @param g        Graph instance
     * @param vOrig    information of the Vertex origin
     * @param vDest    information of the Vertex destination
     * @param pathKeys minimum path vertices keys
     * @param path     stack with the minimum path (correct order)
     */
    private static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest,
                                       V [] pathKeys, LinkedList<V> path) {
        if(vDest.equals(vOrig)) {
            path.push(vDest);
        } else {
            path.push(vDest);
            int vKey = g.key(vDest);
            vDest = pathKeys[vKey];
            getPath(g,vOrig,vDest,pathKeys,path);
        }
    }

    /** Calculates the minimum distance graph using Floyd-Warshall
     * 
     * @param g initial graph
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @return the minimum distance graph
     */
    //NEED TO TEST THIS METHOD
    public static <V,E> MatrixGraph<V,E> minDistGraph(Graph <V,E> g, Comparator<E> ce, BinaryOperator<E> sum) {
        int numVerts = g.numVertices();
        if(numVerts == 0)
            return null;

        //COPYING THE GRAPH'S WEIGHTS TO A NEW MATRIX
        @SuppressWarnings("unchecked")
        E[][] mat = (E[][]) new Object[numVerts][numVerts];
        for (int i = 0; i < numVerts; i++) {
            for (int j = 0; j < numVerts; j++) {
                Edge<V,E> edge = g.edge(i,j);
                if(edge != null)
                    mat[i][j] = edge.getWeight();
            }
        }

        //OBTAIN THE MATRIX WITH LEAST COSTS BETWEEN EDGES
        for (int k = 0; k < numVerts; k++) {
            for (int i = 0; i < numVerts; i++) {
                if(i != k && mat[i][k] != null) {
                    for (int j = 0; j < numVerts; j++) {
                        if(j != k && j != i && mat[k][j] != null) {
                            E s = sum.apply(mat[i][k], mat[k][j]);
                            if(mat[i][j] == null || ce.compare(mat[i][j],s) > 0) {
                                mat[i][j] = s;
                            }
                        }
                    }
                }
            }
        }

/*        for (int k = 0; k < mat.length; k++) {
            for (int i = 0; i < mat.length; i++) {
                System.out.print(mat[k][i]);
                System.out.print(" ");
            }
            System.out.println();
        }*/
        return new MatrixGraph<>(g.isDirected(), g.vertices(), mat);

        //throw new UnsupportedOperationException("Not supported yet.");
    }

}