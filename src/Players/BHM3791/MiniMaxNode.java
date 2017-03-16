package Players.BHM3791;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static Players.BHM3791.Board.next_player;

/**
 * Created by benjamin on 3/14/17.
 */
public class MiniMaxNode {

    public static final XoRoRNG rand = new XoRoRNG();

    private int value;
    private int raw_value;
    private MyMove togethere;
    private Board gamestate;
    private int current_player; // the player that will be making a move

    private ArrayList<MiniMaxNode> children;

    public MiniMaxNode(Board gamestate, int current_player) {
        togethere = null;
        this.gamestate = new Board(gamestate);
        this.current_player = current_player;
        this.value = gamestate.evaluate();

        if( current_player == 2){
            value = -value;
        }
        this.raw_value = value;
    }

    public MiniMaxNode(MyMove made, Board gamestate) {
        this.togethere = made;
        this.gamestate = new Board(gamestate);
        this.gamestate.update(made);
        current_player = next_player(made.id);
        this.value = this.gamestate.evaluate();

        if( current_player == 2){
            value = -value;
        }
        this.raw_value = value;
    }

    public int evaluate(int depth){
        if(depth == 0 || value == Integer.MAX_VALUE || value == Integer.MIN_VALUE){
            return this.value;
        }

        // if there are no children, expand.
        if (children == null){
            List<MyMove> moves = gamestate.allMoves(current_player);
            children = new ArrayList<>(moves.size());
            for(MyMove move : moves){
                children.add(new MiniMaxNode(move,gamestate));
            }
        }

        Collections.shuffle(children);
        children.sort(Comparator.comparingInt(c -> c.value));

        for(MiniMaxNode child : children){
            int result = -child.evaluate(depth - 1);

            if( result < value){
                value = result;
            }
        }

        return this.value;
    }

    public MyMove bestmove(){
        int value = Integer.MIN_VALUE;
        MyMove best = null;
        for(MiniMaxNode child : children){
            int result = child.value;

            if( result > value){
                best = child.togethere;
                value = result;
            }
        }

        if (best == null){
            System.out.println("panic");
            return children.get(0).togethere;
        }

        return best;
    }

    public String toString(){
        return "{" + togethere + ", " + current_player + ", " + raw_value + ", " + value + "}";
    }

    public void diagnostics() {
        children.sort(Comparator.comparingInt(c -> -c.value));
        for (int ii = 0; ii < 10 && ii < children.size(); ii++) {
            System.out.println(children.get(ii));
        }
        System.out.println(children.size());

    }

    public float[][] get_evals(){
        int size = gamestate.get_dimension() * 2 + 1;
        float[][] out = new float[size][size];

        for(MiniMaxNode child : children){
            int result = child.value;
            MyMove move = child.togethere;
            out[move.pos.y][move.pos.x] = result;
        }

        return out;
    }
}
