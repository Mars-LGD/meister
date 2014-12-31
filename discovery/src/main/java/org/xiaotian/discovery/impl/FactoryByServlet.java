/*
 * FactoryByServlet.java created on 2006-9-28 上午10:11:24 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.xiaotian.discovery.DiscoveryFactory;
import org.xiaotian.discovery.IDiscovery;
import org.xiaotian.discovery.utils.DiscoveryServletHelper;

/**
 * 从应用服务器环境构造<code>IDiscovery</code>的工厂类
 * 
 * @author		Martin (付成睿)
 */
public class FactoryByServlet extends DiscoveryFactory
{

	private final static String PATH_OF_WEBINF = "/WEB-INF/";

	private final static String NAME_OF_LIB = "lib";

	private final static String PATH_OF_LIB = "/WEB-INF/lib/";

	private final static String NAME_OF_CLASSES = "classes";

	private final static String PATH_OF_CLASSES = "/WEB-INF/classes/";

	private ServletContext m_oServletContext;

	private Pattern[] m_oIncludeLibrary;

	private Pattern[] m_oExcludeLibrary;

	private Pattern[] m_oIncludeDirectory;

	private Pattern[] m_oExcludeDirectory;

	public FactoryByServlet(ServletContext sc)
	{
		m_oServletContext = sc;
		makeIncludeExcludePattern(sc);
	}

