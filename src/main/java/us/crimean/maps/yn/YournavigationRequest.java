package us.crimean.maps.yn;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.graphhopper.GHRequest;
import com.graphhopper.util.shapes.GHPoint;

import us.crimean.maps.gh.GraphHopperProxy;

public class YournavigationRequest {
    private static final Logger log = Logger.getLogger(YournavigationRequest.class.getName());

	private String flat;
	private String flon;
	private String tlat;
	private String tlon;
	private List<String> wlat = new ArrayList<String>();
	private List<String> wlon = new ArrayList<String>();
	private String v;
	private String fast;
	private String layer;
	private String instructions;

	/**
	 * params example: 
	 * flat=44.94491693799927
	 * flon=34.09347383806334
	 * wlat=44.94077697132961
	 * wlon=34.100330620148554
	 * wlat=44.94743524084706
	 * wlon=34.10314586568292
	 * tlat=44.945645930052336
	 * tlon=34.079706592882
	 * v=bicycle
	 * fast=0
	 * layer=mapnik
	 * <description>To enable simple instructions add: 'instructions=1' as parameter to the URL</description>
	 * instructions=1
	 */
	public YournavigationRequest(List<Entry<String, String>> params) {
		for (Entry<String, String> param : params) {
	    	if ("flat".equalsIgnoreCase(param.getKey())) {
	    		flat = param.getValue();
	    	} else if ("flon".equalsIgnoreCase(param.getKey())) {
	    		flon = param.getValue();
	    	} else if ("tlat".equalsIgnoreCase(param.getKey())) {
	    		tlat = param.getValue();
	    	} else if ("tlon".equalsIgnoreCase(param.getKey())) {
	    		tlon = param.getValue();
	    	} else if ("wlat".equalsIgnoreCase(param.getKey())) {
	    		wlat.add(param.getValue());
	    	} else if ("wlon".equalsIgnoreCase(param.getKey())) {
	    		wlon.add(param.getValue());
	    	} else if ("v".equalsIgnoreCase(param.getKey())) {
	    		v = param.getValue();
	    	} else if ("fast".equalsIgnoreCase(param.getKey())) {
	    		fast = param.getValue();
	    	} else if ("layer".equalsIgnoreCase(param.getKey())) {
	    		layer = param.getValue();
	    	} else if ("instructions".equalsIgnoreCase(param.getKey())) {
	    		instructions = param.getValue();
	    	}
		}
    }

	public boolean isValid() {
		return false;
	}

	public GHRequest getGHRequest() {
        GHRequest req = new GHRequest();
		if (flat == null) {
			log.severe("flat undefined");
			return null;
		}
		if (flon == null) {
			log.severe("flon undefined");
			return null;
		}
		if (tlat == null) {
			log.severe("tlat undefined");
			return null;
		}
		if (tlon == null) {
			log.severe("tlon undefined");
			return null;
		}
        req.addPoint(new GHPoint(Double.parseDouble(flat), Double.parseDouble(flon)));
        if (wlat.size() != wlon.size()) {
			log.warning("wlat size:" + wlat.size() + " not equal to wlon size:" + wlon.size());
        } else {
        	for (int i = 0; i < wlat.size(); i++) {
                req.addPoint(new GHPoint(Double.parseDouble(wlat.get(i)), Double.parseDouble(wlon.get(i))));
        	}
        }
        req.addPoint(new GHPoint(Double.parseDouble(tlat), Double.parseDouble(tlon)));

		/**
		 * Property: Yours.Route.parameters.type
		 * Type of transportation to use for calculation
		 * Using in v parameter 	 
		 * F.e. v=bicycle
		 * motorcar - routing for regular cars
		 * hvg - Heavy goods, routing for trucks
		 * psv - Public transport, routing using public transport
		 * bicycle - routing using bicycle
		 * cycleroute - Bicycle (routes)
		 * foot - routing on foot (default)
		 * goods
		 * horse - ? not used at routesmaps
		 * motorcycle
		 * moped - ? not used at routesmaps
		 * mofa - ? not used at routesmaps
		 * motorboat - ? not used at routesmaps
		 * boat - ? not used at routesmaps
		 */
        /**
         *  Set vehicle like car, bike, foot, ...
         *  @See https://graphhopper.com/api/1/docs/supported-vehicle-profiles/
         */
        if (v == null) {
            req.setVehicle("foot");
        } else if ("motorcar".equalsIgnoreCase(v)) {
       		//routing for regular cars
        	req.setVehicle("car");
        } else if ("hvg".equalsIgnoreCase(v)) {
       		//Heavy goods, routing for trucks
        	//Truck like a MAN or Mercedes-Benz Actros
        	req.setVehicle("truck");
        } else if ("psv".equalsIgnoreCase(v)) {
       		//Public transport, routing using public transport
        	//bus where as public transport marked ways are allowed (psv ways and lanes)
        	req.setVehicle("bus");
        } else if ("goods".equalsIgnoreCase(v)) {
       		//small_truck Small truck like a Mercedes Sprinter, Ford Transit or Iveco Daily
        	req.setVehicle("small_truck");
        } else if ("bicycle".equalsIgnoreCase(v)) {
        	//routing using bicycle
        	/**
        	 * bike Trekking bike avoiding hills bike access bike image
        	 */
        	req.setVehicle("bike");
        } else if ("cycleroute".equalsIgnoreCase(v)) {
        	//cycleroute - Bicycle (routes)
        	/**
        	 * mtb Mountainbike bike access Mountainbike image
        	 * racingbike Bike preferring roads
        	 * https://graphhopper.com/api/1/route?point=44.5927%2C33.801086&point=44.517233%2C33.984729&type=json&key=016f1b38-62f0-4a2b-88f7-dc5b743a9b56&locale=ru-RU&vehicle=racingbike&weighting=fastest&elevation=true
        	 */
        	req.setVehicle("racingbike");
        } else if ("foot".equalsIgnoreCase(v)
        	||"motorcycle".equalsIgnoreCase(v)) {
            req.setVehicle(v);
        } else {
        	log.warning("Try unknown vehicle=" + v);
            req.setVehicle(v);
        }

		/**
		 * @See https://graphhopper.com/api/1/docs/routing/#api-clients-and-examples
		 * Property: Yours.Route.parameters.fast
		 * Method for route calculation
		 * 0 - shortest route (default)
		 * 1 - fastest route
		 */
		//layer: 'mapnik'
        
        
        // Optionally enable/disable path geometry information, default is true
        // If the points for the route should be calculated at all printing out only distance and time.
        req.getHints().put("calc_points", true);
        
        // note: turn off instructions and calc_points if you just need the distance or time 
        // information to make calculation and transmission faster
        //
        // Optionally set specific locale for instruction information, supports already over 25 languages,
        // defaults to English
        //req.setLocale(Locale.GERMAN);

        /**
         * elevation - высота (над уровнем моря)
         *  Optionally enable/disable elevation in output PointList, 
         *  currently bike and foot support elevation, default is false
         * If true a third dimension - the elevation - is included in the polyline or in the GeoJson. 
         * IMPORTANT: If enabled you have to use a modified version of the decoding method
         *  or set points_encoded to false See the points_encoded attribute for more details. 
         * Additionally a request can fail if the vehicle does not support elevation. 
         * See the features object for every vehicle.
         */
        //req.getHints().put("elevation", false);
        
        // Optionally enable/disable turn instruction information, defaults is true
        // If instruction should be calculated and returned.
        //req.getHints().put("instructions", true);
		if (instructions != null) {
            req.getHints().put("instructions", instructions.equals('1'));
        }
        return req;
	}

}
