/*
 * ASMUtil.java created on 2006-9-29 下午07:37:17 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.io.InputStream;
import java.net.URL;

import org.xiaotian.asm.ClassReader;
import org.xiaotian.asm.tree.ClassNode;

/**
 * 使用org.objectweb.asm库分析Java类型
 * 
 * @author		Martin (付成睿)
 */
public class ASMUtil
{

	public final static ClassNode getNodeFromData(byte[] data)
	{
		try
		{
			ClassNode cn = new ClassNode();
			new ClassReader(data).accept(cn, true);
			return cn;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public final static ClassNode getNodeFromURL(URL url)
	{
		InputStream is = null;
		try
		{
			is = url.openStream();
			ClassNode cn = new ClassNode();
			new ClassReader(is).accept(cn, true);
			return cn;
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
		return null;
	}

	/**
	 * 不得外部实例化
	 */
	private ASMUtil()
	{
	}

}