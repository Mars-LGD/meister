/*
 * Discovery.java created on 2006-9-28 上午09:39:54 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.xiaotian.discovery.IDiscovery;
import org.xiaotian.discovery.IType;

/**
 * 实现<code>com.trs.infra.discover.IDiscovery</code>
 * 
 * 关于为什么使用复杂的树形数据结构
 * 从后来的实现来看，这是一个错误，不应该使用树这种过于复杂的数据结构。
 * 最初想法只是尽最大可能的节约内存，并没有充分的预见到带来复杂性。如果使用Hash表的结构则要简洁的多。
 * 至于使用Hash表或者树结构造成的访问速度的差距，基本上不需要考虑，因为这个应用不是对苛求速度的应用。
 * 
 * @author		Martin (付成睿)
 */
public class Discovery implements IDiscovery
{

	/**
	 * 所有的Container实例
	 */
	private List m_oContainers;

	/**
	 * 构造方法
	 */
	public Discovery()
	{
		m_oContainers = new ArrayList();
	}

	/**
	 * 添加Container实例
	 * 
	 * @param	container	指定的待添加的Container
	 */
	void addContainer(Container container)
	{
		m_oContainers.add(container);
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IDiscovery#cleanup()
	 */
	public void cleanup()
	{
		Iterator itr = m_oContainers.iterator();
		while (itr.hasNext())
		{
			Container c = (Container) itr.next();
			if (c != null)
			{
				c.cleanup();
			}
		}
		m_oContainers.clear();
		m_oContainers = null;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IDiscovery#tree(java.io.PrintWriter)
	 */
	public void tree(PrintWriter pw)
	{
		pw.println("com.trs.infra.discover.impl.Discovery");
		List list = m_oContainers;
		int limit = list.size() - 1;
		for (int i = 0; i <= limit; i++)
		{
			String p = null;
			Container c = (Container) list.get(i);
			if (i == limit)
			{
				pw.print("└─");
				p = "    ";
			}
			else
			{
				pw.print("├─");
				p = "│  ";
			}
			pw.println(c.getLocation());
			c.tree(pw, p);
		}
	}

	/**
	 * 分析其中的所有Java类型
	 */
	void analyze()
	{
		//装载所有的Java类型到HashMap
		Map mapTypes = new HashMap();
		List list = m_oContainers;
		int limit = list.size() - 1;
		for (int i = 0; i <= limit; i++)
		{
			Container c = (Container) list.get(i);
			Iterator itr = c.findTypes(null);
			while (itr.hasNext())
			{
				NodeType node = (NodeType) itr.next();
				NodeType olda = (NodeType) mapTypes.get(node.getQFName());
				if (olda != null)
				{
					olda.addCollision(node);
				}
				else
				{
					mapTypes.put(node.getQFName(), node);
				}
			}
		}
		//定义类型的实现和继承关系
		Iterator itrAllType = mapTypes.values().iterator();
		while (itrAllType.hasNext())
		{
			NodeType node = (NodeType) itrAllType.next();
			//处理父类
			String sQFSuperName = node.getQFSuperName();
			if (!isIgnoreType(sQFSuperName))
			{
				NodeType oSuperType = (NodeType) mapTypes.get(sQFSuperName);
				if (oSuperType != null)
				{
					oSuperType.addSubType(node);
				}
			}
			//处理接口
			Iterator itrQFIF = node.getQFInterfaces();
			while (itrQFIF.hasNext())
			{
				String sQFIF = (String) itrQFIF.next();
				if (isIgnoreType(sQFIF))
				{
					continue;
				}
				NodeType oIFType = (NodeType) mapTypes.get(sQFIF);
				if (oIFType != null)
				{
					oIFType.addImplementor(node);
				}
			}
		}
	}

	private boolean isIgnoreType(String name)
	{
		if (name.startsWith("java."))
		{
			return true;
		}
		return false;
	}

	/**
	 * 减少内存的占用
	 */
	void trimMemory()
	{
		Iterator itr = m_oContainers.iterator();
		while (itr.hasNext())
		{
			Container c = (Container) itr.next();
			if (c != null)
			{
				c.trimMemory();
			}
		}
		Utils.trimArrayList(m_oContainers);
	}

	private NodeResource getResourceByPath(String name)
	{
		String[] names = Container.splitPath(name);
		if (names == null || names.length <= 0)
		{
			throw new IllegalArgumentException("Invalid resource path:" + name);
		}
		List list = m_oContainers;
		int limitContainer = list.size() - 1;
		for (int i = 0; i <= limitContainer; i++)
		{
			Container c = (Container) list.get(i);
			Node n = c.findNode(names);
			if (n instanceof NodeResource)
			{
				return (NodeResource) n;
			}
		}
		return null;
	}

	private NodeResource getResourceByName(String name)
	{
		List list = m_oContainers;
		int limitContainer = list.size() - 1;
		for (int i = 0; i <= limitContainer; i++)
		{
			Container c = (Container) list.get(i);
			NodeResource n = c.findResource(name);
			if (n != null)
			{
				return n;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IDiscovery#getResource(java.lang.String)
	 */
	public URL getResource(String name)
	{
		NodeResource node = null;
		if (name.indexOf('/') >= 0)
		{
			node = getResourceByPath(name);
		}
		else
		{
			node = getResourceByName(name);
		}
		if (node != null)
		{
			return node.getURL();
		}
		return null;
	}

	private Iterator getResourcesByPath(String name)
	{
		final String[] names = Container.splitPath(name);
		if (names == null || names.length <= 0)
		{
			throw new IllegalArgumentException("Invalid resource path:" + name);
		}
		Iterator itr = new Iterator()
		{

			private int m_iIndex = 0;

			private URL m_oFound;

			public void findNext()
			{
				if (m_oFound != null)
				{
					return;
				}
				List list = m_oContainers;
				for (; m_iIndex < list.size();)
				{
					Container c = (Container) list.get(m_iIndex++);
					Node n = c.findNode(names);
					if (n instanceof NodeResource)
					{
						m_oFound = ((NodeResource) n).getURL();
						break;
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
					URL url = m_oFound;
					m_oFound = null;
					return url;
				}
				return null;
			}

			public void remove()
			{
			}

		};
		return itr;
	}

	private Iterator getResourcesByName(final String name)
	{
		Iterator itr = new IteratorUnion()
		{

			private int index = 0;

			protected Iterator nextIterator()
			{
				List list = m_oContainers;
				for (; index < list.size();)
				{
					Container c = (Container) list.get(index++);
					return c.findResources(name);
				}
				return null;
			}

			public Object next()
			{
				Object obj = super.next();
				if (obj != null)
				{
					return ((NodeResource) obj).getURL();
				}
				return null;
			}

		};
		return itr;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IDiscovery#getResources(java.lang.String)
	 */
	public Iterator getResources(String name)
	{
		if (name.indexOf('/') >= 0)
		{
			return getResourcesByPath(name);
		}
		
		return getResourcesByName(name);
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IDiscovery#getSubTypes(java.lang.String)
	 */
	public Iterator getSubTypes(String name)
	{
		name = name.replace('.', '/') + ".class";
		NodeResource node = getResourceByPath(name);
		if (node == null || !node.isJavaType())
		{
			//如果指定的资源不存在或者不是Java类型，则返回<code>null</code>
			return null;
		}
		NodeType typeNode = (NodeType) node;
		if (typeNode.isFinal())
		{
			//如果指定类型是final类型，也返回<code>null</code>
			return null;
		}

		//如果是类，则返回直接子类
		if (typeNode.isClass())
		{
			List list = typeNode.getSubTypes();
			if (list == null)
			{
				return IteratorEmpty.getEmptyIterator();
			}
			return new IteratorProxy(list.iterator())
			{

				public Object next()
				{
					Object obj = super.next();
					if (obj != null)
					{
						return ((NodeType) obj).getQFName();
					}
					return null;
				}

			};
		}

		//如果是接口，则要返回直接子接口和当前接口的直接实现
		if (typeNode.isInterface())
		{
			/*
			 * 其实不需要这么复杂
			 * 可能并不存在子接口这种说法，接口的继承其实也是采用实现接口的方式，
			 * 而接口的父类其实java.lang.Object或者null。
			 * 也就是说，对于类只存在子类型，对于接口只存在实现。
			 */
			final List listSub = typeNode.getSubTypes();
			final List listIFs = typeNode.getImplementors();
			if (listSub == null && listIFs == null)
			{
				return IteratorEmpty.getEmptyIterator();
			}
			Iterator itr = new Iterator()
			{

				int index0 = 0;

				int index1 = 0;

				List[] lists = new List[]
					{ listSub, listIFs };

				private NodeType m_oFound = null;

				private void findNext()
				{
					if (m_oFound != null)
					{
						return;
					}
					List list = null;
					for (; index0 < lists.length;)
					{
						list = lists[index0];
						if (list != null && index1 < list.size())
						{
							break;
						}
						index0++;
						index1 = 0;
						list = null;
					}
					if (list != null)
					{
						m_oFound = (NodeType) list.get(index1++);
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
						return nodetype.getQFName();
					}
					return null;
				}

				public void remove()
				{
				}

			};
			return itr;
		}

		// 其他的Java类型恒定返回空的迭代器
		return IteratorEmpty.getEmptyIterator();
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IDiscovery#getImplementors(java.lang.String)
	 */
	public Iterator getImplementors(String name)
	{
		name = name.replace('.', '/') + ".class";
		NodeResource resourceNode = getResourceByPath(name);
		if (resourceNode == null || !resourceNode.isJavaType())
		{
			//如果指定的资源不存在或者不是Java类型，则返回<code>null</code>
			return null;
		}
		final NodeType typeNode = (NodeType) resourceNode;
		if (typeNode.isFinal())
		{
			//如果指定类型是final类型，也返回<code>null</code>
			return null;
		}

		if (typeNode.isClass())
		{
			final List list = typeNode.getSubTypes();
			if (list == null)
			{
				return IteratorEmpty.getEmptyIterator();
			}
			//
			Iterator itr = new Iterator()
			{

				private DFTFrame m_oFrame = new DFTFrame(list);

				private Stack m_oStack = new Stack();

				private NodeType m_oFound = null;

				public void findNext()
				{
					if (m_oFound != null)
					{
						return;
					}
					while (m_oFrame != null)
					{
						while (m_oFrame.index < m_oFrame.list.size())
						{
							NodeType node = null;
							node = (NodeType) m_oFrame.list.get(m_oFrame.index++);
							if (node.hasSubType())
							{
								m_oFrame.node = node;
								m_oStack.push(m_oFrame);
								m_oFrame = new DFTFrame(node.getSubTypes());
							}
							else if (!node.isAbstract())
							{
								m_oFound = node;
								return;
							}
						}
						m_oFrame = null;
						if (m_oStack.size() > 0)
						{
							m_oFrame = (DFTFrame) m_oStack.pop();
						}
						if (m_oFrame != null && m_oFrame.node != null)
						{
							NodeType node = m_oFrame.node;
							m_oFrame.node = null;
							if (!node.isAbstract())
							{
								m_oFound = node;
								return;
							}
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
						return nodetype.getQFName();
					}
					return null;
				}

				public void remove()
				{
				}

			};
			return itr;
		}

		if (typeNode.isInterface())
		{
			final List listSub = typeNode.getSubTypes();
			final List listIFs = typeNode.getImplementors();
			if (listSub == null && listIFs == null)
			{
				return IteratorEmpty.getEmptyIterator();
			}
			/*
			 * 其实不需要这么复杂
			 * 可能并不存在子接口这种说法，接口的继承其实也是采用实现接口的方式，
			 * 而接口的父类其实java.lang.Object或者null。
			 * 也就是说，对于类只存在子类型，对于接口只存在实现。
			 */
			Iterator itr = new Iterator()
			{

				private DFTFrame m_oFrame = new DFTFrame(listSub, listIFs);

				private Stack m_oStack = new Stack();

				private NodeType m_oFound = null;

				private boolean prepareFrameList()
				{
					if (m_oFrame.list == null)
					{
						m_oFrame.list = m_oFrame.list2;
						m_oFrame.list2 = null;
						m_oFrame.index = 0;
					}
					if (m_oFrame.list == null)
					{
						return false;
					}
					if (m_oFrame.index < m_oFrame.list.size())
					{
						return true;
					}
					m_oFrame.list = null;
					return prepareFrameList();
				}

				public void findNext()
				{
					if (m_oFound != null)
					{
						return;
					}
					while (m_oFrame != null)
					{
						while (prepareFrameList())
						{
							NodeType node = null;
							node = (NodeType) m_oFrame.list.get(m_oFrame.index++);
							if (node.hasSubType() || node.hasImplementor())
							{
								m_oFrame.node = node;
								m_oStack.push(m_oFrame);
								m_oFrame = new DFTFrame(node.getSubTypes());
								m_oFrame.list2 = node.getImplementors();
							}
							else if (!node.isAbstract())
							{
								m_oFound = node;
								return;
							}
						}
						m_oFrame = null;
						if (m_oStack.size() > 0)
						{
							m_oFrame = (DFTFrame) m_oStack.pop();
						}
						if (m_oFrame != null && m_oFrame.node != null)
						{
							NodeType node = m_oFrame.node;
							m_oFrame.node = null;
							if (!node.isAbstract())
							{
								m_oFound = node;
								return;
							}
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
						return nodetype.getQFName();
					}
					return null;
				}

				public void remove()
				{
				}

			};
			return itr;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IDiscovery#getCollision()
	 */
	public Iterator getCollision()
	{
		final FilterType filter = new FilterType()
		{

			public boolean accept(NodeType node)
			{
				return node.hasCollision();
			}

		};
		Iterator itr = new IteratorUnion()
		{

			private int index = 0;

			protected Iterator nextIterator()
			{
				List list = m_oContainers;
				for (; index < list.size();)
				{
					Container c = (Container) list.get(index++);
					return c.findTypes(filter);
				}
				return null;
			}

			public Object next()
			{
				Object obj = super.next();
				if (obj != null)
				{
					return ((NodeType) obj).getQFName();
				}
				return null;
			}

		};
		return itr;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IDiscovery#getCollision(java.lang.String)
	 */
	public Iterator getCollision(String name)
	{
		name = name.replace('.', '/') + ".class";
		NodeResource node = getResourceByPath(name);
		if (node == null || !node.isJavaType())
		{
			//如果指定的资源不存在或者不是Java类型，则返回<code>null</code>
			return null;
		}
		NodeType typeNode = (NodeType) node;
		if (typeNode.hasCollision())
		{
			return new IteratorProxy(typeNode.getCollision())
			{

				public Object next()
				{
					Object obj = super.next();
					if (obj != null)
					{
						return ((IType) obj).getURL();
					}
					return null;
				}

			};
		}
		return IteratorEmpty.getEmptyIterator();
	}

	//高级的查找资源的方法

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IDiscovery#findResources(java.lang.String)
	 */
	public Iterator findResources(String pattern)
	{
		//TODO:实现查找资源的高级方法 findResources(String)
		return null;
	}

}