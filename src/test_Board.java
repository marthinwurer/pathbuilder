import Players.BHM3791.Board;
import Players.BHM3791.MyMove;

/**
 * Created by benjamin on 3/13/17.
 */
public class test_Board {
    public static void main(String args[]) {

        Board board = new Board(5);

        System.out.println(board.distance(1, 0).distance);
        System.out.println(board.distance(2, 0).distance);

        board.update(new MyMove(1, 1, 1));
        board.update(new MyMove(3, 3, 2));


        System.out.println(board.distance(1, 0).distance);
        System.out.println(board.distance(2, 0).distance);

        board.update(new MyMove(2, 2, 1));
        board.update(new MyMove(4, 4, 2));


        System.out.println(board);

        board = new Board(3);
        board.update(new MyMove(1, 1, 1));
        board.update(new MyMove(3, 1, 1));
        board.update(new MyMove(5, 1, 1));

        System.out.println(board);

        System.out.println(board.evaluate());

        System.out.println(board.dfs(1));
        System.out.println(board.dfs(2));

        board = new Board(3);
        board.update(new MyMove(1, 1, 1));
        board.update(new MyMove(3, 3, 2));
        board.update(new MyMove(5, 5, 1));

        System.out.println(board);

        System.out.println(board.evaluate());

        System.out.println(board.dfs(1));
        System.out.println(board.dfs(2));

        board = new Board(3);
        board.update(new MyMove(1, 1, 2));
        board.update(new MyMove(1, 3, 2));
        board.update(new MyMove(1, 5, 2));

        System.out.println(board);

        System.out.println(board.evaluate());

        System.out.println(board.dfs(1));
        System.out.println(board.dfs(2));

        board = new Board(3);
        board.update(new MyMove(1, 1, 2));
        board.update(new MyMove(1, 3, 2));
        board.update(new MyMove(1, 5, 1));

        System.out.println(board);

        System.out.println(board.evaluate());

        board = new Board(3);
        board.update(new MyMove(1, 1, 2));

        System.out.println(board);

        System.out.println(board.evaluate());

    }
}
