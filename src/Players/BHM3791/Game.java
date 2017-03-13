package Players.BHM3791;

import java.util.List;

/**
 * Created by benjamin on 3/13/17.
 */
public class Game {
    private Board board;
    private int current_player;
    private XoRoRNG rand;

    public Game(Board b, int current_player, XoRoRNG rand){
        this.board = new Board(b);
        this.current_player = current_player;
        this.rand = rand;
    }

    public static int next_player(int current){
        if (current == 1){
            return 2;
        }else{
            return 1;
        }
    }


    public int play_heuristic_game(){

        if(board.has_won(1)){
            return 1;
        }else if ( board.has_won(2)){
            return 2;
        }

        for(;;){
            board.update(board.closest(current_player));
            System.out.println(board);
            if(board.has_won(current_player)){
                return current_player;
            }

            // switch players
            current_player = next_player(current_player);
        }
    }

    public int play_random_game(){
        if(board.has_won(1)){
            return 1;
        }else if ( board.has_won(2)){
            return 2;
        }

        for(;;){
            List<MyMove> moves = board.allMoves(current_player);
            MyMove tomake = moves.get(rand.nextInt(moves.size()));
            board.update(tomake);
            System.out.println(board);
            if(board.has_won(current_player)){
                return current_player;
            }

            current_player = next_player(current_player);
        }

    }

    public int getCurrent_player() {
        return current_player;
    }



}
