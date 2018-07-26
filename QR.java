package cn.xc.dbutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import cn.xc.jdbc.JdbcUtils;

public class QR<T>{
	
	private DataSource dataSource = null;
	
	public QR() {
		super();
	}

	public QR(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 做insert、update、delete
	 * @param sql
	 * @param params
	 * @return
	 */
	public int update(String sql, Object... params){
		Connection con = null;
		PreparedStatement pstmt = null;
		try{
			con = dataSource.getConnection(); //通过连接池得到Connection对象
			pstmt = con.prepareStatement(sql);
			initParams(pstmt, params);
			
			return pstmt.executeUpdate();
					
		}catch(Exception e){
			throw new RuntimeException(e);
			
		}finally{
			try{
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			}catch(SQLException e1){
				throw new RuntimeException(e1);
			}
		}
	}
	

	public T query(String sql, RsHandle<T> rh, Object... params){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection(); //通过连接池得到Connection对象
			pstmt = con.prepareStatement(sql);
			initParams(pstmt, params);
			
			rs = pstmt.executeQuery();
			return rh.handle(rs);
					
		}catch(Exception e){
			throw new RuntimeException(e);
			
		}finally{
			try{
				if(rs != null ) rs.close(); 
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			}catch(SQLException e1){
				throw new RuntimeException(e1);
			}
		}
	}
	
	private void initParams(PreparedStatement pstmt, Object... params) throws SQLException{
		for(int i = 0; i < params.length; i++){
			pstmt.setObject(i+1, params[i]);
		}
	}
}

interface RsHandle<T>{
	public T handle(ResultSet rs) throws SQLException;
	
}
