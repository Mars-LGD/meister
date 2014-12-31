package org.xiaotian.config.server;

import java.util.List;
import java.util.Map;

import org.xiaotian.base.server.BaseServer;
import org.xiaotian.config.ConfigObjectLoader;
import org.xiaotian.exception.WCMException;
import org.xiaotian.language.I18NMessage;

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
    public List getConfigObjects(Class _cfgObjType) {
        return m_oConfigObjectLoader.getExtensionalObjects(_cfgObjType);
    }

    /**
     * 依据可配置对象在配置文件中的ID，得到对应的配置信息
     * 
     * @param _sExtCfgId
     *            可配置对象在配置文件中的ID
     * @return 一个IConfigElement的数组，可以通过类型转换得到具体的可配置对象
     */
    public List getConfigObjects(String _sExtCfgId) {
        return m_oConfigObjectLoader.getExtensionalObjects(_sExtCfgId);
    }

    // ==================================================================
    // implement BaseServer interface
    /*
     * (non-Javadoc)
     * 
     * @see com.trs.infra.BaseServer#doShutdown()
     */
    protected void doShutdown() {
        m_oConfigObjectLoader.clear();
    }

    /*
     * (non-Javadoc)
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
            throw new WCMException(I18NMessage.get(XMLConfigServer.class,
                    "XMLConfigServer.label1", "初始化配置服务器失败"), ex);
        }
    }

    public Map getPluginMap() {
        return m_oConfigObjectLoader.getPluginMap();
    }
}