package org.xiaotian.constants;

import org.xiaotian.exception.ExceptionMessages;

/**
 * 异常Number号描述<BR>
 * 
 * @author xiaotian15
 *
 */
public class ExceptionNumber {

	/**
	 * =================通用错误编号=============
	 */

	/** 错误编号常量:不明错误 */
	public final static int ERR_UNKNOWN = 0; // 不明错误

	/** 错误编号常量:数据转化错误 */
	public final static int ERR_MYEXCEPTION = 1; // 捕捉的CMyException

	/** 错误编号常量:数据转化错误 */
	public final static int ERR_DATACONVERT = 2; // 数据转化错误

	/** 错误编号常量:无效参数 */
	public final static int ERR_PARAM_INVALID = 10; // 无效参数

	/** 错误编号：类配置错误 */
	public final static int ERR_CLASS_CONFIG = 11;

	/** 错误编号：运行时配置错误 */
	public final static int ERR_RUNTIME_CONFIG = 12;

	/** 错误编号：class未找到 */
	public final static int ERR_CLASS_NOTFOUND = 13;

	/** 错误编号：缺少配置 */
	public final static int ERR_CONFIG_MISSING = 14;

	/** 错误编号常量: 空指针 */
	public final static int ERR_OBJ_NULL = 20; // 空指针

	/** 错误编号常量：实例话对象失败 */
	public final static int ERR_NEW_INSTANCE = 21;

	/** 错误编号常量:数学运算错误 */
	public final static int ERR_NUMOP_FAIL = 30; // 数学运算错误

	/**
	 * ==========================数据库操作错误======================
	 */

	/** 错误编号常量:数据库操作失败 */
	public final static int ERR_DBOP_FAIL = 40; // 数据库操作失败

	/** 错误编号常量:取数据库连接失败 */
	public final static int ERR_CONNECTION_GETFAIL = 41; // 取数据库连接失败

	/**
	 * =====================有关文件操作异常50~100====================
	 */

	/** 错误编号常量:文件操作失败 */
	public final static int ERR_FILEOP_FAIL = 50; // 文件操作失败

	/** 错误编号常量:打开文件失败 */
	public final static int ERR_FILEOP_OPEN = 51; // 打开文件失败

	/** 错误编号常量:关闭文件失败 */
	public final static int ERR_FILEOP_CLOSE = 52; // 关闭文件失败

	/** 错误编号常量:读文件失败 */
	public final static int ERR_FILEOP_READ = 53; // 读文件失败

	/** 错误编号常量:写文件失败 */
	public final static int ERR_FILEOP_WRITE = 54; // 写文件失败

	/** 错误编号常量:文件未找到 */
	public final static int ERR_FILE_NOTFOUND = 55; // 文件未找到

	/** 错误编号常量:移动文件失败 */
	public final static int ERR_FILEOP_MOVE = 56; // 移动文件失败

	/**
	 * ===========有关.net操作异常 100~149===============
	 */

	/** 错误编号常量:无效URL */
	public final static int ERR_URL_MALFORMED = 110; // 无效URL

	/** 错误编号常量:打开网络数据流失败 */
	public final static int ERR_NET_OPENSTREAM = 111; // 打开网络数据流失败

	/**
	 * =================有关XML的异常 150~199=============================
	 */

	/**
	 * Comment for <code>ERR_XMLFILE_PARSE</code><BR>
	 * XML文件解析的异常，错误编号：150
	 */
	public final static int ERR_XMLFILE_PARSE = 150;

	/**
	 * Comment for <code>ERR_XMLSTRUCTURE_NOTROOT</code><BR>
	 * XML结构错误，没有定义根元素，错误编号：151
	 */
	public final static int ERR_XMLSTRUCTURE_NOTROOT = 151;

	/** XML解析错误：指定XML元素没有找到 */
	public final static int ERR_XMLELEMENT_NOTFOUND = 152;

	/** xml error: the specified property is requried */
	public final static int ERR_XML_PROPERTY_REQUIRED = 153;

	/** XML字符串解析异常 */
	public final static int ERR_XMLSTRING_PARSE = 154;

