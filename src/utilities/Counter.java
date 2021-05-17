package utilities;

import data.Session;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Đếm số mã tăng, giảm, đứng giá, không giao dịch => Nhật
 * <p>
 * input: Data
 * output: số mã tăng, giảm, đứng giá, không giao dịch trong ngày gần nhất
 * </p>
 */
public class Counter {

    /**
     *
     * @param sessions
     * @return [số mã tăng, số mã giảm, số mã đứng, số mã không giao dịch]
     */
    public int[] count(Session[] sessions){
        int[] result = new int[4];
        Map<String, Float> map = new HashMap<>();

        Date today = sessions[0].getDate();
        Date previousDay = null; // Ngày giao dịch trước đó

        // Tìm previousDay
        for (Session s : sessions){
            if (!s.getDate().equals(today)){
                previousDay = s.getDate();
                break;
            }
        }
        
        if(previousDay == null) {
        	// Get previousday :)))
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, -1);
            previousDay = calendar.getTime();
        }
        
        System.out.println(previousDay);
        System.out.println(today);

        for (Session s : sessions) {
            String ticker = s.getTicker();
            if(s.getDate().equals(today)){
                map.put(ticker, s.getClose());
            }
            else if (s.getDate().equals(previousDay)){
                if (map.containsKey(ticker)) {
                    float close = s.getClose();
                    float newClose = map.get(ticker);
                    if (newClose - close > 0) {
                        result[0] +=1;
                    } else if (newClose - close < 0) {
                        result[1] +=1;
                    }else{
                        result[2] +=1;
                    }
                }
                else{
                    result[3] += 1;
                }
            }
            else{
                break;
            }
        }

        return result;
    }
}
