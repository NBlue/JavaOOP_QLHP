package modules;

import data.Data;
import data.Session;
import data.StockExchange;
import utilities.Dictionary;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MaxIncreasePercent extends SentenceGenerator{

    @Override
    public String example() {
        return "Hôm nay, mã ABC tăng mạnh nhất với 15%";
    }

    @Override
    public String generate() {
        String maxTicker = "";
        float maxPercent = 0f;

        // Duyệt trên cả 3 sàn, tìm ra mã có phần trăm tăng cao nhất
        for(int i = 0; i < 3; i++){
            Map<String, Float> map = new HashMap<>();

            Session[] sessions = data[i].getSessions();
            Date today = sessions[0].getDate();
            Date previousDay = null; // Ngày giao dịch trước đó

            // Tìm previousDay
            for (Session s : sessions){
                if (!s.getDate().equals(today)){
                    previousDay = s.getDate();
                    break;
                }
            }

            for (Session s : sessions) {
                String ticker = s.getTicker();
                float close = s.getClose();
                if (s.getDate().equals(today)){
                    map.put(ticker, s.getClose());
                }
                else if (s.getDate().equals(previousDay)){
                    if (map.containsKey(ticker)){
                        float newClose = map.get(ticker);
                        if (newClose > close){
                            float percent = (newClose - close) / close;
                            if (percent > maxPercent){
                                maxTicker = ticker;
                                maxPercent = percent;
                            }
                        }
                    }
                }
            }
        }

        String[] ret = new String[3];
        ret[0] = String.format("Hôm nay, mã %s tăng mạnh nhất với %f %%", maxTicker, maxPercent);
        ret[1] = String.format("Kết thúc phiên giao dịch ngày hôm nay, mã %s tăng mạnh nhất với %f %%", maxTicker, maxPercent);
        ret[2] = String.format("Hôm nay, mã %s (%s) tăng mạnh nhất với %f %%",
                maxTicker, new Dictionary().getEnterpriseName(maxTicker), maxPercent);

        return ret[new Random().nextInt(3)];
    }


}
