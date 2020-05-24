
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyDao extends BaseDao{
	
	public List<USModel> search(String sql, Object...params){
		List<USModel> list =new ArrayList<USModel>();
		Connection conn=this.getconn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			pst=this.prepareStatement(conn, sql, params);
			rs=pst.executeQuery();
			while(rs.next()){
				USModel wor=new USModel();
				wor.setId(rs.getInt(1));
				wor.setUrl(rs.getString(2));
				wor.setService_id(rs.getString(3));
				list.add(wor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeAll(conn, pst, rs);
		}
		return list;
	}
	
	//查询表
	public List<USModel> queryAll(){
		String sql="SELECT * FROM `url_sid_list`";
		return search(sql);
	}
	
	//添加方法
	public int insert(USModel t){
		String str="INSERT INTO `url_sid_list`(id, url, service_id) VALUE(?,?,?)";
		return executeUpdate(str, new Object[]{t.getId(), t.getUrl(), t.getService_id()});
	}
	
	//修改方法
	public int update(USModel r){
		String sql="UPDATE `url_sid_list` SET `url` = ?,`service_id` = ?, WHERE service_id = ?";
		return executeUpdate(sql, new Object[]{r.getId(), r.getUrl(), r.getService_id()});
	}
	
	//删除方法
	public int delete(USModel e){
		String sql="DELETE FROM `url_sid_list` WHERE service_id = ?";
		return executeUpdate(sql, new Object[]{e.getService_id()});
	}
}
