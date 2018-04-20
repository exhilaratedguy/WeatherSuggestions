package database;

public class TestConnection {
    public static void main(String[] args) {
        MysqlConnect database = new MysqlConnect();
        //database.insertUser("Andre","ola@g.com","12345");
        // System.out.println(database.getName("ola@g.com"));
        //database.insertInfo("ola@g.com", "Andre", "12345");
        database.getAll();
    }
}
