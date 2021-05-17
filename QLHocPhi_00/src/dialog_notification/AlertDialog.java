/**@admin: Nguyễn Mạnh Duy - 20194262
 * */
package dialog_notification;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertDialog {
    /** TẠO ALERT THÔNG BÁO THÀNH CÔNG */
    public static void display(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.show();
    }

    /** TẠO ALERT THÔNG BÁO LỖI*/
    public static void errorSignIn(String title, String message){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.show();
    }


    /** TẠO ALERT CÂU HỎI (CONFIRMATION) THOÁT MÀN HÌNH*/
    public static boolean displayExit(){
        //Alert.AlertType alertAlertType;
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Bạn chắc chắn muốn thoát không?");

        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTypeYes){
            return true;
        }
        return false;
    }
}
