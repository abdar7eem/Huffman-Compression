import java.io.*;

public class BitInputStream implements AutoCloseable {
    private InputStream input;
    private int currentByte;
    private int bitPosition;

    public BitInputStream(String fileName) throws IOException {
        input = new FileInputStream(fileName);
        currentByte = -1;
        bitPosition = 8;
    }

    // Method to read Bit form the file
    public int readBit() throws IOException {
        if (bitPosition == 8) { 
            currentByte = input.read();
            if (currentByte == -1) {
                return -1; 
            }
            bitPosition = 0;
        }
        int bit = (currentByte >> (7 - bitPosition)) & 1; // shift the byte to avaliable bits and compare with &1 to get 1 or 0
        bitPosition++;
        return bit;
    }

    // Method to read byte from the file
    public int readByte() throws IOException {
        return input.read();
    }

    // Method to read a byte from the file according to the buffer size
    public int readByte(byte[] buffer) throws IOException {
        int bytesRead = input.read(buffer);
        
        if (bytesRead == -1) {
            return -1; 
        }
        return bytesRead; 
    }

    // Method to read integer which represent 4 Byte
    public int readInt() throws IOException {
        int byte1 = readByte();
        int byte2 = readByte();
        int byte3 = readByte();
        int byte4 = readByte();
        return (byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4;
    }

    // Method to check if there is avaliable bytes to read
    public int available() throws IOException {
        return input.available();
    }

    // Method to close the FileInputStream
    @Override
    public void close() throws IOException {
        input.close(); 
    }
}
