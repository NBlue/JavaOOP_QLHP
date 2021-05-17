package controller.ctrmau;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**@admin: Nguyễn Mạnh Duy - 20194262
 * */
public class SumFeesCtrMauController implements Initializable {
    @FXML
    private TextField namHocText;

    @FXML
    private TextField sumFees;

    @FXML
    private Button exitButton;


    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet result = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conn = connection.ConnectSQL.studentConnection();
        sumFees();
    }

    private void sumFees() {
        namHocText.setOnKeyReleased(event -> {
            if (namHocText.getText().equals("")) {
                sumFees.setPromptText("VNĐ");
            } else {
                String sql = "Select sum(fees) as SumFees from tblHocCtrMau where namHoc like '" + namHocText.getText() + "'";
                try {
                    pst = conn.prepareStatement(sql);
                    result = pst.executeQuery();
                    while (result.next()){
                        sumFees.setPromptText(String.valueOf(result.getFloat("SumFees")) + "00 VNĐ");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    @FXML
    void handleCancelSumFees(ActionEvent event) throws IOException {
        ((Stage)exitButton.getScene().getWindow()).close();
    }
}
