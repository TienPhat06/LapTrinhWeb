package murach.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import murach.business.User;

public class UserDB {
    // Đọc từ biến môi trường DB_URL trên Render.
    // Nếu không có, tự động fallback về URL External Direct Connection của Render Postgres
    private static final String URL = System.getenv("DB_URL") != null
            ? System.getenv("DB_URL")
            : "jdbc:postgresql://dpg-cub2rbagij4c73e031s0-a.singapore-postgres.render.com:5432/email_db_jb8p?sslmode=require";

    private static final String USER = System.getenv("DB_USER") != null
            ? System.getenv("DB_USER")
            : "email_user";

    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null
            ? System.getenv("DB_PASSWORD")
            : "omgclqjztr";

    public static int insert(User user) {
        String sql = "INSERT INTO users (email, first_name, last_name) VALUES (?, ?, ?)";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy PostgreSQL Driver: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());

            int rowsAffected = ps.executeUpdate();

            // Đảm bảo commit dữ liệu nếu auto-commit bị tắt
            if (!conn.getAutoCommit()) {
                conn.commit();
            }

            return rowsAffected;
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối hoặc ghi dữ liệu PostgreSQL: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}