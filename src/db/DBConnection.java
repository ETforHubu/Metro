package db;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Mr.Zhang on 2020/11/2.
 */
public class DBConnection {
    private static  String DRIVERNAME;
    private static  String URL;
    private static  String USERNAME;
    private static  String USERPWD;//以上为从db.propertie取得的数据库配置
    static {
        try {
            InputStream fis = DBConnection.class.getClassLoader()
                    .getResourceAsStream("db.properties");
            Properties pro = new Properties();
            pro.load(fis);
            URL = pro.getProperty("URL");
            USERNAME = pro.getProperty("USERNAME");
            USERPWD = pro.getProperty("USERPWD");
            DRIVERNAME=pro.getProperty("DRIVERNAME");
            fis.close();
            fis = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class.forName(DRIVERNAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @author TianZW
     * @throws SQLException
     * */
    public static Connection getConnection() throws SQLException  {
        Connection conn = DriverManager.getConnection(URL, USERNAME, USERPWD);
        return conn;
    }
    /**
     * @author TianZW
     * 关闭链接
     * */
    public static void closeConnection(Connection conn,PreparedStatement state,ResultSet rs){
        if(rs != null)
            try {
                rs.close();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        if(state != null) {
            try {
                state.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String [] args) throws SQLException{//测试数据库是否链接成功

        Connection conn=DBConnection.getConnection();
        PreparedStatement state =
                conn.prepareStatement("select * from att_admin");
        ResultSet rs = state.executeQuery();
        if( rs.next() )
        {
            System.out.println("success!");
        }
        DBConnection.closeConnection(conn,state,rs);
    }
}
