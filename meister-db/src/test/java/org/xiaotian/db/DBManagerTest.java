package org.xiaotian.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.xiaotian.config.XMLConfigServer;
import org.xiaotian.db.config.DBConnectionConfig;

import junit.framework.TestCase;

public class DBManagerTest extends TestCase {
	
	public static void main(String[] args){
		XMLConfigServer server = XMLConfigServer.getInstance();
		List<DBConnectionConfig> list = server.getConfigObjects(DBConnectionConfig.class);
		DataSource datasource=list.get(0).convertToDataSource();
		try {
			Connection conn=datasource.getConnection();
			PreparedStatement ps=null;
			ResultSet rs=null;
			
			ps=conn.prepareStatement("select count(*) from tbl_source_level");
			rs=ps.executeQuery();
			if(rs.next()){
				System.out.println(rs.getInt(1));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
