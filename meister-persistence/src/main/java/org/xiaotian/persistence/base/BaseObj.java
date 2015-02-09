package org.xiaotian.persistence.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import org.xiaotian.constants.ExceptionNumber;



/**
 * CMS对象基类
 * 
 * @author xiaotian15
 * 
 */
public abstract class BaseObj implements Cloneable {
	/** 日志 */
	private static Logger m_logger = Logger.getLogger(BaseObj.class);

	/** 属性是否合法 */
	protected boolean m_bCheckPropertyValid = true;

	/** 对象ID标识，用以提高访问效率 */
	protected int id = 0;

	/** 是否为添加模式，否则为修改模式 */
	protected boolean bAddMode = true;

	/** 私有对象属性集合 */
	@SuppressWarnings("rawtypes")
	protected Hashtable m_hProperties = null;

	/** 对象属性修改的新值(用作修改时的Buffer) */
	@SuppressWarnings("rawtypes")
	private Hashtable m_hNewProperties = null;

	/**
	 * 构造函数
	 */
	public BaseObj() {
		super();
	}

	/**
	 * 取得该对象的类型（定义在WCMTypes中）
	 * <p>
	 * 二次开发中定义在wcmx.common.WCMTypesX中
	 * 
	 * @return 整型类型数
	 */
	public abstract int getWCMType();

	/**
	 * 获得数据库中存储该对象的数据表名称。 如：User对象对应的数据表是WCMUSER
	 * 
	 * @return
	 */
	public abstract String getDbTableName();

	/**
	 * 获得Id字段名。如：wcmUser表中的UserId
	 * 
	 * @return Id字段名
	 */
	public abstract String getIdFieldName();

	/**
	 * 获得对象的标示关键字，用在Hashtable中索引对象
	 * 
	 * @return
	 */
	public Object getKey() {
		return new Integer(this.id);
	}

	/**
	 * 取该对象的类别名称
	 * 
	 * @param _bIncludePackage
	 *            是否包含package名称
	 * @return
	 */
	public String getClassName(boolean _bIncludePackage) {
		String sClassName = this.getClass().getName();
		// 如果不包含package名称，则去掉package名称
		if (!_bIncludePackage) {
			int nPos = sClassName.lastIndexOf('.');
			if (nPos >= 0)
				sClassName = sClassName.substring(nPos + 1);
		}
		return sClassName;
	}

	/**
	 * 获取对象的ID
	 * 
	 * @return
	 */
	public int getId() {
		return this.id;
	}

	// ============================ 属性操作 =====================================
	protected void clearProperties() {
		if (m_hProperties != null)
			this.m_hProperties.clear();
	}

	protected void clearNewProperties() {
		if (m_hNewProperties != null)
			this.m_hNewProperties.clear();
		m_hNewProperties = null;
	}

	@SuppressWarnings("rawtypes")
	public Hashtable getNewProperties() {
		return this.m_hNewProperties;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Hashtable getProperties() {
		return getProperties(false);
	}

	@SuppressWarnings("rawtypes")
	protected Hashtable getProperties(boolean _bNeedCheckInit) {
		if (m_hProperties == null && _bNeedCheckInit) {
			m_hProperties = new Hashtable();
		}
		return m_hProperties;
	}

	/**
	 * 将指定的属性列表数据拷贝到本对象中
	 * 
	 * @param _properties
	 *            属性列表
	 * @return 拷贝成功返回true
	 * @throws PersistenceException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean setProperties(Hashtable _properties) {
		if (_properties == null)
			return false;
		getProperties(true).putAll(_properties);
		return true;
	}

	/**
	 * 重置对象属性
	 * 
	 * @param _properties
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected boolean resetProperties(Hashtable _properties) {
		if (_properties == null)
			return false;
		clearProperties();
		return setProperties(_properties);
	}

	
	//================================ 数据库操作 ================================
	
	protected DBManager getDBManager() {
        DBManager currDBManager = DBManager.getDBManager();
        if (currDBManager == null || !currDBManager.isStarted()) {
            throw new Error(I18NMessage.get(BaseObj.class, "BaseObj.label42",
                    "数据库没有正常启动！"));
            // throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
            // "DBManaer配置不正确！");
        }
        // if (!currDBManager.isStarted()) {
        // throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
        // "DBManaer没有启动！");
        // }
        return currDBManager;
    }
	
	/**
	 * 从db加载指定字段
	 * 
	 * @param _sSelectFields
	 * @return
	 * @throws PersistenceException
	 */
	protected boolean loadFromDB(String _sSelectFields) throws PersistenceException {
        if (this.getId() <= 0)
            throw new PersistenceException(ExceptionNumber.ERR_PARAM_INVALID,"没有设置ID！");

        String sSelectField = _sSelectFields == null ? "*" : _sSelectFields
                .trim();
        if (sSelectField.length() <= 0) {
            sSelectField = "*";
        }
        Connection oConn = null;
        PreparedStatement oStmt = null;
        ResultSet rsData = null;
        ResultSetMetaData rsmdData = null;

        String strSQL = "";

        // 提取数据
        try {
            // 构造SQL语句
            strSQL = "select " + sSelectField + " from "
                    + this.getDbTableName() + " where " + this.getIdFieldName()
                    + "=?";

            // 从数据库中检索结果集
            oConn = getDBManager().getConnection();
            oStmt = oConn.prepareStatement(strSQL);
            oStmt.setInt(1, this.id);
            oStmt.setMaxRows(1);

            // 装载对象数据
            rsData = oStmt.executeQuery();
            if (rsData.next()) {
                rsmdData = rsData.getMetaData();
                this.readFromRs(rsData, rsmdData);
                return true;
            }

            return false;

        } catch (SQLException ex) {
            throw new PersistenceException(ExceptionNumber.ERR_DBOP_FAIL,
                    I18NMessage.get(BaseObj.class, "BaseObj.label44",
                            "装载指定页失败(")
                            + getClass().getName()
                            + ".loadFromDB)：SQL=" + strSQL, ex);
        } catch (Exception ex) {
            throw new PersistenceException(ExceptionNumber.ERR_UNKNOWN,
                    I18NMessage.get(BaseObj.class, "BaseObj.label44",
                            "装载指定页失败(")
                            + getClass().getName()
                            + ".loadFromDB)：SQL=" + strSQL, ex);
        } finally {
            if (rsData != null)
                try {
                    rsData.close();
                } catch (Exception ex) {
                }
            if (oStmt != null)
                try {
                    oStmt.close();
                } catch (Exception ex) {
                }
            if (oConn != null) {
                getDBManager().freeConnection(oConn);
            }
        }// endtry
    }
}