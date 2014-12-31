/*
 * DFTFrame.java created on 2006-10-9 下午02:06:45 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.util.List;

/**
 * DepthFirstTravesal Frame
 * 
 * @author		Martin (付成睿)
 */
class DFTFrame
{

	int index;

	List list;

	List list2;

	NodeType node;

	public DFTFrame(List list0)
	{
		this.index = 0;
		this.list = list0;
		this.list2 = null;
		this.node = null;
	}

	public DFTFrame(List list0, List list1)
	{
		this.index = 0;
		this.list = list0;
		this.list2 = list1;
		this.node = null;
	}

}