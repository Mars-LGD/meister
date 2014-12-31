package org.xiaotian.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.Unmarshaller;
import org.xiaotian.config.bean.ConfigConstants;
import org.xiaotian.config.bean.ConfigFiles;
import org.xiaotian.config.bean.ExtensionConfigs;
import org.xiaotian.config.bean.PluginConfig;
import org.xiaotian.extend.CMyException;
import org.xiaotian.language.I18NMessage;

/**
 * 配置文件的编组解组工具
 * 
 * @author xiaotian15
 * 
 */
public final class ConfigObjectLoader {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
            .getLogger(ConfigObjectLoader.class);

    /** Plugin Config Map <PluginConfig.getHostFilePath(), PluginConfig> */
    private Map m_hmPluginConfigMap = null;

    /**
     * Extension Config by Class Name Map <ElementClassName,
     * ArrayList(IConfigElement)>
     */
    private Map m_hmExtensionConfigByClassMap = null;

    /**
     * Extension Config by Id Map <ElementId, ArrayList(IConfigElement)>
     */
    private Map m_hmExtensionConfigByIdMap = null;

    /**
     * Help to search the config files
     */
    private ConfigFilesFinder m_oConfigFileFinder;

    /**
     * The flag for loaded status
     */
    private boolean m_bLoaded = false;

    public ConfigObjectLoader() {
        // m_oConfigFileFinder = new ConfigFilesFinder(
        // ConfigConstants.DIR_APPROOT, ConfigConstants.NAME_FILE_CONFIG);

        // 适应项目依赖的情况 comment by caohui@2008-7-23 下午01:50:10
        // m_oConfigFileFinder = new ConfigFilesFinder(ConfigConstants
        // .getConfigRootPath(), ConfigConstants.NAME_FILE_CONFIG);

        m_oConfigFileFinder = new ConfigFilesFinder(ConfigHelper
                .getDomainConfigPaths(), ConfigConstants.NAME_FILE_CONFIG);

        m_hmPluginConfigMap = new HashMap();
        m_hmExtensionConfigByClassMap = new HashMap();
        m_hmExtensionConfigByIdMap = new HashMap();
    }

    /**
     * 回收
     */
    public synchronized void clear() {
        m_hmExtensionConfigByClassMap.clear();
        m_hmExtensionConfigByIdMap.clear();
        m_hmPluginConfigMap.clear();
        m_bLoaded = false;
    }

    /**
     * 加载
     */
    public synchronized void load() {
        if (m_bLoaded)
            return;

        try {
            ConfigFiles cfgFiles = m_oConfigFileFinder.getConfigFiles();

            Mapping map = new Mapping();
            try {
                map.loadMapping(ConfigHelper.getMappingSource(cfgFiles));
            } catch (Exception ex) {
                throw new CMyException(I18NMessage.get(
                        ConfigObjectLoader.class, "ConfigObjectLoader.label1",
                        "装载Castor的mapping文件时出错"), ex);
            }

            for (int nConfigFileIndex = 0; nConfigFileIndex < cfgFiles.size(); nConfigFileIndex++) {
                try {
                    File fPlugin = cfgFiles.get(nConfigFileIndex).getPlugin();
                    PluginConfig cfgObj = loadUnmarshallerRoot(fPlugin, map);
                    m_hmPluginConfigMap.put(cfgObj.getHostFilePath(), cfgObj);

                    loadExtensionByClassMapFromPlugin(cfgObj);
                    loadExtensionByIdMapFromPlugin(cfgObj);

                } catch (MappingException ex) {
                    // TODO 重构以确定具体的映射文件出错位置
                    throw new CMyException(I18NMessage.get(
                            ConfigObjectLoader.class,
                            "ConfigObjectLoader.label2",
                            "分析映射文件失败! (当前分析的配置文件[")
                            + cfgFiles.get(nConfigFileIndex).getPlugin()
                                    .getPath() + "])", ex);
                } catch (Exception ex) {
                    throw new CMyException(I18NMessage.get(
                            ConfigObjectLoader.class,
                            "ConfigObjectLoader.label3", "分析配置文件[config-file=")
                            + cfgFiles.get(nConfigFileIndex).getPlugin()
                                    .getPath()
                            + I18NMessage.get(ConfigObjectLoader.class,
                                    "ConfigObjectLoader.label4", "]失败：")
                            + ex.getLocalizedMessage(), ex);

                }

            }
            m_bLoaded = true;
        } catch (CMyException e) {
            throw new RuntimeException("Load Config File error!"
                    + CMyException.getStackTraceText(e));
        }
    }

