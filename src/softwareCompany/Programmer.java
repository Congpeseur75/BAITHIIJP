package softwareCompany;

import java.util.Date;

public class Programmer {
    private String Name;
    private int Age;
    private int Phone;
    private String Email;
    private int overtimeHours;
    private Date workingDay;
    private int basicSalary;

    public Programmer() {
    }

    public Programmer(String name, int age, int phone, String email, int overtimeHours,Date workingDay,int basicSalary) {
        Name = name;
        Age = age;
        Phone = phone;
        Email = email;
        this.overtimeHours = overtimeHours;
        this.workingDay = workingDay;
        this.basicSalary = basicSalary;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getPhone() {
        return Phone;
    }

    public void setPhone(int phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(int overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public Date getWorkingDay() {
        return workingDay;
    }

    public void setWorkingDay(Date workingDay) {
        this.workingDay = workingDay;
    }

    public int getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(int basicSalary) {
        this.basicSalary = basicSalary;
    }
}
