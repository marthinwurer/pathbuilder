package Players.BHM3791;

import Interface.*;

import java.util.List;

/**
 * Created by benjamin on 3/11/17.
 */
public class BHM3791 implements PlayerModule, PlayerModulePart1, PlayerModulePart2, PlayerModulePart3{
    private static final int timeout = 1000;

    private Board current_state;
    private int id;
//    private int current_id;
    private boolean other_invalidated;

    @Override
    public void initPlayer(int dim, int playerId) {
        this.current_state = new Board(dim);
        this.id = playerId;
//        current_id = 0;
        other_invalidated = false;


    }

    @Override
    public void lastMove(PlayerMove playerMove) {
        current_state.update(new MyMove(playerMove));
    }

    @Override
    public void otherPlayerInvalidated() {
        other_invalidated = true;
    }

    @Override
    public PlayerMove move() {
        allLegalMoves();
        if (other_invalidated){
            return current_state.closest(id).their_move();
        }

        MCTSNode root = new MCTSNode(current_state, id);

        long start = System.currentTimeMillis();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {

//            while (System.currentTimeMillis() - start < timeout) {
            while (root.get_playouts() < 1000){
                root.search();
            }
        }
        catch (StackOverflowError error){
            error.printStackTrace();

            System.out.println(root.get_playouts());

        }
        System.out.println(root.get_playouts());

        MyMove toMake = root.get_best_child().get_move();



        System.out.println(toMake);

        if(toMake.id != id){
            System.out.println("DANGER");
        }

        return root.get_best_child().get_move().their_move();
    }

    @Override
    public boolean hasWonGame(int id) {
        return current_state.has_won(id);
    }

    @Override
    public List allLegalMoves() {
        List<MyMove> moves = current_state.allMoves(this.id);

        for( MyMove move : moves){
            System.out.println(move.their_move());
        }

        return null;
    }

    @Override
    public int fewestSegmentsToVictory(int i) {
        return 0;
    }

    @Override
    public boolean isWinnable(int i, int i1, int i2) {
        return false;
    }
}
