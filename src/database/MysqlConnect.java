package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;


public class MysqlConnect {
    /**
     * Connect to a sample database
     */

    public MysqlConnect() {

    }
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/sqlite/Paradigms.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;

    }

    public  void insertUser(String name, String email,String password)
    {
        String sql = "INSERT INTO Users(name,email,password) VALUES(?,?,?)";

        try (Connection conn = this.connect()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                System.out.println("passa");
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, password);
                pstmt.executeUpdate();
                System.out.println("User Inserted.");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}