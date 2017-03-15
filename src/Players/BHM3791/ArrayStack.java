package Players.BHM3791;

/**
 * Created by benjamin on 3/14/17.
 */
public class ArrayStack {

    private int size;
    private Point[] stack;
    private int top;

    public ArrayStack(){
        this(16);
    }

    public ArrayStack(int initial){
        stack = new Point[initial];
        top = 0;
        size = 0;
    }

    private void realloc(){
        Point[] temp = new Point[stack.length * 2];
        System.arraycopy(stack, 0, temp, 0, stack.length);
        stack = temp;
    }

    public void push(Point data){
        if(size == stack.length){
            realloc();
        }
        stack[size] = data;
        size ++;
    }

    public Point pop(){
        size--;
        Point data = stack[size];
        stack[size] = null;
        return data;
    }

    public int get_size(){
        return size;
    }
}
