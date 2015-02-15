package org.xiaotian.persistence.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.xiaotian.base.server.BaseServer;
import org.xiaotian.config.XMLConfigServer;
import org.xiaotian.extend.CMyException;
import org.xiaotian.extend.CMyString;

/**
 * 数据库管理器
 * 
 * @author xiaotian15
 */
public class DBManager extends BaseServer implements IDBManager {

	/** logger */
	private final static Logger m_logger = Logger.getLogger(DBManager.class);

	/** 单例 */
	private static DBManager m_oDBManager = new DBManager();;

	/** 数据库连结缓冲池 */
	private Map<String, DataSource> m_hDataSources;

	public static DBManager getInstance() {
		if (!m_oDBManager.isStarted()) {
			m_oDBManager.startup();
			if (!m_oDBManager.isStarted()) {
				System.err.println("系统启动异常,数据库启动失败!");
				m_logger.error("系统启动异常,数据库启动失败!");
				return null;
			}
		}
		return m_oDBManager;
	}

	/**
	 * 获取有效的数据库连接
	 * 
	 * @param _sTag
	 *            数据库tag（用于适配不同数据库）
	 * @return 数据库连接（Connection）
	 * @throws DBManagerException
	 *             如果数据库管理器没有正确启动，将会抛出异常 <BR>
	 *             如果缓冲池中的连接对象无效，将会抛出异常
	 * @throws SQLException
	 */
	public Connection getConnection(String _sTag) throws DBManagerException {
		if(CMyString.isEmpty(_sTag)){
			throw new DBManagerException("数据库Tag标识为null");
		}
		try {
			DataSource dataSource = getDataSource(_sTag);
			Connection conn=dataSource.getConnection();
			boolean showSql=true;
			if(showSql && !conn.getClass().equals(DebugConnection.class)){
				conn=new DebugConnection(conn).getConnection();
			}
			return conn;
		} catch (Exception e) {
			throw new DBManagerException("获取数据库连接异常", e);
		}
	}

	/**
	 * 获取对应的数据源
	 * 
	 * @throws DBManagerException 
	 */
	private DataSource getDataSource(String _sTag) throws DBManagerException {
		if (m_hDataSources == null)
			initDataSource();
		DataSource datasource=m_hDataSources.get(_sTag);
		if (datasource == null) {
			throw new DBManagerException(_sTag + "无对应数据源配置");
		}
		return datasource;
	}

	// =============================== server ==================================

	@Override
	protected void doShutdown() {
		closeDataSource();
	}

	@Override
	protected boolean doStart() throws CMyException {
		initDataSource();
		return true;
	}

	/**
	 * 根据配置文件进行数据源初始化
	 */
	@SuppressWarnings("unchecked")
	private void initDataSource() {
		m_hDataSources = new HashMap<String, DataSource>();
		XMLConfigServer server = XMLConfigServer.getInstance();
		List<DBConnectionConfig> list = server.getConfigObjects(DBConnectionConfig.class);
		for (DBConnectionConfig config : list) {
			DataSource datasource = config.convertToDataSource();
			m_hDataSources.put(config.getId(), datasource);
		}
	}

	/**
	 * 关闭数据源
	 */
	private void closeDataSource() {
		Collection<DataSource> datasources = m_hDataSources.values();
		for (DataSource datasource : datasources) {
			datasource.close();
		}
		m_hDataSources = null;
	}

	// ================================ 数据库操作 =================================

	/**
	 * 关闭数据库资源
	 * 
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	public static void close(Connection conn) {
		try {			
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			m_logger.error("Cannot close connection.", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭数据库资源
	 * 
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if (rs != null)
				rs.close();

			if (stmt != null)
				stmt.close();

			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			m_logger.error("Cannot close connection.", e);
			e.printStackTrace();
		}
	}
	
	/**
     * 用于跟踪执行的SQL语句
     * @author Winter Lau
     */
    static class DebugConnection implements InvocationHandler {
 
        private Connection conn = null;
 
        public DebugConnection(Connection conn) {
            this.conn = conn;
        }
 
        /**
         * Returns the conn.
         * @return Connection
         */
        public Connection getConnection() {
            return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(),
                             conn.getClass().getInterfaces(), this);
        }
 
        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            try {
                String method = m.getName();
                if("prepareStatement".equals(method) || "createStatement".equals(method))
                    m_logger.info("[SQL] >>> " + args[0]+".");
                return m.invoke(conn, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
 
    }
 
}