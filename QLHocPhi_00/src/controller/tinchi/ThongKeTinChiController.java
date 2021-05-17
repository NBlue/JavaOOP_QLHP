package controller.tinchi;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.StudentTinChi;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ThongKeTinChiController implements Initializable {
    @FXML
    private TextField namHocText;
    @FXML
    private Button exitButton;

    @FXML
    private TableView<StudentTinChi> studentTable;
    @FXML
    private TableColumn<?, ?> idColumn;
    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private Label sumStudentText;

    private Connection conn;        //Kết nối sql
    private PreparedStatement pst;  //Thực thi
    private ResultSet result;       //Kết quả
    private ObservableList<StudentTinChi> data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conn = connection.ConnectSQL.studentConnection();
        data = FXCollections.observableArrayList();
        setSellTable();
        searchNgayNhap();
    }

    private void setSellTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }


    /**  Tìm kiếm bằng ngày nhập  */
    private void searchNgayNhap() {
        namHocText.setOnKeyReleased(event -> {
            if (namHocText.getText().equals("")) {
                data.clear();  //Xóa data trên table
                sumStudentText.setText("0");
            } else {
                data.clear();
                String sqlShow = "Select ID, name from tblHocTinChi where namHoc like '"
                        + namHocText.getText() + "'";
                String sqlCount = "Select count(namHoc) as Dem from tblHocTinChi where namHoc like '"
                        + namHocText.getText() + "'";
                try {
                    // Show data ra table
                    pst = conn.prepareStatement(sqlShow);
                    result = pst.executeQuery();
                    while (result.next()) {
                        StudentTinChi s = new StudentTinChi();
                        s.setId(result.getString("ID"));
                        s.setName(result.getString("Name"));
                        data.add(s);
                    }
                    studentTable.setItems(data);

                    // Dem so luong data show ra
                    pst = conn.prepareStatement(sqlCount);
                    result = pst.executeQuery();
                    while (result.next()) {
                        sumStudentText.setText(String.valueOf(result.getInt("Dem")));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    @FXML
    void handleCancelThongKe(ActionEvent event) throws IOException {
        ((Stage) exitButton.getScene().getWindow()).close();
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("../view/HocTinChi.fxml"));
//        Parent hocTinChi = loader.load();
//        Scene root = new Scene(hocTinChi);
//        stage.setScene(root);
    }
}
