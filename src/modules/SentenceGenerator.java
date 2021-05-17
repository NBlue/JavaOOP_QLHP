package modules;

import data.Data;
import data.Input;
import data.Session;
import javafx.beans.property.SimpleStringProperty;

public abstract class SentenceGenerator {

    protected Data[] data;

    public SentenceGenerator() {
        // Lấy dữ liệu từ lớp Input
        if (Input.inputData != null){
            data = Input.inputData;
        }
        else{
            System.out.println("ERROR: Input data is not initialized");
        }
    }

    /***
     *
     * @return Câu mẫu (để người dùng chọn)
     */
    public abstract String example();

    /***
     *
     * @return Tạo ra một câu theo mẫu, dựa vào dữ liệu đã có
     */
    public abstract String generate();
}
