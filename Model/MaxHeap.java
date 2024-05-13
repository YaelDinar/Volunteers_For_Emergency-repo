package Project.Model;

import java.util.ArrayList;

public class MaxHeap {
    private ArrayList<emergency> Heap;
    private int size;
 
    // Constructor to initialize an
    // empty max heap
    public MaxHeap()
    {
        this.Heap = new ArrayList<>();
        this.size = 0;
    }

    // Returning position of parent
    private int parent(int pos) { return (pos - 1) / 2; }
 
    // Returning left children
    private int leftChild(int pos) { return (2 * pos) + 1; }
 
    // Returning right children
    private int rightChild(int pos) { return (2 * pos) + 2; }
 
    // Returning true if given node is leaf
    private boolean isLeaf(int pos) {
        return pos >= (size / 2) && pos < size;
    }
    
    // Swapping nodes
    private void swap(int fpos, int spos)
    {
        emergency tmp = Heap.get(fpos);
        Heap.set(fpos, Heap.get(spos));
        Heap.set(spos, tmp);
    }
 
    // Recursive function to max heapify given subtree
    private void maxHeapify(int pos) {
        if (!isLeaf(pos)) {
            boolean hasLeftChild = leftChild(pos) < size;
            boolean hasRightChild = rightChild(pos) < size;
    
            boolean shouldSwapWithLeft = hasLeftChild && Heap.get(pos).getPriority() < Heap.get(leftChild(pos)).getPriority();
            boolean shouldSwapWithRight = hasRightChild && Heap.get(pos).getPriority() < Heap.get(rightChild(pos)).getPriority()
                                          && (!hasLeftChild || Heap.get(leftChild(pos)).getPriority() < Heap.get(rightChild(pos)).getPriority());
    
            if (shouldSwapWithLeft || shouldSwapWithRight) {
                if (shouldSwapWithLeft && (!hasRightChild || Heap.get(leftChild(pos)).getPriority() >= Heap.get(rightChild(pos)).getPriority())) {
                    swap(pos, leftChild(pos));
                    maxHeapify(leftChild(pos));
                } else if (shouldSwapWithRight) {
                    swap(pos, rightChild(pos));
                    maxHeapify(rightChild(pos));
                }
            }
        }
    }
    
    // Inserts a new element to max heap
    public void insert(emergency element)
    {
        Heap.add(element);
        int current = size;
        while (Heap.get(current).getPriority() > Heap.get(parent(current)).getPriority()) {
            swap(current, parent(current));
            current = parent(current);
        }
        size++;
    }
 
    // To display heap
    public void print()
    {
        for (int i = 0; i < size / 2; i++) {
            System.out.print("Parent Node : " + Heap.get(i));
            if (leftChild(i) < size)
                System.out.print(" Left Child Node: " + Heap.get(leftChild(i)));
            if (rightChild(i) < size)
                System.out.print(" Right Child Node: " + Heap.get(rightChild(i)));
            System.out.println();
        }
    }
 
    // Remove an element from max heap
    public emergency extractMax()
    {
        if (size == 0)
            return null; // Or throw an exception if appropriate

        emergency popped = Heap.get(0);
        Heap.set(0, Heap.get(size - 1));
        Heap.remove(size - 1);
        size--;
        maxHeapify(0);
        return popped;
    }

    public int getSize() {
        return this.size;
    }

    public emergency getMax() {
        if (size > 0) {
            return Heap.get(0);
        }
        return null; // Or throw an exception if appropriate
    }
}
