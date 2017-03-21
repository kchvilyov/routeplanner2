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
				<h1>About <a href="/routeplanner" title="<%=routeplannerTitle1%>" alt="<%=definition.getAlt(2)%>"><%=routeplannerTitle0%></a></h1>

				<h3><a href="/routeplanner" title="<%=routeplannerTitle1%>" alt="<%=definition.getAlt(0)%>"><%=routeplannerTitle0%></a></h3> is not just 
				<a href="/routeplanner" title="<%=definition.getShortTitle(new int[]{0,0,0,6}).toLowerCase()%>" alt="<%=definition.getAlt(9)%>"><%=routeplannerTitle0.toLowerCase()%></a>. 
				<a href="/routeplanner" title="<%=routeplannerTitle1%>" 	alt="<%=definition.getAlt(0)%>"><%=routeplannerTitle0%></a> is 
				<a href="/routeplanner" title="<%=definition.getShortTitle(new int[]{3,0,0,7}).toLowerCase()%>" alt="<%=definition.getAlt(1)%>"><%=definition.getShortTitle(new int[]{3,0,0}).toLowerCase()%></a>. 
				You can select any of different route parameters  combination to get result you like:
				<ul>
				<li>Type of transport: <label for="motorcar">Motorcar</label>, <label for="hgv">Heavy goods</label>, <label for="goods">Goods</label>, <label for="psv">Public service</label>, <label for="bicycle">Bicycle</label>, <label for="cycleroute">Bicycle (routes)</label>, <label for="foot">Foot</label></li>
				<li>Routing method: <label for="fast">Fastest</label>, <label for="short">Shortest</label></li>
				</ul>
				You can create a route by placing at least one Start and one Finish marker on the map.<br/>
				You can advance route by adding one or more Way points. <br>
				You can drag each marker anywhere any time you want.
				You can see each marked place name in its  search box.
				<br>
				You can find another place by typing its name in the search-box. The marker will be placed on the map when you hit the Enter key and the search is completed successfully.
				You can change the order of all Way-points by find another place by typing its name in the searchbox.
				<br>
				You can open or copy route Permalink (permanent link) on the Info tab.
				<br>
				You can export your result route to  GPX format and save GPX file at your computer
				<br/>
				You can load previously traveled routes by dragging a GPX file onto the map.
				
				<h3>OpenStreetMap</h3>
				<a href="/routeplanner" title="<%=definition.getShortTitle(new int[]{4,0,0,3})%>" alt="<%=definition.getAlt(3)%>"><%=definition.getShortTitle(new int[]{4,0,0})%></a> uses OpenSource software and data as much as possible.
				The OpenStreetMap geographic data is the most important part in this project. 
				While being nowhere near complete enough to be able to say that it covers the whole world, it contains some pretty complete data for countries like: the Netherlands, United Kingdom, United States, Ukraine and Germany. 
				Some other countries are not far behind. 
				So the time is there to actually put the OpenStreetMap data to use.

				<h3>YOURS project</h3>
				<a href="/routeplanner" title="<%=definition.getShortTitle(new int[]{4,0,0,3})%>" alt="<%=definition.getAlt(3)%>"><%=definition.getShortTitle(new int[]{4,0,0})%></a> based on the <a href="http://wiki.openstreetmap.org/wiki/YOURS" target="_BLANK">YOURS project</a> software. 
				I compared the YOURS project navigation results with another routing services. 
				I saw YOURS project routeplanner give the best Foot/Pedestrian/Bicycle results for Crimean peninsula and around.  
				So I express my gratitude to <a href="http://wiki.openstreetmap.org/wiki/User:Lambertus" title="User:Lambertus" alt="L.IJsselstein" target="_BLANK">Lambertus</a> and other contributors gave me to use this project possibility.

				<h3><a href="/routeplanner" title="<%=definition.getShortTitle(new int[]{5,0,0,8})%>" alt="<%=definition.getAlt(4)%>"><%=definition.getShortTitle(new int[]{5,0,0})%></a></h3>
				I hope <a href="/routeplanner" title="<%=definition.getShortTitle(new int[]{4,0,0,3})%>" alt="<%=definition.getAlt(5)%>"><%=routeplannerTitle0%></a> will give you the best results for any travel type in any region of the world.
				So if you have some <a href="/routeplanner" title="<%=definition.getShortTitle(new int[]{4,-1,0,3})%>" alt="<%=definition.getAlt(6)%>"><%=definition.getShortTitle(new int[]{4,-1,0}).toLowerCase()%></a> problems or need to see addition possibilities please send me a message.
				And I will improve <a href="/routeplanner" title="<%=definition.getShortTitle(new int[]{-1,0,0,3})%>" alt="<%=definition.getAlt(7)%>"><%=definition.getShortTitle(new int[]{-1,0,0}).toLowerCase()%></a> to make it 
				<a href="/routeplanner" title="<%=definition.getShortTitle(new int[]{5,0,0,3})%>" alt="<%=definition.getAlt(8)%>"><%=definition.getShortTitle(new int[]{5,0,0})%></a>. 

				<h3>News</h3>
				I have added <a href="/routeplanner?layer=googlet" title="Google Terrain map on <%=definition.getShortTitle(new int[]{4,0,0,3})%>" alt="Google Terrain map on <%=definition.getAlt(3)%>">Google Terrain map</a>
				 to <a href="/routeplanner" title="<%=definition.getShortTitle(new int[]{4,0,0,3})%>" alt="<%=definition.getAlt(3)%>"><%=definition.getShortTitle(new int[]{4,0,0})%></a>
				 here: <a href="/routeplanner?layer=googlet"><img src="/images/GoogleTerrainMapAtRoutesMapsRouteplanner.gif" style="margin:1px 0 0 4px; vertical-align: top;" title="Google Terrain map on <%=definition.getShortTitle(new int[]{4,0,0,3})%>" alt="Google Terrain map on <%=definition.getAlt(3)%>"></a>.
			</div>		
		</div>
		<%@ include file="footer.html" %>
	</body>
</html>
