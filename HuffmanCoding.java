public class HuffmanCoding {

    // Build the Huffman Tree
    public static HuffmanNode buildHuffmanTree(char[] characters, int[] frequencies) {
        MinHeap minHeap = new MinHeap(characters.length);

        // Insert all characters into the min-heap
        for (int i = 0; i < characters.length; i++) {
            minHeap.insert(new HuffmanNode(characters[i], frequencies[i]));
        }

        // Build the tree by combining the two nodes with the smallest frequencies
        while (minHeap.size() > 1) {
            HuffmanNode x = minHeap.deleteMin();
            HuffmanNode y = minHeap.deleteMin();

            HuffmanNode z = new HuffmanNode(x.frequency + y.frequency);
            z.leftChild = x;
            z.rightChild = y;

            minHeap.insert(z);
        }

        // The last remaining node is the root of the Huffman tree
        return minHeap.deleteMin();
    }

    // Utility function to print the Huffman Tree (for testing)
    public static void printHuffmanTree(HuffmanNode root, String prefix) {
        if (root == null) {
            return;
        }

        if (root.leftChild == null && root.rightChild == null) {
            System.out.println(root.character + ": " + prefix);
        }

        printHuffmanTree(root.leftChild, prefix + "0");
        printHuffmanTree(root.rightChild, prefix + "1");
    }

    public static void main(String[] args) {
        // Example: Characters and their frequencies
        char[] characters = {'A', 'B', 'C', 'D', 'E', 'F'};
        int[] frequencies = {5, 9, 12, 13, 16, 45};

        // Build the Huffman Tree
        HuffmanNode root = buildHuffmanTree(characters, frequencies);

        // Print the Huffman Tree (character with their corresponding binary codes)
        System.out.println("Huffman Codes:");
        printHuffmanTree(root, "");
    }
}
