package modules;

import data.Data;
import data.Session;
import data.StockExchange;
import utilities.Counter;

import java.util.*;


/***
 * Đếm số mã tăng, mã giảm và mã đứng giá trên toàn thị trường
 * Ý tưởng thực hiện:
 * Duyệt các phiên giao dịch trong 2 ngày gần nhất.
 * So sánh giá đóng cửa (CLOSE) của cùng một mã cổ phiếu trong 2 ngày đó, xem nó tăng hay giảm, rồi đếm vào kết quả.
 */
public class CountUpAndDown extends SentenceGenerator{
    private Session[] sessions;
    @Override
    public String example() {
        return "Toàn thị trường hiện đang có 77 mã tăng giá, 33 mã giảm giá và 11 mã đứng giá.";
    }

    @Override
    public String generate() {
        Counter counter = new Counter();
        int[] countHNX = counter.count(data[0].getSessions());
        int[] countHSX = counter.count(data[1].getSessions());
        int[] countUPCOM = counter.count(data[2].getSessions());

        int[] count = new int[3];
        for(int i = 0; i < 3; i++){
            count[i] = countHNX[i] + countHSX[i] + countUPCOM[i];
        }

        String[] result = new String[3];
        result[0] = String.format("Toàn thị trường hiện đang có %d mã tăng giá, %d mã giảm giá và %d mã đứng giá", count[0], count[1], count[2]);
        result[1] = String.format("Hiện có %d mã tăng giá, %d mã giảm giá và %d mã đứng giá trên toàn thị trường", count[0], count[1], count[2]);
        result[2] = String.format("Đến thời điểm này, toàn thị trường có %d mã tăng giá, %d mã giảm giá và %d mã đứng giá", count[0], count[1], count[2]);
        Random r = new Random();
        return result[r.nextInt(3)];
    }

}