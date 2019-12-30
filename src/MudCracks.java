import Graph.Edge;
import Graph.Graph;
import Graph.Node;
import Utilities.Point;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class MudCracks extends PApplet {
    float width = 500;
    float height = 500;

    MudCrackGenerator mudCrackGenerator = new MudCrackGenerator();

    public static void main(String... args) {
        PApplet.main("MudCracks");
    }

    public void settings() {
        size((int) width, (int) height);
    }

    public void draw() {
        background(0xffffff);
        mudCrackGenerator.update();
        drawGraphList(mudCrackGenerator.getGraphList());
    }

    void drawPoint(Point p) {
        fill(0xffffff);
        ellipse(p.x(), p.y(), 3, 3);
    }

    void drawEdge(Edge e) {
        strokeWeight((float) 1.1);
        noFill();
        line(
            e.getSource().getX(),
            e.getSource().getY(),
            e.getDestination().getX(),
            e.getDestination().getY()
        );
    }

    void drawGraph(Graph g) {
        stroke(g.getColour());
        ArrayList<Node> drawnNodes = new ArrayList<>();
        for (Edge e : g.getEdges()) {
            drawEdge(e);
            // This is to prevent nodes being drawn multiple times
            // Workout a way to check it is working
            if (!drawnNodes.contains(e.getSource())) {
                drawPoint(e.getSource().getPoint());
                drawnNodes.add(e.getSource());
            }
            if (!drawnNodes.contains(e.getDestination())) {
                drawPoint(e.getDestination().getPoint());
                drawnNodes.add(e.getDestination());
            }
        }
        drawnNodes.clear();
    }

    void drawGraphList(List<Graph> graphList) {
        for (Graph g : graphList) {
            drawGraph(g);
        }
    }
}