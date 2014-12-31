/*
 * Utils.java created on 2006-10-11 下午01:31:10 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用工具类
 * 
 * @author		Martin (付成睿)
 */
public class Utils
{

	/**
	 * 不得外部实例化
	 */
	private Utils()
	{
	}

	/**
	 * 缩减ArrayList的容量为当前大小
	 * 
	 * @param	list	指定的ArrayList
	 */
	public final static void trimArrayList(List list)
	{
		if (list != null && list instanceof ArrayList)
		{
			((ArrayList) list).trimToSize();
		}
	}

}