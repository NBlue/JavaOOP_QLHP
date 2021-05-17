package modules;

import data.Session;
import utilities.Dictionary;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Volume extends SentenceGenerator {

    @Override
    public String example() {
        return "Cổ phiếu X dẫn đầu thị trường với khối lượng giao dịch lên tới 100000 cổ phiếu.";
    }

    @Override
    public String generate() {
        String Ticker = "";
        float maxVolume = 0;

        for (int i = 0; i < 3; i++) {

            Session[] sessions = data[i].getSessions();
            Date today = sessions[0].getDate();

            for (Session s : sessions) {
                if (s.getDate().equals(today)) {
                    String ticker = s.getTicker();
                    float volume = s.getVolume();
                    if (volume > maxVolume) {
                        Ticker = ticker;
                        maxVolume = volume;

                    }
                }
            }
        }

        String[] ret = new String[2];
        ret[0] = String.format("Hôm nay, cổ phiếu %s dẫn đầu thị trường với khối lượng giao dịch lên tới %f cổ phiếu.", Ticker, maxVolume);
        ret[1] = String.format("Hôm nay, cổ phiếu %s của %s dẫn đầu thị trường với khối lượng giao dịch lên tới %f cổ phiếu.",
                Ticker, new Dictionary().getEnterpriseName(Ticker), maxVolume);

        return ret[new Random().nextInt(2)];
    }
}

