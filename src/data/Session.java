package data;

import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * Phiên giao dịch
 */
public class Session {
    /***
     * Mã cổ phiếu
     */
    private String ticker;

    /***
     * Ngày giao dịch
     */
    private Date date;

    /***
     * Giá mở cửa
     */
    private float open;

    /***
     * Giá đóng cửa
     */
    private float close;

    /***
     * Giá cao nhất trong phiên giao dịch
     */
    private float high;

    /***
     * Giá thấp nhất trong phiên giao dịch
     */
    private float low;

    /***
     * Khối lượng giao dịch
     */
    private int volume;

    // TODO: Các setter() dưới đây chỉ là tạm thời, cần phải kiểm duyệt các dữ liệu đầu vào (ví dụ như giá mở cửa phải là số dương, ...)

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        String d = dt1.format(date);
        return "Session{" +
                "ticker='" + ticker + '\'' +
                ", date=" + d +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", volume=" + volume +
                '}';
    }
}
