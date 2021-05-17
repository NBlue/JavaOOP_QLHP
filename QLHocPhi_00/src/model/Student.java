/**@Admin: Tô Đức Hiệp - 20194278
 * */

package model;


public abstract class Student {
    protected String id, name, birthday,  email, studyProgram, namHoc, fees;

    public Student() {
    }

    public abstract float fees(String a);

    public Student(String id, String name, String birthday, String email, String studyProgram, String namHoc, String fees) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.studyProgram = studyProgram;
        this.namHoc = namHoc;
        this.fees = fees;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(String studyProgram) {
        this.studyProgram = studyProgram;
    }

    public String getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(String namHoc) {
        this.namHoc = namHoc;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

}
