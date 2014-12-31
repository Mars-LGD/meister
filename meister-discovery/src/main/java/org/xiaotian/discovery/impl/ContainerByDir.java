/*
 * ContainerByDir.java created on 2006-9-29 上午11:03:48 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.net.URL;

import org.xiaotian.asm.tree.ClassNode;

public class ContainerByDir extends Container
{

	private String m_sTrimPrefix;

	public ContainerByDir(URL location, String sTrimPrefix)
	{
		super(location);
		m_sTrimPrefix = sTrimPrefix;
	}

	void trimDirectory(String path)
	{
		addDirectory(trimPrefix(path));
	}

	void trimResourceAny(String path, URL url)
	{
		path = trimPrefix(path);
		if (!path.endsWith(".class"))
		{
			addResource(path, url);
			return;
		}
		ClassNode cn = ASMUtil.getNodeFromURL(url);
		if (cn == null)
		{
			addResource(path, url);
			return;
		}
		addType(path, cn, url);
	}

	private void addResource(String path, URL url)
	{
		String[] names = splitPath(path);
		int count = names.length;
		if (count <= 0)
		{
			return;
		}
		if (count == 1)
		{
			addRootNode(newResource(names[0], path, url));
			return;
		}
		NodeDirectory parent = getRootDirectory(names[0]);
		int limit = count - 1;
		for (int i = 1; i < limit; i++)
		{
			parent = parent.getDirectoryNode(names[i]);
		}
		parent.addChild(newResource(names[limit], path, url));
	}

	private NodeResource newResource(String name, String path, URL url)
	{
		return new NodeResource(name, url);
	}

	private void addType(String path, ClassNode cn, URL url)
	{
		String[] names = splitPath(path);
		int count = names.length;
		if (count <= 0)
		{
			return;
		}
		if (count == 1)
		{
			addRootNode(newType(names[0], path, cn, url));
			return;
		}
		NodeDirectory parent = getRootDirectory(names[0]);
		int limit = count - 1;
		for (int i = 1; i < limit; i++)
		{
			parent = parent.getDirectoryNode(names[i]);
		}
		parent.addChild(newType(names[limit], path, cn, url));
	}

	private NodeType newType(String name, String path, ClassNode cn, URL url)
	{
		return new NodeType(name, url, cn);
	}

	private String trimPrefix(String path)
	{
		if (m_sTrimPrefix == null)
		{
			return path;
		}
		return path.substring(m_sTrimPrefix.length());
	}

}