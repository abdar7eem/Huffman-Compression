import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Check for arguments or just hard-code them for testing
        System.out.println("HELLO");
        if (args.length != 2) {
            System.out.println("There is no two paremeters");
            return;
        }

        String action = args[0];
        String filePath = args[1];
        int lastSlashIndex = filePath.lastIndexOf("\\");

        String path = filePath.substring(0, lastSlashIndex);

        try {
            File file = new File(filePath);
            if ("compress".equalsIgnoreCase(action)) {

                HuffmanCompression.readCompressFile(file);
                HuffmanCompression.writeCompressFile(file, path);

            } else if ("decompress".equalsIgnoreCase(action)) {

                HuffmanDecompression.readDecompressFile(filePath);
                HuffmanDecompression.writeDecompressFile(path);
                System.out.println(path);

            } else {
                System.out.println(" Use 'compress' or 'decompress'.");
            }

        } catch (Exception ex) {
            System.out.println("ERRRRRRRRRRROR");
        }
    }
}
