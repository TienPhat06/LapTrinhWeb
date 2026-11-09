package murach.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import murach.business.User;

public class UserDB {
    // Đọc thông số từ Environment Variables trên Render, nếu không có sẽ tự dùng cấu hình local
    private static final String URL = System.getenv("DB_URL") != null
            ? System.getenv("DB_URL")
            : "jdbc:postgresql://localhost:5432/email_db";

    private static final String USER = System.getenv("DB_USER") != null
            ? System.getenv("DB_USER")
            : "postgres";

    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null
            ? System.getenv("DB_PASSWORD")
            : "omgclqjztr";

    public static int insert(User user) {
        String sql = "INSERT INTO users (email, first_name, last_name) VALUES (?, ?, ?)";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy PostgreSQL Driver!");
            e.printStackTrace();
            return 0;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm User vào CSDL: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}