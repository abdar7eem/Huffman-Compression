class HuffmanNode {
    char character;
    int frequency;
    HuffmanNode leftChild, rightChild;

    // Constructor for leaf nodes
    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.leftChild = null;
        this.rightChild = null;
    }

    // Constructor for internal nodes
    public HuffmanNode(int frequency) {
        this.character = '\0';  // internal node does not have a character
        this.frequency = frequency;
        this.leftChild = null;
        this.rightChild = null;
    }
}