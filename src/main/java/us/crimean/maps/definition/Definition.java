package us.crimean.maps.definition;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import us.crimean.maps.definition.phrase.Phrase;
import us.crimean.maps.definition.phrase.PhraseInterface;
import us.crimean.maps.definition.phrase.StringPhrase;

/**
 * All special definitions of current domain name
 * @author Konstantin Chvilyov
 */
public class Definition {
    private static final Logger log = Logger.getLogger(Definition.class.getName());
	public static final String SPACE = " ";
	private static final String LOCALHOST_DOMAIN =	"localhost";
	
	private static final String HIKE_TRAVEL_ROUTEPLANNER_DOMAIN =	"routeplanner.hiketravel.info";
	private static final PhraseInterface HIKE_PHRASE =	new StringPhrase("Hike");
	private static final PhraseInterface TRAVEL_PHRASE =new StringPhrase("Travel");
	
	private static final String ROUTES_MAPS_ROUTEPLANNER_DOMAIN =	"routesmaps.com";
	private static final PhraseInterface ROUTES_PHRASE =	new StringPhrase("Routes");
	private static final PhraseInterface MAPS_PHRASE =new StringPhrase("Maps");
	
	private static final PhraseInterface ROUTEPLANNER_PHRASE =						new StringPhrase("Routeplanner");
	private static final String ROUTEPLANNER_PATH =					"/routeplanner";
	private static final String HELP_ROUTEPLANNER_PATH =			"/help-routeplanner";
	private static final String ABOUT_ROUTEPLANNER_PATH =			"/about-routeplanner";
	private static final String[] PATH = new String[]{ROUTEPLANNER_PATH, HELP_ROUTEPLANNER_PATH, ABOUT_ROUTEPLANNER_PATH};
	
