package app.controller;

import app.StockWizard;
import app.controller.helper.Mediator;
import data.Input;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateDataSceneController implements Initializable {

    private File selectedFile;

    @FXML
    private Label filename;
    @FXML
    private ProgressBar updateManuallyProgressBar;
    @FXML
    private ProgressBar automaticProgressBar;
    @FXML
    private Button nextBtn;
    @FXML
    private Button updateManuallyBtn;

    public UpdateDataSceneController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Input.inputData != null) {
            if (Input.isUpdateByAuto)
                automaticProgressBar.setProgress(1);
            else
                updateManuallyProgressBar.setProgress(1);
            nextBtn.setDisable(false);
        }
    }

    @FXML
    private void gotoSelect(ActionEvent event) {
        Mediator.Notify("onGoingSelectSentence");
    }

    @FXML
    private void browseData(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter =
                new FileChooser.ExtensionFilter("Compress file | Data file",
                        "*.zip", "*.rar", "*.txt", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);

        selectedFile = fileChooser.showOpenDialog(StockWizard.primaryStage);
        if (selectedFile != null && selectedFile.exists()){
            filename.setText(selectedFile.getName());
            updateManuallyBtn.setDisable(false);
        }
    }

    @FXML
    private void updateData(ActionEvent event){
        new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    updateManuallyProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                });
                Input.updateDataFromLocal(selectedFile.getAbsolutePath());
                Platform.runLater(() -> {
                    updateManuallyProgressBar.setProgress(1);
                    nextBtn.setDisable(false);
                });
            }
            catch (Exception e) {
                Platform.runLater(() -> {
                    updateManuallyProgressBar.setProgress(0);
                    nextBtn.setDisable(true);
                });
            }
        }).start();

//       updateManuallyProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    }

    @FXML
    private void downloadData(ActionEvent event) {
        new Thread(()->  {
            Input.updateDataFromWeb();
            Platform.runLater(()->{
                automaticProgressBar.setProgress(1);
                nextBtn.setDisable(false);
            });
        }).start();

        automaticProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    }
}
