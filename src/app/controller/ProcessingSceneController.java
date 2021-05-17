package app.controller;

import app.controller.helper.Mediator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;

import java.net.URL;
import java.util.ResourceBundle;

public class ProcessingSceneController implements Initializable {

    private String textOutput;

    @FXML
    private ProgressIndicator progressIndicator;

    private static ProcessingSceneController instance;


    public ProcessingSceneController() {
        this.instance = this;
    }

    public static ProcessingSceneController getInstance() {
        if (instance == null)
            return new ProcessingSceneController();
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            String text = new String();
            for(var item : SelectSceneController.getInstance().getListSentences()) {
                text += item.isSelected() ? "- " + item.getSentenceGenerator().generate() + "\n" : "";
            }
            this.textOutput = text;

            Platform.runLater(() -> {
                progressIndicator.setProgress(1);
                Mediator.Notify("onGoingResult");
            });
        }).start();
    }

    public String getTextOutput() {
        return this.textOutput;
    }
}
