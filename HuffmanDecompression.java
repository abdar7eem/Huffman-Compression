import java.io.*;

public class HuffmanDecompression {
    static int totalBitsRead;
    static int totalDecodedCharacters;
    static StringBuilder fileName;
    static StringBuilder fileExtension;
    static int[] frequencies;
    static BitInputStream bitIn;
    static StringBuilder stats = new StringBuilder();
    static StringBuilder freqState;

    // Method to read the fiel to decompress it 
    public static void readDecompressFile(String inputFile) throws IOException {
        try {
            bitIn = new BitInputStream(inputFile);

            // Read the file name
            int fileNameLength = bitIn.readInt();
            fileName = new StringBuilder(fileNameLength);
            for (int i = 0; i < fileNameLength; i++) {
                fileName.append((char) bitIn.readByte());
            }

            // Read the file Extintion
            int fileExtensionLength = bitIn.readInt();
            fileExtension = new StringBuilder(fileExtensionLength);
            for (int i = 0; i < fileExtensionLength; i++) {
                fileExtension.append((char) bitIn.readByte());
            }

            // Read the frequency table from the header
            int frequencyTableSize = bitIn.readInt();

            freqState = new StringBuilder();
            freqState.append("\n The frequencyTable is: \n");
            frequencies = new int[256];
            for (int i = 0; i < frequencyTableSize; i++) {
                int character = bitIn.readByte();
                int frequency = bitIn.readInt();
                frequencies[character] = frequency;

                char x = (char) character;
                freqState.append(x + ":" + frequency + "\n");
            }
        } catch (IOException e) {

            throw e;
        }
    }

    // Method to write the file after decompresion
    public static void writeDecompressFile(String path) throws IOException {
        try (
                FileOutputStream fileOut = new FileOutputStream(
                        path + File.separator + fileName.toString() + "." + fileExtension.toString())) {
            
            // Build Huffman tree
            HuffmanNode root = HuffmanCompression.buildHuffmanTree(frequencies);
            HuffmanNode currentNode = root;
            totalBitsRead = 0;
            totalDecodedCharacters = 0;

            byte[] readBuffer = new byte[8]; // Buffer for reading
            byte[] writeBuffer = new byte[8]; // Buffer for writing
            int writeBufferIndex = 0; // Index for writeBuffer
            int bytesRead = 0;

            while ((bytesRead = bitIn.readByte(readBuffer)) != -1) {
                for (int i = 0; i < bytesRead * 8; i++) {
                    int bit = (readBuffer[i / 8] >> (7 - (i % 8))) & 1; // Get the byte and extract it's bits values
                    totalBitsRead++;

                    if (bit == 0) {
                        currentNode = currentNode.leftChild;
                    } else {
                        currentNode = currentNode.rightChild;
                    }

                    // Check if the node is leaf node
                    if (currentNode.leftChild == null && currentNode.rightChild == null) {
                        char decodedChar = (char) currentNode.character;

                        // Add decoded character to the write buffer
                        writeBuffer[writeBufferIndex] = (byte) decodedChar;
                        writeBufferIndex++;

                        totalDecodedCharacters++;

                        // When the buffer is full (8 bytes) we write to the file and reset the index
                        if (writeBufferIndex == 8) {
                            fileOut.write(writeBuffer);
                            writeBufferIndex = 0; 
                        }
                        currentNode = root;
                    }
                }
            }

            // Write any remaining data in the writeBuffer if the buffer is not full
            if (writeBufferIndex > 0) {
                fileOut.write(writeBuffer, 0, writeBufferIndex);
            }

            File decompressedFile = new File(
                    path + File.separator + fileName.toString() + "." + fileExtension.toString());
            long decompressedFileSize = decompressedFile.length();

            stats.setLength(0);
            stats.append("Decompression Statistics:\n");
            stats.append("Total Bits Read: ").append(totalBitsRead).append("\n");
            stats.append("Total Characters Decoded: ").append(totalDecodedCharacters).append("\n");
            stats.append("Decompressed File Size: ").append(decompressedFileSize).append(" bytes\n");
            stats.append(freqState.toString() + "\n");

        } catch (IOException e) {
            throw e;
        }
    }

    public static String getDecompressionStatistics() {
        return stats.toString();
    }

}
