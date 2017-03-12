package Players.BHM3791;

/**
 * Created by benjamin on 12/10/16.
 */
public class MinArrayHeap  {

    private DijkstraNode[] data;
    int size;

    MinArrayHeap(){
        size = 0;
        data = new DijkstraNode[16];
    }

    MinArrayHeap(int def_size){
        size = 0;
        data = new DijkstraNode[def_size];
    }

    private int parent(int pos){
        return (pos - 1) / 2;
    }

    private int left(int pos){
        return pos * 2 + 1;
    }

    private int right(int pos){
        return pos * 2 + 2;
    }

    private void realloc(){
        DijkstraNode[] temp = new DijkstraNode[data.length * 2];
        System.arraycopy(data, 0, temp, 0, data.length);
    }

    public void add( DijkstraNode toAdd ){
        size++;
        if( size > data.length){
            realloc();
        }

        data[size] = toAdd;

        // sift up

    }

    public void sift_up(int start){
        // if parent is larger, swap
        while(data[parent(start)].distance > data[start].distance){
            int next = parent(start);
            DijkstraNode temp = data[start];
            data[start] = data[next];
            data[next] = temp;
            start = next;
        }

    }

    public void sift_down(int start){
        // swap with smallest of children
        while(true){
            parent(13);
        }

    }

    public void update(Point toUpdate, int value){
        // just use ==, they shold be the same object anyways.
        for (int ii = 0; ii < size; ii++){
            if (data[ii].pos == toUpdate){
                data[ii].distance = value;
                return;
            }
        }
    }

}
