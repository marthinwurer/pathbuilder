package Players.BHM3791;

/**
 * Created by benjamin on 12/10/16.
 */
public class Board {
    private char[] field;
    private int dimension;
    int currplayer;

    /**
     * Create a new Players.BHM3791.Board with the given dimension
     * @param dimension
     */
    public Board(int dimension){
        this.currplayer = 0;
        this.dimension = dimension;
        int temp = dimension - 1;
        this.field = new char[dimension * dimension + temp * temp];
    }

    /**
     * get the value at the given x and y position
     * @param x
     * @param y
     * @return
     */
    private char pos(int x, int y, boolean outer){
        return field[outer ? y * dimension + x : dimension * dimension + (dimension - 1) * y + x];
    }

    public int distance(int player){
        return 0;
    }


}
