import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller implements Initializable {

    @FXML
    private AnchorPane DecompressScreen;

    @FXML
    private AnchorPane MainScreen;

    @FXML
    private Button compressBackBtn;

    @FXML
    private Button compressBtn;

    @FXML
    private Button compressSave;

    @FXML
    private AnchorPane compressScreen;

    @FXML
    private Button compressShowFileStatistic;

    @FXML
    private Button dcompressShowFileStatistic;

    @FXML
    private Button decomopressBtn;

    @FXML
    private Button decompressBackBtn;

    @FXML
    private Button decompressSave;

    @FXML
    private Button stateBackBtn;

    @FXML
    private TextArea stateTextArea;

    @FXML
    private Label stateTitle;

    @FXML
    private AnchorPane stateVbox;

    static File inFile = null;
    static File outFile = null;

    // Action when clicking on Compress Btn
    @FXML
    void MenuCompressAction(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open file to compress it");
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            String check = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
            if (selectedFile != null && !check.equals(".huff")) {

                HuffmanCompression hf = new HuffmanCompression();
                // Call a method to read the file to compress it
                hf.readCompressFile(selectedFile);
                inFile = selectedFile;

                MainScreen.setVisible(false);
                compressScreen.setVisible(true);
                DecompressScreen.setVisible(false);
                stateVbox.setVisible(false);

            } else {
                showError("File type error", "You can't choose huff files, because they already compressed!!");
            }
        } catch (Exception e) {

        }
    }

    // Action when clicking on Decompress Btn
    @FXML
    void MenudecompressAction(ActionEvent event) {

        try {
            File selectedFile = getHuffFile();
            if (selectedFile != null) {
                HuffmanDecompression hfd = new HuffmanDecompression();
                // Call a method to read the file to decompress it
                hfd.readDecompressFile(selectedFile.getAbsolutePath());
                inFile = selectedFile;

                MainScreen.setVisible(false);
                DecompressScreen.setVisible(true);
                compressScreen.setVisible(false);
                stateVbox.setVisible(false);

            } else {
                showError("File type error", "You must choose huff files");
            }
        } catch (Exception e) {

        }
    }

    // Action when clicking on CompressBack Btn
    @FXML
    void compressBackBtnAction(ActionEvent event) {
        compressScreen.setVisible(false);
        MainScreen.setVisible(true);
        DecompressScreen.setVisible(false);
        stateVbox.setVisible(false);
        inFile = null;
        outFile = null;
        compressShowFileStatistic.setDisable(true);
    }

    // Action when clicking on save Btn in compress screen
    @FXML
    void compressSaveAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory to save the file");
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            String path = selectedDirectory.getAbsolutePath();
            HuffmanCompression.writeCompressFile(inFile, path);
            String fileName = inFile.getName().substring(0, inFile.getName().lastIndexOf(".")) + ".huff";
            outFile = new File(path + File.separator + fileName);

            compressShowFileStatistic.setDisable(false);
        } else {
            showError("Directory Error", "Choose a valid directory path");
        }
    }

    // Action when clicking on show file statictics Btn in compress screen
    @FXML
    void compressShowFileStatisticAction(ActionEvent event) {
        MainScreen.setVisible(false);
        stateVbox.setVisible(true);
        DecompressScreen.setVisible(false);
        compressScreen.setVisible(false);
        try {
            stateTitle.setText(inFile.getName());
            stateTextArea.setText(HuffmanCompression.getCompressionStatistics(inFile, outFile,
                    HuffmanCompression.huffmanCodes, HuffmanCompression.frequencies));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Action when clicking on show file statictics Btn in decompress screen
    @FXML
    void dcompressShowFileStatisticAction(ActionEvent event) {
        MainScreen.setVisible(false);
        stateVbox.setVisible(true);
        DecompressScreen.setVisible(false);
        compressScreen.setVisible(false);
        try {
            stateTitle.setText(inFile.getName());
            stateTextArea.setText(HuffmanDecompression.getDecompressionStatistics());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Action when clicking on CompressBack Btn
    @FXML
    void decompressBackBtnAction(ActionEvent event) {
        DecompressScreen.setVisible(false);
        MainScreen.setVisible(true);
        compressScreen.setVisible(false);
        stateVbox.setVisible(false);

        inFile = null;
        outFile = null;
        dcompressShowFileStatistic.setDisable(true);
    }

    // Action when clicking on save Btn in decompress screen
    @FXML
    void decompressSaveAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory to save the file");
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            String path = selectedDirectory.getAbsolutePath();

            try {
                HuffmanDecompression.writeDecompressFile(path);
            } catch (IOException e) {
                showError("File error", "Choose a valid file");
            }
            outFile = new File(path);

            dcompressShowFileStatistic.setDisable(false);
        } else {
            showError("Directory error", "Choose a valid directory");

        }
    }

    // Action when clicking on back Btn in show statistics screen
    @FXML
    void stateBtnBack(ActionEvent event) {
        stateVbox.setVisible(false);
        MainScreen.setVisible(true);
        DecompressScreen.setVisible(false);
        compressScreen.setVisible(false);
        compressShowFileStatistic.setDisable(true);
        dcompressShowFileStatistic.setDisable(true);
    }

    // Method to get the selected file, which should be a huff file (we filter the file chooser to just show huff files)
    File getHuffFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Huff files");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Huff Files", "*.huff"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        return selectedFile;
    }
    

    // Method we call it to handle exiptions
    public static void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DecompressScreen.setVisible(false);
        MainScreen.setVisible(true);
        compressScreen.setVisible(false);
        stateVbox.setVisible(false);
        compressShowFileStatistic.setDisable(true);
        dcompressShowFileStatistic.setDisable(true);
    }

}