	private static final String ABOUT =	"About";
	private static final PhraseInterface HIKE_TRAVEL_ROUTEPLANNER_PHRASE =	new Phrase(new PhraseInterface[][]{
			new PhraseInterface[]{HIKE_PHRASE
								, HIKE_PHRASE
								, HIKE_PHRASE
								, new StringPhrase("any")
								, new StringPhrase("The")
								, new StringPhrase("The best")	//5
			},
			new PhraseInterface[]{TRAVEL_PHRASE
								, TRAVEL_PHRASE
								, TRAVEL_PHRASE},
			new PhraseInterface[]{ROUTEPLANNER_PHRASE},
			new PhraseInterface[]{null
								, null
								, null
								, new StringPhrase("(route planner)")
								, new StringPhrase("(routing planner)")
								, new StringPhrase("(routes planner)")			//5
								, new StringPhrase(",route planner for hiking")
								, new StringPhrase(",route planner for any travelling")
								, new StringPhrase("(best travels route planner)")	//8
							}
			});
	private static final PhraseInterface ROUTES_MAPS_ROUTEPLANNER_PHRASE =	new Phrase(new PhraseInterface[][]{
			new PhraseInterface[]{ROUTES_PHRASE
								, ROUTES_PHRASE
								, ROUTES_PHRASE
								, new StringPhrase("any")
								, new StringPhrase("The")
								, new StringPhrase("The best")	//5
			},
			new PhraseInterface[]{MAPS_PHRASE
								, MAPS_PHRASE
								, MAPS_PHRASE},
			new PhraseInterface[]{ROUTEPLANNER_PHRASE},
			new PhraseInterface[]{null
								, null
								, null
								, new StringPhrase("(route planner)")
								, new StringPhrase("(routing planner)")
								, new StringPhrase("(routes planner)")			//5
								, new StringPhrase(",map route planner")
								, new StringPhrase(",route planner on map")
								, new StringPhrase("(best maps route planner)")	//8
							}
			});
	private static final PhraseInterface ROUTES_MAPS_ROUTEPLANNER_TITLE_PHRASE =	new Phrase(new PhraseInterface[][]{
			 new PhraseInterface[]{null
								, new StringPhrase("Help to use")
								, new StringPhrase(ABOUT)}
			,new PhraseInterface[]{ROUTES_MAPS_ROUTEPLANNER_PHRASE}
			,new PhraseInterface[]{new StringPhrase("is")
								, new StringPhrase(",")
								, new StringPhrase(",")}
			,new PhraseInterface[]{new StringPhrase("any map route planner,")}
			
			,new PhraseInterface[]{new StringPhrase("best")
								, new StringPhrase("best")
								, new StringPhrase("the best")}
			,new PhraseInterface[]{null
								, null
								, new StringPhrase("online tool for")}
			,new PhraseInterface[]{new StringPhrase("maps route")}
			,new PhraseInterface[]{new StringPhrase("planner")
								, new StringPhrase("planner")
								, new StringPhrase("planning")}
		});
	private static final PhraseInterface HIKE_TRAVEL_ROUTEPLANNER_TITLE_PHRASE =	new Phrase(new PhraseInterface[][]{
			 new PhraseInterface[]{null
								, new StringPhrase("Help to use")
								, new StringPhrase(ABOUT)}
			,new PhraseInterface[]{HIKE_TRAVEL_ROUTEPLANNER_PHRASE}
			,new PhraseInterface[]{new StringPhrase("is")
								, new StringPhrase(",")
								, new StringPhrase(",")}
			,new PhraseInterface[]{new StringPhrase("any travel route planner on map,")}
			
			,new PhraseInterface[]{new StringPhrase("best")
								, new StringPhrase("best")
								, new StringPhrase("the best")}
			,new PhraseInterface[]{null
								, null
								, new StringPhrase("routeplanner for a")}
			,new PhraseInterface[]{new StringPhrase("hike and bike travel route")}
			,new PhraseInterface[]{new StringPhrase("planner")
								, new StringPhrase("planner")
								, new StringPhrase("planning")}
		});
	private static final PhraseInterface ROUTES_MAPS_ROUTEPLANNER_DESCRIPTION_PHRASE =	new Phrase(new PhraseInterface[][]{
			 new PhraseInterface[]{null
								, null
								, new StringPhrase(ABOUT)}
			,new PhraseInterface[]{ROUTES_MAPS_ROUTEPLANNER_PHRASE}
			,new PhraseInterface[]{new StringPhrase("helps you create")
								, new StringPhrase("helps you plan")
								, new StringPhrase("helping you plan")}
			,new PhraseInterface[]{new StringPhrase("a map")
								, new StringPhrase("any map")
								, new StringPhrase("any maps")}
			,new PhraseInterface[]{new StringPhrase("routes")
								, new StringPhrase("route")
								, new StringPhrase("routing")}
			,new PhraseInterface[]{new StringPhrase("plans")
								, new StringPhrase("planning")
								, new StringPhrase("plan")}
			,new PhraseInterface[]{new StringPhrase(",")}
			,new PhraseInterface[]{null
								, new StringPhrase("best online routes on maps,")
								, new StringPhrase("best online route on maps,")}
			,new PhraseInterface[]{new StringPhrase("see each waypoint name,find places by name on map,see route directions,distance,time,change routing order,add and drag waypoints,open route permalink,export route to GPX file,load routes to maps from GPX files")}
	});
	private static final PhraseInterface HIKE_TRAVEL_ROUTEPLANNER_DESCRIPTION_PHRASE =	new Phrase(new PhraseInterface[][]{
			 new PhraseInterface[]{null
								, null
								, new StringPhrase(ABOUT)}
			,new PhraseInterface[]{HIKE_TRAVEL_ROUTEPLANNER_PHRASE}
			,new PhraseInterface[]{new StringPhrase("helps you create")
								, new StringPhrase("helps you plan")
								, new StringPhrase("helping you plan")}
			,new PhraseInterface[]{new StringPhrase("best")
								, new StringPhrase("any travel")
								, new StringPhrase("any travel")}
			,new PhraseInterface[]{new StringPhrase("routes")
								, new StringPhrase("route")
								, new StringPhrase("route")}
			,new PhraseInterface[]{new StringPhrase("on maps")
								, new StringPhrase("on map")
								, new StringPhrase("plan")}
			,new PhraseInterface[]{new StringPhrase(",")}
			,new PhraseInterface[]{null
								, new StringPhrase("best hike and bike travel route plan on maps,")
								, new StringPhrase("best travel routes on maps,")}
			,new PhraseInterface[]{new StringPhrase("see each place name,find places by name,see route directions,distance,travel time,change routing order,add and drag waypoints,open route permalink,export route to GPX file,load routes from GPX files")}
	});
	private static final PhraseInterface HIKE_TRAVEL_ROUTEPLANNER_ALT_PHRASE =	new Phrase(new String[][]{
			new String[]{"routeplanner travel route hike planner hike travelling routes planners travels routeplanners hiking"	//0
						,"any travel routeplanner route planner for any travelling route planner routes planners routeplanners"
						,"routeplanner travel route travels routes planners travelling routeplanners"
						,"the routeplanner travel route planner the routes planners routeplanners travelling"
						,"the best routeplanner travel route planner the best travels routes planners routeplanners"
						,"the travel routeplanner travels route planner the routes planners travelling routeplanners"			//5
						,"the travel routeplanner travels route planner travelling routes planners routeplanners"
						,"the routeplanner travel route planner travels the routes planners travelling routeplanners"
						,"the best travel routeplanner travels route planner the best travelling routes planners routeplanners"
						,"hike travel routeplanner hiking route planner routes planners routeplanners route planner for hiking"
						,"routeplanner travel route  travels routes planners travelling routeplanners"							//10
						,"Routeplanner route planner routes planners routeplanners"
						,"routeplanner travel route planner routes maps travels planners map travelling routeplanners"
						,"routeplanner travel route planner travels routes maps planners travelling routeplanners"
						,"help outeplanner route planner routes maps planners routeplanners"
						,"about routeplanner route planner routes maps planners routeplanners"									//15
						,"travel routeplanner travets route planner routes planners travelling routeplanners"
			}
	});
	
