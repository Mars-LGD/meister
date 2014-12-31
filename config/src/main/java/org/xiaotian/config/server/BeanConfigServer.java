/**
 * Created:         2005-6-3 11:24:10
 * Last Modified:   2005-6-3/2005-6-3
 * Description:
 *      class BeanConfigServer
 */
package org.xiaotian.config.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exolab.castor.mapping.Mapping;
import org.xiaotian.base.server.BaseServer;
import org.xiaotian.config.ConfigFilesFinder;
import org.xiaotian.config.ConfigHelper;
import org.xiaotian.config.ConfigObjectLoader;
import org.xiaotian.config.bean.BeanConfig;
import org.xiaotian.config.bean.ConfigConstants;
import org.xiaotian.config.bean.ConfigFiles;
import org.xiaotian.config.bean.PluginConfig;
import org.xiaotian.exception.WCMException;
import org.xiaotian.extend.CMyException;
import org.xiaotian.language.I18NMessage;

/**
 * Title: TRS ����Э��ƽ̨��TRS WCM�� <BR>
 * Description: <BR>
 * ΪSpringBean�������ṩ���� <BR>
 * Copyright: Copyright (c) 2004-2005 TRS��Ϣ�������޹�˾ <BR>
 * Company: TRS��Ϣ�������޹�˾(www.trs.com.cn) <BR>
 * 
 * @author TRS��Ϣ�������޹�˾
 * @version 1.0
 */

public class BeanConfigServer extends BaseServer {
    private ConfigFilesFinder m_oConfigFileFinder;

    /**
     * <BeanId : String, BeanClassName : String>
     */
    private Map m_hmBeanIdMap;

    /**
     * PluginId : String, BeanClassName : String>
     */
    private Map m_hmPluginIdMap;

    /**
     * SpringBean Config List [BeanConfig]
     */
    private List m_listBeanConfigCollection = null;

    private static BeanConfigServer itsInstance = new BeanConfigServer();

    private BeanConfigServer() {
        // m_oConfigFileFinder = new ConfigFilesFinder(
        // ConfigConstants.DIR_APPROOT, ConfigConstants.NAME_FILE_BEAN);

        // ��Ӧ��Ŀ���������
        // comment by caohui@2008-7-23 ����01:50:10
        // m_oConfigFileFinder = new ConfigFilesFinder(ConfigConstants
        // .getConfigRootPath(), ConfigConstants.NAME_FILE_BEAN);

        m_oConfigFileFinder = new ConfigFilesFinder(ConfigHelper
                .getDomainConfigPaths(), ConfigConstants.NAME_FILE_BEAN);

        m_hmBeanIdMap = new HashMap();
        m_hmPluginIdMap = new HashMap();
        m_listBeanConfigCollection = new ArrayList();
    }

    /**
     * @return ����������
     * @throws CMyException
     */
    public static BeanConfigServer getInstance() {
        if (itsInstance == null) {
            itsInstance = new BeanConfigServer();
        }

        if (!itsInstance.isStarted()) {
            // DCL
            synchronized (itsInstance) {
                if (itsInstance.isStarted()) {
                    return itsInstance;
                }

                itsInstance.startup();
                if (!itsInstance.isStarted()) {
                    throw new RuntimeException(
                            "Bean Config Server startup Error!"
                                    + itsInstance.getErrors());
                }
            }

        }

        return itsInstance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.infra.BaseServer#doShutdown()
     */
    protected void doShutdown() {
        m_hmBeanIdMap.clear();
        m_hmPluginIdMap.clear();
        m_listBeanConfigCollection.clear();

        if (m_oConfigFileFinder != null)
            m_oConfigFileFinder = null;
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

            load();
            return true;
        } catch (Exception ex) {
            throw new WCMException(I18NMessage.get(BeanConfigServer.class,
                    "BeanConfigServer.label1", "��ʼ�����÷�����ʧ��"), ex);
        }
    }

    /**
     * �õ�����Bean�ļ���[BeanConfig]
     * 
     * @return ����Bean�ļ���[BeanConfig]
     */
    public List getBeanConfigCollection() {
        return m_listBeanConfigCollection;
    }

