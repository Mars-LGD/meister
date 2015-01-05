package org.xiaotian.config.server;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.xiaotian.config.BeanConfig;
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
