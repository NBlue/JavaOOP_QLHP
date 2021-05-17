package utilities;

import data.Input;
import data.Session;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Filter {

    /**
     * Lọc các phiên giao dịch theo nhóm cổ phiếu
     */
    public Session[] filter(Session[] sessions, String group) {
        String[] tickers = null;
        Dictionary dict = new Dictionary();

        // Tìm các mã cổ phiếu tương ứng trong các nhóm ngành
        for (String s : Dictionary.INDUSTRY_GROUPS) {
            if (s.equals(group)) {
                tickers = dict.getTickersByIndustryGroup(group);
                break;
            }
        }

        // Nếu ko thấy, tiếp tục tìm các mã cổ phiếu cần tìm trong các rổ
        if (tickers == null) {
            for (String s : Dictionary.STOCK_BASKETS) {
                if (s.equals(group)) {
                    tickers = dict.getTickersByStockBasket(group);
                    break;
                }
            }
        }

        if (tickers == null) {
            System.out.println("Tên nhóm ngành hoặc tên rổ cổ phiếu không hợp lệ");
            return null;
        }

        ArrayList<Session> ret = new ArrayList<>();
        for (Session session : sessions) {
            for (var ticker : tickers) {
                if (ticker.equals(session.getTicker())) {
                    ret.add(session);
                    break;
                }
            }
        }

        return ret.toArray(new Session[0]);
    }

    /**
     * Lọc phiên giao dịch theo thời gian
     */
    public Session[] filter(Session[] sessions, Date from, Date to) {
        // Kiểm tra đầu vào hợp lệ
        if (from.compareTo(to) > 0){
            throw new IllegalArgumentException("\"from\" không được lớn hơn \"to\"");
        }

        ArrayList<Session> ret = new ArrayList<>();
        for (var s : sessions) {
            if (s.getDate().compareTo(from) >= 0 && s.getDate().compareTo(to) <= 0) {
                ret.add(s);
            }
        }

        return ret.toArray(new Session[0]);
    }
}
