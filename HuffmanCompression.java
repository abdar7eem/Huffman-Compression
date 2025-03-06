
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class HuffmanCompression {
    static String fileName;
    static int dotIndex;
    static int[] frequencies;
    static String[] huffmanCodes;
    static StringBuilder header=new StringBuilder();

    // Method to build Huffman tree
    public static HuffmanNode buildHuffmanTree(int[] frequencies) {
        MinHeap minHeap = new MinHeap(frequencies.length);
        for (char c = 0; c < 256; c++) {
            if (frequencies[c] > 0) {
                minHeap.insert(new HuffmanNode(c, frequencies[c]));
            }
        }

        while (minHeap.size() > 1) {
            HuffmanNode x = minHeap.deleteMin();
            HuffmanNode y = minHeap.deleteMin();

            HuffmanNode z = new HuffmanNode(x.frequency + y.frequency);
            z.leftChild = x;
            z.rightChild = y;

            minHeap.insert(z);
        }

        return minHeap.deleteMin();
    }

    // Method to generate Huffman codes
    public static void generateHuffmanCodes(HuffmanNode root, String[] codes, String currentCode) {
        if (root == null) {
            return;
        }

        if (root.leftChild == null && root.rightChild == null) {
            codes[root.character] = currentCode;
        }

        generateHuffmanCodes(root.leftChild, codes, currentCode + "0"); // If left add 0
        generateHuffmanCodes(root.rightChild, codes, currentCode + "1");// If right add 1
    }

    // Method to compress the file
    public static void readCompressFile(File inputFile) throws IOException {
        frequencies = new int[256];

        try (FileInputStream fileInput = new FileInputStream(inputFile)) {
            byte[] buffer = new byte[8];
            int bytesRead;
            while ((bytesRead = fileInput.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    int byteValue;
                    if (buffer[i] < 0) { // This if satatment is to check if the bit is positive because values are unsigned
                        byteValue = buffer[i] + 256;
                    } else {
                        byteValue = buffer[i];
                    }
                    frequencies[byteValue]++;
                }
            }
        }
        HuffmanNode root = buildHuffmanTree(frequencies);

        huffmanCodes = new String[256];
        generateHuffmanCodes(root, huffmanCodes, "");

        fileName = inputFile.getName();
        dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            fileName = fileName.substring(0, dotIndex);
        }

    }

    // Method to write the file after compression
    public static void writeCompressFile(File inputFile, String path) {
        try (BitOutputStream bitOut = new BitOutputStream(path + File.separator + fileName + ".huff")) {

            // Write the file name
            bitOut.writeInt(fileName.length());
            for (char ch : fileName.toCharArray()) {
                bitOut.writeByte((byte) ch);
            }

            // write the file extintion
            String fileExtension = "";
            if (dotIndex > 0 && dotIndex < inputFile.length() - 1) {
                fileExtension = inputFile.getName().substring(dotIndex + 1);
            }
            bitOut.writeInt(fileExtension.length());
            for (char ch : fileExtension.toCharArray()) {
                bitOut.writeByte((byte) ch);
            }

            // write the frequency table
            int frequencyTableSize = 0;
            for (int frequency : frequencies) {
                if (frequency > 0) {
                    frequencyTableSize++;
                }
            }
            bitOut.writeInt(frequencyTableSize);

            writeFrequencyTable(frequencies, bitOut);

            // write the file data
            try (FileInputStream fileInput = new FileInputStream(inputFile)) {
                byte[] readBuffer = new byte[8];
                int bytesRead;

                int writeBuffer = 0; // Buffer for storing bits
                int bitsInBuffer = 0; // Count for bits in the buffer

                while ((bytesRead = fileInput.read(readBuffer)) != -1) {
                    for (int i = 0; i < bytesRead; i++) {
                        int byteValue;
                        if (readBuffer[i] < 0) { // This if statment is to check if the bit value is positive because the values are unsigned
                            byteValue = readBuffer[i] + 256;
                        } else {
                            byteValue = readBuffer[i];
                        }

                        String code = huffmanCodes[byteValue];
                        for (char bit : code.toCharArray()) {
                            writeBuffer = writeBuffer << 1;
                            if (bit == '1') { // check if the bit value is 0 or 1
                                writeBuffer = writeBuffer | 1;
                            } else {
                                writeBuffer = writeBuffer | 0;
                            }
                            bitsInBuffer++;

                            // If the buffer is full then we will write the buffer and rest the buffers  
                            if (bitsInBuffer == 8) {
                                bitOut.writeByte((byte) writeBuffer);
                                writeBuffer = 0;
                                bitsInBuffer = 0;
                            }
                        }
                    }
                }

                // Write remaining bits in the buffer if it is not full
                if (bitsInBuffer > 0) {
                    writeBuffer <<= (8 - bitsInBuffer); // Tell us how many bits are in the buffer and pad it
                    bitOut.writeByte((byte) writeBuffer);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Method to write the frequency table in the header
    private static void writeFrequencyTable(int[] frequencies, BitOutputStream bitOut) throws IOException {
        for (int i = 0; i < 256; i++) {
            if (frequencies[i] > 0) {
                bitOut.writeByte(i);
                bitOut.writeInt(frequencies[i]);
            }
        }
    }

    // Method to get statistects about the compressed file
    public static String getCompressionStatistics(File inputFile, File compressedFile, String[] huffmanCodes,
            int[] frequencies) throws IOException {
        StringBuilder sb = new StringBuilder();

        long originalFileSize = inputFile.length();
        long compressedFileSize = compressedFile.length();

        double compressionRatio = (double) originalFileSize / compressedFileSize;

        sb.append("Compression Statistics:\n");
        sb.append("Original File Size: ").append(originalFileSize).append(" bytes\n");
        sb.append("Compressed File Size: ").append(compressedFileSize).append(" bytes\n");
        sb.append(String.format("Compression Ratio: %.2f\n", compressionRatio));

        sb.append("\nCharacter Frequencies (before compression):\n");
        for (char i = 0; i < 256; i++) {
            if (frequencies[i] > 0) {
                sb.append("Character: '").append((char) i).append("' | Frequency: ").append(frequencies[i])
                        .append("\n");
            }
        }
        sb.append("\n*********** *********** ***********\n");

        sb.append("\nHuffman Codes (after compression):\n");
        for (char i = 0; i < 256; i++) {
            if (frequencies[i] > 0) {
                sb.append("Character: '").append((char) i).append("' | Huffman Code: ").append(huffmanCodes[i])
                        .append("\n");
            }
        }

        return sb.toString();
    }

}