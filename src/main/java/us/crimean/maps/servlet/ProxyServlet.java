package us.crimean.maps.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import us.crimean.maps.gh.GraphHopperProxy;

@SuppressWarnings("serial")
public class ProxyServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(ProxyServlet.class.getName());
	private static final String URL_PARAM_NAME = "url";
	private static final String FIRST_PARAM_DIVIDER = "?";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException
	        {
				//super.doGet(request, response);
	            response.setContentType("application/xml;charset=UTF-8");
	            response.setCharacterEncoding("UTF-8");
	            //allow axess for browser's javascript from other domains
	            //TODO remove or correct for real site! 
	            //response.setHeader("Access-Control-Allow-Origin", "*"); //* allow for all
	            
	            //Can be with while symbol /%20: http://domain.tld/data.do?url=http://nominatim.openstreetmap.org/search/?q=%D0%9D%D0%B5%D0%B0%D0%BF%D0%BE%D0%BB%D1%8C%20%D0%A1%D0%BA%D0%B8%D1%84%D1%81%D0%BA%D0%B8%D0%B9&format=xml
	            /*
	            LightHttpClient httpClient = new LightHttpClient();
	            String url = request.getParameter(URL_PARAM_NAME);
	            if (null == url || url.length() < 3) {
	            	throw new ServletException("Incorrect " + URL_PARAM_NAME + " param value: " + url + " in request: " + request.getRequestURI());
	            }
	            Map<String,String> parameterMap = new TreeMap<String,String>();
	            //Map parameterMap = request.getParameterMap();
	            for (Object key : request.getParameterMap().keySet()) {
	            	if (!URL_PARAM_NAME.equals(key)) {
	            		parameterMap.put((String) key, request.getParameter((String) key));
	            	}
	            }
	             */
	            //So better to find request URL by first "url=" String
	            LightHttpClient httpClient = new LightHttpClient();
	            String url = request.getQueryString();
	            if (null == url || url.length() < 3) {
	            	throw new ServletException("Incorrect " + URL_PARAM_NAME + " param value: " + url + " in request: " + request.getRequestURI());
	            }
	        	//String requestDivider = FIRST_PARAM_DIVIDER + URL_PARAM_NAME + "=";
	        	String requestDivider = URL_PARAM_NAME + "=";
	            int firstParamPosition = url.indexOf(requestDivider);
	            if (firstParamPosition < 0) {
	            	throw new ServletException("No parameters in request:" + request.getRequestURI());
	            }
	            url = url.substring(firstParamPosition + requestDivider.length());
	            if (null == url || url.length() < 3) {
	            	throw new ServletException("Incorrect request:" + url + " in request:" + request.getRequestURI());
	            }
	            Map<String,String> parameterMap = new TreeMap<String,String>();
	            String responseString = httpClient.getRequest(url, parameterMap);
	            if ((null == responseString || responseString.length() < 80) && url.indexOf("route.php") > 0) {
		        	// url example: http://www.yournavigation.org/api/dev/route.php?flat=44.94491693799927&flon=34.09347383806334&tlat=44.945645930052336&tlon=34.079706592882&v=bicycle&fast=0&layer=mapnik&instructions=1
	            	//Server not ready to process requests. Please try again later.
	            	log.info("\"" + responseString + "\" response of request:" + url);
	            	responseString = GraphHopperProxy.requestFromYournavigationUrl(url);
		            if (null == responseString || responseString.length() < 80) {
		            	log.warning("\"" + responseString + "\" GraphHopper response of request:" + url);	            }
	            }
	            PrintWriter out = response.getWriter();
	            out.println(responseString);
	            out.close();
	        }
}
