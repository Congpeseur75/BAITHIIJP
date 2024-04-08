package pharmacity;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Main {
    static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static final String DB_URL = "jdbc:sqlserver://DESKTOP-31VGQOU:1433;databaseName=PHARMA;trustServerCertificate=true";

    static final String USER = "sa";
    static final String PASS = "123";

    static Connection conn = null;
    static Statement stmt = null;

    static ArrayList<Product> products = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        connectDatabase();
        boolean exit = false;
        while (!exit){
            try {
                System.out.println("\n============================== MENU ================================");
                System.out.println("==          1. Thêm sản phẩm                                      ==");
                System.out.println("==          2. Hiển thị danh sách sản phẩm                        ==");
                System.out.println("==          3. hiển thị danh sách có lưu trong database           ==");
                System.out.println("==          4. Cập nhật số lượng đã bán của sản phẩm              ==");
                System.out.println("==          5. Sắp xếp danh sách sản phẩm theo số lượng đã bán    ==");
                System.out.println("==          6. lấy sản phẩm bán chạy nhất                         ==");
                System.out.println("==          7. tìm kiêm sản phẩm theo tên                         ==");
                System.out.println("==          8. Xóa sản phẩm quá hạn sử dụng                       ==");
                System.out.println("==          0. Thoát                                              ==");
                System.out.println("====================================================================");
                System.out.print("Chọn chức năng: ");
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        addProduct();
                        break;
                    case 2:
                        displayProduct();
                        break;
                    case 3:
                        displayProductDatabase();
                        break;
                    case 4:
                        updateProduct();
                        break;
                    case 5:
                        sortedByQuantitySold();
                        break;
                    case 6:
                        findBestSellingProduct();
                        break;
                    case 7:
                        searchProductByName();
                        break;
                    case 8:
                        deleteExpiredProducts();
                        break;
                    case 0:
                        exit = true;
                        break;
                    default:
                        System.out.println("lựa chọn không hợp lệ vui lòng chọn lại");
                }
            }
            catch (NumberFormatException e){
                System.out.println("Vui lòng nhập số nguyên");
            }
            catch (Exception e){
                System.out.println("Đã xảy ra lỗi: " + e.getMessage());
            }
        }
        disconnectDatabase();
    }

    static void connectDatabase(){
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Đang kết nối đến cơ sở dữ liệu...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Kết nối thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void disconnectDatabase() {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
            System.out.println("Đã đóng kết nối đến cơ sở dữ liệu.");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

     static void addProduct(){
        System.out.println("\nnhập ID sản phẩm: ");
        String ID = scanner.nextLine();
        System.out.println("nhập tên sản phẩm: ");
        String Name = scanner.nextLine();
        System.out.println("nhập hạn sử dụng(yyyy-MM-dd): ");
        String ExpirySrt = scanner.nextLine();
        Date Expiry = Date.valueOf(ExpirySrt);
        System.out.println("nhập số lượng nhập vào: ");
        int QuantityEntered = Integer.parseInt(scanner.nextLine());

        Product product = new Product(ID,Name,Expiry,QuantityEntered,0);
        products.add(product);
        insertProductDatabase(product);
        System.out.println("Nhân viên được thêm vào thành công.");
    }

     static void insertProductDatabase(Product product){
        try{
            String sql ="INSERT INTO Product (ID, Name, Expiry, QuantityEntered, AmountSold) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, product.getID());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setDate(3, new java.sql.Date(product.getExpiry().getTime()));
            preparedStatement.setInt(4,  product.getQuantityEntered());
            preparedStatement.setInt(5, product.getAmountSold());
            preparedStatement.executeUpdate();
            System.out.println("Nhân viên được thêm vào thành công.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void displayProduct(){
        System.out.println("\n=========Danh sách sản phẩm========");
        for (Product product : products){
            System.out.println("ID: " + product.getID() +
                    ", Tên: " + product.getName() +
                    ", Hạn sử dụng: " + product.getExpiry());
        }
    }

    static void displayProductDatabase(){
        System.out.println("\n=============== Danh Sách Sản Phẩm Lấy Từ Database ===============");
        try {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Product";
            ResultSet rs = stmt.executeQuery(sql);
            boolean found = false;
            while (rs.next()){
                String ID = rs.getNString("ID");
                String Name = rs.getNString("Name");
                Date Expiry = rs.getDate("Expiry");
                int QuantityEntered = rs.getInt("QuantityEntered");
                int AmountSold = rs.getInt("AmountSold");
                System.out.println("ID: " + ID + ", Name: " + Name + ", Expiry: " + Expiry + ", QuantityEntered: " + QuantityEntered + ", AmountSold: " + AmountSold);
                found = true;
            }
            if (!found){
                System.out.println("không có sản phẩm nào trong cơ sở dữ liệu");
            }
            rs.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void updateProduct(){
        displayProduct();
        System.out.println("\n=========Cập nhật danh sách đã bán=========");
        System.out.println("nhập ID sản phẩm đã bán cần cập nhật");
        String ID = scanner.nextLine();
        System.out.println("nhập số lượng đã bán");
        int AmountSold = Integer.parseInt(scanner.nextLine());

        Product product = null;

        for (Product product1 : products){
            if (product1.getID().equals(ID)) {
                product = product1;
                break;
            }
        }

        if (product != null){
            if (product.getQuantityEntered() - product.getAmountSold() >= AmountSold){
                product.setAmountSold(product.getAmountSold()+ AmountSold);
                int theRemainingAmount = product.getQuantityEntered() - product.getAmountSold();
                updateProductDatabase(product);
                System.out.println("số lượng đã được cập nhật thành công");
                System.out.println("số lượng còn lại là: " + theRemainingAmount);
            }
            else {
                System.out.println("sản phẩm này không đủ số lượng theo yêu cầu");
            }
        }
        else {
            System.out.println("không tìm thấy sản phẩm bởi id đã nhập");
        }
    }
    static void updateProductDatabase(Product product){
        try {
            String sql = "UPDATE Product SET AmountSold=? WHERE ID=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, product.getAmountSold());
            preparedStatement.setString(2, product.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void deleteExpiredProducts(){
        System.out.println("\n==========Xóa sản phẩm quá hạn sử dụng============");
        for (Product product : products){
            if (product.getExpiry().before(new java.util.Date())){
                System.out.println("đã xóa sản phẩm: " + product.getName());
                deleteExpiredProductsDatabase(product);
            }
        }
        products.removeIf(product -> product.getExpiry().before(new java.util.Date()));
    }

    static void deleteExpiredProductsDatabase(Product product){
        try {
            String sql = "DELETE FROM Product WHERE ID=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, product.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void sortedByQuantitySold(){
        products.sort((p1,p2) -> p2.getAmountSold() - p1.getAmountSold());
        System.out.println("\n===============Danh sách đã sắp sếp theo số lượng đã bán===============");
        displayProduct();
    }
    static void  findBestSellingProduct(){
        if (products.isEmpty()){
            System.out.println("Danh sách sản phẩm trống.");
            return;
        }
        Product bestSellingProduct = products.get(0);
        for (Product product : products){
            if (product.getAmountSold() > bestSellingProduct.getAmountSold()){
                bestSellingProduct = product;
            }
        }
        System.out.println("\n==========sản phẩm bán chạy nhất=============");
        System.out.println("ID: " + bestSellingProduct.getID());
        System.out.println("Name: " + bestSellingProduct.getName());
        System.out.println("AmountSold: " + bestSellingProduct.getAmountSold());
    }

    static void searchProductByName(){
        System.out.println("\n=========Tìm kiếm sản phẩm theo tên===========");
        System.out.println("Nhập tên sản phẩm cầm tìm kiếm: ");
        String searchName = scanner.nextLine();
        boolean found = false;
        for (Product product : products){
            if (product.getName().equalsIgnoreCase(searchName)){
                System.out.println("\n===========đã tìm thấy sản phẩm==========");
                System.out.println("ID: " + product.getID());
                System.out.println("Name: " + product.getName());
                System.out.println("Expiry: " + product.getExpiry());
                System.out.println("QuantityEntered: " + product.getQuantityEntered());
                System.out.println("AmountSold: " + product.getAmountSold());
                found = true;
                break;
            }
        }
        if (!found){
            System.out.println("không tìm thấy sản phẩm có tên: " + searchName);
        }
    }
}
