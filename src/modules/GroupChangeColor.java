package modules;

import data.Input;
import data.Session;
import utilities.Dictionary;
import utilities.Filter;

import java.util.*;

public class GroupChangeColor extends SentenceGenerator{

    @Override
    public String example() {
        return "Nhiều cổ phiếu dầu khí như PET, PXS đã đổi màu.";
    }

    @Override
    public String generate() {
        Random r = new Random();
        String randomGroup = Dictionary.INDUSTRY_GROUPS[r.nextInt(Dictionary.INDUSTRY_GROUPS.length)];
        return generate(randomGroup);
    }

    public String generate(String group){
        ArrayList<String> ret = new ArrayList<>();
        Filter filter = new Filter();
        Session[] sessions = filter.filter(Input.inputData[3].getSessions(), group);

        Date today = sessions[0].getDate();
        Date previousDay = null, dayBeforeYesterday = null; // Ngày giao dịch trước đó
        Map <String, Float> map1 = new HashMap<>();
        Map <String, Float> map2 = new HashMap<>();

        // Tìm previousDay và dayBeforeYesterday
        for (int i = 0; i < sessions.length - 1; i++){
            Session s = sessions[i];
            if (!s.getDate().equals(today)){
                previousDay = s.getDate();
                break;
            }
        }

        for(Session s : sessions){
            if(!s.getDate().equals(today) && !s.getDate().equals(previousDay)){
                dayBeforeYesterday = s.getDate();
                break;
            }
        }

        for(Session s : sessions){
            if(s.getDate().equals(today)){
                continue;
            }
            if(s.getDate().equals(previousDay)){
                map1.put(s.getTicker(), s.getClose());
            }else{
                if(s.getDate().equals(dayBeforeYesterday)){
                    map2.put(s.getTicker(), s.getClose());
                }else{
                    break;
                }
            }
        }

        for(Session s : sessions){
            if(s.getDate().equals(today)){
                String ticker = s.getTicker();
                if(map1.containsKey(ticker) && map2.containsKey(ticker)){
                    if((map1.get(ticker)-s.getClose())*(map1.get(ticker)-map2.get(ticker)) > 0){
                        ret.add(ticker);
                    }
                }
            }else{
                break;
            }
        }

        if(ret.isEmpty()){
            return "Nhóm cổ phiếu " + group.toLowerCase() + " không có cổ phiếu nào đổi màu.";
        }

        if(ret.size() == 1){
            return "Nhóm cổ phiếu " + group.toLowerCase() + " có " + ret.get(0) + " đã đổi màu.";
        }

        String string = "Nhiều cổ phiếu " + group.toLowerCase() + " như ";
        for(int i = 0; i < ret.size()-1; i++){
            if(i > 2){
                break;
            }
            string += ret.get(i) + ", ";
        }
        string += ret.get(ret.size()-1);
        if(ret.size() > 4){
            string += ",...";
        }
        return string + " đã đổi màu.";
    }

    public static void main(String[] args) {
        Input.updateDataFromLocal("D:\\ACE\\CafeF.SolieuGD.Upto28042020.zip");
        GroupChangeColor s = new GroupChangeColor();
        System.out.println(s.generate());
    }
}
