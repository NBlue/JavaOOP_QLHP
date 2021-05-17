package modules;

import data.Session;
import java.util.Date;

public class SumLiquidity extends SentenceGenerator {

    @Override
    public String example() {
        return "Kết thúc phiên giao dịch ngày hôm nay, thanh khoản của thị trường lên tới hơn 2 triệu đơn vị.";
    }

    @Override
    public String generate() {
        long sumtoday = 0;
        long sumpsday = 0;

        for (int i = 0; i < 3; i++) {

            Session[] sessions = data[i].getSessions();
            Date today = sessions[0].getDate();
            Date previousDay = null;

            for (Session s : sessions) {
                if (!s.getDate().equals(today)) {
                    previousDay = s.getDate();
                    break;
                }
            }

            for (Session s : sessions) {
                
                if (s.getDate().equals(today)) {
                	sumtoday += s.getVolume();
                } else if (s.getDate().equals(previousDay)) {
                    sumpsday += s.getVolume();
                }
            }
        }
        if (sumtoday > sumpsday) {
            return "Kết thúc phiên giao dịch ngày hôm nay thanh khoản của thị trường lên tới " + sumtoday +
                    " tăng " + (sumtoday - sumpsday) + " đơn vị so với ngày hôm qua.";
        } else return "Kết thúc phiên giao dịch ngày hôm nay thanh khoản của thị trường lên tới " + sumtoday +
                " nhưng vẫn giảm " + (sumpsday - sumtoday) + " đơn vị so với ngày hôm qua.";

    }
}

