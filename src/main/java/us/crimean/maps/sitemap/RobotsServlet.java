/**
 * Return robors.txt context
 */
package us.crimean.maps.sitemap;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Konstantin Chvilyov
 *
 */
@SuppressWarnings("serial")
public class RobotsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException
	        {
				final String server = request.getServerName();
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
