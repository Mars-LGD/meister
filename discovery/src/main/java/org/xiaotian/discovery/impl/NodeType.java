/*
 * NodeType.java created on 2006-9-29 下午02:46:08 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xiaotian.asm.Opcodes;
import org.xiaotian.asm.tree.ClassNode;
import org.xiaotian.discovery.IType;

/**
 * 类型节点，实现IType，继承自NodeResource
 * 
 * @author		Martin (付成睿)
 */
public class NodeType extends NodeResource implements IType, Opcodes
{

	/**
	 * 类型的修饰掩码
	 */
	private int m_iAccessFlags;

	/**
	 * Java类型的版本
	 */
	private int m_iTypeVersion;

	private String m_sQFName;

	private String m_sQFSuperName;

	private List m_sQFInterfaces;

	private List m_oSubTypes;

	private List m_oImplementors;

	private List m_oCollisions;

	/**
	 * 构造方法
	 */
	public NodeType(String name, URL url, ClassNode cn)
	{
		super(name, url);
		init(cn);
	}

	private void init(ClassNode cn)
	{
		m_iAccessFlags = cn.access;
		m_iTypeVersion = cn.version;
		m_sQFName = cn.name.replace('/', '.');
		m_sQFSuperName = cn.superName.replace('/', '.');
		List src = cn.interfaces;
		if (src != null && src.size() > 0)
		{
			List dst = new ArrayList(src.size());
			for (int i = 0; i < src.size(); i++)
			{
				dst.add(((String) src.get(i)).replace('/', '.').intern());
			}
			m_sQFInterfaces = dst;
		}
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append(",QFName=").append(m_sQFName);
		return sb.toString();
	}

	public void cleanup()
	{
		super.cleanup();
		m_sQFName = null;
		m_sQFSuperName = null;
		if (m_sQFInterfaces != null)
		{
			m_sQFInterfaces.clear();
			m_sQFInterfaces = null;
		}
		if (m_oSubTypes != null)
		{
			m_oSubTypes.clear();
			m_oSubTypes = null;
		}
		if (m_oImplementors != null)
		{
			m_oImplementors.clear();
			m_oImplementors = null;
		}
		if (m_oCollisions != null)
		{
			m_oCollisions.clear();
			m_oCollisions = null;
		}
	}

	protected void trimMemory()
	{
		super.trimMemory();
		m_sQFName = m_sQFName.intern();
		if (m_sQFSuperName != null)
		{
			m_sQFSuperName = m_sQFSuperName.intern();
		}
		Utils.trimArrayList(m_sQFInterfaces);
		Utils.trimArrayList(m_oSubTypes);
		Utils.trimArrayList(m_oImplementors);
		Utils.trimArrayList(m_oCollisions);
	}

	public boolean isJavaType()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IType#getQFName()
	 */
	public String getQFName()
	{
		return m_sQFName;
	}

	public String getQFSuperName()
	{
		return m_sQFSuperName;
	}

	public Iterator getQFInterfaces()
	{
		List list = m_sQFInterfaces;
		if (list == null || list.size() <= 0)
		{
			return IteratorEmpty.getEmptyIterator();
		}
		return new IteratorProxy(list.iterator());
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IType#getVersion()
	 */
	public int getVersion()
	{
		return m_iTypeVersion;
	}

	public boolean isPublic()
	{
		return (m_iAccessFlags & ACC_PUBLIC) != 0;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IType#isEnum()
	 */
	public boolean isEnum()
	{
		return (m_iAccessFlags & ACC_ENUM) != 0;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IType#isClass()
	 */
	public boolean isClass()
	{
		final int notClass = ACC_ANNOTATION | ACC_ENUM | ACC_INTERFACE;
		return (m_iAccessFlags & notClass) == 0;
	}

	public boolean isFinal()
	{
		return (m_iAccessFlags & ACC_FINAL) != 0;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IType#isAbstract()
	 */
	public boolean isAbstract()
	{
		return (m_iAccessFlags & ACC_ABSTRACT) != 0;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IType#isInterface()
	 */
	public boolean isInterface()
	{
		return (m_iAccessFlags & ACC_INTERFACE) != 0;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IType#isAnnotation()
	 */
	public boolean isAnnotation()
	{
		return (m_iAccessFlags & ACC_ANNOTATION) != 0;
	}

	void addCollision(NodeType type)
	{
		if (this.m_oCollisions == null)
		{
			this.m_oCollisions = type.m_oCollisions = new ArrayList();
			this.m_oCollisions.add(this);
		}
		this.m_oCollisions.add(type);
	}

	public boolean hasCollision()
	{
		return (m_oCollisions != null && m_oCollisions.size() > 0);
	}

	public Iterator getCollision()
	{
		if (m_oCollisions == null)
		{
			return IteratorEmpty.getEmptyIterator();
		}
		return new IteratorProxy(m_oCollisions.iterator());
	}

	void addSubType(NodeType type)
	{
		if (this.m_oSubTypes == null)
		{
			this.m_oSubTypes = new ArrayList();
		}
		this.m_oSubTypes.add(type);
	}

	boolean hasSubType()
	{
		return m_oSubTypes != null && m_oSubTypes.size() > 0;
	}

	List getSubTypes()
	{
		return m_oSubTypes;
	}

	void addImplementor(NodeType type)
	{
		if (this.m_oImplementors == null)
		{
			this.m_oImplementors = new ArrayList();
		}
		this.m_oImplementors.add(type);
	}

	boolean hasImplementor()
	{
		return m_oImplementors != null && m_oImplementors.size() > 0;
	}

	List getImplementors()
	{
		return m_oImplementors;
	}

}