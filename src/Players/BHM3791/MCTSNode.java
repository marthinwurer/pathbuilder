package Players.BHM3791;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 3/13/17.
 *
 * size: MCTS - Tough - depth
 * 6 : 10 - 1
 * 7 : 10 - 1
 * 8 : 10 - 6
 * 9 : 2 - 10 --- 6 - 1 - 4 w/ 0.5 exploration constant
 * 12: 10-3 w/ multithreading, rave, and better performance
 */
public class MCTSNode {

    public static final XoRoRNG rand = new XoRoRNG();

    public static final double fuzz = 1e-6;
    public static final double decay = 0.99;
    public static final double explore = 0.5;
    public static final double rave_constant = 100.0;


    public static int max_depth = 0;

    // Data structures for Rapid Action Value Estimate AMAF
    public static int[][][] rave_boys = new int[2][21][21];
    public static double[] rave_wins = new double[2];

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
        int side = gamestate.get_dimension() * 2 + 1;
        rave_boys = new int[2][side][side];
        rave_wins = new double[2];
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
        current_player = Board.next_player(made.id);
        leaf = true;
    }


    public boolean isLeaf(){
        return playouts == 0.0;
    }

    public double getUCT(int player, double parent_plays){

        double ratio = p1_wins / (playouts + fuzz);
        if( player == 2){
            ratio = 1.0 - ratio;
        }
        double upper = Math.sqrt(Math.log(parent_plays + 1) / (playouts + fuzz)) * explore;
        double retval = ratio + upper;
        return retval;
    }

    public static double get_amaf(int player, int xx, int yy){
        double amaf = rave_boys[player % 2][yy][xx]/(rave_wins[player % 2] + fuzz);
        return amaf;
    }

    private synchronized void inc_playouts(){
        playouts += 1.0;
    }

    public double search(){
        return search(0);
    }


    public double search(int depth){


        inc_playouts();
        if( !leaf ){

            double value = get_best_child().search(depth + 1);
            p1_wins += value;

            // do move decay - wins deeper are worth less than shallow ones.
            return value * decay;
        }
        else{
            if( depth > max_depth){
                max_depth = depth;
            }
            // check to see if there is a winner
            int winner = gamestate.winner();
            if(winner == 0){
                winner = play_random_game();
                leaf = false;
            }

            // if no winner, do rollout.
            p1_wins += winner % 2;
            return winner % 2;
        }
    }


    public synchronized MCTSNode get_best_child(){
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
            // get the uct value
            double fuzz_amount = rand.nextDouble() * fuzz;
            double uctValue = child.getUCT(current_player, playouts);
            uctValue += fuzz_amount;

            // get the RAVE value
            double amaf_value = get_amaf(current_player, child.get_move().pos.x, child.get_move().pos.y);
            double rave_proportion = Math.max(0.0, (rave_constant - playouts)/ (rave_constant));
            double RAVE = rave_proportion * amaf_value + (1.0 - rave_proportion) * uctValue;
//            double RAVE = uctValue;

            if (RAVE > bestValue){
                selected = child;
                bestValue = RAVE;
            }
        }
        if(selected == null){
            System.out.println(gamestate);
            System.out.println("panic");
        }
        return selected;
    }

    public int play_random_game(){
        Board rollout = new Board(gamestate);
        int rollout_player = current_player;

        int winner = rollout.winner();
        if(winner != 0){
            return winner;
        }

        for(;;){
            List<MyMove> moves = rollout.allMoves(rollout_player);
            MyMove tomake = moves.get(rand.nextInt(moves.size()));
            rollout.update(tomake);
            if(rollout.has_won(rollout_player)){

                // update RAVE
                rollout.update_rave(rave_boys[rollout_player % 2], rollout_player);
                rave_wins[rollout_player % 2] += 1;

                return rollout_player;
            }

            rollout_player = Board.next_player(rollout_player);
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
                p1_wins + "/" + playouts + "=" + value(current_player) + ", " +
                getUCT(current_player, playouts) + ", " +
                rave_boys[current_player % 2][togethere.pos.y][togethere.pos.x]/rave_wins[current_player % 2] + "}";
    }

    public void diagnostics(){
        System.out.println(get_playouts() + ", " +  max_depth);
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

    public static void view_rave(int id){
        String out = "";

        for (int yy = 1; yy < rave_boys[id % 2].length - 1; yy++){
            for( int xx = 1; xx < rave_boys[id % 2][yy].length - 1; xx++){

                int value = (int) (get_amaf(id, xx, yy) * 26);
                if( value == 0 || value == 25){
                    out += " ";
                }else{
                    out += Character.toString((char)(value + 65));
                }


            }
            out += "\n";
        }

        System.out.println(out);
    }

    public static float[][] get_RAVE_vals(int id){
        float[][] out = new float[rave_boys[id % 2].length][rave_boys[id % 2][0].length];
        for (int yy = 1; yy < rave_boys[id % 2].length - 1; yy++) {
            for (int xx = 1; xx < rave_boys[id % 2][yy].length - 1; xx++) {
                out[yy][xx] = (float)get_amaf(id, xx, yy);
            }
        }

        return out;
    }

}
