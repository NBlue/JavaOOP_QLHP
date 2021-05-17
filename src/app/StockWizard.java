package app;

import app.controller.helper.Mediator;
import app.controller.helper.ScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

public class StockWizard extends Application {

    public static Stage primaryStage;

    private ScreenController screenController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Display GUI
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("view/UpdateDataScene.fxml"));
        Scene scene = new Scene(root, 750, 450);
        scene.getStylesheets().add("app/view/style.css");
        screenController = new ScreenController(scene);

        primaryStage.setTitle("Stock Wizard");
        File f = new File(StockWizard.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        primaryStage.getIcons().add(new Image("logo.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        Mediator.unSubscribe("onGoingUpdateData");
        Mediator.unSubscribe("onGoingSelectSentence");
        Mediator.unSubscribe("onGoingProcessing");
        Mediator.unSubscribe("onGoingResult");
        Mediator.subscribe("onGoingUpdateData", event -> onGoingUpdateData(null));
        Mediator.subscribe("onGoingSelectSentence", event -> onGoingSelectSentence(null));
        Mediator.subscribe("onGoingProcessing", event -> onGoingProcessing(null));
        Mediator.subscribe("onGoingResult", event -> onGoingResult(null));
    }

    public void onGoingUpdateData(ActionEvent e) {
        screenController.active(getClass().getResource("view/UpdateDataScene.fxml"));
    }

    public void onGoingSelectSentence(ActionEvent e) {
        screenController.active(getClass().getResource("view/SelectScene.fxml"));
    }

    public void onGoingProcessing(ActionEvent e) {
        screenController.active(getClass().getResource("view/ProcessingScene.fxml"));
    }

    public void onGoingResult(ActionEvent e) {
        screenController.active(getClass().getResource("view/ResultScene.fxml"));
    }

    public static void main(String[] args) {
        launch(args);
    }



}
