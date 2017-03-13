package Players.BHM3791;

/**
 * Created by benjamin on 12/10/16.
 */
public class DijkstraNode {
    public Point pos;
    public int distance;
    public DijkstraNode previous;

    public DijkstraNode(Point pos, int distance){
        this(pos, distance, null);
    }
    public DijkstraNode(Point pos, int distance, DijkstraNode previous){
        this.pos = pos;
        this.distance = distance;
        this.previous = previous;
    }

    public String toString(){
        return "{" + pos + "," + distance + "}";
    }

    public boolean equals(DijkstraNode other){
        return other.pos.equals(pos);
    }
}
