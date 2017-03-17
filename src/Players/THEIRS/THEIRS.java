package Players.THEIRS;
//
// Source code recreated from dimension .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import Interface.Coordinate;
import Interface.PlayerModule;
import Interface.PlayerMove;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This is the toughplayer class.
 */
public final class THEIRS implements PlayerModule {
    private int dimension;
    private int[][] board;
    private int id;

    public THEIRS() {
    }

    public final void otherPlayerInvalidated() {
    }

    public final void lastMove(PlayerMove var1) {
        Coordinate var2 = var1.getCoordinate();
        this.board[var2.getRow()][var2.getCol()] = var1.getPlayerId();
    }

    public final void initPlayer(int dim, int id) {
        this.dimension = 2 * dim + 1;
        this.id = id;
        this.board = new int[this.dimension][this.dimension];

        for(dim = 0; dim < this.dimension; ++dim) {
            for(id = 0; id < this.dimension; ++id) {
                if(dim % 2 + id % 2 == 1) {
                    this.board[dim][id] = -1;
                } else if(dim != 0 && dim != this.dimension - 1 && id != 0 && id != this.dimension - 1) {
                    this.board[dim][id] = 0;
                } else {
                    this.board[dim][id] = -1;
                }
            }
        }

    }

    public final PlayerMove move() {
        THEIRS var1 = this;
        ArrayList<PlayerMove> allmoves = new ArrayList<>();

        for(int var3 = 1; var3 < var1.dimension - 1; ++var3) {
            for(int var4 = 1; var4 < var1.dimension - 1; ++var4) {
                if(var3 % 2 == var4 % 2 && var1.board[var3][var4] == 0) {
                    PlayerMove var5 = new PlayerMove(new Coordinate(var3, var4), var1.id);
                    allmoves.add(var5);
                }
            }
        }

        Interface.a var13 = this.a(this.id);
        Interface.a var14 = this.a(3 - this.id);
        ArrayList<PlayerMove> var15 = new ArrayList<>();
        Interface.a var16 = new Interface.a(-1, -1);
        int var6 = 2147483647;

        Coordinate current_coord;
        for(Iterator var12 = allmoves.iterator(); var12.hasNext(); this.board[current_coord.getRow()][current_coord.getCol()] = 0) {
            PlayerMove current_move;
            current_coord = (current_move = (PlayerMove)var12.next()).getCoordinate();
            this.board[current_coord.getRow()][current_coord.getCol()] = this.id;
            Interface.a var9 = this.a(this.id);
            Interface.a var10 = this.a(3 - this.id);
            if((Integer) (var9 = new Interface.a(
                    (Integer) var13.a() - (Integer) var9.a(),
                    (Integer) var10.a() - (Integer) var14.a()
                        )
                    ).a() + (Integer) var9.b() > (Integer) var16.a() + (Integer) var16.b()) {
                (var15 = new ArrayList<>()).add(current_move);
                var16 = var9;
                var6 = (Integer) var10.b();
            } else if((Integer) var9.a() + (Integer) var9.b() == (Integer) var16.a() + (Integer) var16.b() && (Integer) var9.b() > (Integer) var16.b()) {
                (var15 = new ArrayList<>()).add(current_move);
                var16 = var9;
                var6 = (Integer) var10.b();
            } else if(((Integer)var9.a()).equals(var16.a()) && ((Integer)var9.b()).equals(var16.b())) {
                if((Integer) var10.b() < var6) {
                    (var15 = new ArrayList<>()).add(current_move);
                    var16 = var9;
                    var6 = (Integer) var10.b();
                } else if((Integer) var10.b() == var6) {
                    var15.add(current_move);
                }
            }
        }

        Collections.shuffle(var15);
        return (PlayerMove)var15.get(0);
    }

