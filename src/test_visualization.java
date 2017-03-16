import Players.BHM3791.*;

/**
 * Created by benjamin on 3/15/17.
 */
public class test_visualization {

    public static void main(String args[]) {

        Board board = new Board(3);

        System.out.println(board.distance(1, 0).distance);
        System.out.println(board.distance(2, 0).distance);

        board.update(new MyMove(1, 1, 1));
        board.update(new MyMove(3, 3, 2));

        Visualization v = new Visualization(board);
        v.setVisible(true);

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        board.update(new MyMove(1, 5, 1));
        board.update(new MyMove(5, 1, 2));

        v.update_board(board);

        MCTSNode root = new MCTSNode(board, 1);

        for( int ii = 0; ii< 1000; ii++){
            root.search();
        }

        v.setValues(MCTSNode.get_RAVE_vals(1));

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MiniMaxNode mini = new MiniMaxNode(board, 1);

        mini.evaluate(4);
        System.out.println("Mini Done!");
        v.setValues(mini.get_evals());




    }
}