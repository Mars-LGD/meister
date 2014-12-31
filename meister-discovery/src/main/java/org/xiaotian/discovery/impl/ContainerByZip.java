/*
 * ContainerByZip.java created on 2006-9-29 上午09:43:12 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.xiaotian.asm.tree.ClassNode;

public class ContainerByZip extends Container
{

	private boolean m_zEasyJointURL;

	public ContainerByZip(URL location)
	{
		super(location);
		m_zEasyJointURL = "file".equals(location.getProtocol());
	}

	public void load() throws IOException
	{
		InputStream is = null;
		URL url = getLocation();
		try
		{
			ZipEntry ze = null;
			is = url.openStream();
			byte[] buffer = new byte[4096];
			ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
			ZipInputStream zis = new ZipInputStream(is);
			while ((ze = zis.getNextEntry()) != null)
			{
				String zeName = ze.getName();
				if (ze.isDirectory())
				{
					addDirectory(zeName);
					continue;
				}
				int length = 0;
				baos.reset();
				while ((length = zis.read(buffer)) != -1)
				{
					baos.write(buffer, 0, length);
				}
				byte[] zeData = baos.toByteArray();
				if (!ze.getName().endsWith(".class"))
				{
					addResource(zeName, zeData);
					continue;
				}
				ClassNode cn = ASMUtil.getNodeFromData(zeData);
				if (cn == null)
				{
					addResource(zeName, zeData);
					continue;
				}
				addType(zeName, cn);
			}
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (Exception e)
				{
					// Ignore
				}
			}
		}
	}

	private void addResource(String path, byte[] data)
	{
		String[] names = splitPath(path);
		int count = names.length;
		if (count <= 0)
		{
			return;
		}
		if (count == 1)
		{
			addRootNode(newResource(names[0], path, data));
			return;
		}
		NodeDirectory parent = getRootDirectory(names[0]);
		int limit = count - 1;
		for (int i = 1; i < limit; i++)
		{
			parent = parent.getDirectoryNode(names[i]);
		}
		parent.addChild(newResource(names[limit], path, data));
	}

	private NodeResource newResource(String name, String path, byte[] data)
	{
		URL url = null;
		if (m_zEasyJointURL)
		{
			try
			{
				url = new URL("jar:" + getLocation().toString() + "!/" + path);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return new NodeResource(name, url);
	}

	private void addType(String path, ClassNode cn)
	{
		String[] names = splitPath(path);
		int count = names.length;
		if (count <= 0)
		{
			return;
		}
		if (count == 1)
		{
			addRootNode(newType(names[0], path, cn));
			return;
		}
		NodeDirectory parent = getRootDirectory(names[0]);
		int limit = count - 1;
		for (int i = 1; i < limit; i++)
		{
			parent = parent.getDirectoryNode(names[i]);
		}
		parent.addChild(newType(names[limit], path, cn));
	}

	private NodeType newType(String name, String path, ClassNode cn)
	{
		URL url = null;
		if (m_zEasyJointURL)
		{
			try
			{
				url = new URL("jar:" + getLocation().toString() + "!/" + path);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return new NodeType(name, url, cn);
	}

}