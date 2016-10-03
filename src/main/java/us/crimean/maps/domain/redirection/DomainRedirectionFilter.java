package us.crimean.maps.domain.redirection;

import java.io.IOException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DomainRedirectionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURL = httpRequest.getRequestURL().toString();
        URL url = new URL(requestURL);

        if (url.getHost().startsWith("www.")) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            String newLocation;
            if (null == httpRequest.getQueryString()) {
            	newLocation = requestURL.replace("://www.", "://");
            } else {
            	newLocation = requestURL.replace("://www.", "://") + "?" + httpRequest.getQueryString();
            }
            httpServletResponse.setHeader("Location", newLocation);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}