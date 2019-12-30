package Graph;

import Utilities.Line;
import Utilities.Point;

import static processing.core.PApplet.println;
import static processing.core.PApplet.sqrt;

public class Edge {
    private Node _source;
    private Node _destination;
    private Line _line;
    private boolean _isNull;

    public Edge() {
        _isNull = true;
    }

    public Edge(Node source, Node destination) {
        _source = source;
        _destination = destination;
        _line = new Line(source.getPoint(), destination.getPoint());
        _isNull = false;
    }

    public void logger() {
        println("Src: ", _source.getUuid(), " Dst: ", _destination.getUuid());
        println(_source.getPoint().x(), _source.getPoint().y());
        println(_destination.getPoint().x(), _destination.getPoint().y());
    }

    public boolean isNull() {
        return _isNull;
    }

    public Node getSource() {
        return _source;
    }

    public Node getDestination() {
        return _destination;
    }

    public Line getLine() {
        return _line;
    }

    public float getLength() {
        float dX = _source.getPoint().x() - _destination.getPoint().x();
        float dY = _source.getPoint().y() - _destination.getPoint().y();
        return sqrt((dX * dX) + (dY * dY));
    }

    public Point getMidPoint() {
        float dX = (_source.getPoint().x() + _destination.getPoint().x()) / 2;
        float dY = (_source.getPoint().y() + _destination.getPoint().y()) / 2;
        return new Point(dX, dY);
    }
}
