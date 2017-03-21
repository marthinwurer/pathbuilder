package Players.BHM3791;

import javax.swing.*;
import java.awt.*;

/**
 * Created by benjamin on 3/15/17.
 */
public class Visualization extends JFrame{

    private Board board;

    private float[][] values;

    private int size;

    public Visualization(Board gamestate){
        this(gamestate, "Visualization");
    }

    public Visualization(Board gamestate, String title){
        board = new Board(gamestate);
        size = gamestate.get_dimension() * 2 + 1;
        values = new float[size][size];

        setSize(size* 16, size * 16);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(title);

    }

    @Override
    public void paint(Graphics g){
        super.paint(g); // Call its parent for proper rendering.

        for (int yy = 0; yy < size; yy++) {
            for (int xx = 0; xx < size; xx++) {

                // select the color
                Color c;

                int val = board.value(xx, yy);
                if( val != 0) {
                    if (val == 1) {
                        c = Color.RED;
                    } else {
                        c = Color.BLUE;
                    }
                }else{
                    // do the center tiles
                    if( xx % 2 != yy %2){
                        if( xx % 2 == 0){
                            c = Color.RED;
                        } else {
                            c = Color.BLUE;
                        }

                    }else{
                        try{
                            c = new Color(values[yy][xx], values[yy][xx], values[yy][xx]);
                        }catch (IllegalArgumentException e){
//                            e.printStackTrace();
                            System.out.println("Illegal value: " + values[yy][xx]);
                            c = Color.CYAN;
                        }
                    }
                }

                g.setColor(c);
                int x_corner = xx * 16;
                int y_corner = yy * 16;
                g.fillRect(x_corner, y_corner,15,15);
            }
        }
    }

    public void update_board(Board gamestate){
        board = new Board(gamestate);
        repaint();
    }

    public void setValues(float[][] newvalues){

        // find the min and max of the new values (and ignore zero)
        float max = Float.NEGATIVE_INFINITY;
        float min = Float.POSITIVE_INFINITY;
        for (int yy = 0; yy < size; yy++) {
            for (int xx = 0; xx < size; xx++) {
                float next = newvalues[yy][xx];
                // calculate min and max
                if (next != 0) {
                    if (next > max) {
                        max = next;
                    } else if (next < min) {
                        min = next;
                    }
                }
            }
        }

        float difference = max - min;
        for (int yy = 0; yy < size; yy++) {
            for (int xx = 0; xx < size; xx++) {
                values[yy][xx] = (newvalues[yy][xx] - min) / difference;
                if(newvalues[yy][xx] == 0.0f){
                    values[yy][xx] = 0.0f;
                }
            }
        }

        repaint();
    }

    public void grey(){
        for (int yy = 0; yy < size; yy++) {
            for (int xx = 0; xx < size; xx++) {
                values[yy][xx] = 0.5f;
            }
        }
    }


}