	public String getPageKeywords() {
		if (isServerName(HIKE_TRAVEL_ROUTEPLANNER_DOMAIN)) {
			return "routeplanner,maps,route planner,directions,routing planer,travel,GPX,travelling,hike,hiking,map,car,bike,bicycle,cycleroute,routes";
		} else {
			return "routes,maps,routeplanner,route,map,route planner,route map,directions,routing,route plan,map route,GPX";
		}
			
	}

	public String getShortTitle(int version) {
		PhraseInterface phrase = null;
		if (isServerName(HIKE_TRAVEL_ROUTEPLANNER_DOMAIN)) {
			phrase = HIKE_TRAVEL_ROUTEPLANNER_PHRASE;
		} else {
			phrase = ROUTES_MAPS_ROUTEPLANNER_PHRASE;
		}
		if (null == phrase) {
			return null;
		}
		return phrase.getFullVersion(version);
	}
	
	public String getShortTitle(int[] versions) {
		PhraseInterface phrase = null;
		if (isServerName(HIKE_TRAVEL_ROUTEPLANNER_DOMAIN)) {
			phrase = HIKE_TRAVEL_ROUTEPLANNER_PHRASE;
		} else {
			phrase = ROUTES_MAPS_ROUTEPLANNER_PHRASE;
		}
		if (null == phrase) {
			return null;
		}
		return phrase.getFullVersion(versions);
	}
	
	
	public String getPageTitle(int version) {
		PhraseInterface phrase = null;
		if (isServerName(HIKE_TRAVEL_ROUTEPLANNER_DOMAIN)) {
			phrase = HIKE_TRAVEL_ROUTEPLANNER_TITLE_PHRASE;
		} else {
			phrase = ROUTES_MAPS_ROUTEPLANNER_TITLE_PHRASE;
		}
		if (null == phrase) {
			return null;
		}
		return phrase.getFullVersion(version);
	}

