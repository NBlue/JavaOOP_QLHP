/**@Admin: Tô Đức Hiệp - 20194278
 * */
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
        Parent hocCtrMau = FXMLLoader.load(getClass().getResource("/view/HocCtrMau.fxml"));
        Scene scene = new Scene(hocCtrMau);
        stage.setScene(scene);
    }

    @FXML
    void HandlerStudentTinChi(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent hocTinChi = FXMLLoader.load(getClass().getResource("/view/HocTinChi.fxml"));
        Scene scene = new Scene(hocTinChi);
        stage.setScene(scene);
    }
}
