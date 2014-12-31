/*
 * IType.java created on 2006-9-27 下午05:11:30 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery;

import java.util.Iterator;

/**
 * Java类型的抽象描述
 * 
 * @author		Martin (付成睿)
 */
public interface IType extends IResource
{

	/**
	 * 返回当前Java类型的全限定名称
	 * 
	 * @return	当前Java类型的全限定名称
	 */
	public String getQFName();

	/**
	 * 返回当前Java类型的超类的全限定名称
	 * 
	 * @return	当前Java类型的超类的全限定名称
	 */
	public String getQFSuperName();

	/**
	 * 返回当前Java类型所实现的全部接口的迭代器
	 * 
	 * @return	当前Java类型所实现的全部接口的迭代器
	 */
	public Iterator getQFInterfaces();

	/**
	 * 返回当前Java类型的版本
	 * 
	 * @return	当前Java类型的版本
	 */
	public int getVersion();

	/**
	 * 返回是否枚举类型
	 * 
	 * @return	是否枚举类型
	 */
	public boolean isEnum();

	/**
	 * 返回是否普通类
	 * 
	 * @return	是否普通类
	 */
	public boolean isClass();

	/**
	 * 返回是否接口
	 * 
	 * @return	是否接口
	 */
	public boolean isInterface();

	/**
	 * 返回是否注释
	 * 
	 * @return	是否注释
	 */
	public boolean isAnnotation();

	/**
	 * 返回是否终态类型
	 * 
	 * @return	是否终态类型
	 */
	public boolean isFinal();

	/**
	 * 返回是否抽象类型
	 * 
	 * @return	是否抽象类型
	 */
	public boolean isAbstract();

	/**
	 * 返回是否存在冲突
	 * 
	 * @return	是否存在冲突
	 */
	public boolean hasCollision();

	/**
	 * 返回是否公开的
	 * 
	 * @return	是否公开的
	 */
	public boolean isPublic();

	/**
	 * 返回当前Java类型的所有冲突类型
	 * 
	 * @return	当前Java类型的所有冲突类型
	 */
	public Iterator getCollision();

}