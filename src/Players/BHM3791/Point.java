package Players.BHM3791;

/**
 * Created by benjamin on 12/10/16.
 */
public class Point {
    public int x;
    public int y;

    public static Point[][] created = new Point[0][0];
    public static int greatest = 0;


    public Point( int xx, int yy){
        this.x = xx;
        this.y = yy;
    }

    public static void init(int dimension){
        if( dimension > greatest) {
            Point[][] temp = new Point[dimension][dimension];
            for (int yy = 0; yy < greatest; yy++){
                for( int xx = 0; xx < greatest; xx++){
                    temp[yy][xx] = created[yy][xx];
                }
                temp[yy][greatest + 1] = new Point(greatest + 1, yy);


            }


            greatest = dimension;
            // last row
            for( int xx = 0; xx < greatest; xx++){
                temp[greatest][xx] = new Point(xx, greatest);
            }
            created = temp;
        }

    }

}
