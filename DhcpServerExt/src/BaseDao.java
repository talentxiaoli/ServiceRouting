import java.sql.*;

public class BaseDao {

    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/mysql?serverTimezone=GMT&useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8";
    private String name = "lihai";
    private String pwd= "lihai";
    Connection conn=null;

    protected  Connection getconn(){
        conn=null;
        try {
            Class.forName(driver);
            conn= DriverManager.getConnection(url, name, pwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    protected void closeAll(Connection conn , PreparedStatement ps, ResultSet rs){
        if(rs!=null) {
            try {
                if(rs!=null)
                    rs.close();
                if(ps!=null)
                    ps.close();
                if(conn!=null)
                    conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public int executeUpdate(String sql ,Object []ob){
        conn=getconn();
        PreparedStatement ps=null;
        try {
            ps=prepareStatement(conn,sql,ob);
            int i=ps.executeUpdate();
            return i;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            //	e.printStackTrace();
            return 0;
        }finally{
            closeAll(conn, ps, null);
        }

    }

    protected PreparedStatement prepareStatement(Connection conn,String sql,Object []ob){
        PreparedStatement ps=null;
        try {
            int index=1;
            ps = conn.prepareStatement(sql);
            if(ps!=null&&ob!=null){
                for (int i = 0; i < ob.length; i++) {
                    ps.setObject(index, ob[i]);
                    index++;
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ps;
    }

}