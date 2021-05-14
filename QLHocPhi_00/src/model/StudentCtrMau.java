package model;

public class StudentCtrMau extends Student {
    private int hocPhan;

    public StudentCtrMau() {
    }

    @Override
    public float fees(String a) {
        return (float) (Float.valueOf(a) * 2.5 * 250 + 1000);
    }

    public StudentCtrMau(String id, String name, String birthday, String email, String studyProgram, String namHoc, String fees, int hocPhan) {
        super(id, name, birthday, email, studyProgram, namHoc, fees);
        this.hocPhan = hocPhan;
    }

    public int getHocPhan() {
        return hocPhan;
    }

    public void setHocPhan(int hocPhan) {
        this.hocPhan = hocPhan;
    }


}
