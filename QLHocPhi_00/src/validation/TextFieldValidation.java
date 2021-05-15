package validation;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TextFieldValidation {
    //Kiểm tra textField có rỗng ko?
    public  static boolean textFieldNotEmpty(TextField tf){
        boolean b = false;
        if(tf.getText().length() != 0 || !tf.getText().isEmpty()){
            b = true;
        }
        return b;
    }

    //Kiểm tra nếu textField rỗng thì hiển thị label: errorMessage
    public static boolean textFieldNotEmpty(TextField tf, Label lb, String errorMessage){
        boolean b = true;
        String msg = null;
        if(!textFieldNotEmpty(tf)){
            b = false;
            msg = errorMessage;
        }
        lb.setText(msg);
        return b;
    }


    /** Kiểm tra textField nhập vào là 1 số */
    public static boolean textFieldTypeNumber(TextField tf){
        boolean b = false;
        if(tf.getText().matches("([0-9]+(\\.[0-9]+)?)+"))
            b = true;
        return b;
    }

    public static boolean textFieldTypeNumber(TextField tf, Label lb, String errorMessage){
        boolean b = true;
        String msg = null;
        if(!textFieldTypeNumber(tf)){
            b = true;
            msg = errorMessage;
        }
        lb.setText(msg);
        return b;
    }
}
