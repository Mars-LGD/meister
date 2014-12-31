package org.xiaotian.exception;

import org.apache.log4j.Logger;
import org.xiaotian.constants.ExceptionNumber;
import org.xiaotian.extend.CMyException;

/**
 * WCM异常类型定义<BR>
 * 
 * @author xiaotian15
 * 
 */
public class WCMException extends CMyException {

	private static final long serialVersionUID = 8819172007753214059L;

	private final static Logger logger = Logger.getLogger(WCMException.class.getName());

	public WCMException(int _errNo) {
		super(_errNo);
	}

	public WCMException(int _errNo, String _sMsg) {
		super(_errNo, _sMsg);
	}

	public WCMException(String _sMsg) {
		super(_sMsg);
		errNo = ExceptionNumber.ERR_MESSAGE;
	}

	/**
	 * 构造函数
	 * 
	 * @param _errNo
	 *            错误编号（int）
	 * @param _sMsg
	 *            错误信息（String）可忽略
	 * @param _rootCause
	 *            错误根源（Throwable）可忽略
	 */
	public WCMException(int _errNo, String _sMsg, Throwable _rootCause) {
		super(_errNo, _sMsg, _rootCause);
	}

	/**
	 * 构造函数
	 * 
	 * @param _sMsg
	 *            错误信息（String）可忽略
	 * @param _rootCause
	 *            错误根源（Throwable）可忽略
	 */
	public WCMException(String _sMsg, Throwable _rootCause) {
		super(_sMsg, _rootCause);
		errNo = ExceptionNumber.ERR_WCMEXCEPTION;
	}

	public static void catchException(String _strDesc, Exception _exCaught, boolean bSeverity) throws WCMException {
		// 错误输出到标准输出设备
		logger.error(_strDesc, _exCaught);
		// 如果是严重错误的话需要继续抛出错误
		if (true == bSeverity) {
			throw new WCMException(1000, _strDesc, _exCaught);
		}
	}
}