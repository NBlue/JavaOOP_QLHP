package app.controller;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import app.StockWizard;
import app.controller.helper.Mediator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.awt.Window;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import javafx.stage.DirectoryChooser;


public class ResultSceneController implements Initializable {

    @FXML
    private TextArea resultTextArea = new TextArea();

    public ResultSceneController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resultTextArea.setText(ProcessingSceneController.getInstance().getTextOutput());
    }

    @FXML
    public void gotoSelect(ActionEvent event) {
        Mediator.Notify("onGoingSelectSentence");
    }

    @FXML
    public void save(ActionEvent event) throws IOException {
    	DirectoryChooser chooser = new DirectoryChooser();
    	chooser.setTitle("JavaFX Projects");
    	File selectedDirectory = chooser.showDialog(StockWizard.primaryStage);

    	String filePath = selectedDirectory.getAbsolutePath();
    	System.out.println(ProcessingSceneController.getInstance().getTextOutput());

        String str = ProcessingSceneController.getInstance().getTextOutput();

        FileOutputStream outputStream = new FileOutputStream(filePath+ "/result.txt");
        try (Writer out = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            out.write(str);
        }

        Alert alert = new Alert(AlertType.INFORMATION);

        alert.setContentText("Result is saved in : "+ filePath+"/result.txt");
        alert.show();
    }
}
