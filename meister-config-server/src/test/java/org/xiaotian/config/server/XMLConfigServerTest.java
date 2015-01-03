package org.xiaotian.config.server;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.xiaotian.config.BeanConfig;
import org.xiaotian.config.ConfigConstants;
import org.xiaotian.config.XMLConfigServer;

public class XMLConfigServerTest {

	@Test
	public void testGetConfigObjectsClass() {
		XMLConfigServer server = XMLConfigServer.getInstance();
		@SuppressWarnings("unchecked")
		List<BeanConfig> list = server.getConfigObjects(BeanConfig.class);
		for (BeanConfig bean : list) {
			System.out.println(bean.getClassName());
		}
		System.out.println(ConfigConstants.getRootMappingFilePath());
	}

	@Test
	public void testGetConfigObjectsString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPluginMap() {
		fail("Not yet implemented");
	}

}
