/*
 * NodeResource.java created on 2006-9-29 下午02:45:16 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.impl;

import java.net.URL;

import org.xiaotian.discovery.IResource;

/**
 * 资源节点，实现IResource，继承自Node
 * 
 * @author		Martin (付成睿)
 */
public class NodeResource extends Node implements IResource
{

	private URL m_oLocation;

	public NodeResource(String name, URL url)
	{
		super(name);
		m_oLocation = url;
	}

	public void cleanup()
	{
		super.cleanup();
		m_oLocation = null;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IResource#getURL()
	 */
	public URL getURL()
	{
		return m_oLocation;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IResource#isJavaType()
	 */
	public boolean isJavaType()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see com.trs.infra.discover.IResource#isReadOnly()
	 */
	public boolean isReadOnly()
	{
		return true;
	}

}