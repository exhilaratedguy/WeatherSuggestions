package database;
import org.postgresql.util.PSQLException;

import java.sql.*;


public class MysqlConnect {

    private static final String URL = "jdbc:postgres://ahvaqbaw:WDPCED8wadZj7fKWwRxSAvBOy9JmveN2@dumbo.db.elephantsql.com:5432/ahvaqbaw";
    private static final String USER = "ahvaqbaw";
    private static final String PW = "WDPCED8wadZj7fKWwRxSAvBOy9JmveN2";
    private static final String PATH = "C:/Users/Acer/Desktop/UPB/Programming Paradigms/test/Paradigms.db";
    private Connection c;

    /**
     * Connect to a sample database
     */

    public MysqlConnect() { }

    public void getAll() throws SQLException{
        try{
            c = DriverManager.getConnection(URL, USER, PW);
            //EXECUTE SQL COMMANDS AND LOAD RESULTS TO RESULTSET
            Statement stmt = c.createStatement();
            String sql = "SELECT * FROM Users";
            ResultSet rs = stmt.executeQuery(sql);

            //CURSOR OPERATIONS TO GET DATA
            while(rs.next()){
                String email = rs.getString("Email");
                String name = rs.getString("Name");
                String pw = rs.getString("Password");

                System.out.println("Email: "+email+"\tName: "+name+"\t Pw: "+pw);
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally{
            c.close();
        }

    }

    public void insertInfo(String email, String name, String pw){
        try{
            c = DriverManager.getConnection(URL, USER, PW);

            String sql = "INSERT INTO Users VALUES";
            sql = sql + "(" + email + ", '"+ name + "', '" + pw +"');";

            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Successfully inserted user.");
            c.close();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + PATH;
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