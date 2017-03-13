import Players.BHM3791.Board;
import Players.BHM3791.MyMove;

/**
 * Created by benjamin on 3/13/17.
 */
public class test_Board {
    public static void main(String args[]) {

        Board board = new Board(5);

        System.out.println(board.distance(1).distance);
        System.out.println(board.distance(2).distance);

        board.update(new MyMove(1, 1, 1));
        board.update(new MyMove(3, 3, 2));


        System.out.println(board.distance(1).distance);
        System.out.println(board.distance(2).distance);

        board.update(new MyMove(2, 2, 1));
        board.update(new MyMove(4, 4, 2));


        System.out.println(board);
    }
}