	/**
	 * ================ 有关ZIP的异常 200~219=====================
	 */

	/** 解压文件异常，错误编号：200 */
	public final static int ERR_ZIP_UNZIP = 200;

	/**
	 * ====================== 配置文件错误=================
	 */
	/** 工厂文件配置错误 */
	public static final int ERR_FACTORY_CONFIG = 1400;

	/**
	 * ======================= publish异常 ======================
	 */
	// to define publish components error number: 220~300

	/** publish error */
	public final static int ERR_PUBLISH = 220;

	/** publish error: the publish server has not been started */
	public final static int ERR_PUBLISH_SERVER_NOTSTARTED = 221;

	/** publish error: fails to update the quoting folders */
	public final static int ERR_PUBLISH_UPDATE_QUOTING_FOLDERS = 222;

	/** publish error: the publish task is still open (has not been executed) */
	public final static int ERR_PUBLISH_TASK_STILLOPEN = 223;

	/** publish error: failed to create publish task */
	public final static int ERR_PUBLISH_CREATE_TASK = 224;

	/** publish error: failed to cancel the publish task */
	public final static int ERR_PUBLISH_CANCEL_TASK = 225;

	/** publish error: unknown publish folder type */
	public final static int ERR_PUBLISH_UNKNOWN_FOLDER_TYPE = 226;

	/** publish error: unknown publish content type */
	public final static int ERR_PUBLISH_UNKNOWN_CONTENT_TYPE = 227;

	/** publish error: unknown publish type */
	public final static int ERR_PUBLISH_UNKNOWN_PUBLISH_TYPE = 228;

	/** publish error: failed to analyze the detail page tasks */
	public final static int ERR_PUBLISH_ANALYZE_DETAIL_PAGE_TASK = 229;

	/** publish error: outline template required */
	public final static int ERR_PUBLISH_OUTLINE_TEMPLATE_REQUIRED = 230;

	/** publish error: detail template required */
	public final static int ERR_PUBLISH_DETAIL_TEMPLATE_REQUIRED = 231;

	/** publish error: tag document grammar is illegal */
	public final static int ERR_PUBLISH_TAGDOC_GRAMMAR_ILLEGAL = 232;

	/** publish error: failed to analyze the semantic relations in template */
	public final static int ERR_PUBLISH_TEMPLATE_SEMANTIC_ANALYSE = 233;

	/** publish error: failed to parse the specified tag */
	public final static int ERR_PUBLISH_TAG_PARSE = 234;

	/** publish error: invalid tag property value */
	public final static int ERR_PUBLISH_TAG_PROPERTY_VALUE_INVALID = 235;

	/** publish error: failed to distribute the file */
	public final static int ERR_PUBLISH_FIlE_DISTRIBUTE = 236;

	/** publish error: failed to generate page */
	public final static int ERR_PUBLISH_PAGE_GENERATE = 237;

	/** publish error: failed to published contents */
	public final static int ERR_PUBLISH_PUBLISHED_CONTENTS = 238;
	
	// =========================900-1000(内部模块定义)=============================
	public final static int ERR_MODULE_CONFIG=901;

	// =========================================================================
	// 1000以上，为WCM定义错误

	/** WCM Exception */
	public final static int ERR_WCMEXCEPTION = 1100;

	/**
	 * 消息
	 */
	public final static int ERR_MESSAGE = 1008;

	/** 用户未登录或超时 */
	public final static int ERR_USER_NOTLOGIN = 1001;

	/** 用户没有操作权限 */
	public final static int ERR_USER_NORIGHT = 1002;

	/** 指定对象未找到 */
	// public final static int ERR_OBJ_NOTFOUND = 1011;
	/** 对象未锁定 */
	public final static int ERR_OBJ_NOTLOCKED = 1012;

	/** 对象已被其他用户锁定 */
	public final static int ERR_OBJ_LOCKED = 1013;

	/** 不存在该属性 */
	public final static int ERR_PROPERTY_NOT_EXIST = 1101;

	/** 属性不允许修改 */
	public final static int ERR_PROPERTY_NOTALLOW_EDIT = 1102;

