/*
 * DiscoveryStartupFilter.java
 * Description:
 *		DiscoveryStartupFilter
 * Update Log:
 *	2008-12-16 下午06:10:17	FuChengrui
 *		Created
 */

package org.xiaotian.discovery.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 在 Servlet 2.3 (含)，及之前的版本中，用于确保 Discovery 先于其他初始化
 * 
 * @author  Martin (Fu Chengrui)
 * @see     DiscoveryStartupListener
 */
public class DiscoveryStartupFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    public void init(FilterConfig fc) throws ServletException {
        DiscoveryServletHelper.initDiscovery(fc.getServletContext());
    }

    public void destroy() {
        DiscoveryServletHelper.destroyDiscovery();
    }

}
