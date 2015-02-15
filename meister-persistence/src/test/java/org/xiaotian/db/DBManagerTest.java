package org.xiaotian.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import junit.framework.TestCase;

import org.xiaotian.persistence.db.DBManager;

public class DBManagerTest extends TestCase {
	
	public static void main(String[] args){
		try {
			Connection conn=DBManager.getInstance().getConnection("haoma23");
			PreparedStatement ps=null;
			ResultSet rs=null;
			
			ps=conn.prepareStatement("select count(*) from tbl_message_12321");
			rs=ps.executeQuery();
			if(rs.next()){
				System.out.println(rs.getInt(1));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
