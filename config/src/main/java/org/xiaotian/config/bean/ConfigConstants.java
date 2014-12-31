package org.xiaotian.config.bean;

import java.io.File;
import java.net.URL;

import org.apache.log4j.helpers.Loader;
import org.xiaotian.config.server.TRSConfigServer;
import org.xiaotian.extend.CMyFile;

/**
 * Config的常量定义<BR>
 * 
 * @author xiaotian15
 * 
 */
public final class ConfigConstants {

	/**
     * 
     */
	public static String DIR_APPROOT = makeAppRootPath(); // ConfigConstants.
															// getAppRootPath();

	public static String DIR_CLASSROOT = makeClassRootPath(); // ConfigConstants.
																// getAppRootPath
																// ();

	public static final String EXT_FILE_CONFIG = "xml";

	public static final String NAME_FILE_CONFIG = "config." + EXT_FILE_CONFIG;

	public static final String NAME_FILE_BEAN = "beans." + EXT_FILE_CONFIG;

	public static final String TAG_ELEMENT_BEANS = "beans";

	public static final String TAG_ELEMENT_BEAN = "bean";

	public static final String TAG_ATTRIBUTE_BEANID = "id";

	public static final String TAG_ATTRIBUTE_BEANCLASSNAME = "class";

	public static final String NAME_FILE_MAPPING = "mapping." + EXT_FILE_CONFIG;

	public static final String TAG_ATTRIBUTE_BEANISSINGLETON = "singleton";

	// public static final String PATH_FILE_MAPPING_CORE = DIR_APPROOT
	// + NAME_FILE_MAPPING;

	// public static final String PATH_FILE_CONFIG_CORE = DIR_APPROOT
	// + NAME_FILE_CONFIG;

	public static final int ERR_FORMAT_INVALID = 3000;

	private static String makeAppRootPath() {
		URL url = Loader.getResource(TRSConfigServer.FILENAME_APP_PROPERTIES);
		if (url == null) {
			throw new RuntimeException("Cannot find file[" + TRSConfigServer.FILENAME_APP_PROPERTIES + "] at the root of application!");
		}

		String sPropertiesFilePath = null;
		sPropertiesFilePath = url.getFile();

		File file = new File(sPropertiesFilePath);
		String sPath = file.getAbsolutePath();
		if (File.separatorChar == '\\') {
			sPath = sPath.replace('/', '\\');
		}
		sPath = CMyFile.extractFilePath(sPath);
		return sPath;
	}

	private static String makeClassRootPath() {
		try {
			URL url = Loader.getResource("RootFile.trs");
			if (url == null) {
				throw new RuntimeException("Cannot find file[" + TRSConfigServer.FILENAME_APP_PROPERTIES + "] at the root of application!");
			}

			String sPropertiesFilePath = null;
			sPropertiesFilePath = url.getFile();

			File file = new File(sPropertiesFilePath);
			String sPath = file.getAbsolutePath();
			if (File.separatorChar == '\\') {
				sPath = sPath.replace('/', '\\');
			}
			sPath = CMyFile.extractFilePath(sPath);
			return sPath;
		} catch (Throwable e) {
			return makeAppRootPath();
		}
	}

	public static String getAbsoluteFileNameOfCoreMapping() {
		return getConfigRootPath() + NAME_FILE_MAPPING;
	}

	public static String getAbsoluteFileNameOfCoreConfigXML() {
		return getConfigRootPath() + NAME_FILE_CONFIG;
	}

	public static String getAbsoluteFileNameOfCoreBeansXML() {
		return getConfigRootPath() + NAME_FILE_BEAN;
	}

	public static String getConfigRootPath() {
		try {
			return TRSConfigServer.getConfigRootPath() + "domain" + File.separator;
		} catch (Exception e) {
			throw new RuntimeException("Fail to read config path!", e);
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println(ConfigConstants.DIR_APPROOT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}