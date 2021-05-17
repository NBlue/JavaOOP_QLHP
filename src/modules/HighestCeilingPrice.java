package modules;

import data.Session;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HighestCeilingPrice extends SentenceGenerator {

    @Override
    public String example() {
        return "Tính đến thời điểm này thì mã ABC có giá trần cao nhất là 21.9.";
    }

    @Override
    public String generate() {
        String maxTicker = "";
        float max = 0f;

        // Duyệt trên cả 3 sàn, tìm ra mã có giá trần cao nhất
        for(int i = 0; i < 3; i++) {
            Map<String, Float> map = new HashMap<>();

            Session[] sessions = data[i].getSessions();
            Date today = sessions[0].getDate();
            Date previousDay = null; // Ngày giao dịch trước đó

            // Tìm previousDay
            for (Session s : sessions) {
                if (!s.getDate().equals(today)) {
                    previousDay = s.getDate();
                    break;
                }
            }

            for (Session s : sessions) {
                String ticker = s.getTicker();
                float high = s.getHigh();
                if (s.getDate().equals(today)) {
                    map.put(ticker, s.getHigh());
                }
                else if (s.getDate().equals(previousDay)) {
                    if (map.containsKey(ticker)) {
                        float newHigh = map.get(ticker);
                        if (newHigh > high) {
                            float max1 = newHigh;
                            if (max1 > max) {
                                maxTicker = ticker;
                                max = max1;
                            }
                        }
                    }
                }
            }
        }

        String[] ret = new String[2];
        ret[0] = String.format("Tính đến thời điểm này thì mã %s có giá trần cao nhất là %f .", maxTicker, max);
        ret[1] = String.format("Kết thúc phiên giao dịch ngày hôm nay, mã %s có giá trần lớn nhất là %f.", maxTicker, max);
        return ret[new Random().nextInt(2)];
    }
}