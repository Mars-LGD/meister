package org.xiaotian.persistence.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.xiaotian.extend.CMyString;
import org.xiaotian.persistence.annotation.Clob;
import org.xiaotian.persistence.annotation.Column;
import org.xiaotian.persistence.annotation.Id;
import org.xiaotian.persistence.annotation.Table;
import org.xiaotian.persistence.db.DBManager;
import org.xiaotian.persistence.db.PersistenceException;
import org.xiaotian.persistence.model.CustomBeanProcessor;
import org.xiaotian.persistence.model.PageModel;
import org.xiaotian.utils.ReflectionUtils;

/**
 * BaseDao适配实现类
 * 
 * @author xiaotian15
 * 
 * @param <BaseObj>
 */
public class BaseDaoAdapter<T> implements BaseDao<T> {

	/** Entity Class */
	private Class<T> m_cEntityClass;

	/** 数据源标识 */
	private String m_sTag;

	/** 数据库table name */
	private String m_sTableName;

	/** table 主键字段 */
	private String m_sIdFieldName;

	/** table 除主键字段之外的其余字段集合 */
	private List<String> m_lFieldList;

	/** table 大字段集合，应用于判断是否加载大字段 */
	private List<String> m_lClobFieldList;

	@SuppressWarnings("unchecked")
	public BaseDaoAdapter() {
		m_cEntityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Table oTable = m_cEntityClass.getAnnotation(Table.class);
		if (oTable == null) {
			m_sTag = "default";
			m_sTableName = m_cEntityClass.getSimpleName();
		} else {
			String tableNameInfo = oTable.name();
			String[] tempAry = tableNameInfo.split("\\.");
			if (tempAry.length != 2) {
				throw new RuntimeException(m_cEntityClass.getName() + "对象@Table属性(name=" + tableNameInfo + ")配置错误，正确格式:Tag.TableName");
			}
			m_sTag = tempAry[0];
			m_sTableName = tempAry[1];
		}

		// 初始化数据库字段信息
		initTableFieldInfos();
	}

	/**
	 * 获得对应数据源Tag
	 * 
	 * @return
	 */
	protected String getDbTagName() {
		return m_sTag;
	}

	/**
	 * 获得数据库中存储该对象的数据表名称。 如：User对象对应的数据表是WCMUSER
	 * 
	 * @return
	 */
	protected String getDbTableName() {
		return m_sTableName;
	}

	/**
	 * 获得Id字段名。如：wcmUser表中的UserId
	 * 
	 * @return Id字段名
	 */
	protected String getIdFieldName() {
		return m_sIdFieldName;
	}

	/**
	 * 列出要插入到数据库的域集合(除主键，主键由数据库维护)，子类可以覆盖此方法
	 * 
	 * @return
	 */
	protected Map<String, Object> getDbFiledInfoMap(T _oEntity) {
		try {
			Map<String, Object> props = new HashMap<String, Object>();
			for (String fieldName : m_lFieldList) {
				String dbField = CMyString.camelToUnderline(fieldName, '_');
				props.put(dbField, ReflectionUtils.getFieldValue(_oEntity, fieldName));
			}
			return props;
		} catch (Exception e) {
			throw new RuntimeException("获取" + m_cEntityClass + "对象字段信息失败");
		}
	}

