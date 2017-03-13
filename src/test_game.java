import Players.BHM3791.Board;
import Players.BHM3791.Game;
import Players.BHM3791.XoRoRNG;

/**
 * Created by benjamin on 3/13/17.
 */
public class test_game {

    public static void main(String args[]) {

        XoRoRNG rand = new XoRoRNG(12345);
        Game game1 = new Game(new Board(10), 1, rand);

        System.out.println(game1.play_heuristic_game() + " Wins!");

        Game game2 = new Game(new Board(10), 1, rand);

        System.out.println(game2.play_random_game() + " Wins!");


    }
}
