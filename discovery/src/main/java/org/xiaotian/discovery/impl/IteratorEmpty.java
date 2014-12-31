/*
 * IteratorEmpty.java created on 2006-9-30 上午09:34:59 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.util.Iterator;

/**
 * 空的迭代器
 * 
 * @author		Martin (付成睿)
 */
class IteratorEmpty implements Iterator
{

	/**
	 * 缺省空迭代器，因为空迭代器没有必要创建多个
	 */
	private final static Iterator EMPTY_ITERATOR = new IteratorEmpty();

	/**
	 * 返回缺省空迭代器
	 * 
	 * @return	缺省空迭代器
	 */
	public final static Iterator getEmptyIterator()
	{
		return EMPTY_ITERATOR;
	}

	/**
	 * 缺省构造方法
	 */
	public IteratorEmpty()
	{
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Object next()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove()
	{
	}

}