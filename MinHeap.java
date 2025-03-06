// Define the HuffmanNode class
public class MinHeap {
    private HuffmanNode[] heap;
    private int size;
    
    public MinHeap(int capacity) {
        heap = new HuffmanNode[capacity];
        size = 0;
    }

    // Helper function to swap two nodes in the heap
    private void swap(int i, int j) {
        HuffmanNode temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Get the index of the left child of the node at index i
    private int leftChild(int i) {
        return 2 * i + 1;
    }

    // Get the index of the right child of the node at index i
    private int rightChild(int i) {
        return 2 * i + 2;
    }

    // Get the index of the parent of the node at index i
    private int parent(int i) {
        return (i - 1) / 2;
    }

    // Ensure the min-heap property is maintained by heapifying down
    private void heapifyDown(int i) {
        int left = leftChild(i);
        int right = rightChild(i);
        int smallest = i;

        // Compare with left child
        if (left < size && heap[left].frequency < heap[smallest].frequency) {
            smallest = left;
        }

        // Compare with right child
        if (right < size && heap[right].frequency < heap[smallest].frequency) {
            smallest = right;
        }

        // If smallest is not i, swap and heapify down further
        if (smallest != i) {
            swap(i, smallest);
            heapifyDown(smallest);
        }
    }

    // Ensure the min-heap property is maintained by heapifying up
    private void heapifyUp(int i) {
        while (i > 0 && heap[parent(i)].frequency > heap[i].frequency) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    // Insert a node into the min-heap
    public void insert(HuffmanNode node) {
        if (size >= heap.length) {
            // If heap is full, increase the size (optional, depends on use case)
            resize();
        }
        heap[size] = node;
        size++;
        heapifyUp(size - 1);
    }

    // Resize the heap array when needed (optional, if dynamic size is required)
    private void resize() {
        HuffmanNode[] newHeap = new HuffmanNode[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, heap.length);
        heap = newHeap;
    }

    // Remove the minimum element (root of the heap)
    public HuffmanNode deleteMin() {
        if (size <= 0) {
            throw new IllegalStateException("Heap is empty");
        }

        // The root is the minimum element
        HuffmanNode minNode = heap[0];

        // Replace the root with the last node
        heap[0] = heap[size - 1];
        size--;

        // Restore the heap property by heapifying down from the root
        heapifyDown(0);

        return minNode;
    }

    // Check if the heap is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Return the size of the heap
    public int size() {
        return size;
    }
}

