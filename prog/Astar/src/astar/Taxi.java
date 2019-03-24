package astar;

public class Taxi extends Position {
    private final int id;

    public Taxi(double x, double y, int id) {
        super(x, y);
        this.id = id;
    }

    public Taxi(Position pos, int id) {
        super(pos.getX(), pos.getY());
        this.id = id;
    }

    public String getName() {
        return Integer.toString(this.id);
    }

    @Override
    public String toString() {
        return String.format("\nTaxi{id=%d, pos=%s}", id, super.toString());
    }
}
