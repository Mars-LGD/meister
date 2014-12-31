/*
 * IteratorProxy.java created on 2006-9-30 上午09:44:48 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.util.Iterator;

/**
 * 代理迭代器，以屏蔽<code>java.util.Iterator#remove()</code>方法
 * 
 * @author		Martin (付成睿)
 */
public class IteratorProxy implements Iterator
{

	/**
	 * 被代理的迭代器
	 */
	private Iterator m_oProxyIterator;

	/**
	 * 构造函数
	 */
	public IteratorProxy(Iterator itr)
	{
		m_oProxyIterator = itr;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext()
	{
		return m_oProxyIterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Object next()
	{
		return m_oProxyIterator.next();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove()
	{
	}

}