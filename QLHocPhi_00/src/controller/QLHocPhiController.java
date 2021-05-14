package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class QLHocPhiController {
    @FXML
    private Button exitButton;

    @FXML
    void HandlerExitButton(ActionEvent event) {
        if(dialog_notification.AlertDialog.displayExit()){
            ((Stage) exitButton.getScene().getWindow()).close();
        }
    }

    @FXML
    void HandlerStudentCTMau(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/HocCtrMau.fxml"));
        Parent hocCtrMau = loader.load();
        Scene scene = new Scene(hocCtrMau);
        stage.setScene(scene);
    }

    @FXML
    void HandlerStudentTinChi(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/HocTinChi.fxml"));
        Parent hocTinChi = loader.load();
        Scene scene = new Scene(hocTinChi);
        stage.setScene(scene);
    }
}
