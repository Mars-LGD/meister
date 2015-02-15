package org.xiaotian.persistence.dao;

import java.util.List;

import org.xiaotian.persistence.db.PersistenceException;
import org.xiaotian.persistence.model.PageModel;

public interface BaseDao<T> {

	/**
	 * 根据where子句执行数据库删除操作
	 * 
	 * @param _sFilter
	 *            sql的filter子句
	 * @param _paramAry
	 *            sql语句的参数
	 * 
	 * @return 受影响（删除）的数据条数
	 * @throws PersistenceException
	 */
	public int delete(String _sFilter, final Object[] _paramAry) throws PersistenceException;

	/**
	 * 根据id(主键)执行数据库删除操作
	 * 
	 * @param _nId
	 *            主键id值
	 * 
	 * @return 受影响（删除）的数据条数
	 * @throws PersistenceException
	 */
	public int delete(Long _nId) throws PersistenceException;

	/**
	 * 根据id读取数据库对象
	 * 
	 * @param id
	 *            数据库主键
	 * @return
	 * @throws PersistenceException
	 */

	public T findById(Long id) throws PersistenceException;

	/**
	 * 插入对象
	 * 
	 * @param _oEntity
	 *            操作的对象实体
	 * @return
	 * @throws PersistenceException
	 */
	public int save(T _oEntity) throws PersistenceException;

	/**
	 * 更新对象
	 * 
	 * @param object
	 * @return
	 * @throws PersistenceException
	 */
	public Object update(T _oEntity) throws PersistenceException;

	/**
	 * 读取所有数据库对象
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	public List<T> getAll() throws PersistenceException;
	
	/**
	 * 获取记录条数
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	public int getCount() throws PersistenceException;
	
	/**
	 * 根据where子句读取所有数据库对象
	 * 
	 * @param _filter
	 * @return
	 * @throws PersistenceException
	 */
	public List<T> getList(String _filter) throws PersistenceException;
	
	/**
	 * 获取记录条数
	 * 
	 * @param _filter
	 * @return
	 * @throws PersistenceException
	 */
	public int getCount(String _filter) throws PersistenceException;

	/**
	 * 根据where子句读取所有数据库对象
	 * 
	 * @param _filter
	 * @param _paramAry
	 * @return
	 * @throws PersistenceException
	 */
	public List<T> getList(String _filter, final Object[] _paramAry) throws PersistenceException;
	
	/**
	 * 获取记录条数
	 * 
	 * @param _filter
	 * @param _paramAry
	 * @return
	 * @throws PersistenceException
	 */
	public int getCount(String _filter, final Object[] _paramAry) throws PersistenceException;


	/**
	 * 分页查询返回List对象
	 * 
	 * @param _filter
	 * @param _paramAry
	 * @param _nStartPos
	 * @param _nPageSize
	 * @return
	 * @throws PersistenceException
	 */
	public List<T> queryPageList(final String _filter, final Object[] _paramAry, final int _nStartPos, final int _nPageSize) throws PersistenceException;

	/**
	 * 分页查询返回List对象
	 * 
	 * @param _filter
	 * @param _nStartPos
	 * @param _nPageSize
	 * @return
	 * @throws PersistenceException
	 */
	public List<T> queryPageList(final String _filter, final int _nStartPos, final int _nPageSize) throws PersistenceException;


	/**
	 * 分页查询返回List对象
	 * 
	 * @param _nStartPos
	 * @param _nPageSize
	 * @return
	 * @throws PersistenceException
	 */
	public List<T> queryPageList(final int _nStartPos, final int _nPageSize) throws PersistenceException;

	/**
	 * 分页查询返回PageModel
	 * 
	 * @param _filter
	 * @param _paramAry
	 * @param _nCurrentPage
	 * @param _nPageSize
	 * @return
	 * @throws PersistenceException
	 */
	public PageModel queryPageModel(final String _filter, final Object[] _paramAry, final int _nCurrentPage, final int _nPageSize) throws PersistenceException;

	/**
	 * 分页查询返回PageModel
	 * 
	 * @param _filter
	 * @param _nCurrentPage
	 * @param _nPageSize
	 * @return
	 * @throws PersistenceException
	 */
	public PageModel queryPageModel(final String _filter, final int _nCurrentPage, final int _nPageSize) throws PersistenceException;
	
	/**
	 * 分页查询返回PageModel
	 * 
	 * @param _nCurrentPage
	 * @param _nPageSize
	 * @return
	 * @throws PersistenceException
	 */
	public PageModel queryPageModel(final int _nCurrentPage, final int _nPageSize) throws PersistenceException;

}
