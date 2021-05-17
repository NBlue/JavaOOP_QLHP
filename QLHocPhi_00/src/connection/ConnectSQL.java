/** @ADMIN: Nguyễn Phương Nam - 20194336
 *  - Tạo Database: QLHocPhiSV, backup lại để cách thành viên restore, kết nối database bằng class ConnectSQl.
 *
 *
 *  CHÚ Ý: ĐỂ PROJECT CHẠY ĐƯỢC CẦN:
 *  +) CÓ SQL SERVER, DATABASE: QLHocPhiSV
 *  +) username and password của máy desktop, laptop đăng nhập SQL server của bạn.
 *  => Thay đổi code đúng ở dòng 20, 21
 * */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectSQL {
    public static Connection studentConnection(){
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QLHocPhiSV;" +
                                             "username=sa;password=01052001");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }
}
