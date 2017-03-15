import Players.BHM3791.Board;
import Players.BHM3791.MCTSNode;
import Players.BHM3791.MyMove;

/**
 * Created by benjamin on 3/14/17.
 */
public class test_mcts {
    public static void main(String args[]) {

        Board board = new Board(3);
        board.update(new MyMove(1, 1, 1));
        board.update(new MyMove(3, 1, 1));
        board.update(new MyMove(5, 1, 1));


        MCTSNode root = new MCTSNode(board, 2);

        root.play_random_game();

    }

}
