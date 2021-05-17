package controller.dangnhap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private TextField firstNameText;

    @FXML
    private Label FirstNameError;

    @FXML
    private TextField lastNameText;

    @FXML
    private Label LastNameError;

    @FXML
    private TextField userNameText;

    @FXML
    private Label userNameError;

    @FXML
    private PasswordField passWordText;

    @FXML
    private Label passwordError;

    @FXML
    private PasswordField paswordAgainText;

    @FXML
    private Label passwordAgainError;

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet result = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conn = connection.ConnectSQL.studentConnection();
    }

    @FXML
    void CancelButton(ActionEvent event) {
        try {
            if (dialog_notification.AlertDialog.displayExit()) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/view/SignIn.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OKButton(ActionEvent event) throws SQLException {
        boolean fistNameIsEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(firstNameText, FirstNameError, "Tên là cần thiết để đăng ký!");
        boolean lastNameIsEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(lastNameText, LastNameError, "Họ là cần thiết để đăng ký!");
        boolean usernameIsEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(userNameText, userNameError, "Username là cần thiết để đăng ký!");
        boolean passwordIsEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(passWordText, passwordError, "Password là cần thiết để đăng ký!");
        boolean passwordAgainIsEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(paswordAgainText, passwordAgainError, "Password cần được nhập lại!");
        if (fistNameIsEmpty && lastNameIsEmpty && usernameIsEmpty && passwordIsEmpty && passwordAgainIsEmpty) {
            if(!checkUsername(userNameText.getText())) {
                String sql = "INSERT INTO Sign_In (firstname, lastname, username, pass) VALUES(?, ?, ?, ?)";
                try {
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, firstNameText.getText());
                    pst.setString(2, lastNameText.getText());
                    pst.setString(3, userNameText.getText());
                    pst.setString(4, passWordText.getText());
                    if (passWordText.getText().equals(paswordAgainText.getText())) {
                        int i = pst.executeUpdate();
                        if (i == 1) {
                            dialog_notification.AlertDialog.display("Acount", "Tài khoản đăng kí thành công!");
                            firstNameText.clear();
                            lastNameText.clear();
                            userNameText.clear();
                            passWordText.clear();
                            paswordAgainText.clear();
                        }
                    } else {
                        dialog_notification.AlertDialog.errorSignIn("Password", "Password nhập lại chưa đúng!");
                        passWordText.clear();
                        paswordAgainText.clear();
                        //passwordError.setText("Password lỗi!");
                        passwordAgainError.setText("Password nhập lại chưa đúng!");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    pst.close();
                }
            } else {
                dialog_notification.AlertDialog.errorSignIn("Username", "Username đã tồn tại!");
                passWordText.clear();
                paswordAgainText.clear();
                userNameError.setText("Username đã tồn tại!");
            }
        }
    }

    private boolean checkUsername(String usernameText) throws SQLException {
        String sql = "Select username from Sign_In where username like '%" + usernameText + "%'";
        try {
            pst = conn.prepareStatement(sql);
            result = pst.executeQuery();
            if (result.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
