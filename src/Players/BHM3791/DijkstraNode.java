package Players.BHM3791;

/**
 * Created by benjamin on 12/10/16.
 */
public class DijkstraNode {
    Point pos;
    int distance;

    DijkstraNode(Point pos, int distance){
        this.pos = pos;
        this.distance = distance;
    }
}
