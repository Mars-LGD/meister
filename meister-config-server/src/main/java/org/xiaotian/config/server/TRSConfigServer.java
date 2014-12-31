/**
 * Created:         2006-4-5 12:51:55
 * Last Modified:   2006-4-5/2006-4-5
 * Description:
 *      class TRSConfigServer
 */
package org.xiaotian.config.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.helpers.Loader;
import org.xiaotian.config.WCMDiscoveryFactory;
import org.xiaotian.exception.WCMException;
import org.xiaotian.extend.CMyFile;
import org.xiaotian.extend.CMyString;
import org.xiaotian.language.I18NMessage;

public class TRSConfigServer {
    private static final String SERVER_CLASS_NAME_INI = "server.class.name.ini";

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
            .getLogger(TRSConfigServer.class);

    private static String sPropertiesFilePath = null;

    private static final int STATUS_NOTSTART = 0;

    private static final int STATUS_STARTING = 1;

    private static final int STATUS_STARTED = 2;

    private static final int STATUS_STARTFAIL = 3;

    public static String FILENAME_APP_PROPERTIES = "TRSApp.properties";

    private static String KEY_ROOT_PATH = "config.root.path";

    private static String PATH_CONFIG_ROOT = null;

    private static int STATUS_CURRENT = STATUS_NOTSTART;

    private static Map MAP_CONFIG_SERVER = null;

    private static String s_sAppRootPath = null;

    public static BaseConfigServer findConfigServerById(String _sConfigServerId) {
        if (MAP_CONFIG_SERVER == null) {
            start();
        }

        return (BaseConfigServer) MAP_CONFIG_SERVER.get(_sConfigServerId
                .toLowerCase());
    }

    /**
     * 获取配置服务器的根路径
     * 
     * @return
     * @throws WCMException
     */
    public static String getConfigRootPath() {
        if (PATH_CONFIG_ROOT == null) {
            try {
                makeConfigRootPath();
            } catch (WCMException e) {
                throw new RuntimeException("Fail to load config root path!", e);
            }
        }

        return PATH_CONFIG_ROOT;
    }

    /**
     * 启动配置服务器
     */
    public static void start() {
        if (MAP_CONFIG_SERVER == null) {
            try {
                init();
            } catch (Throwable e) {
                logger.error(I18NMessage.get(TRSConfigServer.class,
                        "TRSConfigServer.label1", "启动ConfigServer失败!"), e);
                STATUS_CURRENT = STATUS_STARTFAIL;
            }
        }
    }

    private static synchronized void init() throws WCMException {
        if (MAP_CONFIG_SERVER != null || STATUS_CURRENT == STATUS_STARTFAIL)
            return;

        STATUS_CURRENT = STATUS_STARTING;

        makeConfigRootPath();

        // 启动各个配置服务器
        Map mTemp = new HashMap(10);
        Iterator itConvertos = WCMDiscoveryFactory.getDiscovery()
                .getImplementors(BaseConfigServer.class.getName());
        while (itConvertos.hasNext()) {
            String sConfigServerClassName = (String) itConvertos.next();

            BaseConfigServer server = null;
            try {
                server = (BaseConfigServer) Class.forName(
                        sConfigServerClassName).newInstance();
            } catch (Exception e) {
                logger.error(I18NMessage.get(TRSConfigServer.class,
                        "TRSConfigServer.label2", "实例化配置服务器实例失败！[Class=")
                        + sConfigServerClassName
                        + ", ClassName="
                        + sConfigServerClassName + "]", e);
                continue;
            }

            // server.setConfigPath(sSubConfigServerPath);
            try {
                server.startup();
            } catch (Throwable e) {
                logger.error(I18NMessage.get(TRSConfigServer.class,
                        "TRSConfigServer.label3", "启动配置服务器实例失败！[Class=")
                        + sConfigServerClassName
                        + ", ClassName="
                        + sConfigServerClassName + "]", e);
            }

            mTemp.put(server.getServerName().toLowerCase(), server);
        }

        // File rootPath = new File(PATH_CONFIG_ROOT);
        // if (!rootPath.exists() || !rootPath.isDirectory()) {
        // throw new WCMException("设置的目录[" + PATH_CONFIG_ROOT +
        // I18NMessage.get(TRSConfigServer.class, "TRSConfigServer.label4",
        // "]不正确！"));
        // }
        //
        // File[] files = rootPath.listFiles();
        // Map mTemp = new HashMap(files.length);
        // for (int i = 0; i < files.length; i++) {
        // File fileSubConfigServer = files[i];
        //
        // if (!fileSubConfigServer.isDirectory())
        // continue;
        //
        // String sSubConfigServerPath = CMyString.setStrEndWith(
        // fileSubConfigServer.getAbsolutePath(), File.separatorChar);
        // String sConfigInfoFileName = sSubConfigServerPath
        // + SERVER_CLASS_NAME_INI;
        // File fileConfigClassInfo = new File(sConfigInfoFileName);
        // if (!fileConfigClassInfo.exists() || !fileConfigClassInfo.isFile())
        // continue;
        //
        // String sConfigServerClassName = null;
        // try {
        // sConfigServerClassName = CMyFile.readFile(fileConfigClassInfo
        // .getAbsolutePath());
        // } catch (CMyException e) {
        // logger.error("读取配置服务器实例失败！["
        // + fileConfigClassInfo.getAbsolutePath() + "]", e);
        // continue;
        // }
        //
        // BaseConfigServer server = null;
        // try {
        // server = (BaseConfigServer) Class.forName(
        // sConfigServerClassName).newInstance();
        // } catch (Exception e) {
        // logger.error("实例化配置服务器实例失败！[File="
        // + fileConfigClassInfo.getAbsolutePath()
        // + ", ClassName=" + sConfigServerClassName + "]", e);
        // continue;
        // }
        //            
        //
        // server.setConfigPath(sSubConfigServerPath);
        //
        // try {
        // server.startup();
        // } catch (Throwable e) {
        // logger.error("启动配置服务器实例失败！[File="
        // + fileConfigClassInfo.getAbsolutePath()
        // + ", ClassName=" + sConfigServerClassName + "]", e);
        // }
        //
        // String sConfigServerKey = fileSubConfigServer.getName();
        // mTemp.put(sConfigServerKey, server);
        //            
        //            
        // }

        MAP_CONFIG_SERVER = mTemp;

        STATUS_CURRENT = STATUS_STARTED;
    }

