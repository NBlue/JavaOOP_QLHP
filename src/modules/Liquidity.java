package modules;

import data.Input;
import data.Session;
import utilities.Dictionary;

import java.util.Date;
import java.util.Random;
import java.util.zip.DeflaterInputStream;

public class Liquidity extends SentenceGenerator {

    @Override
    public String example() {
        return "Cổ phiếu cuả Công ty Cổ phần Nhựa An Phát Xanh có tính thanh khoản cao có thể đầu tư lâu dài";
    }

    @Override
    public String generate() {
        Dictionary d = new Dictionary();
        String[] vn30Tickers = d.getTickersByStockBasket("VN30");
        Random r = new Random();
        return generate(vn30Tickers[r.nextInt(vn30Tickers.length)]);
    }

    public String generate(String stock) {
        Session[] sessions = data[1].getSessions();
        Date today = sessions[0].getDate();
        for (Session s : sessions) {
            String ticker = s.getTicker();
            float high = s.getHigh();
            float low = s.getLow();
            int volume = s.getVolume();
            if ((s.getDate().equals(today)) && (s.getTicker().equals(stock))) {
                if ((volume > 10000) && (((high - low) / high) < 0.1)) {
                    return String.format("Cổ phiếu %s của %s có tính thanh khoản cao có thể đầu tư lâu dài", stock, new Dictionary().getEnterpriseName(stock));
                }
            }
        }

        return String.format("Cổ phiếu %s của %s có tính thanh khoản chưa cao, cần phải xem xét để có thể đầu tư", stock, new Dictionary().getEnterpriseName(stock));
    }
}
