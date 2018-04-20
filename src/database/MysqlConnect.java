package database;


import java.sql.*;


public class MysqlConnect {

    private static final String URL = "jdbc:postgresql://dumbo.db.elephantsql.com/";
    private static final String USER = "eykorjca";
    private static final String PW = "fT4L3J7pixWx1DooA3TA4Dl3bcGbYg7R";
    private static final String PATH = "C:/Users/Acer/Desktop/UPB/Programming Paradigms/test/Paradigms.db";

    /**
     * Connect to a sample database
     */

    public MysqlConnect() { }

    public void getAll() {
        try{
            Connection conn = connect();

            if ( conn.isValid(0) )
                System.out.println("SUCESSO");
            else
                System.out.println("FALHOU");

            //EXECUTE SQL COMMANDS AND LOAD RESULTS TO RESULTSET
            Statement stmt = conn.createStatement();
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
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void insertUser(String email, String name, String pw){
        try{
            Connection conn = connect();

            String sql = "INSERT INTO Users VALUES";
            sql = sql + "('" + email + "', '"+ name + "', '" + pw +"');";

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Successfully inserted user.");

            conn.close();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:"+PATH);
            //conn = DriverManager.getConnection(URL, USER, PW);
            System.out.println("Successfully connected to the database.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;

    }

    /*
    public void insertUser(String name, String email,String password) {
        String sql = "INSERT INTO Users(name,email,password) VALUES(?,?,?)";

        try (Connection conn = connect()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                //System.out.println("passa");
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, password);
                pstmt.executeUpdate();
                //System.out.println("User Inserted.");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    */

    // to test 2 cases: if User already exists when registering a new one
    // and to check if the Login info is correct when trying to log in
    public boolean isUser(String squery){
        boolean res = false;

        try(Connection conn = connect()) {
            try (PreparedStatement pStmt = conn.prepareStatement(squery)){
                ResultSet rSet = pStmt.executeQuery();
                if(rSet.next())     // if the query returns some result then res=true
                    res = true;     // if not, res is already false
            }
            conn.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return res;
    }

    public String getName(String email){
        String squery = "SELECT * FROM Users WHERE email IS '" + email + "';";

        try(Connection conn = connect()) {
            try (PreparedStatement pStmt = conn.prepareStatement(squery)){
                ResultSet rSet = pStmt.executeQuery();
                if(rSet.next())
                    return rSet.getString("Name");
            }
            conn.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }


}