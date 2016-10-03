/*
Main Route class: Routeplanner
A class in addition to Yours project
 for routing with OpenLayers and Gosmore
Requires: jQuery
Author: Konstantin Chvilyov
*/
var Routeplanner = {};

Routeplanner.distanceFactorByUnit = {}; // equals to: var distanceFactorByUnit = new Object();
Routeplanner.distanceFactorByUnit["km"] = 1;  // equals to: distanceFactorByUnit.km = 1;
Routeplanner.distanceFactorByUnit["mi"] = 1.609344;

/*
Function: elementClickMode
Parameters: mode - the name of clicked button
*/
Routeplanner.elementClickMode = function(mode) {
	switch (mode) {
		case 'sitemap':
			//TODO remove not used - just working example for future
			var url = '/sitemap';
			var self = this;
			this.request = $.get(url, {}, function(xml) {
				alert("Sitemap return: " + xml);
			}, "xml").error(function(jqXHR, textStatus, errorThrown) {
				alert("Sitemap ERROR: " + errorThrown);
			});
			break;
	}
}

/*
Returns HTML element DOM Object by it's identifier
	id - HTML element identifier
*/
Routeplanner.getElementById = function(id) {
	var elementDOMObject; 
	if (document.getElementById) {
		elementDOMObject = document.getElementById(id);
		if (elementDOMObject == null) {
			console.error("Can't find element DOM Object by id:" + id);
		}
	} else {
		var elementJQueryObject = $('#'+id);
		if (elementJQueryObject != null) {
			elementDOMObject = elementJQueryObject[0];
		}
		if (elementDOMObject == null) {
			console.error("Can't find DOM or jQuery Object by id:" + id);
		}
	}
	return elementDOMObject; 
}

/*
Division dividend by divisor
	precision - count of figures after point in result
*/
Routeplanner.division = function(dividend, divisor, precision) {
	if (divisor==null || divisor==0 || dividend==null) {
		console.error("Can't divide dividend:" + dividend + " by divisor:" +divisor+"!");
		return "???";
	} else if (divisor==1) {
		return dividend.toFixed(precision);	//toPrecision(precision).toFixed(precision);
	} else {
		return (dividend/divisor).toFixed(precision); //toPrecision(precision).toFixed(precision);
	}
}


/**
 * convert Waypoint name to the part of URI
 * Returns: string
 */
Routeplanner.clearWaypointName = function(name) {
	if (name == undefined) {
		console.warn('Undefined Waypoint Name');
		return '';
	}
	/* Replace all multi-spaces with a single '_' symbol by regular expression*/
	var res = name.replace(/\s{1,}/g,'_');
	/* Replace all multi-'_' symbols with a single '_' symbol */
	res = res.replace(/_{2,}/g,'_');
	/* Replace all divider's combinations*/
	res = res.replace(/,_/g, ',');
	res = res.replace(/_,/g, ',');
	/* Remove postal index range - two indexes and '-' in between */
	res = res.replace(/\d{5}(?:[-\s]\d{4})?_?-_?\d{5}(?:[-\s]\d{4})?,?/g, '');
	/* Remove one postal index */
	res = res.replace(/\d{5}(?:[-\s]\d{4})?,?/g, '');
	return res;
}

/**
 * convert Waypoint name to the part of URI
 * Returns: string
 */
Routeplanner.encodeWaypointName = function(name) {
	var clearName = Routeplanner.clearWaypointName(name);
	var res = encodeURIComponent(clearName);
	return res;
}

/**
 * convert the part of URI to Waypoint name
 * Returns: string
 */
Routeplanner.decodeUriPart = function(uriPart) {
	var res = decodeURIComponent(uriPart);
	return res;
}

/**
 * Returns: last character of string
 */
Routeplanner.lastChar = function(string) {
	if (string == undefined) {
		return '';
	}
	var length = string.length;
	if ( length < 1) {
		return '';
	}
	var res = string.substr(length - 1);
	return res;
}

/**
 * Returns: number of a common part of chars in the end of all strings in the array
 */
Routeplanner.commonLastCharsNumber = function(array) {
	var res = 0;
	if (array !== undefined && array.length !== undefined && array.length > 0) {
		for (var number = 1; number < 1000; number++) {
			var commonChar = undefined;
			for (var i = 0; i < array.length; i++) {
				if (array[i] == undefined || array[i].length == undefined || array[i].length < number ) {
					return res;
				} else {
					var char = array[i].substr(array[i].length - number, 1);
					if (char == undefined) { 
						return res;
					}
					if (commonChar == undefined) {
						commonChar = char;
					} else if (commonChar != char) {
						return res;
					}
				}
			}
			res = number;
		}
		console.warn("Return limited number of a common part in the end:" + res);
		return res;
	}
	return 0;
};
