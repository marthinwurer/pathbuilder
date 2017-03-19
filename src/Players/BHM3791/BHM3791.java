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

    private Visualization v_rave;

    @Override
    public void initPlayer(int dim, int playerId) {
        this.current_state = new Board(dim);
        this.id = playerId;
//        current_id = 0;
        other_invalidated = false;

        // get the number of cores.
        core_count = Runtime.getRuntime().availableProcessors();


        v_rave = new Visualization(current_state,"RAVE");
        v_rave.setVisible(true);
        v_rave.grey();

    }

    @Override
    public void lastMove(PlayerMove playerMove) {
        current_state.update(new MyMove(playerMove));
        v_rave.update_board(current_state);
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

        // reset the max depth for better diagnostics.
        MCTSNode.max_depth = 0;

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




        root.diagnostics();

        MyMove toMake = root.get_next_move();



        System.out.println(toMake);

        if(toMake.id != id){
            System.out.println("DANGER");
        }

        System.out.println(MCTSNode.get_amaf(id, toMake.pos.x, toMake.pos.y));

        v_rave.update_board(current_state);
        v_rave.setValues(root.get_RAVE_vals(id));

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
