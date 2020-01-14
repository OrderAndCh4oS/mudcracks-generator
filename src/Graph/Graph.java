package Graph;

import Exceptions.NotFoundException;
import Utilities.Line;
import Utilities.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static javafx.application.Platform.exit;
import static processing.core.PApplet.println;

public class Graph {
    private Edge _longestEdge;
    private ArrayList<Node> _nodes = new ArrayList<>();
    private ArrayList<Edge> _edges = new ArrayList<>();
    private String _uuid;
    private int _colour = 0;

    public Graph() {
        _uuid = UUID.randomUUID().toString();
    }

    public Graph(int colour) {
        this();
        _colour = colour;
    }

    public Graph(List<Node> nodes) {
        this();
        _nodes = new ArrayList<>(nodes);
    }

    public String getUuid() {
        return _uuid;
    }

    public void addNode(Node node) {
        _nodes.add(node);
    }

    public void addConnection(String srcUuid, String destinationUuid) {
        try {
            Node sourceNode = findNode(srcUuid);
            Node destinationNode = findNode(destinationUuid);
            this.addConnection(sourceNode, destinationNode);
        } catch (NotFoundException e) {
            println(e.getMessage());
            exit();
        }
    }

    public void addConnection(Node sourceNode, Node destinationNode) {
        if (!hasConnection(sourceNode, destinationNode)) {
            Edge edge = new Edge(sourceNode, destinationNode);
            sourceNode.addEdge(edge);
            destinationNode.addEdge(edge);
            _edges.add(edge);
        }
    }

    public boolean hasConnection(Node source, Node destination) {
        for (Edge e : _edges) {
            if (e.getSource() == source && e.getDestination() == destination) {
                return true;
            }
        }

        return false;
    }

    public boolean hasEdges() {
        return _edges.size() > 0;
    }

    public boolean hasNodes() {
        return _nodes.size() > 0;
    }

    public ArrayList<Node> getNodes() {
        return _nodes;
    }

    public ArrayList<Edge> getEdges() {
        return _edges;
    }

    public Node findNode(String uuid) throws NotFoundException {
        for (Node n : _nodes) {
            if (n.getUuid().equals(uuid)) return n;
        }

        throw new NotFoundException("Graph.Node not found");
    }

    public Edge getLongestEdge() {
        return _longestEdge;
    }

    public Edge findLongestEdge() {
        Edge longestEdge = _edges.get(0);
        for (Edge e : _edges) {
            if (e.getLength() > longestEdge.getLength()) {
                longestEdge = e;
            }
        }
        _longestEdge = longestEdge;
        return longestEdge;
    }

    public void findLargestLoop() {
        for (Node n : _nodes) {
            float lastAngle = 10;
            for (Map.Entry<Float, Edge> entry : n.getEdgesSortedByAngle().entrySet()) {
                // Todo: Do something with sorted edges.
                //       Navigate around the edges via the clockwise/anticlockwise most points
                //       Either the edge will terminate at a node with a no more edges or it'll
                //       Reach the starting node, at which point it's created a loop.
//                println(entry.getKey());
            }
        }
    }


    // Todo: workout what's happening here
    public void splitLongestEdge() {
        if (_longestEdge == null) {
            return;
        }
        Point midPoint = _longestEdge.getMidPoint();
        Node mid = new Node(midPoint);
        Node source = _longestEdge.getSource();
        Node destination = _longestEdge.getDestination();
        addConnection(source, mid);
        addConnection(mid, destination);
        removeEdge(_longestEdge);
    }

    public void splitEdge(Edge edge, Node splitPoint) {
        Node source = edge.getSource();
        Node destination = edge.getDestination();
        addConnection(source, splitPoint);
        addConnection(splitPoint, destination);
        removeEdge(edge);
    }

    public void removeEdge(Edge edge) {
        _edges.removeIf(e ->
            e.getSource().getUuid().equals(edge.getSource().getUuid())
                && e.getDestination().getUuid().equals(edge.getDestination().getUuid())
        );
    }

    public Edge findIntersect(Line line) {
        for (Edge e : _edges) {
            if (e.getLine().isIntersect(line)) {
                return e;
            }
        }
        return new Edge();
    }

    public boolean mergeGraph(Graph graph) {
        if (graph.getUuid().equals(_uuid)) {
            return false;
        }
        _nodes.addAll(graph.getNodes());
        _edges.addAll(graph.getEdges());

        return true;
    }

    public void logger() {
        println("Graph");
        println("UUID: ", _uuid);
        for (Edge e : _edges) {
            e.logger();
        }
    }

    public int getColour() {
        return _colour;
    }
}
