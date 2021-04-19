import java.sql.*;

public class Main {
    public static void main(String[] args){
        String url = "jdbc:mysql://127.0.0.1:3306/?user=root";
        String user = "root";
        String password = "jotajota24";
        try {
            Connection myConn = DriverManager.getConnection(url, user, password);
            Statement myStmt = myConn.createStatement();
            String sql = "SELECT * FROM testing_enphase_tweets.enphase_energy_tweets_small ";
            ResultSet rs = myStmt.executeQuery(sql);
            while (rs.next()){
                System.out.println(rs.getString("TweetText"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
