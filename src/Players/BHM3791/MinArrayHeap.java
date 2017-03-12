package Players.BHM3791;

/**
 * Created by benjamin on 12/10/16.
 */
public class MinArrayHeap  {

    private DijkstraNode[] data;
    int size;

    public MinArrayHeap(){
        size = 0;
        data = new DijkstraNode[16];
    }

    public MinArrayHeap(int def_size){
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
        data = temp;
    }

    public void add( DijkstraNode toAdd ){

        if( size + 1 >= data.length){
            realloc();
        }

        data[size] = toAdd;
        // sift it up.
        sift_up(size);
        size++;
    }

    private void sift_up(int start){
        // if parent is larger, swap
        while(data[parent(start)].distance > data[start].distance){
            int next = parent(start);
            DijkstraNode temp = data[start];
            data[start] = data[next];
            data[next] = temp;
            start = next;
        }

    }

    private void sift_down(int start){
        // swap with smallest of children
        int current = start;
        while(true){
            int left_c = left(current);
            // if there is no left, then this is at a leaf position. We are done, short circuit.
            if(data[left_c] == null){
                break;
            }
            int right_c = right(current);
            int next = left_c;
            boolean swap = false;

            // if only one child, it will automatically be next.
            if( data[right_c] == null){
                next = left_c;
            }
            else{
                // if two children, pick the smallest.
                if( data[left_c].distance < data[right_c].distance){
                    next = left_c;
                } else {
                    next = right_c;
                }
            }

            // check if the swap needs to occur
            if( data[current].distance > data[next].distance) {
                DijkstraNode temp = data[start];
                data[start] = data[next];
                data[next] = temp;
                current = next;
            } else {
                // if no swap needed, this is sifted down far enough.
                break;
            }
        }
    }

    /**
     * update can't work, sifting does not work in the way that I would like
     * it to. I would need to re-heapify everything every time I update. I
     * think that I'm just going to implement a nice arrayPriorityQueue.
     *
     */
//
//    public void update(Point toUpdate, int value){
//        // just use ==, they shold be the same object anyways.
//        for (int ii = 0; ii < size; ii++){
//            try {
//
//                if (data[ii].pos.equals(toUpdate)) {
//                    data[ii].distance = value;
//                    // now that it has an updated value, sift it up the tree.
//                    sift_up(ii);
//                    return;
//                }
//            }
//            catch(NullPointerException e){
//                continue;
//            }
//        }
//    }

    /**
     * return the value of the first item in the heap.
     * @return
     */
    public DijkstraNode peek(){
        return data[0];
    }

    public DijkstraNode pop(){
        DijkstraNode toReturn = data[0];
        size --;
        data[0] = data[size];
        data[size] = null;
        sift_down(0);
        return toReturn;
    }

    public int getSize(){
        return size;
    }



}
