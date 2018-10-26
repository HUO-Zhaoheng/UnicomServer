import javax.servlet.*;
import java.io.IOException;
import java.util.*;

public class LogFilter implements Filter{
    public void init(FilterConfig config) throws ServletException {
        Enumeration<String> parameterNames= config.getInitParameterNames();

        while(parameterNames.hasMoreElements()) {
            System.out.println(parameterNames.nextElement());
        }

    }
    public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws IOException,ServletException {
        System.out.println("doing Filter");

        chain.doFilter(request,response);

    }
    public void destroy(){

    }
}
