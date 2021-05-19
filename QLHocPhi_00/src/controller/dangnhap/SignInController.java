/**@Admin: Tô Đức Hiệp - 20194278
 * Nguyễn Phương Nam: LoadingProgress
 * */
package controller.dangnhap;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class SignInController implements Initializable {
    @FXML
    private TextField userNameText;
    @FXML
    private PasswordField passwordText;

    @FXML
    private Label userNameError;
    @FXML
    private Label passwordError;


    @FXML
    private Button cancel;

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet result = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conn = connection.ConnectSQL.studentConnection();
    }

    @FXML
    void CancelButton(ActionEvent event) {
        if (dialog_notification.AlertDialog.displayExit()) {
            ((Stage) cancel.getScene().getWindow()).close();
        }
    }

    @FXML
    void SignInButton(ActionEvent event) {
        boolean usernameIsEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(userNameText, userNameError, "Username được yêu cầu đăng nhập!");
        boolean paswordIsEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(passwordText, passwordError, "Password được yêu cầu đăng nhập!");
        if (usernameIsEmpty && paswordIsEmpty) {
            String sql = "select * from Sign_In where username like '%" + userNameText.getText() + "%' and pass like '%" +
                    passwordText.getText() + "%'";
            try {
                pst = conn.prepareStatement(sql);
                result = pst.executeQuery();
                if (result.next()) {
                    // Chuyển đến scene LoadingProgess
                    Stage stageLoading = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Parent rootLoading = FXMLLoader.load(getClass().getResource("/view/LoadProgress.fxml"));
                    Scene sceneLoad = new Scene(rootLoading);
                    stageLoading.setScene(sceneLoad);

                    PauseTransition delay = new PauseTransition(Duration.seconds(3));
                    delay.setOnFinished(event1 -> {
                        stageLoading.close();

                        // Sau khi SceneLoading kết thúc, gọi StageQLHocPhi
                        Stage stage = new Stage();
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("/view/QLHocPhi.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.setMaximized(false);
                        stage.show();
                    });
                    delay.play();
                } else {
                    dialog_notification.AlertDialog.errorSignIn("Error!", "Username hoặc Password không đúng!");
                    passwordText.clear();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void SignUpButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/SignUp.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
