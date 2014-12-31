/*
 * IteratorUnion.java created on 2006-10-9 下午01:44:51 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.util.Iterator;

/**
 * 聚合迭代器，以聚合多个迭代器
 * 
 * @author		Martin (付成睿)
 */
public abstract class IteratorUnion implements Iterator
{

	/**
	 * 当前被代理的迭代器
	 */
	private Iterator m_oProxyIterator;

	/**
	 * 缺省构造方法
	 */
	public IteratorUnion()
	{
		m_oProxyIterator = null;
	}

	/**
	 * 查找下一个代理迭代器
	 */
	private void findIterator()
	{
		while (m_oProxyIterator == null || !m_oProxyIterator.hasNext())
		{
			if ((m_oProxyIterator = nextIterator()) == null)
			{
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext()
	{
		findIterator();
		if (m_oProxyIterator == null)
		{
			return false;
		}
		return m_oProxyIterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Object next()
	{
		findIterator();
		if (m_oProxyIterator == null)
		{
			return null;
		}
		return m_oProxyIterator.next();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove()
	{
	}

	/**
	 * 返回下一个代理迭代器。或者返回<code>null</code>，如果已经没有下一个的话。
	 * 
	 * @return	下一个代理迭代器
	 */
	protected abstract Iterator nextIterator();

}