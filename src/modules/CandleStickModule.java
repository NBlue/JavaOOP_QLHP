package modules;

import data.Input;
import data.Session;
import utilities.CandleStick;
import utilities.CandleType;
import utilities.Filter;

public class CandleStickModule extends SentenceGenerator{

    private final String basketName;

    public CandleStickModule(String basketName){
        super();
        this.basketName = basketName;
    }

    @Override
    public String example() {
        return this.basketName + " hôm nay giao dịch tạo thành cây nến Doji.";
    }

    @Override
    public String generate() {
        Filter f = new Filter();
        Session[] sessions = Input.inputData[3].getSessions();
        sessions = f.filter(sessions, this.basketName );
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
        return this.basketName +" hôm nay giao dịch tạo thành cây nến " + type.toString();
    }
}
