package Players.BHM3791;

import java.lang.Math;

/**
 * Created by benjamin on 12/10/16.
 */
public class Point {
    public final int x;
    public final int y;

    public static Point[][] created = new Point[0][0];
    public static int greatest = 0;


    /**
     * Private constructor to make sure that this is not misused. these should all be singleton.
     * @param xx
     * @param yy
     */
    private Point( int xx, int yy){
        this.x = xx;
        this.y = yy;
    }

    /**
     * get the point that represents the given position.
     * @param xx
     * @param yy
     * @return
     */
    public static Point getPoint( int xx, int yy){
        try{
            return created[yy][xx];
        }
        catch (IndexOutOfBoundsException e){

            // if a point asked was out of range, generate the table or throw an error.
            if( xx < 0 || yy < 0){
                throw new IllegalArgumentException("cannot have negative points");
            }
            init(Math.max(xx, yy) + 1);
            return getPoint(xx, yy);
        }

    }

    public static void init(int dimension){
        if( dimension > greatest) {
            Point[][] temp = new Point[dimension][dimension];

            // copy all the old objects to the new array.
            for (int yy = 0; yy < greatest; yy++){
                for( int xx = 0; xx < greatest; xx++){
                    temp[yy][xx] = created[yy][xx];
                }
            }


            greatest = dimension;
            // add the new objects to the array.
            for (int yy = 0; yy < greatest; yy++){
                for( int xx = 0; xx < greatest; xx++){
                    if (temp[yy][xx] == null){
                        temp[yy][xx] = new Point(xx, yy);
                    }
                }
            }

            // swap the arrays.
            created = temp;
        }

    }

    public boolean equals(Point p){
        return this.x == p.x && this.y == p.y;
    }

    public String toString(){
        return "(" + x + ", " + y + ")";
    }

    public Point north(int offset){
        return getPoint(x, y - offset);
    }

    public Point south(int offset){
        return getPoint(x, y + offset);
    }

    public Point east(int offset){
        return getPoint(x + offset, y);
    }

    public Point west(int offset){
        return getPoint(x - offset, y);
    }

}
