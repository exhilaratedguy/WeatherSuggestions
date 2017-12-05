package database;
import java.sql.*;


public class MysqlConnect {
    /**
     * Connect to a sample database
     */

    public MysqlConnect() {

    }
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/Users/Acer/Desktop/UPB/Programming Paradigms/test/Paradigms.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;

    }

    public void insertUser(String name, String email,String password)
    {
        String sql = "INSERT INTO Users(name,email,password) VALUES(?,?,?)";

        try (Connection conn = this.connect()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                //System.out.println("passa");
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

    // to test 2 cases: if User already exists when registering a new one
    // and to check if the Login info is correct when trying to log in
    public boolean isUser(String squery){
        boolean res = false;

        try(Connection conn = this.connect()) {
            try (PreparedStatement pStmt = conn.prepareStatement(squery)){
                ResultSet rSet = pStmt.executeQuery();
                if(rSet.next())     // if the query returns some result then res=true
                    res = true;     // if not, res is already false
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return res;
    }

    public String getName(String email){
        String squery = "SELECT * FROM Users WHERE email IS '" + email + "';";

        try(Connection conn = this.connect()) {
            try (PreparedStatement pStmt = conn.prepareStatement(squery)){
                ResultSet rSet = pStmt.executeQuery();
                if(rSet.next())
                    return rSet.getString("Name");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }


}