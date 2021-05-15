package controller;

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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.StudentTinChi;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HocTinChiController extends StudentTinChi implements Initializable {
    @FXML
    private TextField tcIdText;
    @FXML
    private TextField tcNameText;
    @FXML
    private DatePicker tcBirthdayDate;
    @FXML
    private TextField tcEmailText;
    @FXML
    private TextField tcStudyProgramText;
    @FXML
    private TextField tcTinChiText;
    @FXML
    private TextField tcNamHocText;

    @FXML
    private TableView<StudentTinChi> tcStudentTable;
    @FXML
    private TableColumn<?, ?> tcIDColumn;
    @FXML
    private TableColumn<?, ?> tcNameColumn;
    @FXML
    private TableColumn<?, ?> tcBirthdayColumn;
    @FXML
    private TableColumn<?, ?> tcEmailColumn;
    @FXML
    private TableColumn<?, ?> tcStudyProgramColumn;
    @FXML
    private TableColumn<?, ?> tcTinChiColumn;
    @FXML
    private TableColumn<?, ?> tcFeesColumn;
    @FXML
    private TableColumn<?, ?> tcNamHocColumn;

    @FXML
    private Label tcIdError;
    @FXML
    private Label tcNameError;
    @FXML
    private Label tcBirthdayError;
    @FXML
    private Label tcEmailError;
    @FXML
    private Label tcStudyProgramError;
    @FXML
    private Label tcTinChiError;
    @FXML
    private Label tcNamHocError;

    private Connection conn = null;
    private PreparedStatement pst = null;

    private ResultSet result = null;
    private ObservableList<StudentTinChi> data;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conn = connection.ConnectSQL.studentConnection();  //Thực hiên lưu data vào sql
        data = FXCollections.observableArrayList();
        setCellTable();
        loadDataFromDatabase();
        setCellValueFromTableToTextField();
    }

    /**
     * ADD DỮ LIỆU VÀO DATABASE CỦA SQL SERVER
     */
    @Override
    public float fees(String a) {
        return super.fees(a);
    }
    @FXML
    void handleAddStudent(ActionEvent event) throws SQLException {
        // Kiểm tra textField rỗng
        boolean isIdEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(tcIdText, tcIdError, "ID được yêu cầu nhập!");
        boolean isNameEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(tcNameText, tcNameError, "Tên được yêu cầu nhập!");
        boolean isBirthdayEmpty = validation.TextFieldValidation.
                textFieldNotEmpty((TextField) tcBirthdayDate.getEditor(), tcBirthdayError, "Ngày sinh được yêu cầu nhập!");
        boolean isEmailEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(tcEmailText, tcEmailError, "Email được yêu cầu nhập!");
        boolean isStudyProgram = validation.TextFieldValidation.
                textFieldNotEmpty(tcStudyProgramText, tcStudyProgramError, "Chương trình học được yêu cầu nhập!");
        boolean isTinChi = validation.TextFieldValidation.
                textFieldTypeNumber(tcTinChiText, tcTinChiError, "Số tín chỉ được yêu cầu nhập là 1 số!");
        boolean isNamHoc = validation.TextFieldValidation.
                textFieldNotEmpty(tcNamHocText, tcNamHocError, "Năm học được yêu cầu nhập!");

        if (isIdEmpty && isNameEmpty && isBirthdayEmpty && isEmailEmpty && isStudyProgram && isTinChi && isNamHoc) {
            if (!checkIDStudentTinChi()) {
                String sql = "INSERT INTO tblHocTinChi(ID, name, birthday, email, studyProgram, tinChi, fees, ngayNhapData, namHoc)" +
                        " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                // chuyển kiểu dữ liệu sang định dạng dd/MM/yyyy (Mặc định của DatePicker là M/d/yyyy)
                String localDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String birthday = tcBirthdayDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                try {
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, tcIdText.getText());
                    pst.setString(2, tcNameText.getText());
                    pst.setString(3, birthday);
//                pst.setString(3, ((TextField) tcBirthdayDate.getEditor()).getText());
                    pst.setString(4, tcEmailText.getText());
                    pst.setString(5, tcStudyProgramText.getText());
                    pst.setFloat(6, Float.valueOf(tcTinChiText.getText()));
                    pst.setFloat(7, fees(tcTinChiText.getText()));
                    pst.setString(8, localDate);   //lấy thời gian ngày nhập data
                    pst.setString(9, tcNamHocText.getText());

                    int i = pst.executeUpdate();
                    if (i == 1)
                        dialog_notification.AlertDialog.display("Data", "Data được thêm thành công!");
                    setCellTable();
                    loadDataFromDatabase();
                    clearTextField();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    pst.close();
                }
            } else {
                dialog_notification.AlertDialog.errorSignIn("ID", "ID  này đã được sử dung!");
                tcIdError.setText("Nhập ID khác");
                clearTextField();
            }
        }
    }

    /**
     * CẬP NHẬT DỮ LIỆU VÀO DATABASE
     */
    @FXML
    void handleUpdateStudent(ActionEvent event) {
        boolean isIdEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(tcIdText, tcIdError, "ID được yêu cầu nhập!");
        boolean isNameEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(tcNameText, tcNameError, "Tên được yêu cầu nhập!");
        boolean isBirthdayEmpty = validation.TextFieldValidation.
                textFieldNotEmpty((TextField) tcBirthdayDate.getEditor(), tcBirthdayError, "Ngày sinh được yêu cầu nhập!");
        boolean isEmailEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(tcEmailText, tcEmailError, "Email được yêu cầu nhập!");
        boolean isStudyProgram = validation.TextFieldValidation.
                textFieldNotEmpty(tcStudyProgramText, tcStudyProgramError, "Chương trình học được yêu cầu nhập!");
        boolean isTinChi = validation.TextFieldValidation.
                textFieldTypeNumber(tcTinChiText, tcTinChiError, "Số tín chỉ được yêu cầu nhập là 1 số!");
        boolean isNamHoc = validation.TextFieldValidation.
                textFieldNotEmpty(tcNamHocText, tcNamHocError, "Năm học được yêu cầu nhập!");
        if (isIdEmpty && isNameEmpty && isBirthdayEmpty && isEmailEmpty && isStudyProgram && isTinChi && isNamHoc) {
            String sql = "Update tblHocTinChi set name = ?, birthday = ?, " +
                    "email = ?, studyProgram = ?, tinChi = ?, fees = ?, namHoc = ? where ID = ?";
            float tinhHocPhi = Float.valueOf(tcTinChiText.getText()) * 250;
            String birthday = tcBirthdayDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, tcNameText.getText());
                pst.setString(2, birthday);
                pst.setString(3, tcEmailText.getText());
                pst.setString(4, tcStudyProgramText.getText());
                pst.setFloat(5, Float.valueOf(tcTinChiText.getText()));
                pst.setFloat(6, tinhHocPhi);
                pst.setString(7, tcNamHocText.getText());
                pst.setString(8, tcIdText.getText());

                int i = pst.executeUpdate();
                if (i == 1)
                    dialog_notification.AlertDialog.display("Data", "Data được cập nhật thành công!");
                setCellTable();
                loadDataFromDatabase();
                clearTextField();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * XÓA DỮ LIỆU KHỎI DATABASE
     */
    @FXML
    void handleDeleteStudent(ActionEvent event) {
        boolean isIdEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(tcIdText, tcIdError, "ID được yêu cầu để xóa thông tin!");
        if (isIdEmpty) {
            String sql = "Delete from tblHocTinChi where ID = ?";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, tcIdText.getText());
                int i = pst.executeUpdate();
                if (i == 1) {
                    dialog_notification.AlertDialog.display("Information", "Data xóa thành công!");
                    loadDataFromDatabase();
                    clearTextField();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * TÌM KIẾM THÔNG TIN SINH VIÊN
     */
    @FXML
    private TextField searchStudentText;

    //MenuButton
    @FXML
    void searchStudentByID(ActionEvent event) {
        searchStudentByID();
    }

    @FXML
    void searchStudentByName(ActionEvent event) {
        searchStudentByName();
    }

    @FXML
    void searchStudentByTinChi(ActionEvent event) {
        searchStudentByTinChi();
    }

    private void searchStudentByID() {
        searchStudentText.setOnKeyReleased(event -> {
            if (searchStudentText.getText().equals("")) {
                loadDataFromDatabase();
            } else {
                data.clear();
                String sqlID = "Select ID, name, birthday, email, studyProgram, tinChi, fees, namHoc " +
                        "from tblHocTinChi where ID Like '%" + searchStudentText.getText() + "%'";
                try {
                    pst = conn.prepareStatement(sqlID);
                    result = pst.executeQuery(); //truy van query
                    while (result.next()) {  //result.next(): nếu trả về kết quả
                        StudentTinChi s = new StudentTinChi();
                        s.setId(result.getString("ID"));
                        s.setName(result.getString("name"));
                        s.setBirthday(result.getString("birthday"));
                        s.setEmail(result.getString("email"));
                        s.setStudyProgram(result.getString("studyProgram"));
                        s.setTinChi(result.getInt("tinChi"));
                        s.setFees(String.valueOf(result.getFloat("fees")) + "00");
                        s.setNamHoc(result.getString("namHoc"));

                        data.add(s);
                    }
                    tcStudentTable.setItems(data);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    private void searchStudentByName() {
        searchStudentText.setOnKeyReleased(event -> {
            if (searchStudentText.getText().equals("")) {
                loadDataFromDatabase();
            } else {
                data.clear();
                String sqlName = "Select ID, name, birthday, email, studyProgram, tinChi, fees, namHoc " +
                        "from tblHocTinChi where name Like N'%" + searchStudentText.getText() + "%'";
                try {
                    pst = conn.prepareStatement(sqlName);
                    result = pst.executeQuery();
                    while (result.next()) {  //result.next(): nếu trả về kết quả
                        StudentTinChi s = new StudentTinChi();
                        s.setId(result.getString("ID"));
                        s.setName(result.getString("name"));
                        s.setBirthday(result.getString("birthday"));
                        s.setEmail(result.getString("email"));
                        s.setStudyProgram(result.getString("studyProgram"));
                        s.setTinChi(result.getInt("tinChi"));
                        s.setFees(String.valueOf(result.getFloat("fees")) + "00");
                        s.setNamHoc(result.getString("namHoc"));

                        data.add(s);
                    }
                    tcStudentTable.setItems(data);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    private void searchStudentByTinChi() {
        searchStudentText.setOnKeyReleased(event -> {
            if (searchStudentText.getText().equals("")) {
                loadDataFromDatabase();
            } else {
                data.clear();
                String sqlTinChi = "Select ID, name, birthday, email, studyProgram, tinChi, fees, namHoc " +
                        "from tblHocTinChi where tinChi = " + Float.valueOf(searchStudentText.getText());
                try {
                    pst = conn.prepareStatement(sqlTinChi);
                    result = pst.executeQuery();
                    while (result.next()) {  //result.next(): nếu trả về kết quả
                        StudentTinChi s = new StudentTinChi();
                        s.setId(result.getString("ID"));
                        s.setName(result.getString("name"));
                        s.setBirthday(result.getString("birthday"));
                        s.setEmail(result.getString("email"));
                        s.setStudyProgram(result.getString("studyProgram"));
                        s.setTinChi(result.getInt("tinChi"));
                        s.setFees(String.valueOf(result.getFloat("fees")) + "00");
                        s.setNamHoc(result.getString("namHoc"));

                        data.add(s);
                    }
                    tcStudentTable.setItems(data);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    /**
     * Thống kê theo 1 khoảng thời gian nhập vào
     */
    @FXML
    void handleStatisticStudent(ActionEvent event) throws IOException {
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/ThongKeTinChi.fxml"));
        Parent thongKe = loader.load();
        Scene root = new Scene(thongKe);
        stage.setScene(root);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.show();
    }

    /**
     * Tính tổng học phí trong 1 khoảng thời gian nhập vào
     */
    @FXML
    void handleSumFees(ActionEvent event) throws IOException {
        Stage stage = new Stage();
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/SumFeesTinChi.fxml"));
        Parent sumFees = loader.load();
        Scene root = new Scene(sumFees);
        stage.setScene(root);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.show();
    }

    /**
     * Binding StudentTinChi and tableView
     */
    private void setCellTable() {
        tcIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        tcEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcStudyProgramColumn.setCellValueFactory(new PropertyValueFactory<>("studyProgram"));
        tcTinChiColumn.setCellValueFactory(new PropertyValueFactory<>("tinChi"));
        tcFeesColumn.setCellValueFactory(new PropertyValueFactory<>("fees"));
        tcNamHocColumn.setCellValueFactory(new PropertyValueFactory<>("namHoc"));
    }

    /**
     * Load database tren SQL vao StudentTinChi ---- Chú ý: đúng thứ tự constructor
     */
    private void loadDataFromDatabase() {
        data.clear();
        try {
            pst = conn.prepareStatement("Select ID, name, birthday, email, studyProgram, tinChi, fees, namHoc from tblHocTinChi");
            result = pst.executeQuery();
            while (result.next()) {
                StudentTinChi s = new StudentTinChi();
                s.setId(result.getString("ID"));
                s.setName(result.getString("name"));
                s.setBirthday(result.getString("birthday"));
                s.setEmail(result.getString("email"));
                s.setStudyProgram(result.getString("studyProgram"));
                s.setTinChi(result.getInt("tinChi"));
                s.setFees(String.valueOf(result.getFloat("fees")) + "00");
                s.setNamHoc(result.getString("namHoc"));

                data.add(s);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        tcStudentTable.setItems(data);
    }

    // Làm mới dữ liệu hiển thị trên textField
    private void clearTextField() {
        tcIdText.clear();
        tcNameText.clear();
        tcBirthdayDate.setValue(null);
        tcEmailText.clear();
        tcStudyProgramText.clear();
        tcTinChiText.clear();
        tcNamHocText.clear();
    }

    /**
     * Cài đặt hiển thị giá trị từ bảng cho textField (Khi ấn 1 dòng data từ tableView)
     */
    private void setCellValueFromTableToTextField() {
        // cài đặt khi click chuột
        tcStudentTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                StudentTinChi s = tcStudentTable.getItems().get(tcStudentTable.getSelectionModel().getSelectedIndex());
                tcIdText.setText(s.getId());
                tcNameText.setText(s.getName());
                tcBirthdayDate.setValue(LocalDate.parse(s.getBirthday(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                tcEmailText.setText(s.getEmail());
                tcStudyProgramText.setText(s.getStudyProgram());
                tcTinChiText.setText(String.valueOf(s.getTinChi()));
                tcNamHocText.setText(s.getNamHoc());
            }
        });
    }

    /**
     * Check ID (primary key đã tồn tại chưa!)
     */
    private boolean checkIDStudentTinChi() {
        String sqlCheck = "Select * from tblHocTinChi where ID Like N'%" + tcIdText.getText() + "%'";
        try {
            pst = conn.prepareStatement(sqlCheck);
            result = pst.executeQuery();
            if (result.next())
                return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    /**
     * Thoát về giao diện quản lí học phí
     */
    @FXML
    void handleCancelHocTinChi(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("../view/QLHocPhi.fxml"));
        Parent QLHocPhi = loader.load();
        Scene scene = new Scene(QLHocPhi);
        stage.setScene(scene);
    }

}