    /**
     * 将所有按ClassName分类的自定义配置对象集合的装入散列结构
     * 
     * @param _oPlugin
     *            配置根对象
     */
    private void loadExtensionByClassMapFromPlugin(PluginConfig cfgObj) {
        List listExtentions = cfgObj.getExtensions();
        if (listExtentions.size() == 0)
            return;

        for (int j = 0; j < listExtentions.size(); j++) {
            ExtensionConfigs ext = (ExtensionConfigs) listExtentions.get(j);
            String sClassName = ext.getClassName();

            if (m_hmExtensionConfigByClassMap.containsKey(sClassName)) {
                getConfigElementsByClass(sClassName).addAll(
                        ext.getConfigElements());
                continue;
            }
            ArrayList listConfigElements = new ArrayList();
            listConfigElements.addAll(ext.getConfigElements());
            m_hmExtensionConfigByClassMap.put(ext.getClassName(),
                    listConfigElements);
        }
    }

    /**
     * 将所有按ID分类的自定义配置对象集合的装入散列结构
     * 
     * @param _oPlugin
     *            配置根对象
     * @throws RuntimeException
     */
    private void loadExtensionByIdMapFromPlugin(PluginConfig _oPlugin) {
        List listExtensions = _oPlugin.getExtensions();

        HashMap hmComparasion = new HashMap();
        for (int j = 0; j < listExtensions.size(); j++) {
            ExtensionConfigs oCurrExt = (ExtensionConfigs) listExtensions
                    .get(j);

            String sCurrExtId = oCurrExt.getId();
            if (hmComparasion.containsKey(sCurrExtId)) {
                ExtensionConfigs oComparationExt = (ExtensionConfigs) hmComparasion
                        .get(sCurrExtId);
                if (oCurrExt.getClassName() != null
                        && oCurrExt.getClassName().compareTo(
                                oComparationExt.getClassName()) != 0)
                    throw new RuntimeException(I18NMessage.get(
                            ConfigObjectLoader.class,
                            "ConfigObjectLoader.label5", "定义了一个[id=")
                            + oCurrExt.getId()
                            + "]、[element-class="
                            + oCurrExt.getClassName()
                            + I18NMessage.get(ConfigObjectLoader.class,
                                    "ConfigObjectLoader.label6",
                                    "]配置项，但该项的[id]已经被另外一个[elemnt-class=")
                            + oComparationExt.getClassName()
                            + I18NMessage.get(ConfigObjectLoader.class,
                                    "ConfigObjectLoader.label7", "]的配置文件占用!"));

                ArrayList listConfigElements = (ArrayList) m_hmExtensionConfigByIdMap
                        .get(sCurrExtId);
                listConfigElements.addAll(oCurrExt.getConfigElements());
            } else {
                hmComparasion.put(sCurrExtId, oCurrExt);

                ArrayList listConfigElements = new ArrayList();
                listConfigElements.addAll(oCurrExt.getConfigElements());
                m_hmExtensionConfigByIdMap.put(sCurrExtId, listConfigElements);
            }
        }
    }

    /**
     * @param sClassName
     * @return
     */
    private ArrayList getConfigElementsByClass(String sClassName) {
        return (ArrayList) m_hmExtensionConfigByClassMap.get(sClassName);
    }

    /**
     * 依据一个配置文件，解组得到一个可配置根对象
     * 
     * @param _fCfgXml
     *            配置文件的路径
     * @param _map
     * @return @throws Exception
     */
    public static PluginConfig loadUnmarshallerRoot(File _fCfgXml, Mapping _map)
            throws MappingException, Exception {
        PluginConfig result = null;
        Unmarshaller unmarshaller = new Unmarshaller();

        Reader reader = null;
        try {
            unmarshaller.setMapping(_map);
            reader = new InputStreamReader(new FileInputStream(_fCfgXml), "GBK");
            result = (PluginConfig) unmarshaller.unmarshal(reader);

            result.setHostFilePath(_fCfgXml.getPath());
            return result;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    // Ignor.
                }
            }
        }
    }

    private Map getExtensionConfigByClassMap() {
        if (!m_bLoaded) {
            load();
        }
        return m_hmExtensionConfigByClassMap;
    }

    private Map getExtensionConfigByIdMap() {
        if (!m_bLoaded) {
            load();
        }
        return m_hmExtensionConfigByIdMap;
    }

    /**
     * 依据可配置对象类型，得到所有该类型的配置信息
     * 
     * @param _cElementType
     *            可配置对象的类型
     * @return 一个ICfgEntity的数组
     */
    public List getExtensionalObjects(Class _cElementType) {
        return (ArrayList) this.getExtensionConfigByClassMap().get(
                _cElementType.getName());
    }

    /**
     * 依据可配置对象ID标示，得到所有该类型的配置信息
     * 
     * @param _sExtId
     *            可配置对象ID标示
     * @return 一个ICfgEntity的数组
     */
    public List getExtensionalObjects(String _sExtId) {
        return (ArrayList) this.getExtensionConfigByIdMap().get(_sExtId);
    }

    /**
     * 得到所有的配置文件的格式为 <PluginConfig.getHostFilePath(), PluginConfig>散列集
     * 
     * @return 配置文件的格式为 <PluginConfig.getHostFilePath(), PluginConfig>散列集
     */
    public Map getPluginMap() {
        if (!m_bLoaded) {
            load();
        }
        return this.m_hmPluginConfigMap;
    }
}