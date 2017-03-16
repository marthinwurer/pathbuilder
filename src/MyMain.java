import Interface.PlayerModule;
import Interface.PlayerMove;
import Players.BHM3791.BHM3791;
import Players.BHM3791.Board;
import Players.BHM3791.MyMove;
import Players.BHM3791.Visualization;
import Players.THEIRS.THEIRS;

import java.util.List;

/**
 * Created by benjamin on 3/16/17.
 */
public class MyMain {
    public static void main(String args[]) {
        Board board = new Board(6);
        Visualization v = new Visualization(board, "CURRENT");
        v.setVisible(true);
        PlayerModule[] players = new PlayerModule[2];
        players[1] = new BHM3791();
        players[0] = new THEIRS();

        players[1].initPlayer(6, 1);
        players[0].initPlayer(6, 2);


        int current_player = 1;


        for(;;){
            PlayerMove tomake = players[current_player % 2].move();
            board.update(new MyMove(tomake));
            players[1].lastMove(tomake);
            players[0].lastMove(tomake);
            v.update_board(board);

            if(board.has_won(current_player)){
                break;
            }

            current_player = Board.next_player(current_player);
        }

        System.out.println("Player " + current_player + " Wins!");

    }
}