package modules;

import data.Input;
import data.Session;
import utilities.Dictionary;
import utilities.Filter;

import java.util.*;

public class GroupMaximumAugmentation extends SentenceGenerator {
    @Override
    public String example() {
        return "Nhóm cổ phiếu chứng khoán có KLS, BVS và WSS tăng trần.";
    }

    @Override
    public String generate() {
        Random r = new Random();
        String randomGroup = Dictionary.INDUSTRY_GROUPS[r.nextInt(Dictionary.INDUSTRY_GROUPS.length)];
        return generate(randomGroup);
    }

    public String generate(String group){
        ArrayList <String> ret = new ArrayList<>();
        Filter filter = new Filter();
        Session[] sessions = filter.filter(Input.inputData[3].getSessions(), group);

        Date today = sessions[0].getDate();

        for (Session s : sessions) {
            if(s.getDate().equals(today)){
                if(s.getClose() == s.getHigh() && s.getClose() > s.getOpen()){
                    ret.add(s.getTicker());
                }
            }
        }

        String string = "Nhóm cổ phiếu " + group.toLowerCase();
        if(ret.isEmpty()){
            return string + " không có mã nào tăng trần";
        }
        string += " có ";
        for(int i = 0; i < ret.size()-1; i++){
            if(i > 2){
                break;
            }
            string += ret.get(i);
            if((i < 1 && ret.size() == 3) || (ret.size() == 4 && i < 2) || ret.size() > 4){
                string += ", ";
            }
        }
        if(ret.size() > 1 && ret.size() <= 4){
            string += " và ";
        }
        string += ret.get(ret.size() - 1);
        if(ret.size() > 4){
            string += ",... ";
        }
        return string + " tăng trần.";
    }

    public static void main(String[] args) {
        Input.updateDataFromLocal("D:\\ACE\\CafeF.SolieuGD.Upto28042020.zip");
        GroupMaximumAugmentation s = new GroupMaximumAugmentation();
        System.out.println(s.generate());
    }
}
