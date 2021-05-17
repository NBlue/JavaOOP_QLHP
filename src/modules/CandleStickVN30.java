package modules;

import data.Input;
import data.Session;
import utilities.CandleStick;
import utilities.CandleType;
import utilities.Dictionary;
import utilities.Filter;

import java.util.Date;

public class CandleStickVN30 extends SentenceGenerator{
    @Override
    public String example() {
        return "VN30 hôm nay giao dịch tạo thành cây nến Doji.";
    }

    @Override
    public String generate() {
        Filter f = new Filter();
        Session[] sessions = Input.inputData[3].getSessions();
        sessions = f.filter(sessions, "VN30");
        sessions = f.filter(sessions, sessions[0].getDate(), sessions[0].getDate());
        float sumOpen = 0, sumClose = 0, sumHigh = 0, sumLow = 0;
        for (var s : sessions) {
            sumOpen += s.getOpen();
            sumClose += s.getClose();
            sumHigh += s.getHigh();
            sumLow += s.getLow();
        }
        CandleStick candleStick = new CandleStick(sumOpen, sumHigh, sumLow, sumClose);
        CandleType type = candleStick.getType();
        return "VN30 hôm nay giao dịch tạo thành cây nến " + type.toString();
    }
}
