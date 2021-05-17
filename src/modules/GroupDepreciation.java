package modules;

import data.Input;
import data.Session;
import utilities.Dictionary;
import utilities.Filter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GroupDepreciation extends SentenceGenerator{

    @Override
    public String example() {
        return "Trên sàn UPCOM, những mã dầu khí như OIL, BSR đồng loạt giảm sâu trong phiên hôm nay.";
    }

    @Override
    public String generate() {
        Random r = new Random();
        String randomGroup = Dictionary.INDUSTRY_GROUPS[r.nextInt(Dictionary.INDUSTRY_GROUPS.length)];
        int randomStockExchange = r.nextInt(4);
        return generate(randomStockExchange, randomGroup);
    }

    /***
     * Sinh ra câu có những mã giảm trên sàn stockExchange thuộc nhóm ngành group
     */
    public String generate(int stockExchange, String group) {
        Filter filter = new Filter();
        Session[] sessions = filter.filter(Input.inputData[stockExchange].getSessions(), group);

        String ticker1 = "";
        String ticker2 = "";
        double percentReduction1 = 0, percentReduction2 = 0;
        Map <String, Float> map = new HashMap<>();

        Date today = sessions[0].getDate();
        Date previousDay = null; // Ngày giao dịch trước đó

        // Tìm previousDay
        for (Session s : sessions){
            if (!s.getDate().equals(today)){
                previousDay = s.getDate();
                break;
            }
        }

        // Tìm danh sách những mã giảm
        for (Session s : sessions) {
            String ticker = s.getTicker();
            if(s.getDate().equals(today)){
                map.put(ticker, s.getClose());
            }
            else if (s.getDate().equals(previousDay)){
                if (map.containsKey(ticker)) {
                    float percentReduction = (s.getClose() - map.get(ticker))/s.getClose();
                    if(percentReduction > percentReduction2){
                        percentReduction2 = percentReduction;
                        ticker2 = ticker;
                        if(percentReduction > percentReduction1){
                            percentReduction2 = percentReduction1;
                            ticker2 = ticker1;
                            percentReduction1 = percentReduction;
                            ticker1 = ticker;
                        }
                    }
                }
            }
            else{
                break;
            }
        }

        String s = "Trên ";
        switch (stockExchange){
            case 0: s += "sàn HNX"; break;
            case 1: s += "sàn HSX"; break;
            case 2: s += "sàn UPCOM"; break;
            case 3: s += "cả ba sàn";
        }
        if(!ticker2.isEmpty()){
            s += ", những mã " + group.toLowerCase() + " như " + ticker1 + ", " + ticker2 + " đồng loạt giảm sâu trong phiên hôm nay.";
        }else{
            if(ticker1.isEmpty()){
                return s + ", không có mã " + group.toLowerCase() + " nào giảm trong phiên hôm nay.";
            }
            s += ", mã " + ticker1 + " thuộc nhóm " + group.toLowerCase() + " giảm sâu trong phiên hôm nay";
        }
        return s;
    }
}
