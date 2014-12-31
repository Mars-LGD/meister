/*
 * FactoryByFiles.java created on 2006-9-28 上午10:14:44 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.xiaotian.discovery.DiscoveryFactory;
import org.xiaotian.discovery.IDiscovery;

/**
 * 从文件系统创建<code>IDiscovery</code>的工厂类
 * 
 * @author		Martin (付成睿)
 */
public class FactoryByFiles extends DiscoveryFactory
{

	private File[] m_oInitLibs;

	private File[] m_oInitDirs;

	public FactoryByFiles(File[] libs, File[] dirs)
	{
		m_oInitLibs = libs;
		m_oInitDirs = dirs;
	}

	public IDiscovery build()
	{
		Discovery discovery = new Discovery();
		//装载并解析所有的jar文件
		if (m_oInitLibs != null)
		{
			for (int i = 0; i < m_oInitLibs.length; i++)
			{
				File path = m_oInitLibs[i];
				if (path != null && path.exists())
				{
					recursionLibrary(discovery, path);
				}
			}
		}
		//装载并解析所有的目录
		if (m_oInitDirs != null)
		{
			for (int i = 0; i < m_oInitDirs.length; i++)
			{
				File path = m_oInitDirs[i];
				if (path != null && path.exists() && path.isDirectory())
				{
					buidDirectoryContainer(discovery, path);
				}
			}
		}
		//分析所有的类型
		discovery.analyze();
		//减少内存的使用
		discovery.trimMemory();
		//返回
		return discovery;
	}

	private void buidDirectoryContainer(Discovery discovery, File path)
	{
		try
		{
			//URL url = path.toURL();
			URL url = path.toURI().toURL();
			String prefixpath = path.getAbsolutePath().replace('\\', '/');
			ContainerByDir container = new ContainerByDir(url, prefixpath);
			recursionDirectory(container, path);
			discovery.addContainer(container);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void recursionDirectory(ContainerByDir container, File path) throws IOException
	{
		File[] files = path.listFiles();
		if (files == null || files.length <= 0)
		{
			return;
		}
		for (int i = 0; i < files.length; i++)
		{
			File file = files[i];
			String subpath = file.getAbsolutePath().replace('\\', '/');
			if (file.isDirectory())
			{
				container.trimDirectory(subpath);
				recursionDirectory(container, file);
			}
			else
			{
				URL url = file.toURI().toURL();
				container.trimResourceAny(subpath, url);
			}
		}
	}

	private void recursionLibrary(Discovery discovery, File path)
	{
		if (path.isDirectory())
		{
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				recursionLibrary(discovery, files[i]);
			}
		}
		else
		{
			String name = path.getName().toLowerCase();
			if (name.endsWith(".jar") || name.endsWith(".zip"))
			{
				buidLibraryContainer(discovery, path);
			}
		}
	}

	private void buidLibraryContainer(Discovery discovery, File path)
	{
		try
		{
			ContainerByZip container = new ContainerByZip(path.toURI().toURL());
			container.load();
			discovery.addContainer(container);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}