package astar;

import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.System.exit;
import java.util.Scanner;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

public class Astar {
    public static void main(String[] args) throws TransformerException {
        int openSetWindow;
        if (args.length == 0 || args.length > 1) {
            // default size, if no argument is provided
            openSetWindow = 150000;
        } else {
            openSetWindow = Integer.parseInt(args[0]);
        }

        System.out.println("Loading client position...");
        Position client = null;
        try (Scanner scanner = new Scanner(new File("client.csv"))) {
            scanner.nextLine();

            String[] tokens = scanner.nextLine().split(",");
            double clientX = Double.parseDouble(tokens[0]);
            double clientY = Double.parseDouble(tokens[1]);

            client = new Position(clientX, clientY);
        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("Fatal: 'clients.csv' file: " + e.toString());
            exit(1);
        }
        System.out.println("Client position loaded: " + client);

        System.out.println("Loading taxis positions...");
        ArrayList<Taxi> taxis = new ArrayList<>(50);
        try (Scanner scanner = new Scanner(new File("taxis.csv"))) {
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(",");
                double posX = Double.parseDouble(tokens[0]);
                double posY = Double.parseDouble(tokens[1]);
                int taxiId = Integer.parseInt(tokens[2]);

                taxis.add(new Taxi(posX, posY, taxiId));
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("Fatal: 'taxis.csv' file: " + e.toString());
            exit(1);
        }
        System.out.println("Taxis positions loaded: " + taxis);

        System.out.println("Loading map vertices...");

        Graph g = new Graph(openSetWindow);

        int prevId = 0;
        Vertex prev = new Vertex(20.0, 20.0, 0); // Dummy Vertex
        try (Scanner scanner = new Scanner(new File("nodes.csv"))) {
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(",");

                double vertexX = Double.parseDouble(tokens[0]);
                double vertexY = Double.parseDouble(tokens[1]);
                int streetId = Integer.parseInt(tokens[2]);

                Vertex curr = new Vertex(vertexX, vertexY, streetId);

                if (tokens.length == 4)
                    curr.setName(tokens[3]);

                if (g.containsAlready(curr)) {
                    curr = g.getVertex(curr);
                    curr.appendId(streetId);
                } else {
                    g.addVertex(curr);
                }

                if (streetId == prevId) {
                    curr.addNeighbor(prev);
                    prev.addNeighbor(curr);
                }

                prevId = streetId;
                prev = curr;
            }
            // g.printAllVertices();
        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("'nodes.csv' file not found.");
            exit(1);
        }
        System.out.println("Map loaded.");

        try {
            ArrayList<Path> paths = new ArrayList<>(15);
            KMLGenerator gen = new KMLGenerator();
            gen.init();

            for (Taxi taxi : taxis) {
                Path p = g.findPath(client, taxi);
                p.setTaxiName(taxi.getName());
                paths.add(p);
            }

            Path shortest = paths.get(0);
            for (Path path : paths) {
                if (path.compareTo(shortest) <= 0) {
                    shortest = path;
                }
            }
            shortest.markShortest();

            for (Path path : paths) {
                gen.append(path.getTaxiName(), path.isShortest() ? "green" : "red", path.getTotalRouteString());
            }

            gen.write();
        } catch (ParserConfigurationException | TransformerConfigurationException ex) {
            System.out.println(ex.toString());
            exit(1);
        }
    }
}
