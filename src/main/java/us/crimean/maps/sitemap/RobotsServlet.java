/**
 * Return robors.txt context
 */
package us.crimean.maps.sitemap;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Konstantin Chvilyov
 *
 */
public class RobotsServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(RobotsServlet.class.getName());

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException
	        {
				final String server = request.getServerName();
	            /*
	            response.setContentType("text/plain;charset=UTF-8");
	            response.setCharacterEncoding("UTF-8");
	            PrintWriter out = response.getWriter();
	            out.println("User-agent: *");
	            out.println("Allow: /");
	            out.println("Sitemap: http://" + server + "/sitemap.xml");
	            out.println("Host: " + server);
	            out.close();
	            */
	            response.setContentType("text/plain");
	        	OutputStream os = response.getOutputStream();
        		os.write("User-agent: *\n".getBytes());
        		os.write("Allow: /\n".getBytes());
        		os.write(("Sitemap: http://" + server + "/sitemap.xml\n").getBytes());
        		os.write(("Host: " + server).getBytes());
	        	os.flush();
	        	os.close();	
	        }
}
