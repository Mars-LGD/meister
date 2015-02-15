package org.xiaotian.persistence.db;

import java.sql.Connection;

public interface IDBManager {
	
	/**
     * 获取有效的数据库连接
     * 
     * @param _sTag 数据库tag（用于适配不同数据库）
     * @return 数据库连接（Connection）
     * @throws DBManagerException
     *             如果数据库管理器没有正确启动，将会抛出异常 <BR>
     *             如果缓冲池中的连接对象无效，将会抛出异常
     */
    public Connection getConnection(String _sTag) throws DBManagerException;

}
