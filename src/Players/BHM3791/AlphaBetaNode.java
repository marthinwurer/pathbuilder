package Players.BHM3791;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by benjamin on 3/19/17.
 */
public class AlphaBetaNode extends MiniMaxNode {

    public AlphaBetaNode(Board gamestate, int current_player) {
        super(gamestate, current_player);
    }

    public AlphaBetaNode(MyMove made, Board gamestate) {
        super(made, gamestate);
    }

    public int alpha_beta(int depth, int alpha, int beta){
        if(depth == 0 || value == Integer.MAX_VALUE || value == -Integer.MAX_VALUE){
            value = gamestate.evaluate();

            if( current_player == 2){
                value = -value;
            }
            raw_value = value;
            return value;
        }
        else {
            this.value = Integer.MAX_VALUE;
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
//            int result = -child.alpha_beta(depth - 1);

//            if( result < value){
//                value = result;
//            }
        }




        return 0;
    }
}