    /**
     * 
     */
    private static synchronized void makeConfigRootPath() throws WCMException {
        if (PATH_CONFIG_ROOT != null)
            return;

        String sConfigFilesDir = loadDefaultProperties();
        if (sConfigFilesDir == null)
            throw new WCMException(I18NMessage.get(TRSConfigServer.class,
                    "TRSConfigServer.label5", "读取默认的配置文件根目录失败！"));

        // else
        File rootPath = new File(sConfigFilesDir);
        if (!rootPath.exists() || !rootPath.isDirectory()) {
            // 相对路径，使用ClassLoader获取路径
            sConfigFilesDir = CMyString.setStrEndWith(sConfigFilesDir, '/');
            URL urlServerClassNames = Loader
                    .getResource(FILENAME_APP_PROPERTIES);
            File fileServerClasssNames = new File(urlServerClassNames.getFile());
            sConfigFilesDir = CMyFile.extractFilePath(fileServerClasssNames
                    .getAbsolutePath())
                    + sConfigFilesDir;

            rootPath = new File(sConfigFilesDir);
        }

        if (!rootPath.exists() || !rootPath.isDirectory()) {
            throw new WCMException(I18NMessage.get(TRSConfigServer.class,
                    "TRSConfigServer.label6", "设置的目录[")
                    + sConfigFilesDir
                    + I18NMessage.get(TRSConfigServer.class,
                            "TRSConfigServer.label4", "]不正确！"));
        }

        // finally, set the PATH_CONFIG_ROOT
        if (File.separatorChar == '\\') {
            sConfigFilesDir = sConfigFilesDir.replace('/', File.separatorChar);
        }
        PATH_CONFIG_ROOT = CMyString.setStrEndWith(sConfigFilesDir,
                File.separatorChar);
    }

    /**
     * Load services definition from default properties
     * 
     */
    private static String loadDefaultProperties() {
        URL url = Loader.getResource(FILENAME_APP_PROPERTIES);
        if (url == null) {
            throw new RuntimeException("Cannot find file["
                    + FILENAME_APP_PROPERTIES + "] at the root of application!");
        }

        /*
         * InputStream is = this.getClass().getResourceAsStream(
         * this.serviceConfigProperties);
         */
        InputStream is = null;
        try {
            // sPropertiesFilePath = SourcePathBuddy.getAppRootPath()
            // + FILENAME_APP_PROPERTIES;
            // is = new FileInputStream(sPropertiesFilePath);
            sPropertiesFilePath = url.getFile();
            is = url.openStream();
        } catch (IOException ex) {
            throw new RuntimeException(
                    "Cannot find file[" + FILENAME_APP_PROPERTIES
                            + "] at the root of application!", ex);
        }

        String result = null;
        if (is != null) {
            logger.info("Load service from properties: " + sPropertiesFilePath
                    + "..");
            Properties defaultProperties = new Properties();
            try {
                defaultProperties.load(is);
                result = defaultProperties.getProperty(KEY_ROOT_PATH);
            } catch (IOException e) {
                logger.info("Errors occur when reading " + sPropertiesFilePath
                        + ", ommited");
                e.printStackTrace();
            }
            logger.info("Finished to load service from properties.");
        }

        File file = new File(sPropertiesFilePath);
        s_sAppRootPath = file.getAbsolutePath();
        if (File.separatorChar == '\\') {
            s_sAppRootPath = s_sAppRootPath.replace('/', '\\');
        }
        s_sAppRootPath = CMyFile.extractFilePath(s_sAppRootPath);

        return result;
    }

    public static String getAppRootPath() {
        if (s_sAppRootPath != null) {
            return s_sAppRootPath;
        }

        start();
        return s_sAppRootPath;
    }

    public static void main(String[] args) {
        try {
            System.out.println("result:" + TRSConfigServer.getConfigRootPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TRSConfigServer.start();
    }

}