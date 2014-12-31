package org.xiaotian.config;

import java.io.File;

import org.xiaotian.config.bean.ConfigConstants;
import org.xiaotian.discovery.DiscoveryFactory;
import org.xiaotian.discovery.IDiscovery;
import org.xiaotian.discovery.utils.DiscoveryServletHelper;
import org.xiaotian.language.I18NMessage;

public class WCMDiscoveryFactory {
    private static IDiscovery s_oDiscovery = null;

    /**
     * @param _pLibDirs
     * @param _pClassesDirs
     * @return
     */
    public static IDiscovery initDiscovery(File[] _pLibDirs,
            File[] _pClassesDirs) {
        if (s_oDiscovery != null)
            return s_oDiscovery;

        synchronized (WCMDiscoveryFactory.class) {
            if (s_oDiscovery != null)
                return s_oDiscovery;

            s_oDiscovery = DiscoveryFactory.create(_pLibDirs, _pClassesDirs);
            DiscoveryServletHelper.setDiscovery(s_oDiscovery);
        }

        return s_oDiscovery;
    }

    /**
     * 获取当前系统的Discovery
     * 
     * @return
     */
    public static IDiscovery getDiscovery() {
        if (s_oDiscovery == null) {
            synchronized (WCMDiscoveryFactory.class) {
                s_oDiscovery = DiscoveryServletHelper.getDiscovery();
                if (s_oDiscovery != null) {
                    return s_oDiscovery;
                }
            }

            File[] pClassesDirs = { getConfigRootFilePath(),
                    getClassRootFilePath() };
            initDiscovery(null, pClassesDirs);
        }
        return s_oDiscovery;
    }

    /**
     * 获取配置文件的根路径
     * 
     * @return
     * @throws Exception
     */
    public static File getConfigRootFilePath() {
        // String sFilePath =
        // CMyString.class.getClassLoader().getResource("TRSApp.properties").
        // getFile();
        // System.out.println(sFilePath);
        // String sPathEndFlag = "/classes/";
        // int nPos = sFilePath.indexOf(sPathEndFlag);
        // if(nPos>0){
        // sFilePath =sFilePath.substring(0, nPos + sPathEndFlag.length());
        // }
        File file = new File(ConfigConstants.DIR_APPROOT);
        if (!file.exists()) {
            throw new RuntimeException(I18NMessage.get(
                    WCMDiscoveryFactory.class, "WCMDiscoveryFactory.label1",
                    "目录[")
                    + ConfigConstants.DIR_APPROOT
                    + I18NMessage.get(WCMDiscoveryFactory.class,
                            "WCMDiscoveryFactory.label2", "]不存在！"));
        }
        return file;
    }

    /**
     * 获取Class文件的根路径
     * 
     * @return
     * @throws Exception
     */
    public static File getClassRootFilePath() {
        File file = new File(ConfigConstants.DIR_CLASSROOT);
        if (!file.exists()) {
            throw new RuntimeException(I18NMessage.get(
                    WCMDiscoveryFactory.class, "WCMDiscoveryFactory.label1",
                    "目录[")
                    + ConfigConstants.DIR_CLASSROOT
                    + I18NMessage.get(WCMDiscoveryFactory.class,
                            "WCMDiscoveryFactory.label2", "]不存在！"));
        }
        return file;
    }
}