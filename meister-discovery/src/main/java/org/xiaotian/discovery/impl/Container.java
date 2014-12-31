/*
 * Container.java created on 2006-9-29 上午09:35:03 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

public class Container
{

	protected static String[] splitPath(String path)
	{
		StringTokenizer st = new StringTokenizer(path, "/");
		String[] rs = new String[st.countTokens()];
		for (int i = 0; i < rs.length && st.hasMoreTokens(); i++)
		{
			rs[i] = st.nextToken();
		}
		return rs;
	}

	URL m_oLocation;

	List m_oRootNode;

	public Container(URL location)
	{
		m_oLocation = location;
		m_oRootNode = new ArrayList();
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Container:Location=").append(m_oLocation);
		sb.append(",RootNodeList=").append(m_oRootNode);
		return sb.toString();
	}

	public void cleanup()
	{
		Iterator itr = m_oRootNode.iterator();
		while (itr.hasNext())
		{
			Node c = (Node) itr.next();
			if (c != null)
			{
				c.cleanup();
			}
		}
		m_oRootNode.clear();
		m_oRootNode = null;
	}

	public void tree(PrintWriter pw, String prefix)
	{
		List list = m_oRootNode;
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

	public URL getLocation()
	{
		return m_oLocation;
	}

	/**
	 * 减少内存的占用
	 */
	protected void trimMemory()
	{
		Iterator itr = m_oRootNode.iterator();
		while (itr.hasNext())
		{
			Node c = (Node) itr.next();
			if (c != null)
			{
				c.trimMemory();
			}
		}
		Utils.trimArrayList(m_oRootNode);
	}

	protected void addRootNode(Node node)
	{
		m_oRootNode.add(node);
		node.setContainer(this);
	}

	protected NodeDirectory getRootDirectory(String name)
	{
		Iterator itr = m_oRootNode.iterator();
		while (itr.hasNext())
		{
			Node node = (Node) itr.next();
			if (node.getName().equals(name))
			{
				return (NodeDirectory) node;
			}
		}
		NodeDirectory nd = new NodeDirectory(name);
		addRootNode(nd);
		return nd;
	}

	protected void addDirectory(String path)
	{
		String[] names = splitPath(path);
		int count = names.length;
		if (count <= 0)
		{
			return;
		}
		NodeDirectory parent = getRootDirectory(names[0]);
		for (int i = 1; i < count; i++)
		{
			parent = parent.getDirectoryNode(names[i]);
		}
	}

	protected Node findNode(String[] path)
	{
		List list = m_oRootNode;
		int limit = path.length - 1;
		for (int i = 0; i <= limit; i++)
		{
			Node node = null;
			String name = path[i];
			for (int j = 0; j < list.size(); j++)
			{
				Node temp = (Node) list.get(j);
				if (name.endsWith(temp.getName()))
				{
					node = temp;
					break;
				}
			}
			if (node == null)
			{
				//如果在当前节点下找不到，则结束循环
				break;
			}
			if (i == limit)
			{
				return node;
			}
			//如果按照输入路径的，仍然还需要向下查找，则要判断当前节点是否目录
			if (node.isDirectory())
			{
				list = ((NodeDirectory) node).children();
                if (list == null)
                {
                    break;
                }
			}
			else
			{
				break;
			}
		}
		return null;
	}

	private NodeResource findResource0(List list, String name)
	{
		for (int i = 0; i < list.size(); i++)
		{
			Node node = (Node) list.get(i);
			if (node.isDirectory())
			{
				if (node.hasChild())
				{
					List list0 = ((NodeDirectory) node).children();
					NodeResource node0 = findResource0(list0, name);
					if (node0 != null)
					{
						return node0;
					}
				}
			}
			else
			{
				if (name.equals(node.getName()))
				{
					return (NodeResource) node;
				}
			}
		}
		return null;
	}

	protected NodeResource findResource(String name)
	{
		return findResource0(m_oRootNode, name);
	}

	protected Iterator findResources(final String name)
	{
		Iterator itr = new Iterator()
		{

			private DFTFrame m_oFrame = new DFTFrame(m_oRootNode);

			private Stack m_oStack = new Stack();

			private NodeResource m_oFound = null;

			private void findNext()
			{
				if (m_oFound != null)
				{
					return;
				}
				while (m_oFrame != null)
				{
					while (m_oFrame.index < m_oFrame.list.size())
					{
						Node node = (Node) m_oFrame.list.get(m_oFrame.index++);
						if (name.equals(node.getName()))
						{
							if (node instanceof NodeResource)
							{
								m_oFound = (NodeResource) node;
								return;
							}
						}
						if (node instanceof NodeDirectory)
						{
							NodeDirectory dir = (NodeDirectory) node;
							if (dir.hasChild())
							{
								m_oStack.push(m_oFrame);
								m_oFrame = new DFTFrame(dir.children());
							}
						}
					}
					m_oFrame = null;
					if (m_oStack.size() > 0)
					{
						m_oFrame = (DFTFrame) m_oStack.pop();
					}
				}
			}

			public boolean hasNext()
			{
				findNext();
				if (m_oFound != null)
				{
					return true;
				}
				return false;
			}

			public Object next()
			{
				findNext();
				if (m_oFound != null)
				{
					NodeResource node = m_oFound;
					m_oFound = null;
					return node;
				}
				return null;
			}

			public void remove()
			{
			}

		};
		return itr;
	}

	protected Iterator findTypes(final FilterType filter)
	{
		Iterator itr = new Iterator()
		{

			private DFTFrame m_oFrame = new DFTFrame(m_oRootNode);

			private Stack m_oStack = new Stack();

			private NodeType m_oFound = null;

			private void findNext()
			{
				if (m_oFound != null)
				{
					return;
				}
				while (m_oFrame != null)
				{
					while (m_oFrame.index < m_oFrame.list.size())
					{
						Node node = (Node) m_oFrame.list.get(m_oFrame.index++);
						if (node instanceof NodeType)
						{
							NodeType temp = (NodeType) node;
							if (filter == null || filter.accept(temp))
							{
								m_oFound = (NodeType) node;
								return;
							}
						}
						if (node instanceof NodeDirectory)
						{
							NodeDirectory dir = (NodeDirectory) node;
							if (dir.hasChild())
							{
								m_oStack.push(m_oFrame);
								m_oFrame = new DFTFrame(dir.children());
							}
						}
					}
					m_oFrame = null;
					if (m_oStack.size() > 0)
					{
						m_oFrame = (DFTFrame) m_oStack.pop();
					}
				}
			}

			public boolean hasNext()
			{
				findNext();
				if (m_oFound != null)
				{
					return true;
				}
				return false;
			}

			public Object next()
			{
				findNext();
				if (m_oFound != null)
				{
					NodeType nodetype = m_oFound;
					m_oFound = null;
					return nodetype;
				}
				return null;
			}

			public void remove()
			{
			}

		};
		return itr;
	}

}