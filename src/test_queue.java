import Players.BHM3791.DijkstraNode;
import Players.BHM3791.MinArrayPriorityQueue;
import Players.BHM3791.Point;

/**
 * Created by benjamin on 3/12/17.
 */
public class test_queue {
    public static void main(String args[]) {

        // add them in any order, should pop out in the correct order
        MinArrayPriorityQueue queue = new MinArrayPriorityQueue();
        queue.enqueue(new DijkstraNode(null, 10));
        queue.enqueue(new DijkstraNode(null, 1));
        queue.enqueue(new DijkstraNode(null, 3));
        queue.enqueue(new DijkstraNode(null, 9));
        queue.enqueue(new DijkstraNode(null, 5));
        queue.enqueue(new DijkstraNode(null, 8));
        queue.enqueue(new DijkstraNode(null, 7));

        while (queue.getSize() > 0) {
            System.out.println("next: " + queue.pop());
        }

        // add them in reverse order, they should come out in correct order
        for(int ii = 10; ii > 0; ii--){
            queue.enqueue(new DijkstraNode(Point.getPoint(ii, ii), ii));
        }


        // modify two of the points, they should now be at the back and front respectively.

        queue.update(Point.getPoint(3,3), 30);
        queue.update(Point.getPoint(5, 5), 0);


        while (queue.getSize() > 0) {
            System.out.println("next: " + queue.pop());
        }

//        java.lang.Class.forName();

    }
}
