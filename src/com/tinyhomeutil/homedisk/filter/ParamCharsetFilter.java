/**
 * 
 */
package com.tinyhomeutil.homedisk.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 字符集过滤器
 * @author jack
 *
 */
public class ParamCharsetFilter  implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String encoding = "utf-8";
		request.setCharacterEncoding(encoding); 
		response.setContentType("text/html;charset="+encoding); 
		chain.doFilter(request, response); 
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
