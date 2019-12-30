package Utilities;

import static processing.core.PApplet.max;
import static processing.core.PApplet.min;

public class Line {
    private Point _p, _q;

    public Line(Point p, Point q) {
        this._p = p;
        this._q = q;
    }

    public Point getIntersect(Line l2) {
        float a1 = _p.y() - _q.y();
        float b1 = _q.x() - _p.x();
        float c1 = a1 * _q.x() + b1 * _q.y();

        float a2 = l2.p().y() - l2.q().y();
        float b2 = l2.q().x() - l2.p().x();
        float c2 = a2 * l2.q().x() + b2 * l2.q().y();

        float delta = a1 * b2 - a2 * b1;
        return new Point((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta);
    }

    public boolean isIntersect(Line l2) {
        Point intersect = getIntersect(l2);
        return intersect.isFinite() && onSegment(intersect) && l2.onSegment(intersect);
    }

    public boolean onSegment(Point r) {
        return r.x() <= max(p().x(), q().x()) && r.x() >= min(p().x(), q().x()) &&
            r.y() <= max(p().y(), q().y()) && r.y() >= min(p().y(), q().y());
    }

    public float maxX() {
        return max(_p.x(), _q.x());
    }

    public float maxY() {
        return max(_p.y(), _q.y());
    }

    public float minX() {
        return min(_p.x(), _q.x());
    }

    public float minY() {
        return min(_p.y(), _q.y());
    }

    public Point p() {
        return _p;
    }

    public Point q() {
        return _q;
    }
}
