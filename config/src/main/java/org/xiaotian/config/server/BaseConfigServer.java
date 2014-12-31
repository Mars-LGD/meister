package org.xiaotian.config.server;

import org.xiaotian.base.server.BaseServer;


public abstract class BaseConfigServer extends BaseServer {
    protected String m_sConfigPath = null;

    public BaseConfigServer() {
    	super();
    }

    /**
     * @return
     */
    public String getConfigPath() {
        return m_sConfigPath;
    }

    /**
     * @param configPath
     */
    public void setConfigPath(String configPath) {
        m_sConfigPath = configPath;
    }

}