package Players.BHM3791;

import Interface.*;

import java.util.List;

/**
 * Created by benjamin on 3/11/17.
 */
public class BHM3791 implements PlayerModule, PlayerModulePart1, PlayerModulePart2, PlayerModulePart3{
    private static final int timeout = 7000;

    private Board current_state;
    private int id;
//    private int current_id;
    private boolean other_invalidated;

    private int core_count = 1;

    @Override
    public void initPlayer(int dim, int playerId) {
        this.current_state = new Board(dim);
        this.id = playerId;
//        current_id = 0;
        other_invalidated = false;

        // get the number of cores.
        core_count = Runtime.getRuntime().availableProcessors();



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
//        allLegalMoves();
        if (other_invalidated){
            return current_state.closest(id).their_move();
        }

        MCTSNode root = new MCTSNode(current_state, id);

        long start = System.currentTimeMillis();
        try {
            for( int ii = 0; ii < core_count; ii++){
                new Thread(() ->{
                    while (System.currentTimeMillis() - start < timeout) {
                        root.search(0);
                    }
                }).start();
            }

            Thread.sleep(timeout + 5);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        MCTSNode.max_depth = 0;

//        try {
//
//            while (System.currentTimeMillis() - start < timeout) {
////            while (root.get_playouts() < 1000){
//                root.search(0);
//            }
//        }
//        catch (StackOverflowError error){
//            error.printStackTrace();
//
//            System.out.println(root.get_playouts());
//
//        }
        System.out.println(root.get_playouts() + "," +  MCTSNode.max_depth);

        root.diagnostics();

        MyMove toMake = root.get_next_move();



        System.out.println(toMake);

        if(toMake.id != id){
            System.out.println("DANGER");
        }

        System.out.println(MCTSNode.get_amaf(id, toMake.pos.x, toMake.pos.y));

//        MCTSNode.view_rave(id);

        return toMake.their_move();
    }

    @Override
    public boolean hasWonGame(int id) {
        return current_state.has_won(id);
    }

    @Override
    public List allLegalMoves() {
        List<MyMove> moves = current_state.allMoves(this.id);

//        for( MyMove move : moves){
//            System.out.println(move.their_move());
//        }

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
