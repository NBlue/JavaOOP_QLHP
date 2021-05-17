package modules;

import data.Data;
import data.Session;
import data.StockExchange;
import utilities.Dictionary;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DifferencePercent extends SentenceGenerator {

    @Override
    public String example() {
        return "Tính đến thời điểm này thì mã VNM tăng mạnh nhất với 12% và mã DDG giảm mạnh nhất với 1.5%";
    }

    @Override
    public String generate() {
        String maxTicker = "";
        String minTicker = "";
        float maxPercent = 0f;
        float minPercent = 0f;
        float Difference = 0f;

        // Duyệt trên cả 3 sàn, tìm ra mã có phần trăm tăng cao nhất và mã có phần trăm giảm cao nhất
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
                float close = s.getClose();
                if (s.getDate().equals(today)) {
                    map.put(ticker, s.getClose());
                }
                else if (s.getDate().equals(previousDay)) {
                    if (map.containsKey(ticker)) {
                        float newClose = map.get(ticker);
                        if (newClose > close) {
                            float percent1 = (newClose - close) / close;
                            if (percent1 > maxPercent) {
                                maxTicker = ticker;
                                maxPercent = percent1;
                            }
                        } else if (newClose < close) {
                            float percent2 = (newClose - close) / close;
                            if (percent2 < minPercent) {
                                minTicker = ticker;
                                minPercent = -percent2;
                            }
                        }
                    }
                }
            }
        }

        String[] ret = new String[2];
        ret[0] = String.format("Tính đến thời điểm này thì mã %s tăng mạnh nhất với %f %% và mã %s giảm mạnh nhất với %f %%", maxTicker, maxPercent,minTicker,minPercent);
        ret[1] = String.format("Kết thúc phiên giao dịch ngày hôm nay, mã %s tăng mạnh nhất với %f %% và mã %s giảm mạnh nhất với %f %%", maxTicker, maxPercent,minTicker, minPercent);
        return ret[new Random().nextInt(2)];
    }
}