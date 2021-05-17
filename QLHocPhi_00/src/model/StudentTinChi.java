/**@Admin: Tô Đức Hiệp - 20194278
 * */

package model;

public class StudentTinChi extends Student {
    private int tinChi;

    public StudentTinChi() {
    }

    @Override
    public float fees(String a) {
        return Float.valueOf(a) * 250;
    }

    public StudentTinChi(String id, String name, String birthday, String email, String studyProgram, String namHoc, String fees, int tinChi) {
        super(id, name, birthday, email, studyProgram, namHoc, fees);
        this.tinChi = tinChi;
    }

    public int getTinChi() {
        return tinChi;
    }

    public void setTinChi(int tinChi) {
        this.tinChi = tinChi;
    }
}
