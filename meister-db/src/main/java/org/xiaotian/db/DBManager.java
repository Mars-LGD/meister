package org.xiaotian.db;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;
import org.xiaotian.config.XMLConfigServer;
import org.xiaotian.db.config.DBConnectionConfig;


/**
 * 数据库管理器
 * 
 * @author xiaotian15
 */
public class DBManager implements IDBManager {
	/** logger */
	private final static Logger m_logger = Logger.getLogger(DBManager.class);

	public Connection getConnection(String _sTag) throws DBManagerException {
		return null;
	}
	
	

}