package Players.BHM3791;

import java.util.ArrayList;

/**
 * Created by benjamin on 3/21/17.
 */
public class AlphaBetaRoot extends MiniMaxNode {

    private ArrayList<MyMove> best_so_far;

    private int timeout;


    public AlphaBetaRoot(Board gamestate, int current_player, int timeout) {
        super(gamestate, current_player);
        best_so_far = new ArrayList<>(gamestate.allMoves(current_player));
        this.timeout = timeout;
    }

    public void iterative_deepening(){
        long start = System.currentTimeMillis();


        num_pruned = 0;

        try {
            Thread execution = new Thread(() ->{
                int depth = 2;
                try {
                    while (System.currentTimeMillis() - start < timeout) {
                        alpha_beta(depth, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                        depth++;

                        best_so_far = new ArrayList<>(children.size());
                        for(MiniMaxNode child : children){
                            MyMove made = child.togethere;
                            made.id = child.value;
                            best_so_far.add(made);
                        }
                    }
                }catch (InterruptedException e){
                    System.out.println("Depth obtained: " + (depth - 1));
                }
            });

            execution.start();


            Thread.sleep(timeout);
            execution.interrupt();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyMove bestmove() {
        int value = Integer.MIN_VALUE;
        MyMove best = null;
        for(MyMove move: best_so_far){
            int result = -move.id;

            if( result > value){
                best = move;
                value = result;
            }
        }

        if (best == null){
            System.out.println("panic");
            return children.get(0).togethere;
        }

        best.id = current_player;

        return best;
    }
}
