package modules;

import data.Input;
import data.Session;

import java.text.NumberFormat;
import java.util.Date;

import java.util.Locale;
import java.util.Random;

/**
 * Tóm tắt tình hình của một công ty
 *
 * @author Hieu
 */
public class Summary extends SentenceGenerator {

    private String ticker;

    public Summary(){
        String[] tickerSample = {"STB", "HPG", "CTG", "ITA", "PVD", "FPT", "SBT", "DPM", "HAG", "SSI", "VNM", "KDM", "MSN", "PNJ", "CII", "GC", "VCB", "EIB", "VIC", "QCG", "DIG", "BVH", "GMD", "ISJ", "KDC", "SJS", "REE", "HVG", "ACB", "", "BVS", "CAP", "CEO", "DDG", "DGC", "DHT", "DP3", "DTD", "HUT", "KLF", "L14", "LHC", "MBS", "NDN", "NRC", "PVB", "PVC", "PVI", "PVS", "SHB", "SHS", "SLS", "TNG", "TVC", "VCB", "VCG", "VGS", "VMC"};
        ticker = tickerSample[new Random().nextInt(tickerSample.length)];
    }

    @Override
    public String example() {
        return "So với ngày hôm qua, giá cổ phiếu  tăng: 15%, tổng số tiền giao dịch: 150.000 VNĐ, tổng khối lượng giao dịch: 200 cổ phiếu";
    }

    @Override
    public String generate() {
        double[] result = calc(Input.inputData[3].getSessions());
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VI"));
        String res = "";
        if (result[0] > 0) {
            res = String.format("So với ngày hôm qua, giá cổ phiếu " + ticker + " tăng: %.2f%%, tổng số tiền giao dịch: %s VNĐ, " +
                    "tổng khối lượng giao dịch: %d cổ phiếu", result[0], numberFormat.format(result[2]), (long) result[1]);
        } else if (result[0] < 0) {
            res = String.format("So với ngày hôm qua, giá cổ phiếu " + ticker + " giảm: %.2f%%, tổng số tiền giao dịch: %s VNĐ, " +
                    "tổng khối lượng giao dịch: %d cổ phiếu", -result[0], numberFormat.format(result[2]), (long) result[1]);
        } else {
            res = String.format("So với ngày hôm qua, giá cổ phiếu " + ticker + " không đổi, tổng số tiền giao dịch: %s VNĐ, " +
                    "tổng khối lượng giao dịch: %d cổ phiếu", numberFormat.format(result[2]), (long) result[1]);
        }
        return res;
    }

    private double[] calc(Session[] sessions) {
        double[] result = new double[3];
        Date Today = sessions[0].getDate();
        Date Tomorrow = null;

        // Tìm ngày hôm qua
        for (Session s : sessions) {
            if (!Today.equals(s.getDate())) {
                Tomorrow = s.getDate();
                break;
            }
        }
        /* Tính toán: result[0]: % tăng giảm
                      result[1]: khối lượng giao dịch
                      result[2]: tổng tiền
         */
        for (Session s : sessions) {
            if (s.getTicker().equals(ticker)) {
                if (s.getDate().equals(Today)) {
                    if (s.getClose() == 0) break; // nếu ngày hôm qua ko giao dịch thì break
                    result[0] = s.getClose();
                    result[1] = s.getVolume();
                    result[2] = result[0] * result[1];
                } else if (s.getDate().equals(Tomorrow)) {
                    result[0] = (result[0] - s.getClose()) / result[0] * 100;
                    break;
                }

            }
        }
        return result;
    }
}
