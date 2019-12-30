package Utilities;

import Graph.Graph;
import Graph.Node;

import static Utilities.Random.random;

public class PathTracer {
    private Graph _graph;
    private Node _lastNode;
    private Node _currentNode;
    private Vector _position;
    private Vector _velocity;
    private float _angle = 0;
    private float _speed = 10;

    public PathTracer(Graph graph, Node node) {
        _graph = graph;
        _currentNode = node;
        _lastNode = node;
        _position = new Vector(node.getPoint());
        _velocity = new Vector(0, 0);
        _velocity.setLength(_speed);
        _velocity.setAngle(_angle);
    }

    public PathTracer(Graph graph, Node node, float angle, float speed) {
        this(graph, node);
        _angle = angle;
        _speed = speed;
        _velocity.setLength(_speed);
        _velocity.setAngle(_angle);
    }

    public Graph getGraph() {
        return _graph;
    }

    public void setGraph(Graph graph) {
        _graph = graph;
    }

    public void createNextNode() {
        _lastNode = _currentNode;
        _angle += 0.2 - random((float) 0.4);
        _velocity.setAngle(_angle);
        _position.addTo(_velocity.multiply(random(_speed / 3 * 2) + _speed / 3));
        _currentNode = new Node(_position.getPoint());
    }

    public Node getCurrentNode() {
        return _currentNode;
    }

    public void terminateNode(Node intersectNode) {
        _currentNode = intersectNode;
    }

    public Node getLastNode() {
        return _lastNode;
    }

    public Point getPoint() {
        return _position.getPoint();
    }

    public Point getLastPoint() {
        return _lastNode.getPoint();
    }

    public Line getLine() {
        return new Line(_position.getPoint(), getLastPoint());
    }

    public boolean isInBounds() {
        return !(x() > 550 || x() < -50 || y() > 550 || y() < -50);
    }

    public float x() {
        return _position.x();
    }

    public float y() {
        return _position.y();
    }
}