    /**
     * �õ�ָ��Id��Plugin�µ�Bean�ļ���[BeanConfig]
     * 
     * @param _sPluginId
     *            ָ��Plugin��id��ʶ
     * @return
     */
    public List getBeanConfigCollection(String _sPluginId) {
        return (List) m_hmPluginIdMap.get(_sPluginId);
    }

    void load() throws CMyException {
        ConfigFiles cfgFiles = m_oConfigFileFinder.getConfigFiles();

        Mapping map = new Mapping();
        try {
            // map.loadMapping(m_oConfigFileFinder
            // .getMappingSource(ConfigConstants.PATH_FILE_MAPPING_CORE));
            map.loadMapping(ConfigHelper.getMappingSource(ConfigConstants
                    .getAbsoluteFileNameOfCoreMapping()));

        } catch (Exception ex) {
            throw new RuntimeException(I18NMessage.get(BeanConfigServer.class,
                    "BeanConfigServer.label2", "װ��Castor��mapping�ļ�ʱ����:")
                    + ex.getMessage(), ex);
        }

        for (int nConfigFileIndex = 0; nConfigFileIndex < cfgFiles.size(); nConfigFileIndex++) {
            try {

                File fPlugin = cfgFiles.get(nConfigFileIndex).getPlugin();
                PluginConfig cfgObj = ConfigObjectLoader.loadUnmarshallerRoot(
                        fPlugin, map);

                loadBeanCollectionFromPlugin(cfgObj);
                loadBeanIdMapFromPlugin(cfgObj);
                loadPluginIdFromPluginMap(cfgObj);
            } catch (Exception ex) {
                throw new CMyException(I18NMessage.get(BeanConfigServer.class,
                        "BeanConfigServer.label3", "���������ļ�[config-file=")
                        + cfgFiles.get(nConfigFileIndex).getPlugin().getPath()
                        + I18NMessage.get(BeanConfigServer.class,
                                "BeanConfigServer.label4", "]ʧ�ܣ�")
                        + ex.getLocalizedMessage(), ex);
            }
        }
    }

    /**
     * @param _oPlugin
     * @throws CMyException
     */
    private void loadPluginIdFromPluginMap(PluginConfig _oPlugin)
            throws CMyException {
        if (m_hmPluginIdMap.containsKey(_oPlugin.getId()))
            throw new CMyException("[id="
                    + _oPlugin.getId()
                    + I18NMessage.get(BeanConfigServer.class,
                            "BeanConfigServer.label5", "]��plugin�Ѷ���"));

        m_hmPluginIdMap.put(_oPlugin.getId(), _oPlugin.getBeans());
    }

    /**
     * �����е�BeanConfigװ�뵽������ȥ
     * 
     * @param _oPlugin
     *            ���ø����
     */
    private void loadBeanCollectionFromPlugin(PluginConfig _oPlugin) {
        m_listBeanConfigCollection.addAll(_oPlugin.getBeans());
    }

    /**
     * @param _oPlugin
     */
    void loadBeanIdMapFromPlugin(PluginConfig _oPlugin) {
        List beans = _oPlugin.getBeans();
        for (int i = 0; i < beans.size(); i++) {
            BeanConfig beanCfg = (BeanConfig) beans.get(i);
            m_hmBeanIdMap.put(beanCfg.getId(), beanCfg.getClassName());
        }
    }

    /**
     * ����ָ��id��ʶ��Bean���ö����Ƿ����
     * 
     * @param id
     *            bean��id��ʶ
     * @return
     */
    public boolean containsBean(String _beanId) {
        // if (s_logger.isDebugEnabled())
        // s_logger.debug("the counter of bean config map(id, classname) is:"
        // + m_hmBeanIdMap.size());
        return m_hmBeanIdMap.containsKey(_beanId);
    }

    /**
     * �ж�BeanConfigServer�Ƿ��Ѿ������������е�Bean�����ļ����Ѿ���������
     * 
     * @return
     */
    public static boolean started() {
        return itsInstance.isStarted();
    }
}