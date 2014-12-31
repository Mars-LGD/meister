package org.xiaotian.base.server;

import org.apache.log4j.Logger;
import org.xiaotian.exception.WCMException;
import org.xiaotian.extend.CMyErrors;
import org.xiaotian.extend.CMyException;
import org.xiaotian.language.I18NMessage;

/**
 * Title: TRS 内容协作平台（TRS WCM） <BR>
 * Description: <BR>
 * Server基类 <BR>
 * Copyright: Copyright (c) 2004-2005 TRS信息技术有限公司 <BR>
 * Company: TRS信息技术有限公司(www.trs.com.cn) <BR>
 * 
 * @author TRS信息技术有限公司
 * @version 1.0
 */

public abstract class BaseServer implements IServer {
    /** 版本信息 */
    public final static String APP_VERSION = "5.1"; // 版本信息

    /** 系统内部版本号 */
    public String APP_BUILD = "1070"; // 系统内部版本号

    /** 当前连接的数据库版本信息 */
    public String DB_BUILD = "1070"; // 当前连接的数据库版本信息

    /** Build时间 */
    public String APP_BUILD_TIME = "2005-02-23"; // Build时间

    /** logger */
    private final static Logger logger = Logger.getLogger(BaseServer.class
            .getName());

    // Server状态信息
    protected boolean m_bIsStarted = false; // Server是已启动

    protected boolean m_bIsStarting = false;

    protected CMyErrors errors = new CMyErrors(); // Server启动的错误信息记录

    protected String m_sAppName = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.support.IServer#addError(java.lang.String)
     */
    protected void addError(String _sError) {
        errors.add(_sError);
    }

    protected void addError(CMyErrors _errors) {
        errors.add(_errors);
    }

    protected void addError(String _sInfo, Exception _errors) {
        errors.add(_sInfo, _errors);
    }

    /**
     * 添加异常信息
     * 
     * @param _ex
     *            异常
     */
    protected void addError(Exception _ex) {
        errors.add(_ex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.support.IServer#getErrors()
     */
    public CMyErrors getErrors() {
        return errors;
    }

    /**
     * 判断Server是否已经启动
     * 
     * @return
     */
    public boolean isStarted() {
        return this.m_bIsStarted;
    }

    /**
     * @return <code>true</code> :Server正在启动
     *         <P>
     *         <code>false</code> :Server不在启动过程中
     *         <P>
     */
    public boolean isStarting() {
        return m_bIsStarting;
    }

    /**
     * 设置Server是否正在启动
     * 
     * @param isStarting
     *            <code>true</code> :Server正在启动
     *            <P>
     *            <code>false</code> :Server不在启动过程中
     *            <P>
     */
    protected void setStarting(boolean isStarting) {
        m_bIsStarting = isStarting;
    }

    public boolean startup() {
        if (this.isStarted()) {
            logger.info("["
                    + getServerName()
                    + I18NMessage.get(BaseServer.class, "BaseServer.label1",
                            "]已经启动了!"));
            return true;
        }

        if (this.isStarting()) {
            logger.info("["
                    + getServerName()
                    + I18NMessage.get(BaseServer.class, "BaseServer.label2",
                            "]正在启动..."));
            return false;
        }

        synchronized (this) {
            if (this.isStarted()) {
                return true;
            }

            this.setStarting(true);
            try {
                if (!this.doStart()) {
                    logger.info("[" + getServerName() + "]启动失败!");
                    return false;
                }
            } catch (Throwable ex) {
                this.addError(CMyException.getStackTraceText(ex));
                logger.error("["
                        + getServerName()
                        + I18NMessage.get(BaseServer.class,
                                "BaseServer.label3", "]启动失败!"), ex);
                return false;
            } finally {
                this.setStarting(false);
            }
            setStarted(true);
        }

        return true;
    }

    public synchronized void shutdown() {
        if (!this.isStarted())
            return;

        this.doShutdown();

        // 设置当前Server状态
        this.setStarted(false);
        logger.info("["
                + getServerName()
                + I18NMessage
                        .get(BaseServer.class, "BaseServer.label4", "]关闭!"));
    }

    /**
     * @param isStarted
     */
    protected void setStarted(boolean isStarted) {
        m_bIsStarted = isStarted;
    }

    public synchronized boolean restart() {
        if (!this.isStarted())
            return false;

        this.shutdown();
        return this.startup();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.infra.IServer#getAppName()
     */
    public String getServerName() {
        if (m_sAppName == null)
            return this.getClass().getName();

        return m_sAppName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.infra.IServer#setAppName(java.lang.String)
     */
    public void setServerName(String _sAppName) {
        m_sAppName = _sAppName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.infra.IServer#getAppBuild()
     */
    public String getServerBuild() {
        return APP_BUILD;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.infra.IServer#getBuildTime()
     */
    public String getBuildTime() {
        return APP_BUILD_TIME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.infra.IServer#getDbBuild()
     */
    public String getDbBuild() {
        return DB_BUILD;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.infra.IServer#getVersion()
     */
    public String getVersion() {
        return APP_VERSION;
    }

    protected abstract void doShutdown();

    protected abstract boolean doStart() throws WCMException;
}