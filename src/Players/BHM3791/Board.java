package Players.BHM3791;

import Interface.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 12/10/16.
 */
public class Board {
    /* the board is arranged with an inner grid and an outer grid. The outer grid has all odd locations.
       The inner grid has all even locations.

       each location can be:
           0 - empty
           1 - player 1
           2 - player 2
     */

//    private byte[] field;
    private byte[][] unpacked; // an unpacked version of the board state for initial testing.
    private int dimension;

    /**
     * Create a new board with the given dimension
     * @param dimension
     */
    public Board(int dimension){
        this.dimension = dimension;
        int temp = dimension - 1;
//        this.field = new byte[dimension * dimension + temp * temp]; // outer grid cells come before inner.
        this.unpacked = new byte[dimension * 2 + 1][dimension * 2 + 1];

    }

    /**
     * get the value at the given x and y position
     * @param x
     * @param y
     * @return
     */
    private byte value(int x, int y){
//        return field[outer ? y * dimension + x : dimension * dimension + (dimension - 1) * y + x];
        return unpacked[y][x];
    }

    private void set(int x, int y, byte val){
        unpacked[y][x] = val;
    }

    public Point conv_coord(Coordinate coord){
        int xx = coord.getCol();
        int yy = coord.getRow();

        return Point.getPoint(xx, yy);
    }

    /**
     * a coordinate is valid for a move if the parity of x and y are equal.
     * legal coordinates are also 0 < legal < dim * 2
     * @param p
     * @return
     */
    public boolean valid_edge(Point p){
        return p.x % 2 == p.y % 2 && p.x > 0 && p.y > 0 && p.x < dimension * 2 && p.y < dimension * 2;
    }


    /**
     * Check whether you can move to the next point as well as what the distance that it would take, and add it to the queue if it is valid.
     * @param x
     * @param y
     * @param next
     * @param visited
     * @param player
     * @param queue
     */
    private void check_movement(int x, int y, DijkstraNode next, ArrayList<Point> visited, int player, MinArrayPriorityQueue queue){

        // find the new distance
        if (value(x, y) == 0){
            next.distance += 1;
        }
        // if you can't move through the square (not empty and not yours) then return.
        else if(value(x, y) != player){
            return;
        }

        // if it is not visited yet, just add it.
        if( !visited.contains(next.pos)) {
            queue.enqueue(next);
            visited.add(next.pos);
        }
        // if the node has been visited, update it if the new path is less.
        else{
            queue.update_if_less(next.pos, next);
        }
    }


    public DijkstraNode distance(int player){

        // make a queue to add the nodes to
        MinArrayPriorityQueue queue = new MinArrayPriorityQueue();
        ArrayList<Point> visited = new ArrayList<>();

        // add the initial points for the given player
        for (int ii = 1; ii < dimension * 2; ii = ii + 2){
            Point toadd;
            if ( player == 1){
                toadd = Point.getPoint(0, ii);
            }else{
                toadd = Point.getPoint(ii, 0);
            }
            queue.enqueue(new DijkstraNode(toadd, 0, null));
        }

        DijkstraNode current = null;
        // actually do dijkstra's
        while (queue.getSize() > 0){

            // get the next node.
            current = queue.pop();
            Point pos = current.pos;

            // if this is the end, break out and rebuild the path.
            Point toadd;
            if ( player == 1 && current.pos.x == dimension * 2){
                return current;
            }else if (current.pos.y == dimension * 2){
                return current;
            }

            // if not, then add all of the legal neighbors with recalculated weights.

            // north
            if( current.pos.y > 1){
                DijkstraNode next = new DijkstraNode(pos.north(2), current.distance, current);

                check_movement(pos.x, pos.y - 1, next, visited, player, queue);
            }

            // south
            if( current.pos.y < dimension * 2 - 1){
                DijkstraNode next = new DijkstraNode(pos.south(2), current.distance, current);

                check_movement(pos.x, pos.y + 1, next, visited, player, queue);
            }

            // east
            if( current.pos.x < dimension * 2 - 1){
                DijkstraNode next = new DijkstraNode(pos.east(2), current.distance, current);

                check_movement(pos.x + 1, pos.y, next, visited, player, queue);
            }

            // west
            if( current.pos.x > 1){
                DijkstraNode next = new DijkstraNode(pos.west(2), current.distance, current);

                check_movement(pos.x - 1, pos.y, next, visited, player, queue);
            }
        }


        return null;

    }

    public void update(MyMove move){
        set(move.pos.x, move.pos.y, (byte)move.id);
    }

    public List<MyMove> allMoves(int id){
        ArrayList<MyMove> moves = new ArrayList<>(dimension * dimension * 2);
        for(int yy = 1; yy < dimension * 2; yy++){
            for(int xx = 1; xx < dimension * 2; xx++){
                if (valid_edge(Point.getPoint(xx, yy)) && value(xx, yy) == 0){
                    moves.add(new MyMove(Point.getPoint(xx, yy), id));
                }
            }
        }
        return moves;
    }

    public boolean has_won(int id){
        DijkstraNode result = distance(id);

        return result != null && result.distance == 0;
    }

    public MyMove closest(int id){
        DijkstraNode result = distance(id);

        if (result.distance % 2 == 0){
            while(result.previous.distance == result.distance){
                result = result.previous;
            }
        }else{
            do{
                result = result.previous;
            }while(result.previous.distance > 0);
        }

        return new MyMove(Point.between(result.pos, result.previous.pos), id);
    }
}
