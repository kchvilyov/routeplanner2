﻿/* Copyright (c) 2009, L. IJsselstein and others
  Yournavigation.org All rights reserved.
 */
/*
if (document.location.pathname.charAt(document.location.pathname.length-1) == "/") {
	var apiUrl = document.location.protocol + "//" + document.location.hostname + document.location.pathname + "api/dev/";
} else {
	var apiUrl = document.location.protocol + "//" + document.location.hostname + document.location.pathname + "/api/dev/";
}
*/
//var apiUrl = "http://www.yournavigation.org/api/dev/";	//cross-domain not working
//var apiUrl = "http://www.yournavigation.org/api/1.0/";	//incorrect data returned
//var apiUrl = "http://localhost:8888/data.do?url=http://www.yournavigation.org/api/dev/";
var apiUrl = "/data.do?url=http://www.yournavigation.org/api/dev/";

var namefinderUrl = "transport.php?url=http://gazetteer.openstreetmap.org/namefinder/search.xml&";
//var nominatimUrl = "http://nominatim.openstreetmap.org/";
var nominatimUrl = "/data.do?url=http://nominatim.openstreetmap.org/";
var reverseNamefinderUrl = "transport.php?url=http://dev.openstreetmap.nl/~rullzer/rev_namefinder/&";

/*
	Yours NameSpace with Classes and functions
	@requires OpenLayers
	@requires jQuery
 */
var routeCache = {};

// Create OpenLayers Control Click handler
OpenLayers.Control.Click = OpenLayers.Class(OpenLayers.Control, {
		defaultHandlerOptions: {
			'single': true,
			'double': false,
			'pixelTolerance': 0,
			'stopSingle': false,
			'stopDouble': false
		},
		/*
		 * Initialize is called when the Click control is activated
		 * It sets the behavior of a click on the map
		 */
		initialize: function() {
			this.handlerOptions = OpenLayers.Util.extend(
				{}, this.defaultHandlerOptions
				);
			OpenLayers.Control.prototype.initialize.apply(
				this, arguments
				);
			this.handler = new OpenLayers.Handler.Click(
				this, {
					'click': this.trigger
				}, this.handlerOptions
				);
		},

		/*
		 * How OpenLayers should react to a user click on the map.
		 * Get the LonLat from the user click and position
		 */
		trigger: function(e) {
			/* Old code
			var location = this.map.getLonLatFromViewPortPx(e.xy);
			this.route.updateWaypoint("selected", location);
			*/
			//fix selected position for Safari and Chrome(they don't support marker hotspot)
			var x = e.xy.x;
			var y = e.xy.y;
			var isWebKit = navigator.userAgent.indexOf("AppleWebKit") > -1;
			if(isWebKit){
			  x += 9;
			  y += 34;
			}
			var location = this.map.getLonLatFromViewPortPx(new window.OpenLayers.Pixel(x, y));
			this.route.updateWaypoint("selected", location);
		}
	}
);

/**
*
*  URL encode / decode
*  http://www.webtoolkit.info/
*
**/

