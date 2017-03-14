package Players.BHM3791;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 3/13/17.
 */
public class MCTSNode {

    public static final XoRoRNG rand = new XoRoRNG();

    public static final double fuzz = 1e-2;
    public static final double decay = 0.99;

    private MyMove togethere;
    private Board gamestate;
    private int current_player; // the player making moves from this node.
    private double playouts;
    private double p1_wins;
    private boolean leaf;

    private ArrayList<MCTSNode> children;



    public MCTSNode( Board gamestate, int current_player){
        this.togethere = null;
        this.gamestate = new Board(gamestate);
        playouts = 0.1;
        p1_wins = 0.0;
        this.current_player = current_player;
        leaf = true;
    }

    /**
     * Child node
     * @param made
     * @param gamestate
     */
    public MCTSNode(MyMove made, Board gamestate){
        this.togethere = made;
        this.gamestate = new Board(gamestate);
        this.gamestate.update(made);
        playouts = 0.0;
        p1_wins = 0.0;
        current_player = next_player(made.id);
        leaf = true;
    }

    public static int next_player(int current){
        if (current == 1){
            return 2;
        }else{
            return 1;
        }
    }

    public boolean isLeaf(){
        return playouts == 0.0;
    }

    public double getUCT(int player, double parent_plays){

        double ratio = p1_wins / (playouts + fuzz);
        if( player == 2){
            ratio = 1.0 - ratio;
        }
        double upper = Math.sqrt(Math.log(parent_plays + 1) / (playouts + fuzz));
        double retval = ratio + upper;
        return retval;
    }

    public double search(){


        playouts += 1;
        if( !leaf ){

            double value = get_best_child().search();
            p1_wins += value;

            // do move decay - wins deeper are worth less than shallow ones.
            return value * decay;
        }
        else{
            // check to see if there is a winner
            int winner = gamestate.winner();
            if(winner == 0){
                winner = play_random_game();//new Board(gamestate).play_random_game(current_player);
                leaf = false;
            }

            // if no winner, do rollout.
            p1_wins += winner % 2;
            return winner % 2;
        }
    }


    public MCTSNode get_best_child(){
        if (children == null){
            List<MyMove> moves = gamestate.allMoves(current_player);
            children = new ArrayList<>(moves.size());
            for(MyMove move : moves){
                children.add(new MCTSNode(move,gamestate));
            }
        }
        MCTSNode selected = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        for( MCTSNode child : children){
            double fuzz_amount = rand.nextDouble() * fuzz;
            double uctValue = child.getUCT(current_player, playouts);
            uctValue += fuzz_amount;
            if (uctValue > bestValue){
                selected = child;
                bestValue = uctValue;
            }
        }
        return selected;
    }

    public int play_random_game(){
        Board rollout = new Board(gamestate);
        int rollout_player = current_player;
        if(rollout.has_won(1)){
            return 1;
        }else if ( rollout.has_won(2)){
            return 2;
        }

        for(;;){
            List<MyMove> moves = rollout.allMoves(rollout_player);
            MyMove tomake = moves.get(rand.nextInt(moves.size()));
            rollout.update(tomake);
//            System.out.println(rollout);
            if(rollout.has_won(rollout_player)){
                return rollout_player;
            }

            rollout_player = next_player(rollout_player);
        }
    }

    public double value(int player){
        double ratio = p1_wins / (playouts + fuzz);
        if(player == 2){
            ratio = 1.0 - ratio;
        }
        return ratio;
    }

    public double get_playouts(){
        return playouts;
    }

    public MyMove get_move(){
        return togethere;
    }

    public String toString(){
        return "{" + togethere + ", " + current_player + ", " +
                p1_wins + "/" + playouts + "=" + value(current_player) + ", " + getUCT(current_player, playouts) + "}";
    }

    public void diagnostics(){
        children.sort((o1, o2) -> {
            double val1 = o1.value(current_player);
            double val2 = o2.value(current_player);
            return val1 == val2 ? 0 : val1 > val2 ? -1 : 1 ;
        });
        for( int ii = 0; ii < 10 && ii < children.size(); ii++){
            System.out.println(children.get(ii));
        }
        System.out.println(children.size());
    }

    /**
     * returns the move that leads to the child with the highest number of rollouts.
     */
    public MyMove get_next_move(){
        MCTSNode selected = children.get(0);

        for( MCTSNode child : children){
            if (child.get_playouts() > playouts){
                selected = child;
            }
        }
        return selected.get_move();
    }

}