    private Interface.a a(int var1) {
        HashMap var2 = new HashMap();
        HashMap var3 = new HashMap();
        this.a(var1, var2, var3);
        int var7 = 2147483647;
        int var4 = 0;

        for(int var5 = 1; var5 < this.dimension; var5 += 2) {
            Coordinate var6;
            if(var1 == 1) {
                var6 = new Coordinate(var5, this.dimension - 1);
                if(var2.containsKey(var6)) {
                    if((Integer) var2.get(var6) < var7) {
                        var7 = (Integer) var2.get(var6);
                        var4 = 1;
                    } else if((Integer) var2.get(var6) == var7) {
                        ++var4;
                    }
                }
            } else {
                var6 = new Coordinate(this.dimension - 1, var5);
                if(var2.containsKey(var6)) {
                    if((Integer) var2.get(var6) < var7) {
                        var7 = (Integer) var2.get(var6);
                        var4 = 1;
                    } else if((Integer) var2.get(var6) == var7) {
                        ++var4;
                    }
                }
            }
        }

        return new Interface.a(var7, var4);
    }

    private ArrayList a() {
        ArrayList var1 = new ArrayList();

        for(int var2 = 1; var2 < this.dimension - 1; ++var2) {
            for(int var3 = 1; var3 < this.dimension - 1; ++var3) {
                if(var2 % 2 == var3 % 2 && this.board[var2][var3] == 0) {
                    PlayerMove var4 = new PlayerMove(new Coordinate(var2, var3), this.id);
                    var1.add(var4);
                }
            }
        }

        return var1;
    }

    private static Coordinate a(HashSet var0, HashMap var1) {
        int var2 = 2147483647; // maxint
        Coordinate var3 = null;
        Iterator var5 = var0.iterator();

        while(var5.hasNext()) {
            Coordinate var4 = (Coordinate)var5.next();
            if(var1.containsKey(var4) && (Integer) var1.get(var4) < var2) {
                var2 = (Integer) var1.get(var4);
                var3 = var4;
            }
        }

        return var3;
    }

    private void a(int var1, HashMap var2, HashMap var3) {
        Coordinate var5;
        for(int var4 = 1; var4 < this.dimension; var4 += 2) {
            if(var1 == 1) {
                var5 = new Coordinate(var4, 0);
                var2.put(var5, 0);
                var3.put(var5, var5);
            } else {
                var5 = new Coordinate(0, var4);
                var2.put(var5, 0);
                var3.put(var5, var5);
            }
        }

        HashSet var12 = new HashSet();
        byte var6;
        byte var13;
        if(var1 == 1) {
            var13 = 1;
            var6 = 0;
        } else {
            var13 = 0;
            var6 = 1;
        }

        int var7;
        for(int var14 = var13; var14 < this.dimension; var14 += 2) {
            for(var7 = var6; var7 < this.dimension; var7 += 2) {
                var12.add(new Coordinate(var14, var7));
            }
        }

        while(!var12.isEmpty()) {
            if((var5 = a(var12, var2)) == null) {
                return;
            }

            var12.remove(var5);
            var7 = var5.getRow();
            int var15 = var5.getCol();
            ArrayList var8 = new ArrayList();
            if(var7 > 1) {
                var8.add(new Coordinate(var7 - 2, var15));
            }

            if(var7 < this.dimension - 2) {
                var8.add(new Coordinate(var7 + 2, var15));
            }

            if(var15 > 1) {
                var8.add(new Coordinate(var7, var15 - 2));
            }

            if(var15 < this.dimension - 2) {
                var8.add(new Coordinate(var7, var15 + 2));
            }

            Iterator var16 = var8.iterator();

            while(var16.hasNext()) {
                Coordinate var9 = (Coordinate)var16.next();
                int var10 = (var7 + var9.getRow()) / 2;
                int var11 = (var15 + var9.getCol()) / 2;
                byte var17;
                if(this.board[var10][var11] == 0) {
                    var17 = 1;
                } else if(this.board[var10][var11] == var1) {
                    var17 = 0;
                } else {
                    var17 = -1;
                }

                if(var17 >= 0) {
                    if(!var2.containsKey(var9)) {
                        var2.put(var9, (Integer) var2.get(var5) + var17);
                        var3.put(var9, var5);
                    } else if((var10 = (Integer) var2.get(var5) + var17) < (Integer) var2.get(var9)) {
                        var2.put(var9, var10);
                        var3.put(var9, var5);
                    }
                }
            }
        }

    }
}

