package com.xiaotian.extend;

/**
 * Created:         2002.6
 * Last Modified:   2002.6.10
 * Description:
 *      class CMyFileFilter —— 文件操作过虑器
 */

/**
 * <p>
 * Title: TRS 内容协作平台（TRS WCM）
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * class CMyFileFilter —— 文件操作过虑器
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001-2002 TRS信息技术有限公司
 * </p>
 * <p>
 * Company: TRS信息技术有限公司(www.trs.com.cn)
 * </p>
 * 
 * @author TRS信息技术有限公司
 * @version 1.0
 */

import java.io.File;
import java.io.FilenameFilter;

public class CMyFilenameFilter extends Object implements FilenameFilter {

    private String sExt;

    public CMyFilenameFilter(String _extendName) {
        sExt = _extendName;
    }

    public boolean accept(File _dir, String _name) {
        return _name.endsWith(sExt);
    }

}