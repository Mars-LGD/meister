package org.xiaotian.extend;

import java.util.Vector;

import org.xiaotian.constants.ExceptionNumber;

/**
 * 错误信息记录集合（堆栈）<BR>
 * 
 * @author xiaotian15
 * 
 */
public class CMyErrors {

    private final static int INIT_SIZE = 5;

    private Vector vErrors = null;

    /**
     * 默认构造函数
     */
    public CMyErrors() {
        vErrors = new Vector(INIT_SIZE);
    }

    // =======================================================
    // 逻辑接口

    // 添加错误
    // 返回本对象
    /**
     * 添加错误
     * 
     * @param _sError
     *            错误描述字符串
     * @return
     */
    public CMyErrors add(String _sError) {
        if (_sError != null)
            this.vErrors.add(_sError);
        return this;
    }

    /**
     * 添加错误
     * 
     * @param _ex
     *            异常对象
     * @return
     */
    public CMyErrors add(Exception _ex) {
        if (_ex != null)
            this.vErrors.add(_ex);
        return this;
    }

    public CMyErrors add(String _sInfo, Exception _ex) {
        this.add(_sInfo);
        this.add(_ex);
        return this;
    }

    /**
     * 添加错误
     * 
     * @param _errors
     *            错误对象
     * @return
     */
    public CMyErrors add(CMyErrors _errors) {
        if (_errors == null || _errors.isEmpty())
            return this;

        this.vErrors.addAll(_errors.vErrors);
        return this;
    }

    /**
     * 取错误总数
     * 
     * @return
     */
    public int size() {
        return this.vErrors.size();
    }

    /**
     * 判断是否为空
     * 
     * @return
     */
    public boolean isEmpty() {
        return this.vErrors.isEmpty();
    }

    /**
     * 取所有错误列表
     * 
     * @return 所有错误列表(Vector)
     */
    public Vector getErrors() {
        return this.vErrors;
    }

    /**
     * 取指定位置的错误信息对象
     * 
     * @param _index
     *            指定位置
     * @return
     */
    public Object getAt(int _index) {
        try {
            return this.vErrors.get(_index);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 清除错误列表
     * 
     * @return 对象本身
     */
    public CMyErrors clear() {
        this.vErrors.clear();
        return this;
    }

    // 字符串输出
    // 参数：_bIncludingNo 输出字符串时，每个错误是否输出序号
    /** @see public String toString( boolean _bIncludingNo ) */
    public String toString() {
        return this.toString(true);
    }

    /**
     * 输出错误信息
     * 
     * @param _bIncludingNo
     *            输出字符串时，每个错误是否输出序号，可省略，默认输出
     * @return
     */
    public String toString(boolean _bIncludingNo) {
        if (this.vErrors.size() == 0)
            return "";

        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < vErrors.size(); i++) {
            Object objError = vErrors.get(i);
            if (objError == null)
                continue;

            if (_bIncludingNo)
                buff.append("(" + i + ")");
            if (objError instanceof String) {
                buff.append((String) objError).append("\n");
            } else if (objError instanceof Throwable) {
                buff.append(
                        CMyException.getStackTraceText((Throwable) objError))
                        .append("\n");
            }
        }// end for
        return buff.toString();
    }// END: toString( )

    // /======================================================
    // test for class

    public static void main(String[] args) {
        CMyErrors errors = new CMyErrors();
        CMyException ex = new CMyException(ExceptionNumber.ERR_MYEXCEPTION,
                "My Exception");

        errors.add("This is an error message!");
        errors.add(new CMyException(ExceptionNumber.ERR_CONNECTION_GETFAIL,
                "CMyException error", ex));
        System.out.println(errors.toString());
    }
}