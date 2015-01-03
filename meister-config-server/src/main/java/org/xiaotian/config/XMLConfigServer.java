package org.xiaotian.config;

import java.util.List;
import java.util.Map;

import org.xiaotian.base.server.BaseServer;
import org.xiaotian.exception.WCMException;

/**
 * 对可配置对象的顶层管理类
 * 
 * @author xiaotian15
 * 
 */
public class XMLConfigServer extends BaseServer {
	protected static org.apache.log4j.Logger s_logger = org.apache.log4j.Logger
			.getLogger(XMLConfigServer.class);

	/**
	 * 加载所有数据的装载器
	 */
	private ConfigObjectLoader m_oConfigObjectLoader;

	private static XMLConfigServer itsInstance = new XMLConfigServer();

	/**
	 * @return 单体引用器
	 */
	public static XMLConfigServer getInstance() {
		if (!itsInstance.isStarted()) {
			itsInstance.startup();
			if (!itsInstance.isStarted()) {
				throw new RuntimeException("Config Server startup Error!"
						+ itsInstance.getErrors());
			}
		}

		return itsInstance;
	}

	private XMLConfigServer() {
		super();
		m_oConfigObjectLoader = new ConfigObjectLoader();
	}

	/**
	 * 依据可配置对象类型，得到对应的配置信息
	 * 
	 * @param _cfgObjType
	 *            可配置对象的类型
	 * @return 一个ICfgEntity的数组，可以通过类型转换得到具体的可配置对象
	 */
	@SuppressWarnings("rawtypes")
	public List getConfigObjects(Class _cfgObjType) {
		return m_oConfigObjectLoader.getExtensionalObjects(_cfgObjType);
	}

	/**
	 * 
	 * @see com.trs.infra.BaseServer#doShutdown()
	 */
	protected void doShutdown() {
		m_oConfigObjectLoader.clear();
	}

	/**
	 * 
	 * @see com.trs.infra.BaseServer#doStart()
	 */
	protected boolean doStart() throws WCMException {
		try {
			if (this.m_bIsStarted)
				return false;
			m_oConfigObjectLoader.load();
			return true;
		} catch (Exception ex) {
			throw new WCMException("初始化配置服务器失败", ex);
		}
	}

	/**
	 * 获取<PluginConfig.getHostFilePath(), PluginConfig>散列集
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getPluginMap() {
		return m_oConfigObjectLoader.getPluginMap();
	}
}