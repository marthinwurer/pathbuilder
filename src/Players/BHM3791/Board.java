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

    public Board(Board b){
        this.dimension = b.dimension;
        this.unpacked = new byte[dimension * 2 + 1][dimension * 2 + 1];

        // copy unpacked
        for( int ii = 0; ii < unpacked.length; ii++){
            System.arraycopy(b.unpacked[ii], 0, unpacked[ii], 0, b.unpacked[ii].length);
        }
    }

    public static int next_player(int current){
        if (current == 1){
            return 2;
        }else{
            return 1;
        }
    }


    /**
     * get the value at the given x and y position
     * @param x
     * @param y
     * @return
     */
    public byte value(int x, int y){
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


    public DijkstraNode distance(int player, int start){

        // make a queue to add the nodes to
        MinArrayPriorityQueue queue = new MinArrayPriorityQueue();
        ArrayList<Point> visited = new ArrayList<>();

        // add the initial points for the given player
        if (start == 0) {
            for (int ii = 1; ii < dimension * 2; ii = ii + 2) {
                Point toadd;
                if (player == 1) {
                    toadd = Point.getPoint(0, ii);
                } else {
                    toadd = Point.getPoint(ii, 0);
                }
                queue.enqueue(new DijkstraNode(toadd, 0, null));
                visited.add(toadd);
            }
        }else{
            Point toadd;
            if (player == 1) {
                toadd = Point.getPoint(0, start);
            } else {
                toadd = Point.getPoint(start, 0);
            }
            queue.enqueue(new DijkstraNode(toadd, 0, null));

            // add all of the edge points to the visited list, so that we don't go to them.
            for (int ii = 1; ii < dimension * 2; ii = ii + 2) {
                if (player == 1) {
                    toadd = Point.getPoint(0, ii);
                } else {
                    toadd = Point.getPoint(ii, 0);
                }
                visited.add(toadd);
            }

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

//        System.out.println(this);

        return null;
    }


    /**
     * Just see if the player has made something that connects to the other side.
     * @param player
     * @return
     */
    public boolean dfs(int player){

        // make a stack to add the nodes to
        ArrayStack stack = new ArrayStack(dimension * 2);
        ArrayList<Point> visited = new ArrayList<>();

        // add the initial points for the given player
        for (int ii = 1; ii < dimension * 2; ii = ii + 2){
            Point toadd;
            if ( player == 1){
                toadd = Point.getPoint(0, ii);
            }else{
                toadd = Point.getPoint(ii, 0);
            }
            stack.push(toadd);
        }

        Point current = null;
        // actually do dijkstra's
        while (stack.get_size() > 0){

            // get the next node.
            Point pos = stack.pop();

            // if this is the end, break out and rebuild the path.
            if ( player == 1 && pos.x == dimension * 2){
                return true;
            }else if (pos.y == dimension * 2){
                return true;
            }

            // if not, then add all of the legal neighbors with recalculated weights.

            // north
            if( pos.y > 1){
                Point toadd = pos.north(2);
                if(value(pos.x, pos.y - 1) == player && !visited.contains(toadd)){
                    stack.push(toadd);
                    visited.add(toadd);
                }
            }

            // south
            if( pos.y < dimension * 2 - 1){
                Point toadd = pos.south(2);
                if(value(pos.x, pos.y + 1) == player && !visited.contains(toadd)){
                    stack.push(toadd);
                    visited.add(toadd);
                }
            }

            // east
            if( pos.x < dimension * 2 - 1){
                Point toadd = pos.east(2);
                if(value(pos.x + 1, pos.y) == player && !visited.contains(toadd)){
                    stack.push(toadd);
                    visited.add(toadd);
                }
            }

            // west
            if( pos.x > 1){
                Point toadd = pos.west(2);
                if(value(pos.x - 1, pos.y) == player && !visited.contains(toadd)){
                    stack.push(toadd);
                    visited.add(toadd);
                }
            }
        }

        // if there is no connected path, return false.
        return false;
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

        if(moves.size() == 0){
            System.out.println(this);
            System.out.println("no moves left");
        }
        return moves;
    }

    public boolean has_won(int id){

        return dfs(id);
    }

    public MyMove closest(int id){
        DijkstraNode result = distance(id, 0);

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

    public String toString(){
        String out = "";

        for (int yy = 0; yy < unpacked.length; yy++){
            for( int xx = 0; xx < unpacked[yy].length; xx++){
                if(valid_edge(Point.getPoint(xx, yy))){
                    // fancy math to determine whether the connection is
                    // horizontal or vertical.
                    // if the number is odd, then for player 1, the connection is vertical.
                    boolean direction = xx % 2 == 0;
                    if( value(xx, yy) == 0){
                        out += " ";
                    } else {
                        if (value(xx, yy) == 2){
                            direction = !direction;
                        }
                        if (direction){
                            out += "|";
                        }
                        else{
                            out += "-";
                        }
                    }
                }
                else{
                    out += ".";
                }
            }
            out += "\n";
        }

        return out;
    }

    public boolean finished(){
        return has_won(1) || has_won(2);
    }

    public int winner (){
        if (dfs(2)){
            return 2;
        }
        else if(dfs(1)) {
            return 1;
        }
        else{
            return 0;
        }
    }

    public int get_dimension(){
        return dimension;
    }

    public void update_rave(int[][] table, int player){
        for(int yy = 1; yy < dimension * 2; yy++){
            for(int xx = 1; xx < dimension * 2; xx++){
                if (value(xx, yy) == player){
                    table[yy][xx] ++;
                }
            }
        }
    }

    public int evaluate() {

        int win = winner();
        if( win != 0){
            if( win == 1){
                return Integer.MAX_VALUE;
            }else{
                return -Integer.MAX_VALUE;
            }
        }

        // get the total distances
        int[] total_distances = new int[2];

        // player 1's distances
        for (int player = 1; player <= 2; player++) {
            for (int ii = 1; ii < dimension * 2; ii = ii + 2) {

                // get the distance from each point.
                DijkstraNode result = distance(player, ii);
                int dist;
                if (result != null) {
                    dist = result.distance;
                } else {
                    // this will never be null, becuase if the game finished,
                    // we would have shortcut out with the call to winner.
                    dist = distance(player, 0).distance;
                }
                total_distances[player % 2] += dist;
            }
        }

        return total_distances[0] - total_distances[1];
    }

    public int initial_evaluate(){
        int win = winner();
        if( win != 0){
            if( win == 1){
                return Integer.MAX_VALUE;
            }else{
                return Integer.MIN_VALUE;
            }
        }

        // get the total distances
        int d1 = 0;
        int d2 = 0;
        int ii = 0;
//        for (int ii = 1; ii < dimension * 2; ii = ii + 2){
            d1 += distance(1, ii).distance;
            d2 += distance(2, ii).distance;
//        }

        return d2 - d1;
    }

    @Override
    public int hashCode() {
        int hash = 0;

        for(int yy = 1; yy < dimension * 2; yy++){
            for(int xx = 1; xx < dimension * 2; xx++){
                if (valid_edge(Point.getPoint(xx, yy)) && value(xx, yy) == 0){
                    hash = Integer.rotateLeft(hash, 3);
                    hash = hash ^ value(xx, yy);
                }
            }
        }

        return hash;
    }
}
