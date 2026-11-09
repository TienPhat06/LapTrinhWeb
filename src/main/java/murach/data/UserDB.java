package murach.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import murach.business.User;

public class UserDB {
    // Kết nối đến database email_db vừa tạo trên PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/email_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "omgclqjztr"; // ⚠️ ĐỔI THÀNH MẬT KHẨU POSTGRESQL CỦA BẠN

    public static int insert(User user) {
        String sql = "INSERT INTO users (email, first_name, last_name) VALUES (?, ?, ?)";

        // Tải PostgreSQL Driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy PostgreSQL Driver!");
            e.printStackTrace();
            return 0;
        }

        // Thực thi mở kết nối và chèn dữ liệu
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());

            return ps.executeUpdate(); // Trả về số dòng được thêm (1 nếu thành công)
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm User vào CSDL: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}