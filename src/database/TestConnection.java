package database;

public class TestConnection {
    public static void main(String[] args) {
       MysqlConnect database = new MysqlConnect();
       database.insertUser("Andre","ola@g.com","12345");
    }
}
