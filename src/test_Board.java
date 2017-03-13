import Players.BHM3791.Board;

/**
 * Created by benjamin on 3/13/17.
 */
public class test_Board {
    public static void main(String args[]) {

        Board board = new Board(5);

        System.out.println(board.distance(1).distance);
        System.out.println(board.distance(2).distance);
    }
}
