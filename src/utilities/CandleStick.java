package utilities;

import data.Input;
import data.Session;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static utilities.helper.UtilityHelper.approximatelyEqual;

public class CandleStick {

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
     * Loại nến
     */
    private CandleType type;


    public CandleStick() {
        type = CandleType.NONE;
    }

    public CandleStick(float open, float high, float low, float close) {
        type = CandleType.NONE;
        init(open, high, low, close);
    }

    public CandleStick(Session session) {
        type = CandleType.NONE;
        init(session);
    }

    public void init(Session session) {
        init(session.getOpen(), session.getHigh(), session.getLow(), session.getClose());
    }

    public void init(float open, float high, float low, float close) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;

//        /**
//         *  Get new value attribute of Candle Stick when compare to low
//         */
//        open -= low;
//        high -= low;
//        low = 0;
//        close -= low;

        if (approximatelyEqual(open, close ,0.5f)) {
            setType(CandleType.Doji);
            if (approximatelyEqual(open, high, 1)
                    || approximatelyEqual(close, high, 1))
            {
                setType(CandleType.Dragonfly_Doji);
            }
            else if (approximatelyEqual(open, low, 1)
                    || approximatelyEqual(close, low, 1))
            {
                setType(CandleType.Gravestone_Doji);
            }
        }
        else if (open < close) {
            setType(CandleType.White_Body);

            if (approximatelyEqual(open, low, 1)
                        && approximatelyEqual(close, high, 1))
            {
                setType(CandleType.Marubozu);
            }
            else if (approximatelyEqual(open, close, 5,10))
            {
                if (approximatelyEqual(close, high))
                    setType(CandleType.Hammer);
                else if (approximatelyEqual(open, low))
                    setType(CandleType.Inverted_Hammer);
            }
            else if (approximatelyEqual(high, low, 10, 50)) {
                if (approximatelyEqual(open, close,  5, 10)
                        && approximatelyEqual(high - close, open - low) ) {
                    setType(CandleType.Spinning_Top);
                }
            }

        }
        else if (open > close) {
            setType(CandleType.Black_Body);
            if (approximatelyEqual(open, close, 5, 10))
            {
                if (approximatelyEqual(open, high))
                    setType(CandleType.Hanging_Man);
                else if (approximatelyEqual(close, low, 5))
                    setType(CandleType.Long_Upper_Shadow);
            }
        }
    }

    public CandleType getType() {
        return type;
    }

    public void setType(CandleType type) {
        this.type = type;
    }

    public float getOpen() {
        return open;
    }

    public float getClose() {
        return close;
    }

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }
}
