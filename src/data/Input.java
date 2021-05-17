package data;

import java.util.*;

import data.service.Crawler;
import data.service.FileHelper;

public class Input {

    public static boolean isUpdateByAuto;
    /***
     * Dữ liệu sau khi được cập nhật lưu vào đây
     * inputData[0]: dữ liệu trên sàn HNX
     * inputData[1]: dữ liệu trên sàn HSX
     * inputData[2]: dữ liệu trên sàn UPCOM
     * inputData[3]: dữ liệu trên cả 3 sàn (để tiện cho tính toán)
     */
    public static Data[] inputData;

    private final static String DOWNLOAD_URL = "https://s.cafef.vn/du-lieu/download.chn";

    public static void updateDataFromLocal(String filePath){
        isUpdateByAuto = false;
        Data[] HNX_HSX_UPCOM = getDataFromLocal(filePath);

        // Gộp 3 sàn lại vào inputData[3]
        inputData = new Data[4];
        Session[] sessions = new Session[HNX_HSX_UPCOM[0].getSessions().length
                + HNX_HSX_UPCOM[1].getSessions().length
                + HNX_HSX_UPCOM[2].getSessions().length];
        int k = 0;
        for(int i = 0; i < 3; i++){
            inputData[i] = HNX_HSX_UPCOM[i];
            for(var s : inputData[i].getSessions()){
                sessions[k++] = s;
            }
        }

        inputData[3] = new Data(StockExchange.ALL, sessions);
    }
    
    public static void updateDataFromWeb() {
        isUpdateByAuto = true;
        Data[] HNX_HSX_UPCOM = getDataFromWeb(DOWNLOAD_URL);
        // Gộp 3 sàn lại vào inputData[3]
        inputData = new Data[4];
        Session[] sessions = new Session[HNX_HSX_UPCOM[0].getSessions().length
                + HNX_HSX_UPCOM[1].getSessions().length
                + HNX_HSX_UPCOM[2].getSessions().length];
        int k = 0;
        for(int i = 0; i < 3; i++){
            inputData[i] = HNX_HSX_UPCOM[i];
            System.out.print(""+ inputData[i].getStockExchange() + inputData[i].getSessions().length);
            for(var s : inputData[i].getSessions()){
                sessions[k++] = s;
            }
        }

        inputData[3] = new Data(StockExchange.ALL, sessions);
    }

    private static Data[] getDataFromWeb(String  url){
        Crawler crawler = Crawler.getInstance();
        FileHelper fileHelper = FileHelper.getInstance();
        ArrayList<Data> datasFromWeb = new ArrayList<Data>();
        
        ArrayList<String> downloadLinks = crawler.crawlDownloadLink(url);
        
        if(downloadLinks.size()>0) {
        	datasFromWeb = fileHelper.scanFileByUrl(downloadLinks.get(1));	
        }
        
        Data[] ret = new Data[datasFromWeb.size()];
        for(int i = 0; i < datasFromWeb.size(); i++){
            ret[i] = datasFromWeb.get(i);
        }
        return ret;

    }

    public static Data[] getDataFromLocal(String filePath){
        System.out.println("Loading file " + filePath + " ...");
    	FileHelper fileHelper = FileHelper.getInstance();
    	ArrayList<Data> datasFromLocal = fileHelper.scanFile(filePath);

        System.out.println("Data loaded");
        Data[] ret = new Data[datasFromLocal.size()];
        for(int i = 0; i < datasFromLocal.size(); i++){
            ret[i] = datasFromLocal.get(i);
            System.out.println(ret[i].getStockExchange());
            System.out.println(ret[i].getSessions().length + " record");
        }
        System.out.println();
        return ret;
    }

//    /***
//     *  Hàm lấy dữ liệu ảo.
//     *  Lưu ý: Dữ liệu này này chỉ ở sàn HNX
//     * @return
//     */
//    public Data getExampleData(){
//        List<Session> sessions = new ArrayList<>();
//
//        Scanner sc = null;
//        try {
//            sc = new Scanner(new File("res/Example_Data.csv"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        assert sc != null;
//        sc.useDelimiter("\n");
//        sc.next();
//
//        sc.useDelimiter("[,\n]");
//
//        while (sc.hasNext())  //returns a boolean value
//        {
//            Session session = new Session();
//            session.setTicker(sc.next());
//
//            // e.g: "20201015" -> "2020-10-15"
//            String date = sc.next();
//            Date d = null;
//            try {
//                d = (Date) new SimpleDateFormat("yyyyMMdd").parse(date);
//                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-mm-dd");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            session.setDate(d);
//
//            session.setOpen(Float.parseFloat(sc.next()));
//            session.setHigh(Float.parseFloat(sc.next()));
//            session.setLow(Float.parseFloat(sc.next()));
//            session.setClose(Float.parseFloat(sc.next()));
//            session.setVolume(Integer.parseInt(sc.next().trim()));
//
//            sessions.add(session);
//        }
//        sc.close();
//
//        Session[] ret = new Session[sessions.size()];
//        for(int i = 0; i < sessions.size(); i++){
//            ret[i] = sessions.get(i);
//        }
//
//        return new Data(StockExchange.HNX, ret);
//    }
}