var Url = {

    // public method for url encoding
    encode : function (string) {
        return escape(this._utf8_encode(string));
    },

    // public method for url decoding
    decode : function (string) {
        return this._utf8_decode(unescape(string));
    },

    // private method for UTF-8 encoding
    _utf8_encode : function (string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";

        for (var n = 0; n < string.length; n++) {

            var c = string.charCodeAt(n);

            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }

        return utftext;
    },

    // private method for UTF-8 decoding
    _utf8_decode : function (utftext) {
        var string = "";
        var i = 0;
        var c = 0;
        var c1 = 0;
        var c2 = 0;

        while ( i < utftext.length ) {

            c = utftext.charCodeAt(i);

            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            }
            else if((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i+1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            }
            else {
                c2 = utftext.charCodeAt(i+1);
                c3 = utftext.charCodeAt(i+2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }

        }

        return string;
    }

};

/*
	Class: Yours
	A class for routing with OpenLayers and Gosmore
	Requires:
		jQuery
		OpenLayers
*/
var Yours = {

};

Yours.lookupMethod = {
	nameToCoord : 1,
	coordToName : 2
};

/*
	Function: NamefinderLookup

	Parameters:

		value - value to lookup (string)
		wp - waypoint to place when lookup is succesfull (object)
		callback - Pass a function to handle the result message

	Returns:
		on success - 'OK'
		on failure - Error message
 */
Yours.NamefinderLookup = function(lookupMethod, value, wp, map, callback) {
	var parameters;
	switch (lookupMethod) {
	case Yours.lookupMethod.nameToCoord:
		parameters = "find=" + Url.encode(value) + "&max=1";
		$.get(namefinderUrl + parameters, {}, 
			function(xml) {
				var result = Yours.NameFinder(xml, wp, map);
				if (result == 'OK') {
					//route.draw();
					callback(result);
				} else {
					callback(result);
				}
			},
		"xml");
		break;
	case Yours.lookupMethod.coordToName:
		point = wp.lonlat.clone();
		newPoint = point.transform(map.projection, map.displayProjection);
		parameters = "lon=" + newPoint.lon + "&lat=" + newPoint.lat;
		$.get(reverseNamefinderUrl + parameters, {}, 
			function(xml) {
				var result = Yours.reverseNameFinder(xml, wp, map);
				if (result == 'OK') {
					callback(result);
				} else {
					callback(result);
				}
			},
		"xml");
		break;
	}
};

/*
	Function: NominatimLookup

	Parameters:

		value - value to lookup (string)
		wp - waypoint to place when lookup is succesfull (object)
		callback - Pass a function to handle the result message

	Returns:
		on success - 'OK'
		on failure - Error message
 */
Yours.NominatimLookup = function(lookupMethod, value, wp, map, callback) {
	var parameters;
	switch (lookupMethod) {
	case Yours.lookupMethod.nameToCoord:
		parameters = "q=" + Url.encode(value) + "&format=xml";
		$.get(nominatimUrl + "search/?"+ parameters, {}, 
			function(xml) {
				var result = Yours.Nominatim(xml, wp, map);
				if (result == 'OK') {
					//route.draw();
					callback(result);
				} else {
					callback(result);
				}
			},
		"xml");
		break;
	case Yours.lookupMethod.coordToName:
		if (wp !== undefined) {
			point = wp.lonlat.clone();
			newPoint = point.transform(map.projection, map.displayProjection);
			parameters = "lon=" + newPoint.lon + "&lat=" + newPoint.lat;
			// Cross-origin resource sharing (CORS) to support crossdomain requests in IE9 
			$.support.cors = true;
			$.get(nominatimUrl + "reverse/?" + parameters, {}, 
					function(xml) {
						var result = Yours.Nominatim(xml, wp, map);
						if (result == 'OK') {
							callback(result);
						} else {
							callback(result);
						}
					},
				"xml").error(function(jqXHR, textStatus, errorThrown) {
					callback(textStatus + ": " + errorThrown);
				});
		}
		break;
	}
};

/*
	Function: Export

	Parameters:

	Returns:

 */
Yours.Export = function(route) {
	//TODO: fix this for the new Waypoints collection method

	if (route.Segments.length > 0) {
		//TODO: rewrite to jQuery
		for (i = 0; i < document.forms['export'].elements.length; i++) {
			element = document.forms['export'].elements[i];
			if (element.name == 'type') {
				if (element.checked === true) {
					type = element.value;
				}
			}
		}
		if (type == 'wpt') {
			alert('this format is not supported yet');
			return;
		}
		first= true;
		var data = "";
		for (seg = 0; seg < route.Segments.length; seg++) {
			var components = route.Segments[seg].feature[0].geometry.components;
			for (wp = 0; wp < components.length; wp++) {
				if (first) {
					first = false;
				} else {
					data += ',';
				}
				point = components[wp].clone();
				newPoint = point.transform(route.map.projection, route.map.displayProjection);
				data += newPoint.x.toFixed(6) + ' ' + newPoint.y.toFixed(6);
			}
		}
		
		var html = "";
		var apiUrl2 = "http://www.yournavigation.org/api/dev/";
		
		html += "<html><head></head><body><form id='formid' method='post' action='" + ' ' + apiUrl2 + 'saveas.php'  + "'>";
		html += "<input type='hidden' name='type' value='" + type + "'/>";

		html += "<input type='hidden' name='data' value='" + data + "'/>";
		html += "</form><script type='text/javascript'>document.getElementById(\"formid\").submit()</script></body></html>";
		var newWindow = window.open(apiUrl2+"saveas.php", "Download");
		if (!newWindow) {
			return false;
		}

		newWindow.document.write(html);
	} else {
		alert('There is no route to export');
	}
};

/*
	Function: NameFinder

	Parameters:

		xml - file in xml format returned from a namefinder service
		wp - Waypoint that will be matched when a valid location is found in the xml


	Returns:
		on success - 'OK' and Waypoint with location, drawn on Waypoints Layer
		on failure - Error message

 */
Yours.NameFinder = function(xml, wp, map) {
	if (xml.childNodes.length > 0) {
		if (xml.documentElement.nodeName == "searchresults") {
			//first test if the response contains an error notice
			error = xml.documentElement;

			for (j = 0; j < error.attributes.length; j++) {
				switch(error.attributes[j].nodeName)
				{
					case "error":
						// error found, set status message and return false
						error = (error.attributes[j].nodeValue);
						return error;
				}
			}
			// No error found, continue -->
			var responseLonLat;
			for (var i = 0; i < xml.documentElement.childNodes.length; i++) {
				if (xml.documentElement.childNodes[i].nodeName == "named") {
					named = xml.documentElement.childNodes[i];
					responseLonLat = new OpenLayers.LonLat();
					for (j = 0; j < named.attributes.length; j++) {
						switch(named.attributes[j].nodeName)
						{
							case "lat":
								responseLonLat.lat = parseFloat(named.attributes[j].nodeValue);
								break;
							case "lon":
								responseLonLat.lon = parseFloat(named.attributes[j].nodeValue);
								break;
							case "name":
								wp.name = named.attributes[j].nodeValue;
								break;
							case "category":
								wp.category = named.attributes[j].nodeValue;
								break;
							case "rank":
								wp.rank = named.attributes[j].nodeValue;
								break;
							case "info":
								wp.info = named.attributes[j].nodeValue;
								break;
						}
					}
				}
			}
			if (responseLonLat !== undefined) {
				transformedLonLat = responseLonLat.transform(map.displayProjection, map.projection);
				wp.route.updateWaypoint(wp.position, transformedLonLat, wp.name);
				map.setCenter(transformedLonLat);
				return 'OK';
			}
		} else if (xml.documentElement.nodeName == "parsererror") {
			error = 'No results, please try other search';
			return error;
		}
	} else {
		// xml doc contains no searchresults
		error = 'No results, please try other search';
		return error;
	}
	error = "Something went wrong, but I don't know what";
	return error;
};

/*
	Function: Nominatim

	Parameters:

		xml - file in xml format returned from a namefinder service
		wp - Waypoint that will be matched when a valid location is found in the xml


	Returns:
		on success - 'OK' and Waypoint with location, drawn on Waypoints Layer
		on failure - Error message

 */
Yours.Nominatim = function(xml, wp, map) {
	var responseLonLat;
	var i;
	
	if (xml.childNodes.length > 0) {
		switch (xml.documentElement.nodeName) {
		case "searchresults":
			// No error found, continue -->
			
			for (i = 0; i < xml.documentElement.childNodes.length; i++) {
				if (xml.documentElement.childNodes[i].nodeName == "place") {
					named = xml.documentElement.childNodes[i];
					responseLonLat = new OpenLayers.LonLat();
					for (j = 0; j < named.attributes.length; j++) {
						switch(named.attributes[j].nodeName)
						{
							case "lat":
								responseLonLat.lat = parseFloat(named.attributes[j].nodeValue);
								break;
							case "lon":
								responseLonLat.lon = parseFloat(named.attributes[j].nodeValue);
								break;
							case "display_name":
								wp.name = named.attributes[j].nodeValue;
								break;
						}
					}
					break;	//1st result only at this point
				}
			}
			if (responseLonLat !== undefined) {
				var transformedLonLat = new OpenLayers.LonLat();
				transformedLonLat = responseLonLat.transform(map.displayProjection, map.projection);
				wp.route.updateWaypoint(wp.position, transformedLonLat, wp.name);
				map.setCenter(transformedLonLat);
				return 'OK';
			} else {
				error = 'No results, please try other search';
				return error;
			}
			break;
		case "reversegeocode":
			for (i = 0; i < xml.documentElement.childNodes.length; i++) {
				if (xml.documentElement.childNodes[i].nodeName == "result") {
					wp.name = Yours.getXmlElementTextContent(xml.documentElement.childNodes[i]);
					wp.update('OK');
					return 'OK';
				}
			}
			break;
		default:
			error = 'No results, please try other search';
			return error;
		}
	} else {
		// xml doc contains no searchresults
		error = 'No results, please try other search';
		return error;
	}
	error = "Something went wrong, but I don't know what";
	return error;
};

/*
Function: getXmlElementTextContent
Reason:	XML element object.textContent == undefined in IE 9

Parameters:
	el - XML element object

Returns:
	Xml Element Text Content
*/
Yours.getXmlElementTextContent = function(xmlElement) {
	return xmlElement.textContent || xmlElement.text;
};

Yours.status = {
	starting : 0,
	segmentStarted : 1,
	segmentFinished : 2,
	routeFinished : 3,
	error : 4,
	waypointUpdate : 5
};
	
/**
 * Class: Yours.Route
 *
 * Parameters:
 *   Waypoints - Array containing the waypoints of the route
 *   Start - The 'from' waypoint (Waypoints[0])
 *   End - The 'to' waypoint (Waypoints[Waypoints.lenght - 1])
 *   Selected - The currently selected waypoint
 *   Segments - The route segments
 *   distance - The total distance of the route
 *   nodes - The number of nodes of the route
 *   completeRoute - True if a complete route is available, false if only a partial or no route is available
 *   autoroute - True if changing waypoints should automatically start route calculation, false otherwise
 *   map - <OpenLayers.Map> Map on which the route is drawn
 *   Layer - <OpenLayers.Layer.Vector> Layer on which the route is drawn
 *   Markers - <OpenLayers.Layer.Vector> Layer on which the markers are drawn
 */
Yours.Route = function(map, customRouteCallback, customWaypointCallback) {
	/**
	 * Constructor: new Yours.Route(map)
	 * Constructor for a new Yours.Route instance.
	 *
	 * Parameters:
	 * map - {String} OpenLayers.Map
	 *
	 * <http://dev.openlayers.org/releases/OpenLayers-2.8/doc/apidocs/files/OpenLayers/Map-js.html#OpenLayers.Map.OpenLayers.Map>
	 *
	 * Examples:
	 * (code)
	 * // create a map and draw a simple route
	 * var MyFirstMap = new OpenLayers.Map('map');
	 * var ol_wms = new OpenLayers.Layer.WMS(
	 *		   "OpenLayers WMS",
	 *		   "http://labs.metacarta.com/wms/vmap0",
	 *		   {layers: 'basic'}
	 *	   );
	 * MyFirstMap.addLayers([ol_wms]);
	 * MyFirstMap.addControl(new OpenLayers.Control.LayerSwitcher());
	 *
	 * MyFirstMap.zoomToMaxExtent();
	 * myFirstRoute = new Yours.Route(MyFirstMap);
	 * var flat=51.158883504779;
	 * var flon=3.220214843821;
	 * var tlat=51.241492039675;
	 * var tlon=4.472656250021;
	 * myFirstRoute.waypoint("from").lonlat = new OpenLayers.LonLat(flon,flat);
	 * myFirstRoute.waypoint("to").lonlat = new OpenLayers.LonLat(tlon,tlat);
	 * myFirstRoute.draw(MyCallBack);
	 *
	 * //display the response from the route request:
	 * function MyCallBack(response) {
	 *   alert(response);
	 * }
	 * (end)
	 */
	var self = this;
	this.callback = customRouteCallback;
	this.waypointcallback = customWaypointCallback;

	this.completeRoute = false;
	this.autoroute = true;
	this.instructions = 1;

	// Used during rendering to store the state
	this.rendering = 0;
	this.renderQuiet = false;
	
	/**
	 * Property: Yours.Route.parameters
	 * { Array } with parameters for calculating the route
	 */
	this.parameters = {
		/**
		 * Property: Yours.Route.parameters.fast
		 * Method for route calculation
		 *
		 * 0 - shortest route (default)
		 * 1 - fastest route
		 */
		fast: '0',
		/**
		 * Property: Yours.Route.parameters.type
		 * Type of transportation to use for calculation
		 *
		 * motorcar - routing for regular cars
		 * hvg - Heavy goods, routing for trucks
		 * psv - Public transport, routing using public transport
		 * bicycle - routing using bicycle
		 * foot - routing on foot (default)
		 * goods
		 * horse
		 * motorcycle
		 * moped
		 * mofa
		 * motorboat
		 * boat
		 */
		//temporary stub
		//if foot does not work
		type: 'bicycle',
		//type: 'foot',
		layer: 'mapnik'
	};

	/**
	 * Function: Yours.Route.permalink()
	 * describing old URL permalink string with waypoints lonlat
	 *
	 * Returns:
	 * string
	 */
	this.permalink = function() {
		var permalink_url = '';
		if (this.Start.lonlat !== undefined) {
			var flonlat = this.Start.lonlat.clone().transform(this.map.projection,this.map.displayProjection);
			permalink_url += 'flat=' + flonlat.lat + '&flon=' + flonlat.lon;
		}
		//for each via
		if (this.Waypoints.length > 2) {
			for (var i = 0; i < this.Waypoints.length -1; i++) {
				if (this.Waypoints[i].type == 'via') {
					var wlonlat = this.Waypoints[i].lonlat.clone().transform(this.map.projection,this.map.displayProjection);
					permalink_url += '&wlat=' + wlonlat.lat + '&wlon=' + wlonlat.lon;
				}
			}
		}
		if (this.End.lonlat !== undefined) {
			var tlonlat = this.End.lonlat.clone().transform(this.map.projection,this.map.displayProjection);
			permalink_url += '&tlat=' + tlonlat.lat + '&tlon=' + tlonlat.lon;
		}
		permalink_url += '&v=' + this.parameters.type +
			'&fast=' + this.parameters.fast +
			'&layer=' + this.parameters.layer;
		return location.protocol + '//' + location.host + location.pathname + "?" + permalink_url;
	};

	/**
	 * TODO: Comlete and use or delete!
	 * Function: Yours.Route.permalinkWithPointNames()
	 * describing permalink string with UTF-8 waypoints names parameters in the end
	 *
	 * Returns:
	 * string in format:
	 * domain/?v=..&fast=..&layer=..&d=km or mi
	 * &from=waypoint_name&to=waypoint_name&via=waypoint_name;waypoint_name;..
	 */
	this.permalinkWithNames = function() {
		var names = [];
		var point_names = '';
		names.push( Routeplanner.clearWaypointName(this.End.name) );
		names.push( Routeplanner.clearWaypointName(this.Start.name) );
		//for each via
		if (this.Waypoints.length > 2) {
			for (var i = 0; i < this.Waypoints.length; i++) {
				if (this.Waypoints[i].type == 'via') {
					names.push( Routeplanner.clearWaypointName(this.Waypoints[i].name) );
				}
			}
		}
		var commonLastChars = '';
		if (names.length > 1) {
			//Is there common part in the end of all names?
			var commonNumber = Routeplanner.commonLastCharsNumber(names);
			if (commonNumber > 3) {
				//Should start from ,
				var firstIndex = names[0].indexOf(",", names[0].length - commonNumber);
				if (firstIndex > -1) {
					commonNumber = names[0].length - firstIndex;
					if (commonNumber > 3) {
						commonLastChars = names[0].substr(names[0].length - commonNumber);
						for (i = 0; i < names.length; i++) {
							names[i] = names[i].substr(0, names[i].length - commonNumber);
						}
					} else {
						commonNumber = 0;
					}
				}
			}
		}
		if (names.length > 0 && names[0] !== undefined) {
			point_names += '&to=' + Routeplanner.encodeWaypointName(names[0]);
		}
		if (names.length > 1 && names[1] !== undefined) {
			point_names += '&from=' + Routeplanner.encodeWaypointName(names[1]);
		}
		while (commonLastChars.length > 0 && (commonLastChars[0] == ',' || commonLastChars[0] == ' ')) {
			//delete first , or white symbols
			if (commonLastChars.length > 1) {
				commonLastChars = commonLastChars.substring(1, commonLastChars.length);
			} else {
				commonLastChars = "";
			}
		}
		if (commonLastChars.length > 0) {
			point_names += '&in=' + Routeplanner.encodeWaypointName(commonLastChars);
		}
		for (i = 2; i < names.length; i++) {
			point_names += '&via';
			if(i > 2) {
				point_names += (i-1);
			}
			point_names += '=' + Routeplanner.encodeWaypointName(names[i]);
		}
		return this.permalink() + point_names;
	};

	/**
	 * TODO: Not completed! Use or delete it!
	 * Function: Yours.Route.uriPermalink()
	 * describing new clear URI permalink string with UTF-8 waypoints names
	 *
	 * Returns:
	 * string in format:
	 * domain/to waypoint name/via waypoint name/..from waypoint name?v=..&fast=..&layer=..&d=km or mi
	 */
	this.uriPermalink = function() {
		var names = [];
		var permalink_url = '';
		names.push( Routeplanner.clearWaypointName(this.Start.name) );
		if (this.End.name !== undefined) {
			names.push( Routeplanner.clearWaypointName(this.End.name) );
		}
		//for each via
		if (this.Waypoints.length > 2) {
			for (var i = 0; i < this.Waypoints.length -1; i++) {
				if (this.Waypoints[i].type == 'via') {
					names.push( Routeplanner.clearWaypointName(this.Waypoints[i].name) );
				}
			}
		}
		//Is there common part in the end of all names?
		var commonNumber = Routeplanner.commonLastCharsNumber(names);
		var commonString = '';
		if (commonNumber > 3) {
			commonString = names[1].substr(names[1].length - commonNumber);
			for (i = 0; i < names.length; i++) {
				names[i] = names[i].substr(0, names[i].length - commonNumber);
			}
		}
		for (i = 0; i < names.length; i++) {
			if (i > 0) {
				permalink_url += '/';
			}
			permalink_url += encodeURIComponent(names[i]);
			if (i == 0) {
				permalink_url += '/' + encodeURIComponent(commonString);
			}
		}
		permalink_url += '?v=' + this.parameters.type +
			'&fast=' + this.parameters.fast +
			'&layer=' + this.parameters.layer;
		var permalink_url_begin = location.protocol + '//' + location.host + location.pathname;
		if (Routeplanner.lastChar(permalink_url_begin) != '/') {
			permalink_url_begin += '/';
		}
		return permalink_url_begin + permalink_url;
	};
	
	/*
		Function: directions
		Agregate directions for the segments
	*/
	this.directions = function() {
	//for each segment in route segments[]
	/*if(this.feature === undefined) {
			alert('Feature not set, cannot get directions');
		} else {

		}*/
	};
	
	/**
	 * Function: Yours.Route.waypoint(id)
	 *
	 * Parameters:
	 * 
	 * id - (optional) { integer } or { string } "from" or "to"
	 * 
	 * describing what waypoint to get from the <Waypoints> collection
	 *
	 * Returns:
	 *
	 * <Yours.Waypoint>
	 */
	this.waypoint = function(id) {
		switch(id) {
			case "from":
				wp = this.Waypoints[0];
				wp.type = id;
				break;
			case "to":
				wp = this.Waypoints[this.Waypoints.length - 1];
				wp.type = id;
				break;
			default:
				if (this.End.position == id || id === undefined) {
					wp= new Yours.Waypoint(this);
					wp.position = this.Waypoints.length - 1;
					wp.type = "via";
					
					// Add the waypoint to the array of Waypoints
					this.Waypoints.splice(wp.position, 0, wp);

					// Update the position of the end node, since the array has grown.
					this.Waypoints[this.Waypoints.length - 1].position = this.Waypoints.length - 1;
					this.End = this.Waypoints[this.Waypoints.length - 1];
				} else {
					wp = this.Waypoints[id];
				}
		}
		
		return wp;
	};
	
	/**
	 * Function: Yours.Route.addWaypoint(id)
	 *
	 * Parameters:
	 *   id - (optional) { integer } or { string } "from" or "to"
	 *        describing what waypoint to remove from the <Waypoints> collection
	 *
	 * Returns:
	 *   <Yours.Waypoint> - The Waypoint added
	 */
	this.addWaypoint = function(id) {
		if (id == "from") {
			id = 0;
		} else if (id == "to" || id === undefined) {
			id = this.Waypoints.length;
		}
		// Create the waypoint
		var waypoint = new Yours.Waypoint(this);
		waypoint.type = "via";
		// Add the waypoint to the array of Waypoints
		this.Waypoints.splice(id, 0, waypoint);
		// If the 'from' or 'to' is added, correct the types
		switch (id) {
			case 0:
				this.Start.type = "via";
				this.Start = this.Waypoints[0];
				this.Start.type = "from";
				break;
			case this.Waypoints.length - 1:
				this.End.type = "via";
				this.End.draw(); // Redraw using 'via' marker
				this.End = this.Waypoints[this.Waypoints.length - 1];
				this.End.type = "to";
				break;
		}
		// Renumber all waypoints starting with the inserted one
		for (var i = id; i < this.Waypoints.length; i++) {
			var wp = this.Waypoints[i];
			wp.position = i;
			wp.draw();
		}
		// Return inserted waypoint
		return waypoint;
	};
	
	/**
	 * Function: Yours.Route.removeWaypoint(id)
	 *
	 * Parameters:
	 *   id - (optional) { integer } or { string } "from" or "to"
	 *        describing what waypoint to add to the <Waypoints> collection
	 *
	 */
	this.removeWaypoint = function(id) {
		switch (id) {
			case "from":
			case 0:
				this.Start.destroy();
				if (this.Waypoints.length > 2) {
					// Delete waypoint
					this.Waypoints.splice(0, 1);
					// Make first waypoint 'from'
					this.Start = this.Waypoints[0];
					this.Start.type = "from";
					// Renumber all further waypoints
					for (var i = 0; i < this.Waypoints.length; i++) {
						var wp = this.Waypoints[i];
						wp.position = i;
						wp.draw();
					}
				}
				break;
			case "to":
			case this.Waypoints.length - 1:
				this.End.destroy();
				if (this.Waypoints.length > 2) {
					// Delete waypoint
					this.Waypoints.splice(this.Waypoints.length - 1, 1);
					// Make last waypoint 'to'
					this.End = this.Waypoints[this.Waypoints.length - 1];
					this.End.type = "to";
					this.End.draw();
				}
				break;
			default:
				// Delete waypoint
				this.Waypoints[id].destroy();
				this.Waypoints.splice(id, 1);
				// Renumber all further waypoints
				for (var i = id; i < this.Waypoints.length; i++) {
					var wp = this.Waypoints[i];
					wp.position = i;
					wp.draw();
				}
		}
		// Replan route
		if (this.autoroute) {
			this.renderQuiet = true;
			this.draw();
		}
	};

	/**
	 * Function: Yours.Route.updateWaypoint(id, lonlat, name)
	 *
	 * Parameters:
	 *   id - (optional) { integer } or { string } "from", "to" or "selected"
	 *        describing which waypoint to update
	 *   lonlat - <OpenLayers.LonLat> object holding the location of this waypoint
	 *   name - (optional) The name representing the given location
	 */
	this.updateWaypoint = function(id, lonlat, name) {
		var wp;
		switch (id) {
			case "from":
				wp = this.Start;
				break;
			case "to":
				wp = this.End;
				break;
			case "selected":
				wp = this.Selected;
				// Move selection to next waypoint if that one has no position yet
				if (this.Waypoints[this.Selected.position + 1] != undefined && this.Waypoints[this.Selected.position + 1].lonlat == undefined) {
					this.selectWaypoint(this.Selected.position + 1);
				} else {
					this.selectWaypoint();
				}
				break;
			default:
				wp = this.Waypoints[id];
		}
		wp.name = name;
		wp.lonlat = lonlat;
		wp.draw();
		if (this.autoroute) {
			this.renderQuiet = true;
			this.draw();
		}
	}

	/**
	 * Function: Yours.Route.selectWaypoint(id)
	 *
	 * Parameters:
	 *   id - (optional) { integer } or { string } "from", "to"
	 *        describing which waypoint to select
	 */
	this.selectWaypoint = function(id) {
		switch (id) {
			case "from":
				this.Selected = this.Start;
				break;
			case "to":
				this.Selected = this.End;
				break;
			case undefined:
				this.Selected = undefined;
				break;
			default:
				this.Selected = this.Waypoints[id];
		}
		// Setting the cursor on the layer only does not work, so the cursor is set on the container of all layers
		if (this.Selected === undefined) {
			$(this.Markers.div.parentNode).css("cursor",  "default");
			this.controls.click.deactivate();
		} else {
			this.controls.click.activate();
			$(this.Markers.div.parentNode).css("cursor",  "url(" + this.Selected.markerUrl() + ") 9 34, pointer"); //for IE, Opera not work! chrome, safari not shift cursor hotspot! So need to fix hotspot locations for chrome, safari (webckit) 
			//$(this.Markers.div.parentNode).css("cursor",  "url(markers/route-start.cur), pointer"); //for Opera not work! chrome, safari not shift cursor hotspot!
			//$(this.Markers.div.parentNode).css("cursor",  "url(markers/route-start.cur) 9 34, pointer"); //for IE, Opera not work! chrome, safari not shift cursor hotspot!
		}
	}

	/*
		Constructor: reset
		Initialize a Route Object
	*/
	//TODO: merge reset and clear into a single function
	this.reset = function(OpenLayersMap) {
		if (OpenLayersMap !== undefined) {
			this.map = OpenLayersMap;
			this.Layer = new OpenLayers.Layer.Vector("Route");
			this.Markers = new OpenLayers.Layer.Vector("Markers", {
				styleMap: new OpenLayers.StyleMap({
					"default": new OpenLayers.Style({
						graphicOpacity: 0.75,
						externalGraphic: '${image}',
						graphicWidth: 20,
						graphicHeight: 34,
						graphicXOffset: -10,
						graphicYOffset: -34
					}),
					"select": new OpenLayers.Style({
						graphicOpacity: 1,
						cursor: 'move'
					})
				})
			});
			this.map.addLayers([this.Layer, this.Markers]);
			var route = this;
			this.controls = {
				click: new OpenLayers.Control.Click({
					route: this
				}),
				drag: new OpenLayers.Control.DragFeature(this.Markers, {
					onComplete: function(feature, pixel) {
						// The pixel coordinate represents the mouse pointer, which is not the center of the image,
						// but the location where the user picked the image. Therefore, we use the geometry of the
						// image, which is the actual image location (respecting any offsets defining its base)
						var location = new OpenLayers.LonLat(feature.geometry.x, feature.geometry.y);
						var wp = feature.attributes.waypoint;
						wp.route.updateWaypoint(wp.position, location);
					}
				}),
				select: new OpenLayers.Control.SelectFeature(this.Markers, {hover: true})
			};
			// Add control to handle mouse clicks for placing markers
			this.map.addControl(this.controls.click);
			// Add control to handle mouse drags for moving markers
			this.map.addControl(this.controls.drag);
			this.controls.drag.activate();
			// Add control to show which marker we point at
			this.map.addControl(this.controls.select);
			this.controls.select.activate();
		}
		if (this.map === undefined) {
			error = "Yours.Route should be initialized with a map";
			return error;
		}
		this.Layer.destroyFeatures();
		for (var i = 0; i < this.Waypoints.length; i++) {
			this.Waypoints[i].destroy();
		}
		this.Waypoints[0] = new Yours.Waypoint(this);
		this.Start = this.Waypoints[0];
		this.Waypoints[0].position = 0;
		this.Waypoints[0].type = "from";
		this.Waypoints[1] = new Yours.Waypoint(this);
		this.Waypoints[1].position = 1;
		this.Waypoints[1].type = "to";
		this.End = this.Waypoints[1];
		return true;
	};

	/*
		Function: clear

		Reset the Route Object to an empty array with only a start and end Waypoint
	
	*/
	this.clear = function() {
		this.Waypoints = [];
		this.Segments = [];
		this.Markers.destroyFeatures();
		this.distance = 0;
		this.traveltime = 0;
		this.reset();
	};
		
	/*
	 * Function: draw
	 * Try to draw a route with the Waypoints[] collection
	 */
	this.draw = function() {
		// Clear a previous route result
		for (var i = 0; i < this.Segments.length; i++) {
			this.Segments[i].destroy();
		}
		this.Segments = [];
		
		// Determine the number of segments.
		this.callback(Yours.status.starting, "");
		var numsegs = this.Waypoints.length - 1;
		this.rendering = numsegs;
		this.completeRoute = false;
		// Loop the segments except the last one; no segment required from the to node.
		for (var i = 0; i < this.Waypoints.length - 1; i++) {
			this.Segments[i] = new Yours.Segment(this);
			if (this.Waypoints[i].lonlat === undefined || this.Waypoints[i + 1].lonlat === undefined) {
				if (this.renderQuiet) {
					// Finish silently
					this._segmentFinished(this.Segments[i]);
				} else {
					// Finish with error
					this._segmentError(this.Segments[i], 'No begin or end point specified!');
				}
			} else {
				this.callback(Yours.status.segmentStarted, i + 1);
				this.Segments[i].Start = this.Waypoints[i];
				this.Segments[i].End = this.Waypoints[i + 1];
				this.Segments[i].draw(this.callback);
			}
		}
	};

	/*
		Function: reverse

		Reverse the Waypoints[] collection and redraw the route

		Parameters:

		callback - Pass a function to handle the result message

	*/
	this.reverse = function() {
		if (this.Segments.length > 0) {
			//remove all the markers
			this.Layer.destroyFeatures();
			this.Waypoints.reverse();
			for (var i=0, len=this.Waypoints.length; i<len; ++i ) {
				this.Waypoints[i].position = i;
				if(this.Waypoints[i].position === 0) {
					this.Waypoints[i].type = "from";
					this.Start = this.Waypoints[0];
				} else if (this.Waypoints[i].position == this.Waypoints.length - 1) {
					this.Waypoints[i].type = "to";
					this.End = this.Waypoints[i];
				} else {
					this.Waypoints[i].type = "via";
				}
				this.Waypoints[i].draw();
			}
			this.draw();
		}
	};
	
	this._segmentFinished = function(segment) {
		this.rendering--;
		if (this.rendering == 0) {
			this.description = "";
			this.distance = 0;
			this.traveltime = 0;
			this.nodes = 0;
			this.completeRoute = true;
			for (i = 0; i < this.Segments.length; i++) {
				if (this.Segments[i].distance === undefined) {
					this.completeRoute = false;
				} else {
					this.description += this.Segments[i].description;
					this.distance += this.Segments[i].distance;
					this.traveltime += this.Segments[i].traveltime;
					this.nodes += this.Segments[i].nodes;
				}
			}
			self.callback(Yours.status.routeFinished, {quiet: this.renderQuiet});
			this.renderQuiet = false; // Set to false for next render
		}
	};
	
	this._segmentError = function(segment, error) {
		self.callback(Yours.status.error, "Could not finish segment because:\n\n"+error);
		this._segmentFinished(segment);
	};
	
	// Initialize the map
	this.Waypoints = [];
	this.Segments = [];
	this.distance = parseFloat(0);
	this.traveltime = 0;
	this.nodes = 0;
	this.reset(map);
};

/*
		Class: Yours.Waypoint

		Parameters:
		  type - Type of waypoint, can be one of from/to/via
		  name - Label returned from namefinder or entered by user
		  position - Position of the waypoint in the route sequence.
			  0 = startpoint,
			  highest val = endpoint
		  lonlat - <OpenLayers.LonLat> object holding the location of this waypoint
		  marker - <OpenLayers.Marker> object holding the marker representing this waypoint
	 */
Yours.Waypoint = function(ParentRoute)
{
	this.route = ParentRoute;
	var self = this;

	/*
	 * Function: markerUrl
	 * Get the url of the marker belonging to this waypoint.
	 */
	this.markerUrl = function() {
		switch (this.type) {
			case 'via':
				if (this.position !== undefined && this.position < 27)
					return 'markers/number' + this.position + '.png';
				else 
					return 'markers/blank.png'; 
			case 'from':
				return 'markers/route-start.png';
			case 'to':
				return 'markers/route-stop.png';
			default:
				return 'markers/marker-yellow.png';
		}
	}

	/*
	 * Function: draw
	 *
	 * Draw a Waypoint on the Vector Layer. If no lonlat is available, the
	 * Waypoint will not be drawn.
	 */
	this.draw = function() {
		if (this.lonlat !== undefined) {
			// Delete old marker, if available
			if (this.marker !== undefined) {
				this.route.Markers.removeFeatures([this.marker]);
				this.marker.destroy();
			}

			/* Create a marker and add it to the marker layer */
			this.marker = new OpenLayers.Feature.Vector(
				new OpenLayers.Geometry.Point(this.lonlat.lon, this.lonlat.lat),
				{waypoint: this, image: this.markerUrl()}
			);

			this.route.Markers.addFeatures([this.marker]);
			
			/* Update the marker's location information */
			if (this.name === undefined) {
				var that = this;
				Yours.NominatimLookup(Yours.lookupMethod.coordToName, "", that, this.route.map, this.update);
			}
		}
	};

	/*
		Function: destroy

		Remove Waypoint from the Vector Layer and destroy it's location information

	*/
	this.destroy = function() {
		if (this.marker !== undefined) {
			this.route.Markers.removeFeatures(this.marker);
			this.marker.destroy();
			this.marker = undefined;
			this.lonlat = undefined;
			this.name = undefined;
		}
	};
	
	this.update = function (result) {
		if (result == 'OK') {
			if (this.route !== undefined && this.route.waypointcallback !== undefined) {
				var that = this;
				this.route.waypointcallback(that);
			}
		}
	};
};

/*
	Class: Segment

	Parameters:
		start - a Waypoint Object that initializes the start of the segment
		end - a Waypoint Object that initializes the end of the segment
		distance - the total length of this segment
		parameters - get the routing parameters from the gui
		search - search part of the url to have a gosmore instance return the route
		permalink - the permalink for this segment
		feature - the feature representing this segment
		nodes - the number of components of the feature
*/
Yours.Segment = function(ParentRoute) {
	var self = this;
	this.route = ParentRoute;

	/*
	 * Function: create
	 * Create a route

	 * Parameters:
	 *   distance - the total length of this segment
	 *   feature - the feature representing this segment
	 */
	this.create = function(distance, traveltime, feature, description) {
		this.distance = distance;
		this.traveltime = traveltime;
		this.feature = feature;
		this.description = description;
		if(description === 0 || description === undefined) {
			this.route._segmentError(this, 'Segment has no length, or kml has no distance attribute');
		}
		this.nodes = this.feature[0].geometry.components.length;
		if (this.route.Layer !== undefined) {
			if (featureComponentsValid(this.feature)) {
				this.route.Layer.addFeatures(this.feature);
			}
		}
	};
	
	/*
	 * Checking is valid the feature Components

	 * Parameters:
	 *   feature - the feature representing this segment
	 *   if not - log warning massage
	 *   TODO: fix the reason of invalid data x and y in component for such segmet
	 */
	featureComponentsValid = function(feature) {
		var error;
		if (feature !== undefined) {
			if (feature.length > 0) {
				if (feature[0].geometry !== undefined) {
					if (feature[0].geometry.components !== undefined) {
						if (feature[0].geometry.components.length > 1) {
							return true;
						} else {
							if  (feature[0].geometry.components.length == 1) { 
								error = feature[0].id + " feature[0].geometry.components[0] x=" + feature[0].geometry.components[0].x + ",y=" + feature[0].geometry.components[0].y;
							} else {
								error = feature[0].id + " feature[0].geometry.components.length = " + feature[0].geometry.components.length;
							}
						}
					} else error = feature[0].id + " feature[0].geometry.components undefined";
				} else error = feature[0].id + " feature[0].geometry undefined";
			} else error = "feature length 0";
		} else error = "feature undefined";
		error = "Can't add segment to route: " + error;
		console.warn(error);
		return false;
	};

	/*
	 * Function: parseKML
	 * Create a route from the kml file returned from the gosmore service
	 *
	 * Parameters:
	 *   xml - The xml returned by the server
	 * Have fixed for IE9
	 */
	this.parseKML = function(xml) {
		if (xml.childNodes.length > 0) {
			// Check to make sure that kml is returned.
			if (xml.getElementsByTagName('kml').length > 0) {
					//it can be first or second(in IE9) child node
					var description = Yours.getXmlElementTextContent(xml.getElementsByTagName('description')[0]);
					var traveltime = parseInt(Yours.getXmlElementTextContent(xml.getElementsByTagName('traveltime')[0]));
					var distance = Yours.getXmlElementTextContent(xml.getElementsByTagName('distance')[0]);
					if(distance === 0 || distance === undefined) {
						this.route._segmentError(this, 'Segment has no length, or kml has no distance attribute');
					} else {
						var options = {};
						options.externalProjection = this.route.map.displayProjection;
						options.internalProjection = this.route.map.projection;
						var kml = new OpenLayers.Format.KML(options);
						var distance = parseFloat(distance);
						var feature = kml.read(xml);
						this.create(distance, traveltime, this.cloneFeature(feature), description);
						this.route._segmentFinished(this);
						// Add to cache
						routeCache[this.search] = {distance: distance, traveltime: traveltime, feature: feature, description: description};
					}
			}
			else if (xml.getElementsByTagName('error').length > 0) {
					var error = Yours.getXmlElementTextContent(xml.getElementsByTagName('error')[0]);
					this.route._segmentError(this, error);
			}
			else {
					this.route._segmentError(this, 'Response is no kml, segment cannot be constructed');
			}
		} else {
			this.route._segmentError(this, 'No segment found!');
		}
	};

	this.draw = function() {
		var flonlat= this.Start.lonlat.clone().transform(this.route.map.projection, this.route.map.displayProjection);
		var tlonlat= this.End.lonlat.clone().transform(this.route.map.projection, this.route.map.displayProjection);
/*
		if (flonlat.lon > 3.2 && flonlat.lon < 7 && flonlat.lat > 50.7 && flonlat.lat < 53.6 && tlonlat.lon > 3.2 && tlonlat.lon < 7 && tlonlat.lat > 50.7 && tlonlat.lat < 53.6) {
			// Test send Dutch requests to rullzer
			apiUrl = "proxy.php?u=http://dev.openstreetmap.nl/~rullzer/yours/api/dev/";
			//apiUrl = "transport.php?url=http://dev.openstreetmap.nl/~rullzer/yours/api/dev/";
		}
	*/		
		this.search = 'flat=' + flonlat.lat +
			'&flon=' + flonlat.lon +
			'&tlat=' + tlonlat.lat +
			'&tlon=' + tlonlat.lon;
		this.search += '&v=' + this.route.parameters.type;
		this.search += '&fast=' + this.route.parameters.fast +
			'&layer=' + this.route.parameters.layer +
			'&instructions=' + this.route.instructions;
		this.permalink = location.protocol + '//' + location.host + location.pathname + "?" + this.search;
		if (routeCache[this.search] === undefined) {
			// Not in cache, request from server
			var url = apiUrl + 'route.php?' + this.search;
			var self = this;
			this.request = $.get(url, {}, function(xml) {
				self.request = undefined;
				self.parseKML(xml);
			}, "xml").error(function(jqXHR, textStatus, errorThrown) {
				self.route._segmentError(self, textStatus + ": " + errorThrown);
			});
		} else {
			// In cache, retreive from there
			var s = routeCache[this.search];
			this.create(s.distance, s.traveltime, this.cloneFeature(s.feature), s.description);
			this.route._segmentFinished(this);
		}
	};
	
	/*
	 * Function cloneFeature
	 *
	 * Create a clone of a feature
	 */
	this.cloneFeature = function(feature) {
		var featureClone = [];
		for (var i = 0; i < feature.length; i++) {
			featureClone[i] = feature[i].clone();
		}
		return featureClone;
	}

	/*
		Function: profile

		Generate the altitude profile, requires altitude service
	*/

	this.profile = function() {
		//TODO: does not work, needs fixing.
		var geom = this.feature[0].geometry.clone();
		var length = geom.components.length;
		//TODO: clearify; why is the length required to be less then 300?
		if (length < 300) {
			// Build the profile GET request
			url = "";
			lats = "?lats=";
			lons ="&lons=";
			for (var i = 0; i < geom.components.length; i++) {
				if (i > 0) {
					lats+=",";
					lons+=",";
				}
				point = geom.components[i].transform(map.projection, map.displayProjection);
				lons += roundNumber(point.x, 5);
				lats += roundNumber(point.y, 5);
			}

			// Determine which profile server to query
			// The server configurations shoul come from a config file someday
			start = geom.components[0];
			stop  = geom.components[length-1];

			if (start.x > 0 && start.x < 10 && start.y > 49 && start.y < 54 && stop.x > 0 && stop.x < 10 && stop.y > 49 && stop.y < 54) {
				// Benelux + western Germany
				url = "http://altitude.openstreetmap.nl/profile/gchart";
			}
			else if (start.x > 20 && start.x < 31 && start.y > 43 && start.y < 49 && stop.x > 20 && stop.x < 31 && stop.y > 43 && stop.y < 49) {
				// Romania + Moldova
				url = "http://profile.fedoramd.org/profile/gchart";
			}
			else if (start.x > 10 && start.x < 16 && start.y > 49 && start.y < 55 && stop.x > 10 && stop.x < 16 && stop.y > 49 && stop.y < 55) {
				// Eastern Germany
				url = "http://profile.fedoramd.org/profile/gchart";
			}
			else if (start.x > -179 && start.x < -44 && start.y > -56 && start.y < 60 && stop.x > -179 && stop.x < -44 && stop.y > -56 && stop.y < 60) {
				// North and South America
				url = "http://labs.metacarta.com/altitude/profile/gchart";
			}
			else if (start.x > 23 && start.x < 33 && start.y > 50 && start.y < 57 && stop.x > 23 && stop.x < 33 && stop.y > 50 && stop.y < 57) {
				// Belarus
				url = "http://altitude.komzpa.net/profile/gchart";
			}
			/*
		else {
			// Everything else
			url = "http://labs.metacarta.com/altitude/profile/gchart";
		}
				 */

			// Load the profile image in the browser
			html = $('#feature_info').html();
			if (url.length > 0) {
				url += lats + lons;
				html += '<p>Altitude profile:<br><img src="'+url+'" alt="altitude profile for this route">';
				html += "</p>";
			}
			else {
				html += '<p><font color="red">Note: Altitude profile only available in <a href="http://wiki.openstreetmap.org/index.php/YOURS#Altitude_profile">certain areas</a>.</font></p>';
			}
			return html;
		}
	};

	/*
		Function: directions
		Get directions for the segment
	*/
	this.directions = function() {
		if(this.feature === undefined) {
			return 'Feature not set, cannot get directions';
		} else {
			len = this.feature.geometry.getLength();
			return len;
		}
	};

	/*
	 * Function: destroy
	 * Remove Segment from the Vector Layer and destroy it's location information
	 */
	this.destroy = function() {
		if (this.request !== undefined) {
			this.request.abort();
		}
		if (this.feature !== undefined) {
			this.route.Layer.removeFeatures(this.feature);
			this.feature = undefined;
		}
	};
};

