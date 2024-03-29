import Players.BHM3791.DijkstraNode;
import Players.BHM3791.MinArrayHeap;
import Players.BHM3791.Point;

/**
 * Created by benjamin on 3/11/17.
 */
public class test_heap {
    public static void main(String args[]){
        MinArrayHeap heap = new MinArrayHeap();
        heap.add(new DijkstraNode(null, 10));
        heap.add(new DijkstraNode(null, 1));
        heap.add(new DijkstraNode(null, 3));
        heap.add(new DijkstraNode(null, 9));
        heap.add(new DijkstraNode(null, 5));
        heap.add(new DijkstraNode(null, 8));
        heap.add(new DijkstraNode(null, 7));

        while (heap.getSize() > 0){
            System.out.println("next: " + heap.pop().distance);
        }

        heap.add(new DijkstraNode(null, 10));
        heap.add(new DijkstraNode(null, 1));
        heap.add(new DijkstraNode(Point.getPoint(3, 3), 3));
        heap.add(new DijkstraNode(null, 9));
        heap.add(new DijkstraNode(null, 5));
        heap.add(new DijkstraNode(Point.getPoint(6, 6), 8));
        heap.add(new DijkstraNode(null, 7));

//        heap.update(Point.getPoint(6, 6), 6);

        while (heap.getSize() > 0){
            System.out.println("next: " + heap.pop().distance);
        }
    }
}
