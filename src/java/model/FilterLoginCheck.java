
package model;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = {"/users-profile.html","/addproduct.html","/product-listning.html","/addressing.html","/orders.html"})
public class FilterLoginCheck implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpservletrequest = (HttpServletRequest) request;
        HttpServletResponse httpservletresponse = (HttpServletResponse) response;
        
        if(httpservletrequest.getSession().getAttribute("user")!=null){
            chain.doFilter(request, response);
        }else{
        httpservletresponse.sendRedirect("login.html");
        }
        
    }

    @Override
    public void destroy() {
        
    }
    
}
