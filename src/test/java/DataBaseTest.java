import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseTest {
    public static void main(String[] args) {
        String driverName="com.mysql.cj.jdbc.Driver";//这是要连接的数据库加载器
        String dbURL="jdbc:mysql://127.0.0.1:3306/nyc_tod?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=UTC";//这是要连接的端口号以及数据库名称
        String userName="asja";//用户名
        String userpwd="082264";//用户密码
        try {
            Class.forName(driverName);
            System.out.println("加载驱动成功");
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("加载驱动失败");
        }
        try {
            Connection dbConn= DriverManager.getConnection(dbURL,userName,userpwd);
            System.out.println("连接数据库成功");
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("数据库连接失败");
        }
    }
}
