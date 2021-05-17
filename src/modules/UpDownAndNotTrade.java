package modules;

import utilities.Counter;

import java.util.Random;

public class UpDownAndNotTrade extends SentenceGenerator {

    @Override
    public String example() {
        return "Hiện có 153 mã tăng giá, 10 mã giảm giá và 3 mã chưa có giao dịch.";
    }

    @Override
    public String generate() {
        Counter counter = new Counter();
        int[] countHNX = counter.count(data[0].getSessions());
        int[] countHSX = counter.count(data[1].getSessions());
        int[] countUPCOM = counter.count(data[2].getSessions());

        int[] count = new int[4];
        for(int i = 0; i < 4; i++){
            count[i] = countHNX[i] + countHSX[i] + countUPCOM[i];
        }
        String[] ret = new String[3];
        ret[0] = String.format("Hiện có %d mã tăng giá, %d mã giảm giá và %d mã chưa có giao dịch.", count[0], count[1], count[3]);
        ret[1] = String.format("Toàn thị trường có %d mã tăng, %d mã giảm và %d mã không giao dịch", count[0], count[1], count[3]);
        ret[2] = String.format("Số mã tăng: %d mã, số mã giảm: %d mã, số mã không giao dịch: %d mã", count[0], count[1], count[3]);
        return ret[new Random().nextInt(3)];
    }
}