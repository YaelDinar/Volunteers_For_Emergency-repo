package Project.Model;

public class PriorityQueue {
    private MaxHeap heap;

    public PriorityQueue() {
        this.heap = new MaxHeap();
    }

    // Inserts an item into the priority queue
    public void enqueue(emergency item) {
        heap.insert(item);
    }

    // Removes and returns the highest priority item
    public emergency dequeue() {
        return heap.extractMax();
    }

    // Returns the highest priority item without removing it
    public emergency peek() {
        return heap.getMax();
    }
    

    // Returns true if the priority queue is empty
    public boolean isEmpty() {
        return heap.getSize() == 0;
    }

    // Returns the number of items in the priority queue
    public int size() {
        return heap.getSize();
    }

    // For debugging: prints the entire heap
    public void print() {
        heap.print();
    }
}
