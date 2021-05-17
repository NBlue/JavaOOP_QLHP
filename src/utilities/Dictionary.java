package utilities;

import java.io.*;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Dictionary {
    public static final String[] INDUSTRY_GROUPS = {
            "Bảo hiểm",
            "Bất động sản",
            "Công nghệ",
            "Dầu khí",
            "Dịch vụ bán lẻ",
            "Dịch vụ tài chính",
            "Dịch vụ tiện ích",
            "Đồ dùng cá nhân và đồ gia dụng",
            "Du lịch & Giải trí",
            "Hàng hoá và dịch vụ công nghiệp",
            "Hoá chất",
            "Ngân hàng",
            "Ô tô & linh kiện phụ tùng",
            "Phương tiện truyền thông",
            "Tài nguyên",
            "Thực phẩm & Đồ uống",
            "Viễn thông",
            "Xây dựng & Vật liệu",
            "Y tế",
            "Cao su",
            "Hàng không",
            "Chứng khoán",
            "Giáo dục",
            "Thuỷ sản",
            "Thép",
            "Khoáng sản",
            "Năng lượng",
            "Phân bón",
            "Nhựa & Bao bì",
            "Vận tải"
    };

    public static final String[] STOCK_BASKETS = {
            "VN30", "HNX30", "BlueChip", "HOT", "PHÒNG THỦ"
    };

    /**
     *
     * @param ticker
     * @return Tên doanh nghiệp
     */
    public String getEnterpriseName(String ticker){
        try{
            Scanner scanner = new Scanner(new File("res/general_information/enterprise name.txt"));
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] str = line.split("\t", 2);
                if (str[0].equals(ticker)){
                    return str[1];
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Mã cổ phiếu không tồn tại");
        return null;
    }

    /**
     *
     * @param groupName tên nhóm ngành
     * @return danh sách mã cổ phiếu
     */
    public String[] getTickersByIndustryGroup(String groupName) {
        try{
            Scanner scanner = new Scanner(new File("res/general_information/nhomnganh/" + groupName + ".txt"));
            int numberOfTicker = Integer.parseInt(scanner.nextLine());
            String[] ret = new String[numberOfTicker];
            for(int i = 0; i < numberOfTicker; i++){
                ret[i] = scanner.nextLine();
            }
            return ret;
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param basketName tên rổ cổ phiếu
     * @return danh sách mã cổ phiếu trong rổ tương ứng
     */
    public String[] getTickersByStockBasket(String basketName){
        try {
            BufferedReader ReadFile= null;
            FileReader fr=null;
            fr= new FileReader("res/general_information/rổ cổ phiếu.txt");
            ReadFile= new BufferedReader(fr);
            String line= ReadFile.readLine();
            String[] tickers = null;
            while (line!=null) {


                tickers = line.split("/");
                if (basketName.equals(tickers[0])) // tickers[0] lưu tên rổ
                    break;
                line = ReadFile.readLine();
            }
            return Arrays.copyOfRange(tickers, 1, tickers.length);

        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
