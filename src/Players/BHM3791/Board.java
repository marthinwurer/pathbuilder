package Players.BHM3791;

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

    private byte[] field;
    private int dimension;
    private int currplayer;

    /**
     * Create a new Players.BHM3791.Board with the given dimension
     * @param dimension
     */
    public Board(int dimension){
        this.currplayer = 1;
        this.dimension = dimension;
        int temp = dimension - 1;
        this.field = new byte[dimension * dimension + temp * temp]; // outer grid cells come before inner.
    }

    /**
     * get the value at the given x and y position
     * @param x
     * @param y
     * @return
     */
    private byte pos(int x, int y, boolean outer){
        return field[outer ? y * dimension + x : dimension * dimension + (dimension - 1) * y + x];
    }

    public int distance(int player){
        return 0;
    }
}