	HttpServletRequest request;
	private boolean localDebug = true; //For debug at the local server

	public Definition(HttpServletRequest request) {
		super();
		this.request = request;
		if (!"UTF-8".equals(request.getCharacterEncoding())) {
			try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.warning("Can't set encoding \"UTF-8\" instead of \"" + request.getCharacterEncoding() + "\" for request:" + request.getRequestURI() + " \n" + e);
			}
		}
	}
	
	public String getPageTitle(int[] versions) {
		PhraseInterface phrase = null;
		if (isServerName(HIKE_TRAVEL_ROUTEPLANNER_DOMAIN)) {
			phrase = HIKE_TRAVEL_ROUTEPLANNER_TITLE_PHRASE;
		} else {
			phrase = ROUTES_MAPS_ROUTEPLANNER_TITLE_PHRASE;
		}
		if (null == phrase) {
			return null;
		}
		return phrase.getFullVersion(versions);
	}

	public String getPageDescription(int version) {
		PhraseInterface phrase = null;
		if (isServerName(HIKE_TRAVEL_ROUTEPLANNER_DOMAIN)) {
			phrase = HIKE_TRAVEL_ROUTEPLANNER_DESCRIPTION_PHRASE;
		} else {
			phrase = ROUTES_MAPS_ROUTEPLANNER_DESCRIPTION_PHRASE;
		}
		if (null == phrase) {
			return null;
		}
		return phrase.getFullVersion(version);
	}

	public String getAlt(int version) {
		// TODO remove all alts from anchors
		return HIKE_TRAVEL_ROUTEPLANNER_ALT_PHRASE.getFullVersion(version);
	}

	public String getAlt() {
		return getAlt(getPathIndex(request.getServletPath()));
	}
	
	public String getShortTitle() {
		return getShortTitle(getPathIndex(request.getServletPath()));
	}

	public String getPageTitle() {
		String pathDescription = getPathDescription();
		if (pathDescription != null && pathDescription.length() > 1) {
			return getShortTitle() + " " + pathDescription;
		}
		return getPageTitle(getPathIndex(request.getServletPath()));
	}

	public String getPageDescription() {
		return getPageDescription(getPathIndex(request.getServletPath()));
	}
	
	public String getPageUrl() {
		return (request.getRequestURL().toString()).toLowerCase();
	}
	
	public String getCanonicalUrl() {
		String res = "";
		res = addParameter(res, "to");
		res = addParameter(res, "from");
		res = addParameter(res, "in");
		for (int i=1; i<22; i++) {
			int resLength = res.length();
			res = addParameter(res, "via" + ((i == 1)?"":i));
			if (resLength == res.length()) {
				break;
			}
		}
		if (res.length() > 3) {
			return getPageUrl() + res;
		}
		return getPageUrl();
	}
	
	public String getServerUrl() {
	    String scheme = request.getScheme();             // http
	    String serverName = request.getServerName();     // hostname.com
	    int serverPort = request.getServerPort();        // 80
	    StringBuffer url =  new StringBuffer();
	    url.append(scheme).append("://").append(serverName);
	    if ((serverPort != 80) && (serverPort != 443)) {
	    	//not standard port
	        url.append(":").append(serverPort);
	    }
		return (url.toString()).toLowerCase();
	}
	
	private boolean isServerName(String serverName) {
		if (0 == serverName.compareToIgnoreCase(request.getServerName()) 
				|| (localDebug && 0 == LOCALHOST_DOMAIN.compareToIgnoreCase(request.getServerName()))) {
			return true;
		}
		return false;
	}
	
	private int getPathIndex(String path) {
		for (int index = 0; index < PATH.length; index++) {
			if (0 == PATH[index].compareToIgnoreCase(path)) {
				return index;
			}
		}
		log.warning("Default Index=0 for undefined path:" + path);
		return 0;
	}
	
	/**
	 * @return google analytics java script text
	 */
	public String getGoogleAnalytics() {
		if (isServerName(HIKE_TRAVEL_ROUTEPLANNER_DOMAIN)) {
			//<!-- This tracking code can be used on hiketravel.info or any hiketravel.info sub-domain -->
			return	"(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n" +
					"(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n" +
					"m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n" +
					"})(window,document,'script','//www.google-analytics.com/analytics.js','ga');\n" +
					"ga('create', 'UA-42403016-1', 'hiketravel.info');\n" +
					"ga('send', 'pageview');";
		} else	if (isServerName(ROUTES_MAPS_ROUTEPLANNER_DOMAIN)) {
			//<!-- This tracking code can be used on routesmaps.com or any its sub-domain -->
			return	"(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n" +
					"(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n" +
					"m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n" +
					"})(window,document,'script','//www.google-analytics.com/analytics.js','ga');\n" +
					"ga('create', 'UA-53892662-1', 'auto');\n" +
					"ga('send', 'pageview');";
		}

		// TODO add GA for other sites: maps.crimean.us!
		return "";
	}
	
	/**
	 * @return path description from url parameters from,to,via
	 */
	public String getPathDescription() {
		String res = "";
		res = addParameterDescription(res, "to");
		res = addParameterDescription(res, "from");
		res = addParameterDescription(res, "in");
		for (int i=1; i<22; i++) {
			int resLength = res.length();
			res = addParameterDescription(res, "via" + ((i == 1)?"":i));
			if (resLength == res.length()) {
				break;
			}
		}
		return res;
	}

	private String addParameterDescription(String res, String paramName) {
		String paramValue = request.getParameter(paramName);
		if (paramValue != null && paramValue.length() > 0) {
			try {
				res += (res.length() == 0 ? "" : " ") + paramName + " " + decodeWaypointName(paramValue);
			} catch (UnsupportedEncodingException e) {
				log.warning("Can't decode param "+paramName+":"+paramValue + " \n" + e);
			}
		}
		return res;
	}

	private String decodeWaypointName(String paramValue)
			throws UnsupportedEncodingException {
		String res;
		try {
			URI uri = new URI(paramValue);
			res = uri.getPath();
		} catch (URISyntaxException e) {
			log.warning("Can't decode value:" + paramValue + " \n" + e);
			res = URLDecoder.decode(paramValue, "UTF-8");
		}

		if (res != null) {
			res = res.replace("_", " ");
		}
		return res;
	}

	private String addParameter(String res, String paramName) {
		String paramValue = request.getParameter(paramName);
		if (paramValue != null && paramValue.length() > 0) {
			try {
				res += (res.length() == 0 ? "?" : "&") + paramName + "=" + URLEncoder.encode(paramValue, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.warning("Can't encode param "+paramName+":"+paramValue + " \n" + e);
			}
		}
		return res;
	}
	
	/**
	 * Count of the main indexes in groups
	 * Addition varians are addeed to the same prase
	private int[] mainIndexesCountByParts = {3,1,3};
	public int getAdditionalIndex(int partIndex, int additionalIndex) {
		if (mainIndexesCountByParts == null || partIndex >= mainIndexesCountByParts.length) {
			log.warning("mainIndexesCountByParts has not index for part:" + partIndex);
			return additionalIndex;
		}
		return mainIndexesCountByParts[partIndex] + additionalIndex;
	}

	public void setMainIndexesCountByParts(int[] mainIndexesCountByParts) {
		this.mainIndexesCountByParts = mainIndexesCountByParts;
	}
	 */
}
