package Players.BHM3791;

import Interface.PlayerModule;
import Interface.PlayerModulePart1;
import Interface.PlayerMove;

/**
 * Created by benjamin on 3/11/17.
 */
public class BHM3791 implements PlayerModule, PlayerModulePart1 {
    private Board current_state;
    private int id;
    private boolean other_invalidated;
    @Override
    public void initPlayer(int dim, int playerId) {
        this.current_state = new Board(dim);
        this.id = playerId;
        other_invalidated = false;


    }

    @Override
    public void lastMove(PlayerMove playerMove) {

    }

    @Override
    public void otherPlayerInvalidated() {
        other_invalidated = true;
    }

    @Override
    public PlayerMove move() {
        return null;
    }

    @Override
    public boolean hasWonGame(int i) {
        return false;
    }
}
