package modules;

import data.Session;
import utilities.Dictionary;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PriceFloor extends SentenceGenerator {

    @Override
    public String example() {
        return "Hết phiên giao dịch ngày hôm nay, số mã tăng trần chiếm áp đảo ";
    }

    @Override
    public String generate() {
        int t = 0;
        int g = 0;


        Map<String, Float> map = new HashMap<>();

        Session[] sessions = data[0].getSessions();
        Date today = sessions[0].getDate();
        Date previousDay = null;

        for (Session s : sessions) {
            if (!s.getDate().equals(today)) {
                previousDay = s.getDate();
                break;
            }
        }

        for (Session s : sessions) {
            String ticker = s.getTicker();
            float close = s.getClose();
            if (s.getDate().equals(today)) {
                map.put(ticker, s.getClose());
            } else if (s.getDate().equals(previousDay)) {
                if (map.containsKey(ticker)) {
                    float newClose = map.get(ticker);
                    if (newClose == close * 1.1) {
                        t += 1;
                    }
                    if (newClose == close * 0.9) {
                        g += 1;
                    }
                }
            }
        }

        if (t > g) {
            return "Kết thúc phiên giao dịch ngày hôm nay, trên sàn HNX, số mã tăng trần chiếm áp đảo với "
                    + t + "mã.";
        } else if (t < g) {
            return "Kết thúc phiên giao dịch ngày hôm nay, trên sàn HNX, số mã giảm sàn chiếm áp đảo với"
                    + g + "mã.";
        }

        return null;
    }
}