	// ============================= 实现BaseDao接口 ===============================

	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#delByWhere(java.lang.String,
	 *      java.lang.Object[])
	 */
	public int delete(String _filter, Object[] _paramAry) throws PersistenceException {
		if(CMyString.isEmpty(_filter)){
			throw new PersistenceException(this.getClass() + "->delete(String _filter, Object[] _paramAry)方法参数_filter值为空");
		}
		Connection conn = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("delete from ");
		sqlBuilder.append(m_sTableName);
		sqlBuilder.append(" WHERE ");
		sqlBuilder.append(_filter);
		String sql = sqlBuilder.toString();
		try {
			conn = DBManager.getInstance().getConnection(getDbTagName());
			QueryRunner queryRunner = new QueryRunner();

			int nUpdateRows = queryRunner.update(conn, sql, _paramAry);
			return nUpdateRows;
		} catch (Exception e) {
			throw new PersistenceException(this.getClass() + "->delete(String _filter, Object[] _paramAry)执行失败", e);
		} finally {
			DBManager.close(conn);
		}
	}

	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#delete(java.lang.Long)
	 */
	public int delete(Long _nId) throws PersistenceException {
		return delete(getIdFieldName() + "=?", new Object[] { _nId });
	}

	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#findById(java.lang.Long)
	 */
	public T findById(Long _nId) throws PersistenceException {
		String sql = "select * from " + getDbTableName() + " where " + getIdFieldName() + "=?";
		Connection conn = null;
		try {
			conn = DBManager.getInstance().getConnection(getDbTagName());
			QueryRunner queryRunner = new QueryRunner();
			RowProcessor rp = new BasicRowProcessor(new CustomBeanProcessor());
			T entity = (T) queryRunner.query(conn, sql, new BeanHandler<T>(m_cEntityClass, rp), new Object[] { _nId });
			return entity;
		} catch (Exception e) {
			throw new PersistenceException(this.getClass() + "->findById(" + _nId + ")执行失败", e);
		} finally {
			DBManager.close(conn);
		}
	}

	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#save(java.lang.Object)
	 */
	public int save(T _oEntity) throws PersistenceException {
		// 1.构造sql语句，以及param参数
		Map<String, Object> hFieldInfos = getDbFiledInfoMap(_oEntity);
		String[] dbFields = new String[hFieldInfos.size()];
		hFieldInfos.keySet().toArray(dbFields);
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(getDbTableName());
		sql.append('(');
		for (int i = 0; i < dbFields.length; i++) {
			if (i > 0)
				sql.append(',');
			sql.append(dbFields[i]);
		}
		sql.append(") VALUES(");
		for (int i = 0; i < dbFields.length; i++) {
			if (i > 0)
				sql.append(',');
			sql.append('?');
		}
		sql.append(')');

		Object[] params = new Object[dbFields.length];
		for (int i = 0; i < dbFields.length; i++) {
			params[i] = hFieldInfos.get(dbFields[i]);
		}

		// 2.执行数据库操作
		Connection conn = null;
		try {
			conn = DBManager.getInstance().getConnection(m_sTag);
			QueryRunner runner = new QueryRunner();
			return runner.update(conn, sql.toString(), params);
		} catch (Exception e) {
			throw new PersistenceException(this.getClass() + "->save(" + m_cEntityClass + ")执行失败", e);
		} finally {
			DBManager.close(conn);
		}
	}

	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#update(java.lang.Object)
	 */
	public Object update(T _oEntity) throws PersistenceException {
		// 1.构造sql语句，以及param参数
		Map<String, Object> hFieldInfos = getDbFiledInfoMap(_oEntity);
		StringBuilder sql = new StringBuilder("UPDATE ").append(m_sTableName).append(" SET ");
		String[] dbFields = hFieldInfos.keySet().toArray(new String[hFieldInfos.size()]);
		for (int i = 0; i < dbFields.length; i++) {
			if (i > 0)
				sql.append(',');
			sql.append(dbFields[i]).append("=?");
		}
		sql.append(" where ").append(m_sIdFieldName).append("=").append(ReflectionUtils.getFieldValue(_oEntity, m_sIdFieldName));

		Object[] params = new Object[dbFields.length];
		for (int i = 0; i < dbFields.length; i++) {
			params[i] = hFieldInfos.get(dbFields[i]);
		}

		// 2.执行数据库操作
		Connection conn = null;
		try {
			conn = DBManager.getInstance().getConnection(m_sTag);
			QueryRunner runner = new QueryRunner();
			return runner.update(conn, sql.toString(), params);
		} catch (Exception e) {
			throw new PersistenceException(this.getClass() + "->update(" + m_cEntityClass + ")执行失败", e);
		} finally {
			DBManager.close(conn);
		}
	}

	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#getAll()
	 */
	public List<T> getAll() throws PersistenceException {
		Connection conn = null;
		String sql = "SELECT * FROM " + m_sTableName + " ORDER BY " + m_sIdFieldName + " DESC";
		try {
			conn = DBManager.getInstance().getConnection(m_sTag);
			QueryRunner queryRunner = new QueryRunner();
			RowProcessor rp = new BasicRowProcessor(new CustomBeanProcessor());
			List<T> entityList = queryRunner.query(conn, sql, new BeanListHandler<T>(m_cEntityClass, rp));
			return entityList;
		} catch (Exception e) {
			throw new PersistenceException(this.getClass() + "->getAll()执行失败", e);
		} finally {
			DBManager.close(conn);
		}
	}
	
	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#getCount()
	 */
	public int getCount() throws PersistenceException {
		return getCount(null, new Object[]{});
	}
	
	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#getList(java.lang.String)
	 */
	public List<T> getList(String _filter) throws PersistenceException {
		return getList(_filter, new Object[]{});
	}
	
	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#getList(java.lang.String)
	 */
	public int getCount(String _filter) throws PersistenceException {
		return getCount(_filter, new Object[]{});
	}
	
	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#getCount()
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getCount(String _filter, Object[] _paramAry) throws PersistenceException {
		Connection conn = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT count(*) FROM ");
		sqlBuilder.append(m_sTableName);
		if (!CMyString.isEmpty(_filter)) {
			sqlBuilder.append(" WHERE ");
			sqlBuilder.append(_filter);
		}
		String sql=sqlBuilder.toString();
		try {
			conn = DBManager.getInstance().getConnection(m_sTag);
			QueryRunner queryRunner = new QueryRunner();
			Number num  = (Number)queryRunner.query(conn, sql, new ScalarHandler(){
		        @Override
		        public Object handle(ResultSet rs) throws SQLException {
		            Object obj = super.handle(rs);
		            if(obj instanceof BigInteger)
		                return ((BigInteger)obj).intValue();
		            return obj;
		        }
		    },_paramAry);
			if(num==null||num.longValue()<0){
				throw new PersistenceException(this.getClass() + "->getCount(String _filter, Object[] _paramAry)执行失败");
			}
            return num.intValue();
		} catch (Exception e) {
			throw new PersistenceException(this.getClass() + "->getCount(String _filter, Object[] _paramAry)执行失败", e);
		} finally {
			DBManager.close(conn);
		}
	}

	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#getList(java.lang.String,
	 *      java.lang.Object[])
	 */
	public List<T> getList(String _filter, Object[] _paramAry) throws PersistenceException {
		Connection conn = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT * FROM ");
		sqlBuilder.append(m_sTableName);
		if (!CMyString.isEmpty(_filter)) {
			sqlBuilder.append(" WHERE ");
			sqlBuilder.append(_filter);
		}
		sqlBuilder.append(" ORDER BY ");
		sqlBuilder.append(m_sIdFieldName);
		sqlBuilder.append(" DESC");
		String sql = sqlBuilder.toString();
		try {
			conn = DBManager.getInstance().getConnection(m_sTag);
			QueryRunner queryRunner = new QueryRunner();
			RowProcessor rp = new BasicRowProcessor(new CustomBeanProcessor());
			List<T> entityList = queryRunner.query(conn, sql, new BeanListHandler<T>(m_cEntityClass, rp), _paramAry);
			return entityList;
		} catch (Exception e) {
			throw new PersistenceException(this.getClass() + "->getList(" + _filter + ")执行失败", e);
		} finally {
			DBManager.close(conn);
		}
	}

	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#queryPageList(java.lang.String,
	 *      java.lang.Object[], int, int)
	 */
	public List<T> queryPageList(String _filter, Object[] _paramAry, int _nStartPos, int _nPageSize) throws PersistenceException {
		Connection conn = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT * FROM ");
		sqlBuilder.append(m_sTableName);
		if (!CMyString.isEmpty(_filter)) {
			sqlBuilder.append(" WHERE ");
			sqlBuilder.append(_filter);
		}
		sqlBuilder.append(" ORDER BY ");
		sqlBuilder.append(m_sIdFieldName);
		sqlBuilder.append(" DESC limit ");
		sqlBuilder.append(_nStartPos);
		sqlBuilder.append(",");
		sqlBuilder.append(_nPageSize);
		String sql = sqlBuilder.toString();
		try {
			conn = DBManager.getInstance().getConnection(m_sTag);
			QueryRunner queryRunner = new QueryRunner();
			RowProcessor rp = new BasicRowProcessor(new CustomBeanProcessor());
			List<T> entityList = queryRunner.query(conn, sql, new BeanListHandler<T>(m_cEntityClass, rp), _paramAry);
			return entityList;
		} catch (Exception e) {
			throw new PersistenceException(this.getClass() + "->queryPageList(" + _filter + ")执行失败", e);
		} finally {
			DBManager.close(conn);
		}
	}

	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#queryPageList(java.lang.String,
	 *      int, int)
	 */
	public List<T> queryPageList(String _filter, int _nStartPos, int _nPageSize) throws PersistenceException {
		return queryPageList(_filter, new Object[] {}, _nStartPos, _nPageSize);
	}
	
	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#queryPageList(int, int)
	 */
	public List<T> queryPageList(int _nStartPos, int _nPageSize) throws PersistenceException {
		return queryPageList(null, new Object[] {}, _nStartPos, _nPageSize);
	}

	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#queryPageModel(java.lang.String,
	 *      java.lang.Object[], int, int)
	 */
	public PageModel queryPageModel(String _filter, Object[] _paramAry, int _nCurrentPage, int _nPageSize) throws PersistenceException {		
		// 1. 获取count
		int count=getCount(_filter, _paramAry);
		
		// 2. 获取分页数据
		List<T> entityList = queryPageList(_filter,_paramAry,_nCurrentPage,_nPageSize);
		
		// 3. 构造PageModel		
		PageModel pageModel=new PageModel();
		pageModel.setPageCount(PageModel.countTotalPage(_nPageSize, count));
		pageModel.setPageSize(_nPageSize);
		pageModel.setRecordCount(count);
		pageModel.setDataList(entityList);
		pageModel.setCurrentPage(PageModel.countCurrentPage(_nCurrentPage));
		return pageModel;
	}

	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#queryPageModel(java.lang.String,
	 *      int, int)
	 */
	public PageModel queryPageModel(String _filter, int _nCurrentPage, int _nPageSize) throws PersistenceException {
		return queryPageModel(_filter,new Object[]{},_nCurrentPage,_nPageSize);
	}
	
	/**
	 * @see org.xiaotian.persistence.dao.BaseDao#queryPageModel(int, int)
	 */
	public PageModel queryPageModel(int _nCurrentPage, int _nPageSize) throws PersistenceException {
		return queryPageModel(null,new Object[]{},_nCurrentPage,_nPageSize);
	}

	/**
	 * 列出要插入到数据库的域集合，子类可以覆盖此方法
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	private void initTableFieldInfos() {
		try {
			// 遍历实体字段
			m_lFieldList = new ArrayList<String>();
			Field[] fields = m_cEntityClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					m_lFieldList.add(field.getName());
				}
				if (field.isAnnotationPresent(Id.class)) {
					m_sIdFieldName = field.getName();
				}
				if (field.isAnnotationPresent(Clob.class)) {
					m_lClobFieldList.add(field.getName());
				}
			}

			// 若未设置id主建，抛出异常
			if (m_sIdFieldName == null) {
				throw new RuntimeException(m_cEntityClass.getName() + "对象没有主键");
			}
		} catch (Exception e) {
			throw new RuntimeException("应用反射获取" + m_cEntityClass.getName() + "对象的数据库字段失败", e);
		}
	}
}
