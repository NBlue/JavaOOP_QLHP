package controller.ctrmau;

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
import model.StudentCtrMau;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HocCtrMauController extends StudentCtrMau implements Initializable {
    @FXML
    private TextField ctrIdText;
    @FXML
    private TextField ctrNameText;
    @FXML
    private DatePicker ctrBirthdayDate;
    @FXML
    private TextField ctrEmailText;
    @FXML
    private TextField ctrStudyProgramText;
    @FXML
    private TextField ctrHocPhanText;
    @FXML
    private TextField ctrNamHocText;

    @FXML
    private TableView<StudentCtrMau> ctrStudentTable;
    @FXML
    private TableColumn<?, ?> ctrIDColumn;
    @FXML
    private TableColumn<?, ?> ctrNameColumn;
    @FXML
    private TableColumn<?, ?> ctrBirthdayColumn;
    @FXML
    private TableColumn<?, ?> ctrEmailColumn;
    @FXML
    private TableColumn<?, ?> ctrStudyProgramColumn;
    @FXML
    private TableColumn<?, ?> ctrHocPhanColumn;
    @FXML
    private TableColumn<?, ?> ctrFeesColumn;
    @FXML
    private TableColumn<?, ?> ctrNamHocColumn;

    @FXML
    private Label ctrIdError;
    @FXML
    private Label ctrNameError;
    @FXML
    private Label ctrBirthdayError;
    @FXML
    private Label ctrEmailError;
    @FXML
    private Label ctrStudyProgramError;
    @FXML
    private Label ctrHocPhanError;
    @FXML
    private Label ctrNamHocError;

    private Connection conn = null;
    private PreparedStatement pst = null;

    private ResultSet result = null;
    private ObservableList<StudentCtrMau> data;   //TƯƠNG TỰ ARRAYLIST


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conn = connection.ConnectSQL.studentConnection();
        data = FXCollections.observableArrayList();
        setCellTable();
        loadDataFromDatabase();
        setCellValueFromTableToTextField();
    }

    /**
     * ADD STUDENT
     */
    @Override
    public float fees(String a) {
        return super.fees(a);
    }
    @FXML
    void handleAddStudent(ActionEvent event) throws SQLException {
        boolean isIdEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(ctrIdText, ctrIdError, "ID được yêu cầu nhập!");
        boolean isNameEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(ctrNameText, ctrNameError, "Tên được yêu cầu nhập!");
        boolean isBirthdayEmpty = validation.TextFieldValidation.
                textFieldNotEmpty((TextField) ctrBirthdayDate.getEditor(), ctrBirthdayError, "Ngày sinh được yêu cầu nhập!");
        boolean isEmailEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(ctrEmailText, ctrEmailError, "Email được yêu cầu nhập!");
        boolean isStudyProgram = validation.TextFieldValidation.
                textFieldNotEmpty(ctrStudyProgramText, ctrStudyProgramError, "Chương trình học được yêu cầu nhập!");
        boolean isHocPhan = validation.TextFieldValidation.
                textFieldTypeNumber(ctrHocPhanText, ctrHocPhanError, "Số học phần được yêu cầu nhập là 1 số!");
        boolean isNamHoc = validation.TextFieldValidation.
                textFieldNotEmpty(ctrNamHocText, ctrNamHocError, "Năm học được yêu cầu nhập!");

        if (isIdEmpty && isNameEmpty && isBirthdayEmpty && isEmailEmpty && isStudyProgram && isHocPhan && isNamHoc) {
            if (!checkIDStudentCtrMau()) {
                String sql = "INSERT INTO tblHocCtrMau(ID, name, birthday, email, studyProgram, hocPhan, fees, ngayNhapData, namHoc)" +
                        " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                // chuyển kiểu dữ liệu sang định dạng dd/MM/yyyy (Mặc định của DatePicker là M/d/yyyy)
                String localDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String birthday = ctrBirthdayDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                try {
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, ctrIdText.getText());
                    pst.setString(2, ctrNameText.getText());
                    pst.setString(3, birthday);
//                pst.setString(3, ((TextField) tcBirthdayDate.getEditor()).getText());
                    pst.setString(4, ctrEmailText.getText());
                    pst.setString(5, ctrStudyProgramText.getText());
                    pst.setFloat(6, Float.valueOf(ctrHocPhanText.getText()));
                    pst.setFloat(7, fees(ctrHocPhanText.getText()));
                    pst.setString(8, localDate);   //lấy thời gian ngày nhập data
                    pst.setString(9, ctrNamHocText.getText());

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
                dialog_notification.AlertDialog.errorSignIn("ID", "ID đã được sử dụng!");
                clearTextField();
                ctrIdError.setText("Nhập ID khác!");
            }
        }
    }

    /**
     * UPDATE STUDENT
     */
    @FXML
    void handleUpdateStudent(ActionEvent event) {
        boolean isIdEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(ctrIdText, ctrIdError, "ID được yêu cầu nhập!");
        boolean isNameEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(ctrNameText, ctrNameError, "Tên được yêu cầu nhập!");
        boolean isBirthdayEmpty = validation.TextFieldValidation.
                textFieldNotEmpty((TextField) ctrBirthdayDate.getEditor(), ctrBirthdayError, "Ngày sinh được yêu cầu nhập!");
        boolean isEmailEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(ctrEmailText, ctrEmailError, "Email được yêu cầu nhập!");
        boolean isStudyProgram = validation.TextFieldValidation.
                textFieldNotEmpty(ctrStudyProgramText, ctrStudyProgramError, "Chương trình học được yêu cầu nhập!");
        boolean isHocPhan = validation.TextFieldValidation.
                textFieldTypeNumber(ctrHocPhanText, ctrHocPhanError, "Số học phần được yêu cầu nhập là 1 số!");
        boolean isNamHoc = validation.TextFieldValidation.
                textFieldNotEmpty(ctrNamHocText, ctrNamHocError, "Năm học được yêu cầu nhập!");
        if (isIdEmpty && isNameEmpty && isBirthdayEmpty && isEmailEmpty && isStudyProgram && isHocPhan && isNamHoc) {
            String sql = "Update tblHocCtrMau set name = ?, birthday = ?, " +
                    "email = ?, studyProgram = ?, hocPhan = ?, fees = ?, namHoc = ? where ID = ?";
            float tinhHocPhi = (float) (Float.valueOf(ctrHocPhanText.getText()) * 2.5 * 250 + 1000);
            String birthday = ctrBirthdayDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, ctrNameText.getText());
                pst.setString(2, birthday);
                pst.setString(3, ctrEmailText.getText());
                pst.setString(4, ctrStudyProgramText.getText());
                pst.setFloat(5, Float.valueOf(ctrHocPhanText.getText()));
                pst.setFloat(6, tinhHocPhi);
                pst.setString(7, ctrNamHocText.getText());
                pst.setString(8, ctrIdText.getText());

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
     * DELETE STUDENT
     */
    @FXML
    void handleDeleteStudent(ActionEvent event) {
        boolean isIdEmpty = validation.TextFieldValidation.
                textFieldNotEmpty(ctrIdText, ctrIdError, "ID được yêu cầu để xóa thông tin!");
        if (isIdEmpty) {
            String sql = "Delete from tblHocCtrMau where ID = ?";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, ctrIdText.getText());
                int i = pst.executeUpdate();
                if (i == 1) {
                    dialog_notification.AlertDialog.display("Information", "Data được xóa thành công!");
                    loadDataFromDatabase();
                    clearTextField();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * SEARCH STUDENT
     */
    @FXML
    private TextField searchStudentText;

    @FXML
    void searchStudentByHocPhan(ActionEvent event) {
        searchStudentByHocPhan();
    }

    @FXML
    void searchStudentByID(ActionEvent event) {
        searchStudentByID();
    }

    @FXML
    void searchStudentByName(ActionEvent event) {
        searchStudentByName();
    }

    private void searchStudentByID() {
        searchStudentText.setOnKeyReleased(event -> {
            if (searchStudentText.getText().equals("")) {
                loadDataFromDatabase();
            } else {
                data.clear();
                String sqlId = "Select ID, name, birthday, email, studyProgram, hocPhan, fees, namHoc " +
                        "from tblHocCtrMau where ID like '%" + searchStudentText.getText() + "%'";
                try {
                    pst = conn.prepareStatement(sqlId);
                    result = pst.executeQuery();
                    while (result.next()) {
                        StudentCtrMau s = new StudentCtrMau();
                        s.setId(result.getString("ID"));
                        s.setName(result.getString("name"));
                        s.setBirthday(result.getString("birthday"));
                        s.setEmail(result.getString("email"));
                        s.setStudyProgram(result.getString("studyProgram"));
                        s.setHocPhan(result.getInt("hocPhan"));
                        s.setFees(String.valueOf(result.getFloat("fees")) + "00");
                        s.setNamHoc(result.getString("namHoc"));

                        data.add(s);
                    }
                    ctrStudentTable.setItems(data);
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
                String sqlName = "Select ID, name, birthday, email, studyProgram, hocPhan, fees, namHoc " +
                        "from tblHocCtrMau where name like N'%" + searchStudentText.getText() + "%'";
                try {
                    pst = conn.prepareStatement(sqlName);
                    result = pst.executeQuery();
                    while (result.next()) {
                        StudentCtrMau s = new StudentCtrMau();
                        s.setId(result.getString("ID"));
                        s.setName(result.getString("name"));
                        s.setBirthday(result.getString("birthday"));
                        s.setEmail(result.getString("email"));
                        s.setStudyProgram(result.getString("studyProgram"));
                        s.setHocPhan(result.getInt("hocPhan"));
                        s.setFees(String.valueOf(result.getFloat("fees")) + "00");
                        s.setNamHoc(result.getString("namHoc"));

                        data.add(s);
                    }
                    ctrStudentTable.setItems(data);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    private void searchStudentByHocPhan() {
        searchStudentText.setOnKeyReleased(event -> {
            if (searchStudentText.getText().equals("")) {
                loadDataFromDatabase();
            } else {
                data.clear();
                String sqlHocPhan = "Select ID, name, birthday, email, studyProgram, hocPhan, fees, namHoc " +
                        "from tblHocCtrMau where hocPhan = " + Float.valueOf(searchStudentText.getText());
                try {
                    pst = conn.prepareStatement(sqlHocPhan);
                    result = pst.executeQuery();
                    while (result.next()) {
                        StudentCtrMau s = new StudentCtrMau();
                        s.setId(result.getString("ID"));
                        s.setName(result.getString("name"));
                        s.setBirthday(result.getString("birthday"));
                        s.setEmail(result.getString("email"));
                        s.setStudyProgram(result.getString("studyProgram"));
                        s.setHocPhan(result.getInt("hocPhan"));
                        s.setFees(String.valueOf(result.getFloat("fees")) + "00");
                        s.setNamHoc(result.getString("namHoc"));

                        data.add(s);
                    }
                    ctrStudentTable.setItems(data);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    /**
     * THỐNG KÊ SINH VIÊN THEO THỜI GIAN NHẬP VÀO
     */
    @FXML
    void handleStatisticStudent(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent thongKe = FXMLLoader.load(getClass().getResource("/view/ThongKeCtrMau.fxml"));
        Scene root = new Scene(thongKe);
        stage.setScene(root);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.show();
    }

    /**
     * TÍNH TỔNG HỌC PHÍ TRONG 1 NĂM HỌC NHẬP VÀO
     */
    @FXML
    void handleSumFees(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent sumFees = FXMLLoader.load(getClass().getResource("/view/SumFeesCtrMau.fxml"));
        Scene root = new Scene(sumFees);
        stage.setScene(root);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.show();
    }

    /**
     * RÀNG BUỘC, LIÊN KẾT TABLEVIEW VỚI CÁC THUỘC TÍNH CỦA STUDENT
     */
    private void setCellTable() {
        ctrIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ctrNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ctrBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        ctrEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        ctrStudyProgramColumn.setCellValueFactory(new PropertyValueFactory<>("studyProgram"));
        ctrHocPhanColumn.setCellValueFactory(new PropertyValueFactory<>("hocPhan"));
        ctrFeesColumn.setCellValueFactory(new PropertyValueFactory<>("fees"));
        ctrNamHocColumn.setCellValueFactory(new PropertyValueFactory<>("namHoc"));
    }

    /**
     * LOAD DATA TỪ SQL RA TABLEVIEW THÔNG QUA CÁC THUỘC TÍNH CỦA STUDENT
     */
    private void loadDataFromDatabase() {
        data.clear();
        try {
            pst = conn.prepareStatement("Select ID, name, birthday, email, studyProgram, hocPhan, fees, namHoc from tblHocCtrMau");
            result = pst.executeQuery();  //Truy vấn: executeQuery
            while (result.next()) {
                StudentCtrMau s = new StudentCtrMau();
                s.setId(result.getString("ID"));
                s.setName(result.getString("name"));
                s.setBirthday(result.getString("birthday"));
                s.setEmail(result.getString("email"));
                s.setStudyProgram(result.getString("studyProgram"));
                s.setHocPhan(result.getInt("hocPhan"));
                s.setFees(String.valueOf(result.getFloat("fees")) + "00");
                s.setNamHoc(result.getString("namHoc"));

                data.add(s);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ctrStudentTable.setItems(data);
    }

    /**
     * XÓA DỮ LIỆU Ở TEXTFIELD (LÀM MỚI)
     */
    private void clearTextField() {
        ctrIdText.clear();
        ctrNameText.clear();
        ctrBirthdayDate.setValue(null);
        ctrEmailText.clear();
        ctrStudyProgramText.clear();
        ctrHocPhanText.clear();
        ctrNamHocText.clear();
    }

    /**
     * LOAD DATA TRÊN 1 DÒNG CỦA TABLE VIEW LÊN TEXTFIELD
     */
    private void setCellValueFromTableToTextField() {
        ctrStudentTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                StudentCtrMau s = ctrStudentTable.getItems().get(ctrStudentTable.getSelectionModel().getSelectedIndex());
                ctrIdText.setText(s.getId());
                ctrNameText.setText(s.getName());
                ctrBirthdayDate.setValue(LocalDate.parse(s.getBirthday(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                ctrEmailText.setText(s.getEmail());
                ctrStudyProgramText.setText(s.getStudyProgram());
                ctrHocPhanText.setText(String.valueOf(s.getHocPhan()));
                ctrNamHocText.setText(s.getNamHoc());
            }
        });
    }

    /**
     * Check ID (primary key đã tồn tại chưa!)
     */
    private boolean checkIDStudentCtrMau() {
        String sqlCheck = "Select * from tblHocCtrMau where ID Like N'%" + ctrIdText.getText() + "%'";
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
     * TRỞ VỀ GIAO DIỆN CHÍNH
     */
    @FXML
    void handleCancelHocCtrMau(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent QLHocPhi = FXMLLoader.load(getClass().getResource("/view/QLHocPhi.fxml"));
        Scene scene = new Scene(QLHocPhi);
        stage.setScene(scene);
    }

}
