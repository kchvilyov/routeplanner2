<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@ include file="head.html" %>
	</head>
	<body>
		<%@ include file="header.html" %>
       <div id="main2">
			<div id="outer"><!-- this div fixes a render problem in Opera-->
				<h1><%=page_title%></h1>
				<h3>Markers explained</h3>
				<ul>
					<li><img src="markers/route-start.png" alt="Route start" />Start, marks the beginpoint of the route.</li>
					<li><img src="markers/route-stop.png" alt="Route stop" />Stop, marks the endpoint of the route.</li>
					<li><img src="markers/yellow.png" alt="Add waypoint" />Clicking on 'Add waypoint' button will add a waypoint to the route.</li>
					<li><img src="markers/number1.png" alt="First waypoint" />Waypoint 1, the first waypoint in the route. You can add unlimited count of waypoints.</li>
				</ul>
				<h3>Create a route</h3>
				A route is created by placing at least one Start and one Finish marker on the map. Placing a marker on the map is easy. Open the 'Route' tab and either:
				<ul>
					<li>Click on a green, orange or number balloon and then click somewhere on the map. You should see that a marker is places where you click on the map. Click the map again to move the marker to a different position, or drag the marker on the map.</li>
					<li>Type a name in the searchbox and hit the Enter key. For better results you can add the place- or country-name as well to narrow the search results. The marker will be placed on the map when the search is completed successfully.</li>
				</ul>
				Both methods can be used in combination.<br />
				You can create a more advanced route by adding one or more Waypoints. Click the 'Add Waypoint' button and you will see a new entry in the waypoint list. Add the new Waypoint to the map just like the Start and Finish waypoint as described above. Waypoints can be reordered by dragging the balloon up and down in the list.
				Click 'Find route' when you've added the Start and Finish markers (and maybe even some Waypoint markers too) on the map. The Routing Service will now try to calculate a route. The route is automatically plotted on the map when the calculation has finished.
				
				<h3>Change the route parameters</h3>
				You can change the route parameters by clicking on the radio buttons on the Route tab:
				<ul>
				<li>Type of transport: <label for="motorcar">Motorcar</label>, <label for="hgv">Heavy goods</label>, <label for="goods">Goods</label>, <label for="psv">Public service</label>, <label for="bicycle">Bicycle</label>, <label for="cycleroute">Bicycle (routes)</label>, <label for="foot">Foot</label>, <label for="moped">Moped</label>, <label for="mofa">Mofa</label></li>
				<li>Routing method: <label for="fast">Fastest</label>, <label for="short">Shortest</label></li>
				</ul>
				Then click the 'Find Route' button. A new route will be calculated based on the given parameters.
				
				<h3>Partial new route (single click map/routing data testing)</h3>
				If you click on the map again a new TO or FROM marker is placed. Click on 'Find Route' and the new route is calculated. This is very handy for mappers when testing the route network of a certain area. Once you've spotted a bug in the data you can click on the 'EDIT MAP' button and the online 'Potlatch' map data editor is automatically loaded in a new window/tab with the current map view.
				
				<h3>Reverse a route</h3>
				If you want to know how the return trip might look like then click on the 'Reverse' button on the 'Route' tab. All Waypoints will be reversed and a new route will be calculated.
				
				<h3>Completely new route</h3>
				Click the 'Clear' button if you want to generate a completely new route (or when things go wrong, which happens sometimes...). This should work as a reset. If the site continues to be non-functional please try a forced reload of your browser by pressing ctrl-F5.
				
				<h3>Route directions</h3>
				You can see route directions at the 'Directions' tab of this <a href="/routeplanner" title="<%=routeplannerTitle1%>" alt="<%=definition.getAlt(16)%>"><%=definition.getShortTitle(new int[]{-1,0,0}).toLowerCase()%></a>.
				
				<h3>Export routes to GPX file</h3>
				You can export your route to  GPX format and save GPX file at your computer. Click the 'Export' tab. Select 'GPS exchange format (.gpx)' radio button. Click the 'Export' button. Some browsers immediately save result file with default 'route.gpx' name in default place. Other browsers give you selection to open, to save and to define name and directory for result GPX file.
				
				<h3>Adding previously traveled routes</h3>
				Sometimes, it might be desired to view a route already traveled alongside the planned route. This can be done by dragging a GPX file onto the map. (This feature requires a browser with support for the HTML5 File API.)
				
				<h1>Known issues</h1>
				<ul>
					<li>Gosmore is usually very quick in returning a route, often a route is calculated in under 0.1 seconds and downloading the resulting route takes more time. If it takes more then 30 seconds to calculate a route then you can assume there is some problem in the underlying OSM data and often your route won't be complete.</li>
					<li>Moped and Mofa routing currently behaves identically to car routing.</li>
					<li>The route data is updated approximately once a week after a new planet dump is available, so it is possible that the map is not always in sync with the routing engine.</li>
					<li>Due to lack of memory on the server, calculation time can take very long. The server is then busy loading data from the hard disk into memory which is very slow compared to reading cached data from memory. This can be a problem especially when multiple pleople are requesting routes and the routing engines are fighting for resources as a result.</li>
				</ul>
			</div>		
		</div>
		<%@ include file="footer.html" %>
	</body>
</html>