	/** 属性类型不匹配 */
	public final static int ERR_PROPERTY_TYPE_INVALID = 1103;

	/** 属性值无效 */
	public final static int ERR_PROPERTY_VALUE_INVALID = 1104;

	/** 新建对象无效 */
	public final static int ERR_NEWOBJ_INVALID = 1105;

	/** 对象属性没有设置 */
	public final static int ERR_PROPERTY_NOT_SET = 1106;

	/** 对象属性没有修改 */
	public final static int ERR_PROPERTY_NOT_MODIFIED = 1107;

	/** 属性值重复 */
	public final static int ERR_PROPERTY_VALUE_DUPLICATED = 1108;

	// 导入操作的异常 1200~1250
	/** 导入的文件格式不支持 */
	public final static int ERR_IMPORTFILE_INVALID = 1200;

	// 文档操作的异常 1300~1350
	/** 文档无法移动或转发到自身所在频道 */
	public final static int ERR_DOC_DUPLICATED = 1300;

	// wenyh@2005-3-31 14:24:23 add comment:添加错误编号! from 1351;
	/** 通过Class.newInstance,创建BaseObj对象实例失败 */
	public final static int ERR_INSTANCE_INVALID = 1351;

	/** failed to send notification */
	public final static int ERR_SEND_NOTIFICATION = 1352;

	/** failed to refresh the notification server */
	public final static int ERR_REFRESH_NOTIFICATION_SERVER = 1353;

	/** unknown message send type */
	public final static int ERR_MESSAGE_UNKNOWN_SENDTYPE = 1354;

	// ===============================================================
	// 相关操作
	// public final static String CONFIG_EXCEPTION_DESCRPTION =
	// "ExceptionDesc.xml";

	// ===============================================================
	// ========系统相关操作的异常 1000~1999

	public final static int ERR_SYS_OBJECT_NOT_FOUND = 1100;

	public final static int ERR_SYS_PARAM_NOT_SET = 1101;

	public final static int ERR_SYS_PARAM_INVALID = 1102;

	public final static int ERR_SYS_RUNTIME = 1103;

	// ===============================================================
	// ========栏目相关操作的异常 101000~101999
	// 给用户的提示信息

	// 程序运行错误或者脏数据导致的错误
	public final static int ERR_HOST_NOT_FOUND = 101100;

	// ===============================================================
	// ========模板相关操作的异常 102000~102999
	// 给用户的提示信息

	// 程序运行错误或者脏数据导致的错误
	public final static int ERR_TEMPLATE_NOT_FOUND = 102100;

	public final static int ERR_TEMPLATE_HOST_NOT_FOUND = 102101;

	/* 异常信息提示的阀值，如果大于这个编号，这认为该异常仅为警告或提示信息 */
	public final static int MSG_EXCEPTION_MIN_VALUE = 200000;

	public final static int MSG_NOT_SET_PUBLISH_PROPERTIES = 200001;

	public final static int MSG_NOT_SET_OUTLINE_TEMPLATE = 200002;

	public final static int MSG_NOT_SET_DETAIL_TEMPLATE = 200003;

	/** 设置成不能发布 **/
	public final static int MSG_SET_TO_CANNOT_PUBLISH = 200004;

	/** 为成不能发布状态 **/
	public final static int MSG_STATUS_CANNOT_PUBLISH = 200005;

	public final static int ERR_OBJ_NOTFOUND = 200006;

	/** 专题设计页面，栏目上下使用的默认值没有设置的异常编号 **/
	public final static int MSG_NOT_SET_DEFAULT_CHANNEL = 200007;

	// wenyh@2009-9-22 comment:添加一个错误号
	/**
	 * 使用该错误号以告知应用代码,信息提示需要特殊处理.
	 **/
	public final static int INFO_ERROR_MSG = 11111;

	/**
	 * 取错误编号对应的错误信息
	 * 
	 * @return
	 */
	public static String getErrNoMsg(int _nExceptionNum) {
		String sExceptionMsg = ExceptionMessages.getString(_nExceptionNum);
		if (sExceptionMsg == null)
			return ExceptionMessages.getString(ERR_UNKNOWN);

		return sExceptionMsg;

	}

}