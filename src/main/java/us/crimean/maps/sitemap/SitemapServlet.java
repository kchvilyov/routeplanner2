package us.crimean.maps.sitemap;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import comlanka.appengine.sitemap.ChangeFreq;
import comlanka.appengine.sitemap.W3CDateFormat;
import comlanka.appengine.sitemap.WebSitemapGenerator;
import comlanka.appengine.sitemap.WebSitemapUrl;
import comlanka.appengine.sitemap.W3CDateFormat.Pattern;

@SuppressWarnings("serial")
public class SitemapServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(SitemapServlet.class.getName());
	private static final String HTTP = "http://";
	private File myDir = new File("/sitemap"); //not used in this implementation

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException
	        {
				final String protocol_server = HTTP + request.getServerName();
	            // Use DAY pattern (2009-02-07), Greenwich Mean Time timezone
	    		W3CDateFormat dateFormat = new W3CDateFormat(Pattern.DAY); 
	    		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    		WebSitemapGenerator wsg = WebSitemapGenerator.builder(protocol_server, myDir)	//.autoValidate(true) - haven't file so can recycle!
	    		    .dateFormat(dateFormat).build(); // actually use the configured dateFormat
	    		// this will configure the URL with lastmod=now, priority 0.1-1.0, changefreq=hourly,.. 
	    		WebSitemapUrl url = new WebSitemapUrl.Options(protocol_server + "/routeplanner")
	    	    				.lastMod(new Date()).priority(0.8).changeFreq(ChangeFreq.DAILY).build();
	    		wsg.addUrl(url);
	    		url = new WebSitemapUrl.Options(protocol_server + "/about-routeplanner")
	    						.lastMod(new Date()).priority(0.8).changeFreq(ChangeFreq.DAILY).build();
	    		wsg.addUrl(url);
	    		url = new WebSitemapUrl.Options(protocol_server + "/help-routeplanner")
	    						.lastMod(new Date()).priority(0.8).changeFreq(ChangeFreq.DAILY).build();
	    		wsg.addUrl(url);

	    		response.setContentType("application/xml;charset=UTF-8");
	            response.setCharacterEncoding("UTF-8");
	            PrintWriter out = response.getWriter();
	    		wsg.writeSiteMap(out);
	        }
}
