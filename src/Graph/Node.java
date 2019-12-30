package Graph;

import Utilities.Point;
import Utilities.Vector;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.UUID;

import static processing.core.PApplet.println;

public class Node {
    private Point _point;
    private ArrayList<Edge> _edges = new ArrayList<>();
    private String _uuid;

    private Node() {
        _uuid = UUID.randomUUID().toString();
    }

    public Node(float x, float y) {
        this();
        _point = new Point(x, y);
    }

    public Node(Point p) {
        this();
        try {
            _point = p.clone();
        } catch (CloneNotSupportedException e) {
            println("Cloning Point not supported.");
        }
    }

    public boolean equals(Node n) {
        return _uuid.equals(n.getUuid());
    }

    public float getX() {
        return _point.x();
    }

    public float getY() {
        return _point.y();
    }

    public String getUuid() {
        return _uuid;
    }

    public Point getPoint() {
        return _point;
    }

    public void addEdge(Edge edge) {
        _edges.add(edge);
    }

    public TreeMap<Float, Edge> getEdgesSortedByAngle() {
        TreeMap<Float, Edge> map = new TreeMap<>();
        for (Edge e : _edges) {
            float angle = getEdgeAngle(e);
            map.put(angle, e);
        }

        return map;
    }

    private float getEdgeAngle(Edge edge) {
        float angle;
        Vector v = new Vector(_point);
        if (edge.getSource().getUuid().equals(_uuid)) {
            angle = v.angleTo(new Vector(edge.getDestination().getPoint()));
        } else {
            angle = v.angleTo(new Vector(edge.getSource().getPoint()));
        }
        return angle;
    }
}