	public IDiscovery build()
	{
		Discovery discovery = new Discovery();
		//装载并解析所有的jar文件
		List libs = listLibrary();
		if (libs != null && libs.size() > 0)
		{
			Iterator itr = libs.iterator();
			while (itr.hasNext())
			{
				String path = (String) itr.next();
				Container container = buidLibraryContainer(path);
				if (container != null)
				{
					discovery.addContainer(container);
				}
			}
		}
		//装载并解析所有的目录
		List dirs = listDirectory();
		if (dirs != null && dirs.size() > 0)
		{
			Iterator itr = dirs.iterator();
			while (itr.hasNext())
			{
				String path = (String) itr.next();
				Container container = buidDirectoryContainer(path);
				if (container != null)
				{
					discovery.addContainer(container);
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

	private Container buidDirectoryContainer(String path)
	{
		try
		{
			URL url = m_oServletContext.getResource(path);
			if (url == null)
			{
				return null;
			}
			ContainerByDir container = new ContainerByDir(url, path);
			recursionDirectory(container, path);
			return container;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private void recursionDirectory(ContainerByDir container, String path) throws IOException
	{
		Set set = m_oServletContext.getResourcePaths(path);
		if (set == null)
		{
			return;
		}
		Iterator itr = set.iterator();
		if (itr == null)
		{
			return;
		}
		while (itr.hasNext())
		{
			String subpath = (String) itr.next();
			if (isDirectory(subpath))
			{
				container.trimDirectory(subpath);
				recursionDirectory(container, subpath);
			}
			else
			{
				URL url = m_oServletContext.getResource(subpath);
				container.trimResourceAny(subpath, url);
			}
		}
	}

	private Container buidLibraryContainer(String path)
	{
		try
		{
			URL url = m_oServletContext.getResource(path);
			if (url == null)
			{
				return null;
			}
			ContainerByZip container = new ContainerByZip(url);
			container.load();
			return container;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private List listLibrary()
	{
		Set set = m_oServletContext.getResourcePaths(PATH_OF_LIB);
		if (set == null)
		{
			return null;
		}
		//
		Iterator itr = set.iterator();
		if (itr == null)
		{
			return null;
		}
		List list = new ArrayList(set.size());
		while (itr.hasNext())
		{
			String path = (String) itr.next();
			if (isDirectory(path))
			{
				continue;
			}
			String name = path.substring(PATH_OF_LIB.length());
			if (acceptLibrary(name))
			{
				list.add(path);
			}
		}
		return list;
	}

	private boolean isDirectory(String path)
	{
		return path.endsWith("/");
	}

	private List listDirectory()
	{
		if (m_oIncludeDirectory == null || m_oIncludeDirectory.length <= 0)
		{
			if (acceptDirectory(NAME_OF_CLASSES))
			{
				List list = new ArrayList(1);
				list.add(PATH_OF_CLASSES);
				return list;
			}
			return null;
		}
		//
		Set set = m_oServletContext.getResourcePaths(PATH_OF_WEBINF);
		if (set == null)
		{
			return null;
		}
		//
		Iterator itr = set.iterator();
		if (itr == null)
		{
			return null;
		}
		List list = new ArrayList(set.size());
		while (itr.hasNext())
		{
			String path = (String) itr.next();
			if (!isDirectory(path))
			{
				continue;
			}
			String name = path.substring(PATH_OF_WEBINF.length());
			name = name.substring(0, name.length() - 1);
			if (acceptDirectory(name))
			{
				list.add(path);
			}
		}
		return list;
	}

	private boolean acceptLibrary(String name)
	{
		return accept(m_oExcludeLibrary, m_oIncludeLibrary, name, true);
	}

	private boolean acceptDirectory(String name)
	{
		if (NAME_OF_LIB.equals(name))
		{
			return false;
		}
		boolean def = NAME_OF_CLASSES.equals(name);
		return accept(m_oExcludeDirectory, m_oIncludeDirectory, name, def);
	}

	private boolean accept(Pattern[] ex, Pattern[] in, String name, boolean def)
	{
		if (ex != null && ex.length > 0)
		{
			for (int i = 0; i < ex.length; i++)
			{
				if (ex[i].matcher(name).matches())
				{
					return false;
				}
			}
		}
		if (in != null && in.length > 0)
		{
			for (int i = 0; i < in.length; i++)
			{
				if (in[i].matcher(name).matches())
				{
					return true;
				}
			}
			return false;
		}
		return def;
	}

	private void makeIncludeExcludePattern(ServletContext sc)
	{
		String s = null;
		//
		s = sc.getInitParameter(DiscoveryServletHelper.IncludeLibrary);
		m_oIncludeLibrary = makeIncludeExcludePattern(s);
		if (m_oIncludeLibrary == null || m_oIncludeLibrary.length <= 0)
		{
			m_oIncludeLibrary = makeIncludeExcludePattern("*.jar,*.zip");
		}
		//
		s = sc.getInitParameter(DiscoveryServletHelper.ExcludeLibrary);
		m_oExcludeLibrary = makeIncludeExcludePattern(s);
		//
		s = sc.getInitParameter(DiscoveryServletHelper.IncludeDirectory);
		m_oIncludeDirectory = makeIncludeExcludePattern(s);
		//
		s = sc.getInitParameter(DiscoveryServletHelper.ExcludeDirectory);
		m_oExcludeDirectory = makeIncludeExcludePattern(s);
	}

	private Pattern[] makeIncludeExcludePattern(String regexList)
	{
		if (regexList == null)
		{
			return null;
		}
		List list = new ArrayList();
		StringTokenizer stRegex = new StringTokenizer(regexList, ",");
		while (stRegex.hasMoreTokens())
		{
			String regex = stRegex.nextToken().trim();
			if (regex.length() <= 0)
			{
				continue;
			}
			regex = escapeStar2Regex(regex);
			list.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
		}
		if (list.size() <= 0)
		{
			return null;
		}
		Pattern[] p = new Pattern[list.size()];
		list.toArray(p);
		return p;
	}

	private String escapeStar2Regex(String src)
	{
		StringBuffer sb = new StringBuffer(src.length() * 2);
		int length = src.length();
		for (int i = 0; i < length; i++)
		{
			char c = src.charAt(i);
			switch (c)
			{
				case '*':
					sb.append(".*");
					break;
				case '.':
					sb.append("\\.");
					break;
				default:
					sb.append(c);
					break;
			}
		}
		return sb.toString();
	}

}