/*
 * Node.java created on 2006-9-29 上午11:18:02 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

/**
 * 树形节点的基类
 * 
 * @author		Martin (付成睿)
 */
public class Node
{

	private String m_sName;

	private String m_sPath;

	private Node m_oParent;

	private Container m_oContainer;

	public Node(String name)
	{
		m_sName = name;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName());
		sb.append(":name=").append(m_sName);
		return sb.toString();
	}

	/**
	 * 清除内存的使用
	 */
	public void cleanup()
	{
		m_sName = null;
		m_sPath = null;
		m_oParent = null;
		m_oContainer = null;
	}

	/**
	 * 减少内存的占用
	 */
	protected void trimMemory()
	{
		m_sName = m_sName.intern();
		m_sPath = null;
	}

	public String getName()
	{
		return m_sName;
	}

	public String getPath()
	{
		if (m_sPath == null)
		{
			Node parent = getParent();
			if (parent != null)
			{
				m_sPath = parent.getPath() + '/' + m_sName;
			}
			else
			{
				m_sPath = m_sName;
			}
		}
		return m_sPath;
	}

	public Node getParent()
	{
		return m_oParent;
	}

	protected void setParent(Node parent)
	{
		m_oParent = parent;
		m_oContainer = parent.m_oContainer;
	}

	public Container getContainer()
	{
		return m_oContainer;
	}

	protected void setContainer(Container container)
	{
		m_oContainer = container;
	}

	public boolean isDirectory()
	{
		return false;
	}

	public boolean hasChild()
	{
		return false;
	}

}