// BitOutputStream class to write bits to a file

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream implements AutoCloseable {
    private OutputStream out;
    private int buffer;
    private int bitPosition;

    public BitOutputStream(String filename) throws IOException {
        out = new FileOutputStream(filename);
        buffer = 0;
        bitPosition = 0;
    }

    // Method to write bit on a file
    public void writeBit(int bit) throws IOException {
        buffer = (buffer << 1) | bit;
        bitPosition++;
        if (bitPosition == 8) {
            out.write(buffer);
            buffer = 0;
            bitPosition = 0;
        }
    }

    // Method to write byte on the file
    public void writeByte(int b) throws IOException {
        out.write(b);
    }

    // Method to write Integer on the file which represent 4 bytes
    public void writeInt(int i) throws IOException {
        out.write(i >>> 24);  
        out.write(i >>> 16);  
        out.write(i >>> 8); 
        out.write(i);         
    }
    
    // Method to close the FileOutputStream
    @Override
    public void close() throws IOException {
        if (bitPosition > 0) {
            buffer <<= (8 - bitPosition);
            out.write(buffer);
        }
        out.close();
    }
    
}
