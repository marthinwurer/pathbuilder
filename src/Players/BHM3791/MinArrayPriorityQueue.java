package Players.BHM3791;

/**
 * Created by benjamin on 3/12/17.
 */
public class MinArrayPriorityQueue {

    private DijkstraNode[] data;
    private int size;

    public MinArrayPriorityQueue(){
        this(16);
    }

    public MinArrayPriorityQueue(int s){
        data = new DijkstraNode[s];
        size = 0;

    }

    private void realloc(){
        DijkstraNode[] temp = new DijkstraNode[data.length * 2];
        System.arraycopy(data, 0, temp, 0, data.length);
        data = temp;
    }

    private void sift_forward(int current){

        while (current > 0 && data[current].distance < data[current - 1].distance){
            DijkstraNode temp = data[current];
            data[current] = data[current - 1];
            data[current - 1] = temp;
            current--;
        }

    }

    private void sift_backward(int current){

        while (current < size - 1 && data[current].distance > data[current + 1].distance){
            DijkstraNode temp = data[current];
            data[current] = data[current + 1];
            data[current + 1] = temp;
            current++;
        }

    }

    private void insert_front(DijkstraNode item){
        int current = 0;
        while (!(data[current] == null || data[current].distance > item.distance)){
            current ++;
        }
        DijkstraNode temp = item;

        while (temp != null && current < size){
            temp = data[current];
            data[current] = item;
            item = temp;
            current++;
        }
    }

    public void enqueue(DijkstraNode toEnqueue){
        size ++;
        if(size >= data.length){
            realloc();
        }
        insert_front(toEnqueue);

    }



    public DijkstraNode peek(){
        return data[0];
    }

    public DijkstraNode pop(){
        DijkstraNode toReturn = data[0];

        size --;

        // shift all the nodes one forwards
        DijkstraNode temp;

        for(int index = 0; index < size; index ++){
            data[index] = data[index + 1];
        }

        // clear the last spot in the queue
        data[size] = null;

        return toReturn;
    }

    public int getSize(){
        return size;
    }

    public void update(Point toUpdate, int value){
        for (int ii = 0; ii < size; ii++){

            // if the node has a position, and the position is eqaul, then update it.
            if (data[ii].pos != null && data[ii].pos.equals(toUpdate)) {
                int old_val = data[ii].distance;
                data[ii].distance = value;
                // now that it has an updated value, sift it through the queue
                if(value < old_val){
                    sift_forward(ii);
                }else{
                    sift_backward(ii);
                }

                return;
            }


        }
    }

}
