package Players.ALPHABETA;

import Interface.PlayerMove;
import Players.BHM3791.MCTSNode;
import Players.BHM3791.MiniMaxNode;
import Players.BHM3791.MyMove;
import Players.MINIMAX.MINIMAX;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 3/19/17.
 */
public class ALPHABETA extends MINIMAX {
    private ArrayList<Integer> num_explored = new ArrayList<>();

    @Override
    public PlayerMove move() {
        List<MyMove> selection = current_state.allMoves(id);
        if (other_invalidated) {
            return current_state.closest(id).their_move();
        }


        v_vals.update_board(current_state);

        long start = System.currentTimeMillis();


        MiniMaxNode.num_pruned = 0;


        MiniMaxNode root = new MiniMaxNode(current_state, id);





        try {
            Thread execution = new Thread(() ->{
                int depth = 1;
                while (System.currentTimeMillis() - start < timeout) {
                    root.alpha_beta(depth, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                    depth++;
                }
            });

            execution.start();


            Thread.sleep(timeout);
            execution.interrupt();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }







        root.diagnostics();
        System.out.println(MiniMaxNode.num_pruned);
        num_explored.add(MiniMaxNode.num_pruned);

        int total = 0;
        for(int val : num_explored){
            total += val;
        }
        System.out.println(total/(double)num_explored.size());

        v_vals.setValues(root.get_evals());

        MyMove toMake = root.bestmove();

        System.out.println("time: " + (System.currentTimeMillis() - start));

        System.out.println(toMake);

        if (toMake.id != id) {
            System.out.println("DANGER");
        }

        System.out.println(MCTSNode.get_amaf(id, toMake.pos.x, toMake.pos.y));


        return toMake.their_move();
    }

}
