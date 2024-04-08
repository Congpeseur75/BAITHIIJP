package softwareCompany;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.Statement;

public class TestProgrammer {
    static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static final String DB_URL = "jdbc:sqlserver://DESKTOP-31VGQOU:1433;databaseName=softwareCompany;trustServerCertificate=true";

    static final String USER = "sa";
    static final String PASS = "123";
    static Connection con = null;
    static Statement stmt = null;
    static ArrayList<Programmer> programmers = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        connectDatabase();
        boolean exit = false;
        while (!exit){
            try{
                System.out.println("\n========MENU=======");
                System.out.println("1. thêm lập trình viên");
                System.out.println("2. hiển thị lập trình viên");
                System.out.println("3. câp nhật  lập trình viên");
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice){
                    case 1:
                        addProgrammer();
                        break;
                    case 2:
                        displayProgrammer();
                        break;
                    case 3:
                        updateProgrammer();
                        break;

                }
            }
            catch (NumberFormatException e){
                System.out.println("Vui lòng nhập số nguyên");
            }
            catch (Exception e){
                System.out.println("Đã xảy ra lỗi: " + e.getMessage());
            }
        }
    }

    static void connectDatabase(){
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Đang kết nối đến cơ sở dữ liệu...");
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Kết nối thành công!");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    static void disconnectDatabase(){
        try {
            if (stmt != null) stmt.close();
            if (con != null) con.close();
            System.out.println("Đã đóng kết nối đến cơ sở dữ liệu.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    static void addProgrammer(){
        try {
            System.out.println("nhập tên lập trình viên: ");
            String Name = sc.nextLine();
            System.out.println("nhập tuổi lập trình viên: ");
            int Age = Integer.parseInt(sc.nextLine());
            System.out.println("nhập số điện thoại lập trình viên: ");
            int Phone = Integer.parseInt(sc.nextLine());
            System.out.println("nhập Email lập trình viên: ");
            String Email = sc.nextLine();
            System.out.println("nhập số giờ tăng ca của lập trình viên: ");
            int overtimeHours = Integer.parseInt(sc.nextLine());
            System.out.println("nhập ngày vào làm của lập trình viên(yyyy-MM-dd): ");
            String workingDayStr = sc.nextLine();
            Date workingDay = java.sql.Date.valueOf(workingDayStr);
            System.out.println("nhập lương cơ bản của lập trình viên: ");
            int basicSalary = Integer.parseInt(sc.nextLine());
            Programmer programmer = new Programmer(Name,Age,Phone,Email,overtimeHours,workingDay,basicSalary);
            programmers.add(programmer);
            insertProgrammerDatabase(programmer);
            System.out.println("đã thêm lập trình viên thành công");
        }
        catch (NumberFormatException e){
            System.out.println("đổi số có vấn đề: " + e);
        }
        catch (NullPointerException e){
            System.out.println("truy cập đối tượng có vấn đề: " + e);
        }
    }
    static void insertProgrammerDatabase(Programmer programmer){
        try {
            String sql = "INSERT INTO Programmer(Name, Age, Phone, Email, overtimeHours, workingDay, basicSalary) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, programmer.getName());
            preparedStatement.setInt(2, programmer.getAge());
            preparedStatement.setInt(3, programmer.getPhone());
            preparedStatement.setString(4, programmer.getEmail());
            preparedStatement.setInt(5, programmer.getOvertimeHours());
            preparedStatement.setDate(6, (java.sql.Date) new Date(programmer.getWorkingDay().getTime()));
            preparedStatement.setInt(7, programmer.getBasicSalary());
            preparedStatement.executeUpdate();
            System.out.println("lập trình viên được thêm thành công");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    static void displayProgrammer(){
        System.out.println("\n=========Danh sách lập trình viên========");
        for (Programmer programmer : programmers){
            System.out.println(programmer);
        }
    }
    static void CalculateAverageSalary(){
            for (Programmer programmer : programmers){
                int AverageSalary = programmer.getBasicSalary() + programmer.getOvertimeHours() * 200000;
                System.out.println(AverageSalary);
            }
    }

    static void ListLowWageEmployees(){
        for (Programmer programmer : programmers){
            if (programmer.getOvertimeHours() < 200){
                System.out.println(programmer);
            }
        }
    }
    static void updateProgrammer(){
        displayProgrammer();
        System.out.println("\n===========cập nhật lại lập trình viên==========");
        try {
            System.out.println("nhập tên lập trình viên: ");
            String Name = sc.nextLine();
            boolean found = false;
            for (Programmer programmer : programmers){
                if (Name.equals(programmer.getName())){
                    System.out.println("nhập lương mới cho lập trình viên: ");
                    int Salary = Integer.parseInt(sc.nextLine());
                    programmer.setBasicSalary(Salary);
                    int workingHours = Integer.parseInt(sc.nextLine());
                    programmer.setOvertimeHours(workingHours);
                    updateProgrammerDatabase(programmer);
                    System.out.println("đã sửa thành công");
                    found = true;
                    break;
                }
            }

            if (!found){
                System.out.println("không tìm thấy lập trình viên có tên như vậy");
            }
        }
        catch (NumberFormatException e){
            System.out.println("đổi số không được: ");
        }
    }
    static void updateProgrammerDatabase(Programmer programmer){
        try {
            String sql = "UPDATE Programmer SET basicSalary, overtimeHours";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, programmer.getBasicSalary());
            preparedStatement.setInt(2, programmer.getOvertimeHours());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
