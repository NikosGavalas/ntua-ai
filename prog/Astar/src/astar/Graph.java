package astar;

import java.util.ArrayList;
import java.util.HashSet;

public class Graph {
    private final ArrayList<Vertex> vertices;
    private final HashSet<Vertex> quickLookupSet;

    private int openSetWindow;

    public Graph(int openSetWindow) {
        this.openSetWindow = openSetWindow;
        this.vertices = new ArrayList<>(); // <-- size?
        this.quickLookupSet = new HashSet<>(); // <-- same

    }

    public void addVertex(Vertex v) {
        this.vertices.add(v);
        this.quickLookupSet.add(v);
    }

    private void debugPrintAllVertices() {
        this.vertices.forEach((v) -> {
            System.out.println(v);
        });
    }

    public boolean containsAlready(Vertex v) {
        return this.quickLookupSet.contains(v);
    }

    public Vertex getVertex(Vertex v) {
        return this.vertices.get(this.vertices.indexOf(v));
    }

    private Vertex findClosestVertex(Position pos) {
        Vertex closest = null;
        double runningMin = Double.MAX_VALUE;

        for (Vertex vertex : this.vertices) {
            double dist1 = vertex.distanceFrom(pos);

            if (dist1 < runningMin) {
                closest = vertex;
                runningMin = dist1;
            }
        }

        return closest;
    }

    private void intializeVertices() {
        for (Vertex vertex : this.vertices) {
            vertex.reset();
        }
    }

    public Path findPath(Position clientPos, Position targetPos) {
        Path path = new Path();

        Queue<Vertex> openSet = new Queue<>(this.openSetWindow);

        intializeVertices();

        System.out.println("Finding closest nodes....");
        clientPos = findClosestVertex(clientPos);
        targetPos = findClosestVertex(targetPos);

        Vertex temp = new Vertex(clientPos.getX(), clientPos.getY(), 0);
        Vertex start = this.getVertex(temp);
        start.updateGraphCost(0);

        System.out.println("Finding shortest path....");

        openSet.insert(start);

        while (!openSet.isEmpty()) {
            Vertex current = openSet.pop();
            current.markClosed();

            if (current.equals(targetPos)) {
                temp = current;
                break;
            }

            for (Vertex neighbor : current.getNeighbors()) {
                if (!neighbor.isMarkedClosed()) {
                    neighbor.setHeuristicCost(neighbor.distanceFrom(targetPos));

                    double tmp = current.getGraphCost() + current.distanceFrom(neighbor);
                    if (tmp < neighbor.getGraphCost()) {
                        neighbor.updateGraphCost(tmp);
                        neighbor.setParent(current);
                        openSet.update(neighbor); // currently O(n) but can become O(logn)
                    }

                    if (!neighbor.isAlreadyVisited()) {
                        openSet.insert(neighbor);
                        neighbor.markVisited();
                    }
                }
            }
        }

        System.out.println("Path found.");

        // temp is the found target
        path.setTotalDistance(temp.getGraphCost());

        while (!temp.equals(start)) {
            path.addRoute(temp);
            temp = temp.getParent();
        }

        path.addRoute(start);

        System.out.println("Total distance: " + path.getTotalDistance());
        return path;
    }
}