package org.xiaotian.extend;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.xiaotian.constants.ExceptionNumber;

/**
 * 异常对象的定义与实现<BR>
 * 
 * @author xiaotian15
 * 
 */
public class CMyException extends Exception {

	private static final long serialVersionUID = 1L;

	protected int errNo = 0;

	protected Throwable rootCause = null;

	/**
	 * 构造函数
	 * 
	 * @param _errNo
	 *            错误编号
	 */
	public CMyException(int _errNo) {
		super();
		errNo = _errNo;
	}

	/**
	 * 构造函数
	 * 
	 * @param _errNo
	 *            错误编号
	 * @param _sMsg
	 *            错误描述
	 */
	public CMyException(int _errNo, String _sMsg) {
		super(_sMsg);
		errNo = _errNo;
	}

	/**
	 * 构造函数
	 * 
	 * @param _sMsg
	 *            错误描述
	 */
	public CMyException(String _sMsg) {
		super(_sMsg);
	}

	/**
	 * 构造函数
	 * 
	 * @param _errNo
	 *            错误编号
	 * @param _sMsg
	 *            错误描述
	 * @param _rootCause
	 *            异常对象
	 */
	public CMyException(int _errNo, String _sMsg, Throwable _rootCause) {
		super(_sMsg);
		errNo = _errNo;
		rootCause = _rootCause;
	}

	/**
	 * 构造函数
	 * 
	 * @param _sMsg
	 *            错误描述
	 * @param _rootCause
	 *            异常对象
	 */
	public CMyException(String _sMsg, Throwable _rootCause) {
		super(_sMsg);
		rootCause = _rootCause;
	}

	/**
	 * 获取错误编号
	 * 
	 * @return
	 */
	public int getErrNo() {
		return errNo;
	}

	/**
	 * 获取异常对象
	 * 
	 * @return
	 */
	public Throwable getRootCause() {
		return rootCause;
	}

	/**
	 * 取错误编号对应的错误信息
	 * 
	 * @return
	 */
	public String getErrNoMsg() {
		return ExceptionNumber.getErrNoMsg(errNo);
	}

	/**
	 * 仅仅取该异常自身的消息
	 * 
	 * @return String 当前异常的信息
	 */
	public String getMyMessage() {
		return super.getMessage();
	}

	/**
	 * 将异常信息输出为字符串
	 * 
	 * @return 字符串表示的异常信息
	 */
	public String toString() {
		String sMessage = "[ERR-" + errNo + "] " + this.getMyMessage();
		return sMessage;
	}

	/**
	 * 取错误消息，若rootCause非空，则同时输出rootCause的错误消息
	 * 
	 * @return String 错误信息
	 */
	public String getMessage() {
		String sMessage = this.toString();
		if (rootCause != null) {
			sMessage += "\r\n<-- " + rootCause.toString();
		}// end if

		return sMessage;
	}

	/**
	 * 取错误消息，若rootCause非空，则同时输出rootCause的错误消息
	 * 
	 * @return String 错误信息
	 */
	public String getLocalizedMessage() {
		return getMessage();
	}

	/**
	 * 向指定设备打印错误信息
	 *
	 * @param _ps
	 *            指定设备java.io.PrintStream对象
	 */
	public void printStackTrace(java.io.PrintStream _ps) {
		if (rootCause == null) {
			super.printStackTrace(_ps);
		} else {
			Throwable root = rootCause;
			synchronized (_ps) {
				_ps.println(this.toString());
				Throwable temp = null;
				while (root instanceof CMyException) {
					_ps.println("<-- " + root.toString());
					temp = root;
					root = ((CMyException) root).getRootCause();
					if (root == null) {
						temp.printStackTrace(_ps);
						break;
					}
				}
				if (root != null) {
					_ps.print("<-- ");
					root.printStackTrace(_ps);
				}
			}
		}
	}

	/**
	 * 向指定设备打印错误信息
	 * 
	 * @param _pw
	 *            指定设备java.io.PrintWriter对象
	 */
	public void printStackTrace(java.io.PrintWriter _pw) {
		if (rootCause == null) {
			super.printStackTrace(_pw);
		} else {
			Throwable root = rootCause;
			synchronized (_pw) {
				_pw.println(this.toString());
				Throwable preRoot = null;
				while (root instanceof CMyException) {
					_pw.print("<-- ");
					preRoot = root;
					root = ((CMyException) root).getRootCause();
					if (root == null) {
						preRoot.printStackTrace(_pw);
						break;
					}
					_pw.println(preRoot.toString());
				}
				if (root != null) {
					_pw.print("<-- ");
					root.printStackTrace(_pw);
				}
			}
		}
	}

	public String getStackTraceText() {
		return getStackTraceText(this);
	}

	/**
	 * 取异常堆栈信息
	 * 
	 * @return 异常堆栈信息
	 */
	public static String getStackTraceText(Throwable _ex) {
		StringWriter strWriter = null;
		PrintWriter prtWriter = null;
		try {
			strWriter = new StringWriter();
			prtWriter = new PrintWriter(strWriter);
			_ex.printStackTrace(prtWriter);
			prtWriter.flush();
			return strWriter.toString();
		} catch (Exception ex2) {
			return _ex.getMessage();
		} finally {
			if (strWriter != null)
				try {
					strWriter.close();
				} catch (Exception ex) {
				}
			if (prtWriter != null)
				try {
					prtWriter.close();
				} catch (Exception ex) {
				}
		}
	}

	// =================================================
	// test

	public final static void main(String[] args) {
		CMyException fire0 = new CMyException(ExceptionNumber.ERR_MYEXCEPTION, "my exception 0");
		// Exception ex = new Exception("I am root.", fire0 );
		// Exception ex = new Exception("I am root." );
		CMyException fire = new CMyException(ExceptionNumber.ERR_MYEXCEPTION, "my exception 1", fire0);
		CMyException fire2 = new CMyException(ExceptionNumber.ERR_PARAM_INVALID, "my exception 2", fire);
		fire2.printStackTrace(System.out);

		System.out.println("-------------------");
		System.out.println(fire2.getMessage());

		System.out.println("-------------------");
		System.out.println(fire2.getStackTraceText());

		try {
			int a = 0;
			int b = 1 / a;
			System.out.println(b);
		} catch (Exception ex) {
			System.out.println(CMyException.getStackTraceText(ex));
		}
	}

}