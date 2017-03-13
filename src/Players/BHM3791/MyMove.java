package Players.BHM3791;

import Interface.PlayerMove;

/**
 * Created by benjamin on 3/13/17.
 */
public class MyMove {
    public Point pos;
    public int id;

    public MyMove(int x, int y, int id){
        this(Point.getPoint(x, y), id);
    }

    public MyMove(Point p, int id){
        this.pos = p;
        this.id = id;
    }

    public MyMove(PlayerMove move){
        this.pos = Point.convert_coord(move.getCoordinate());
        this.id = move.getPlayerId();
    }

    public PlayerMove their_move(){
        return new PlayerMove(pos.their_coord(), id);
    }
}
