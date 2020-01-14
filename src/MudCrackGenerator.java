import Graph.Edge;
import Graph.Graph;
import Graph.Node;
import Utilities.PathTracer;
import Utilities.Point;

import java.util.ArrayList;
import java.util.ListIterator;

import static Utilities.Random.random;
import static java.lang.Math.PI;
import static processing.core.PApplet.atan2;

public class MudCrackGenerator {
    private ArrayList<Graph> _graphList = new ArrayList<>();
    private ArrayList<PathTracer> _activePathTracers = new ArrayList<>();
    private ArrayList<PathTracer> _nextPathTracers = new ArrayList<>();

    MudCrackGenerator() {
        for (int i = 0; i < 3; i++) {
            Node node = new Node(new Point(random(400) + 50, random(400) + 50));
            Graph graph = startGraph(node);
            preparePathTracer(graph, node, random((float) PI * 2));
        }
        startNextPathTracers();
    }

    public ArrayList<Graph> getGraphList() {
        return _graphList;
    }

    void preparePathTracer(Graph graph, Node node, float angle) {
        PathTracer pt = new PathTracer(graph, node, angle);
        _nextPathTracers.add(pt);
    }

    void startNextPathTracers() {
        _activePathTracers.addAll(_nextPathTracers);
        _nextPathTracers = new ArrayList<>();
    }

    Graph startGraph(Node node) {
        Graph graph = new Graph();
        graph.addNode(node);
        addGraph(graph);
        return graph;
    }

    void addGraph(Graph graph) {
        _graphList.add(graph);
    }

    Graph getGraphWithLongestEdge() {
        // Todo: workout why this causes a crash
        //       Possibly could use a list iterator instead... or maybe not.
        Graph g = _graphList.get(0);
        Edge longestEdge = g.findLongestEdge();
        int graphIndex = 0;
        for (int i = 1; i < _graphList.size(); i++) {
            g = _graphList.get(i);
            if (g.hasEdges()) {
                Edge currentEdge = g.findLongestEdge();
                if (currentEdge.getLength() > longestEdge.getLength()) {
                    longestEdge = currentEdge;
                    graphIndex = i;
                }
            }
        }

        return _graphList.get(graphIndex);
    }

    float getNewAngle(Edge longestEdge) {
        float newAngle = atan2(
            longestEdge.getSource().getPoint().y() - longestEdge.getDestination().getPoint().y(),
            longestEdge.getSource().getPoint().x() - longestEdge.getDestination().getPoint().x()
        );
        if (random(1) > 0.5) {
            newAngle += PI / 2;
        } else {
            newAngle -= PI / 2;
        }
        return newAngle;
    }

    void prepareNewPathTracer() {
        Graph graph = getGraphWithLongestEdge();
        graph.splitLongestEdge();
        Edge longestEdge = graph.getLongestEdge();
        Node midPointNode = new Node(longestEdge.getMidPoint());
        float newAngle = getNewAngle(longestEdge);
        preparePathTracer(graph, midPointNode, newAngle);
    }

    void update() {
        ListIterator<PathTracer> iter = _activePathTracers.listIterator();
        while (iter.hasNext()) {
            PathTracer pt = iter.next();
            pt.createNextNode();

            if (!pt.isInBounds()) {
                prepareNewPathTracer();
                iter.remove();
                continue;
            }

            ListIterator<Graph> graphIter = _graphList.listIterator();
            while (graphIter.hasNext()) {
                Graph g = graphIter.next();
                // Todo: move findLargestLoop() where it will be needed. Probably startPathTracer.
                g.findLargestLoop();
                Edge intersectEdge = g.findIntersect(pt.getLine());
                if (doesIntersectAndNotFirstEdge(pt, intersectEdge)) {
                    handlePathIntersect(pt, graphIter, g, intersectEdge);
                    prepareNewPathTracer();
                    iter.remove();
                    break;
                }
            }
            //pt.getGraph().logger();

            Node nextNode = pt.getCurrentNode();
            pt.getGraph().addNode(nextNode);
            pt.getGraph().addConnection(pt.getLastNode(), nextNode);
        }
        startNextPathTracers();
    }

    private boolean doesIntersectAndNotFirstEdge(PathTracer pt, Edge intersectEdge) {
        return !intersectEdge.isNull()
            && pt.getLastPoint().x() != intersectEdge.getDestination().getPoint().x()
            && pt.getLastPoint().x() != intersectEdge.getSource().getPoint().x();
    }

    private void handlePathIntersect(PathTracer pt, ListIterator<Graph> graphIter, Graph g, Edge intersectEdge) {
        Node intersectNode = splitEdgeAtIntersect(pt, g, intersectEdge);
        pt.terminateNode(intersectNode);
        mergeGraphs(pt, graphIter, g);
    }

    private void mergeGraphs(PathTracer pt, ListIterator<Graph> graphIter, Graph g) {
        boolean didMergeGraph = pt.getGraph().mergeGraph(g);
        if (didMergeGraph) {
            updateActivePathTracerGraphs(pt, g);
            graphIter.remove();
        }
    }

    private Node splitEdgeAtIntersect(PathTracer pt, Graph g, Edge intersectEdge) {
        Point intersect = intersectEdge.getLine().getIntersect(pt.getLine());
        Node intersectNode = new Node(intersect);
        g.splitEdge(intersectEdge, intersectNode);
        return intersectNode;
    }

    void updateActivePathTracerGraphs(PathTracer pt, Graph g) {
        for (PathTracer pt2 : _activePathTracers) {
            if (pt2.getGraph().getUuid().equals(g.getUuid())) {
                pt2.setGraph(pt.getGraph());
            }
        }
    }
}