package org.xiaotian.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.xiaotian.config.bean.ConfigFile;
import org.xiaotian.config.bean.ConfigFiles;
import org.xiaotian.exception.WCMException;
import org.xiaotian.extend.CMyException;
import org.xiaotian.extend.CMyFile;

/**
 * 按照指定方式（配置文件plugin和mapping.xml在同一目录）读写文件的工具类 <BR>
 * 
 * @author xiaotian15
 * 
 */
public class ConfigFilesFinder {

	private static Logger m_logger = Logger.getLogger(ConfigFilesFinder.class);

	/**
	 * 负有所有配置文件信息的ConfigFile集合
	 */
	private ConfigFiles m_oConfigFiles = null;

	/**
	 * 配置文件存放的根文件夹位置，是查找的入口
	 */
	private ArrayList<String> m_pConfigFileRootPaths = new ArrayList<String>();

	/**
	 * 要查找的配置文件的名称，如 config.xml
	 */
	private String m_sConfigXmlFile;

	private HashMap<String, ConfigFile> m_mapAlreadyDoneWithFileNames = new HashMap<String, ConfigFile>();

	public ConfigFilesFinder(String _sConfigFileRootPath, String _sConfigXmlFile) {
		m_pConfigFileRootPaths.add(_sConfigFileRootPath);
		this.m_sConfigXmlFile = _sConfigXmlFile;
	}

	public ConfigFilesFinder(ArrayList<String> _arConfigFileRootPaths,
			String _sConfigXmlFile) {
		this.m_pConfigFileRootPaths = _arConfigFileRootPaths;
		this.m_sConfigXmlFile = _sConfigXmlFile;
	}

	/**
	 * 得到已经组织好的配置文件集合
	 * 
	 * @return 包含config.xml - mapping.xml的CfgFiles对象
	 * @throws WCMException
	 *             可能的文件读取错误
	 */
	public ConfigFiles getConfigFiles() throws WCMException {
		if (m_oConfigFiles == null) {
			m_oConfigFiles = new ConfigFiles();

			for (int i = 0; i < m_pConfigFileRootPaths.size(); i++) {
				String sConfigFileRootPath = (String) m_pConfigFileRootPaths
						.get(i);

				if (m_logger.isDebugEnabled())
					m_logger.debug("begin to load config files from["
							+ sConfigFileRootPath + "]...");

				File fRoot = new File(sConfigFileRootPath);
				lookupCfgFiles(fRoot);
			}

		} else {
			if (m_logger.isDebugEnabled())
				m_logger.debug("the files have been loaded.");
		}

		return m_oConfigFiles;
	}

	/**
	 * 刷新
	 */
	public ConfigFiles refresh() throws CMyException {
		m_oConfigFiles = null;
		return getConfigFiles();
	}

	/**
	 * 递归检索指定目录，将查找的文件加入到m_hshFiles中去
	 * 
	 * @param _file
	 *            文件夹路径或者文件路径，后者是递归跳出的条件
	 */
	private void lookupCfgFiles(File _file) {
		if (_file.isFile()) {
			// 不是指定的文件名
			if (!_file.getName().equals(this.m_sConfigXmlFile)) {
				return;
			}

			// 在与Config.xml同级的目录中寻找配置对象的描述文件：mapping.xml
			String sMapping = CMyFile.extractFilePath(_file.getPath())
					+ ConfigConstants.NAME_FILE_MAPPING;
			File fMapping = CMyFile.fileExists(sMapping) ? new File(sMapping)
					: null;

			// 分解配置文件
			String sAbsolutFileName = _file.getAbsolutePath();
			String sConfigFileNameExcludeProjectPath = extractConfigFileNameExcludeProjectPath(sAbsolutFileName);

			// 判断是否处理，如果处理了，判断Mapping文件是否设置，没有直接返回
			ConfigFile configFile = (ConfigFile) m_mapAlreadyDoneWithFileNames
					.get(sConfigFileNameExcludeProjectPath);
			if (configFile != null) {
				if (configFile.getMapping() == null && fMapping != null) {
					configFile.setMapping(fMapping);
				}
				return;
			}

			// 记录下已经处理的配置文件
			configFile = new ConfigFile(_file, fMapping);
			m_mapAlreadyDoneWithFileNames.put(
					sConfigFileNameExcludeProjectPath, configFile);

			if (m_logger.isDebugEnabled()) {
				m_logger.debug("load xml file[" + _file.getPath() + "]");
			}

			m_oConfigFiles.add(configFile);

			return;
		}

		// 递归调用，遍历所有子文件夹
		File[] dirs = _file.listFiles();
		if (dirs == null) {
			m_logger.warn("May be a IOException,find an invalid dir:"
					+ _file.getAbsolutePath());
			return;
		}
		for (int i = 0; i < dirs.length; i++) {
			lookupCfgFiles(dirs[i]);
		}
	}

	/**
	 * 获取s_sAppRootPath后面的路径<BR>
	 * 
	 * @param _sAbsolutFileName
	 *            config文件的root路径
	 * @return
	 */
	private String extractConfigFileNameExcludeProjectPath(
			String _sAbsolutFileName) {
		String sConfigFilePathFlag = File.separatorChar
				+ ConfigConstants.CONFIG_ROOT_PATH + File.separatorChar;
		String sConfigFileNameExcludeProjectPath = _sAbsolutFileName;
		int nPos = _sAbsolutFileName.indexOf(sConfigFilePathFlag);
		if (nPos >= 0) {
			sConfigFileNameExcludeProjectPath = _sAbsolutFileName
					.substring(nPos);
		}
		return sConfigFileNameExcludeProjectPath;
	}

}