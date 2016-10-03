package us.crimean.maps.gh;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.PathWrapper;
import com.graphhopper.api.GraphHopperWeb;
import com.graphhopper.util.shapes.GHPoint;
import com.graphhopper.util.shapes.GHPoint3D;

import us.crimean.maps.servlet.ProxyServlet;
import us.crimean.maps.yn.YournavigationRequest;

public class GraphHopperProxy {
    private static final Logger log = Logger.getLogger(GraphHopperProxy.class.getName());
    private static final Pattern AND_PATTERN = Pattern.compile("&");

	/**
	 * url example: http://www.yournavigation.org/api/dev/route.php?flat=44.94491693799927&flon=34.09347383806334&tlat=44.945645930052336&tlon=34.079706592882&v=bicycle&fast=0&layer=mapnik&instructions=1
	 */
	public static String requestFromYournavigationUrl(String url) {
		GHRequest req = GHRequestFromYournavigationUrl(url);
	    final GraphHopperWeb gh = new GraphHopperWeb();
	    //insert my registered key here
	    gh.setKey("458ef488-ab27-435a-9b6a-d0c0cd488f88");
	    GHResponse res = gh.route(req);
        if (res.hasErrors()) {
        	String msg = "GHResponse errors:" + res.getErrors().toString();
			log.severe(msg);
        	return msg;
        }
		return responseToYournavigationString(res);
	}

	private static String responseToYournavigationString(GHResponse response) {
		if (response == null) {
        	String msg = "GHResponse is empty";
			log.severe(msg);
        	return msg;
		}
        PathWrapper path = response.getBest();
		if (path == null) {
        	String msg = "GHResponse path is empty";
			log.severe(msg);
        	return msg;
		}
		if (path.hasErrors()) {
        	String msg = "GHResponse path errors:" + path.getErrors();
			log.severe(msg);
        	return msg;
		}
		StringBuffer result = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		result.append("<kml xmlns=\"http://earth.google.com/kml/2.0\">");
		result.append("<Document>");
		result.append("<name>KML Samples</name>");
		result.append("<open>1</open>");
		result.append("<distance>");
		result.append(path.getDistance());
		result.append("</distance>");
		result.append("<traveltime>");
		result.append(path.getTime());
		result.append("</traveltime>");
		result.append("<description>");
		result.append(path.getDescription());
		result.append("</description>");
		result.append("<Folder>");
		result.append("<name>Paths</name>");
		result.append("<visibility>0</visibility>");
		result.append("<description>Examples of paths.</description>");
		result.append("<Placemark>");
		result.append("<name>Tessellated</name>");
		result.append("<visibility>0</visibility>");
		result.append("<description>");
		result.append("<![CDATA[If the <tessellate> tag has a value of 1, the line will contour to the underlying terrain]]>");
		result.append("</description>");
		result.append("<LineString>");
		result.append("<tessellate>1</tessellate>");
		result.append("<coordinates>");
		for (Iterator<GHPoint3D> iterator = path.getPoints().iterator(); iterator.hasNext();) {
			GHPoint3D point = iterator.next();
			if (point == null) {
				log.warning("Empty point");
			} else {
				result.append(point.lat);
				result.append(',');
				result.append(point.lon);
				result.append(' ');
			}
		}
		result.append("</coordinates>");
		result.append("</LineString>");
		result.append("</Placemark>");
		result.append("</Folder>");
		result.append("</Document>");
		result.append("</kml>");
		return result.toString();
	}

	/**
	 * url example: http://www.yournavigation.org/api/dev/route.php?flat=44.94491693799927&flon=34.09347383806334&tlat=44.945645930052336&tlon=34.079706592882&v=bicycle&fast=0&layer=mapnik&instructions=1
	 */
	private static GHRequest GHRequestFromYournavigationUrl(String url) {
		List<Entry<String, String>> params = paramsFromYournavigationUrl(url);
        // https://graphhopper.com/maps/?point=49.6724%2C11.3494&point=49.655%2C11.418
        YournavigationRequest ynRequest = new YournavigationRequest(params);
        return ynRequest.getGHRequest();
	}

	/**
	 * url example: http://www.yournavigation.org/api/dev/route.php?flat=44.94491693799927&flon=34.09347383806334&tlat=44.945645930052336&tlon=34.079706592882&v=bicycle&fast=0&layer=mapnik&instructions=1
	 */
	private static List<Entry<String, String>> paramsFromYournavigationUrl(String url) {
		List<Entry<String, String>> res = new ArrayList<Entry<String, String>>();
		if (null == url) {
			log.severe("Empty url:" + url);
			return res;
		}
		int questionMarkIndex = url.indexOf('?');
		if (questionMarkIndex < 0) {
			log.warning("Not found ? in URL: " + url);
			return res;
		}
		for (String paramPair : AND_PATTERN.split(url.substring(questionMarkIndex + 1))) {
			if (paramPair == null) {
				log.warning("Empty param pair");
				continue;
			}
			int equalIndex = paramPair.indexOf('=');
			if (equalIndex < 0) {
				log.warning("Not found = in param pair: " + paramPair);
				continue;
			}
			String paramName = paramPair.substring(0, equalIndex).trim();
			String paramValue = paramPair.substring(equalIndex+1);
			if (paramName.isEmpty() || paramValue.isEmpty()) {
				log.info("Empty param name or value in pair: " + paramPair);
				continue;
			}
			Entry<String, String> pair = new AbstractMap.SimpleEntry<String, String>( paramName, paramValue);
			res.add(pair);
		}
		return res;
	}

}
