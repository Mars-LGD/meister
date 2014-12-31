/*
 * NodeDirectory.java created on 2006-9-29 下午02:44:30 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 目录节点，继承自Node
 * 
 * @author		Martin (付成睿)
 */
public class NodeDirectory extends Node
{

	private List m_oChildren;

	public NodeDirectory(String name)
	{
		super(name);
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append(",children=").append(m_oChildren);
		return sb.toString();
	}

	public void cleanup()
	{
		super.cleanup();
		if (m_oChildren != null)
		{
			Iterator itr = m_oChildren.iterator();
			while (itr.hasNext())
			{
				Node c = (Node) itr.next();
				if (c != null)
				{
					c.cleanup();
				}
			}
			m_oChildren.clear();
			m_oChildren = null;
		}
	}

	protected void trimMemory()
	{
		super.trimMemory();
		if (m_oChildren != null)
		{
			Iterator itr = m_oChildren.iterator();
			while (itr.hasNext())
			{
				Node c = (Node) itr.next();
				if (c != null)
				{
					c.trimMemory();
				}
			}
			Utils.trimArrayList(m_oChildren);
		}
	}

	public void tree(PrintWriter pw, String prefix)
	{
		List list = m_oChildren;
		if (list == null)
		{
			return;
		}
		int limit = list.size() - 1;
		for (int i = 0; i <= limit; i++)
		{
			String p = null;
			Node n = (Node) list.get(i);
			if (i == limit)
			{
				pw.print(prefix);
				pw.print("└─");
				p = prefix + "    ";
			}
			else
			{
				pw.print(prefix);
				pw.print("├─");
				p = prefix + "│  ";
			}
			pw.println(n.getName());
			if (n.isDirectory())
			{
				((NodeDirectory) n).tree(pw, p);
			}
		}
	}

	public boolean hasChild()
	{
		return m_oChildren != null && m_oChildren.size() > 0;
	}

	public void addChild(Node node)
	{
		if (m_oChildren == null)
		{
			m_oChildren = new ArrayList();
		}
		m_oChildren.add(node);
		node.setParent(this);
	}

	public List children()
	{
		return m_oChildren;
	}

	public boolean isDirectory()
	{
		return true;
	}

	protected NodeDirectory getDirectoryNode(String name)
	{
		if (m_oChildren != null)
		{
			Iterator itr = m_oChildren.iterator();
			while (itr.hasNext())
			{
				Node node = (Node) itr.next();
				if (node.getName().equals(name))
				{
					return (NodeDirectory) node;
				}
			}
		}
		NodeDirectory nd = new NodeDirectory(name);
		addChild(nd);
		return nd;
	}

}