package kr.co.itcen.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.itcen.mysite.vo.GuestbookVo;

@Repository
public class GuestbookDao {
	
	@Autowired
	private DataSource dataSource; // applicationContext.xml에서 설정 후 사용
	
	public Boolean insert(GuestbookVo guestVo) {
		Boolean result = false;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		
		Statement stmt = null;
		ResultSet rs = null;

		try {
			connection = dataSource.getConnection();
			
			String sql = "insert into guestbook values(null, ?, ?, ?, now())";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, guestVo.getName());
			pstmt.setString(2, guestVo.getPassword());
			pstmt.setString(3, guestVo.getContents());
			
			int count = pstmt.executeUpdate();
			
			result = (count == 1); 
			
			stmt = connection.createStatement();
			// mysql에만 있는 함수
			rs = stmt.executeQuery("select last_insert_id()");
			if(rs.next()) {
				Long no = rs.getLong(1);
				guestVo.setNo(no);
			}
			
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public void delete(GuestbookVo guestVo) {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = dataSource.getConnection();
			
			String sql = "delete from guestbook where no=? and password=?";
			
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, guestVo.getNo());
			pstmt.setString(2, guestVo.getPassword());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<GuestbookVo> getList() {
		List<GuestbookVo> result = new ArrayList<GuestbookVo>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = dataSource.getConnection();
			
			String sql = "select no, name, contents, date_format(reg_date,'%Y-%m-%d %h:%i:%s') from guestbook order by reg_date desc";
			pstmt = connection.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				String contents = rs.getString(3);
				String regDate = rs.getString(4);
				
				GuestbookVo guestVo = new GuestbookVo();
				guestVo.setNo(no);
				guestVo.setName(name);
				guestVo.setContents(contents);
				guestVo.setRegDate(regDate);
				
				result.add(guestVo);
			}
						
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
}
