<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
		<%@ include file="head.html" %>

        <script type="text/javascript" src="jquery/jquery-1.6.3.js"></script>
        <!-- Difficulties <script type="text/javascript" src="jquery/jquery-1.9.1.js"></script>
        <script type="text/javascript" src="jquery/jquery-migrate-1.1.1.js"></script> -->
        <script type="text/javascript" src="jquery/jquery-ui-1.8.custom.min.js"></script>
        <!-- Difficulties  <script type="text/javascript" src="jquery/jquery-ui-1.9.2.js"></script> -->

        <!-- <script type="text/javascript" src="http://openlayers.org/api/OpenLayers.js" ></script>-->
		<!-- <script type="text/javascript" src="http://dev.openlayers.org/releases/OpenLayers-2.13.1/OpenLayers.debug.js"></script>-->
		<script type="text/javascript" src="openlayers/OpenLayers-2.13.1/OpenLayers.debug.js"></script>
        <script type="text/javascript" src="http://openstreetmap.org/openlayers/OpenStreetMap.js"></script>
        <script type="text/javascript" src="openlayers/OpenLayers-2.13.1/lib/deprecated.js"></script>

        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?libraries=places&key=AIzaSyALZZlYUwYUBv9PMLRYPAz5OCydWKVA2uE"></script>

        <script type="text/javascript" src="include/routeplanner.js"></script>
        <script type="text/javascript" src="api/dev/yours.js"></script>
        <script type="text/javascript" src="yournavigation.js"></script>

        <script type="text/javascript">
			$(function() {
				// Initialise the map
				init();
				// Make the navigation tabs
				$("#nav_header").tabs();
				// Make the via points sortable
				$("#route_via").sortable({
					update: waypointReorderCallback
				});
			});
        </script>
    </head>
    <body class="indexbody">
		<%@ include file="header.html" %>
		<div id="main">
			<!-- #outer from yournavigation.css does not visible in chrome and IE9! Replacement with <![if lt IE 7]>* html #outer becomes worser!!! -->
			<div id="outer" style="height: 100%; float: left;">
				<div id="navigation">
					<!-- Tabs -->
					<div id="nav_header">
						<ul>
							<li><a href="#fragment-route"><span>Route   </span></a></li>
							<li><a href="#fragment-directions"><span>Directions</span></a></li>
							<li><a href="#fragment-info"><span>Info</span></a></li>
							<li><a href="#fragment-export"><span>Export</span></a></li>
						</ul>
					</div>
					<div id="fragment-route">
						<form id="route" name="route" action="#" onsubmit="return false;">
							<ul id="route_via" class="route_via">
							</ul>
							<ul>
								<li>
									<div>
										<img src="markers/yellow.png" alt="marker to" height="30" style="vertical-align:middle;"/>
										<input type="button" name="add waypoint" onclick="elementClick(this);" value="Add Waypoint" tabindex="4"/>
									</div>
								</li>
							</ul>
						</form>
						<form id="calculate" action="#">
							<div id="route_action">
								<input type="button" name="calculate" onclick="elementClick(this);" value="Find route" tabindex="3"/>
								<input type="button" name="clear" onclick="elementClick(this);" value="Clear" tabindex="5"/>
								<input type="button" name="reverse" onclick="elementClick(this);" value="Reverse" tabindex="7"/>
							</div>
						</form>
						<form id="parameters" action="#">
							<!--<div id="parameters">-->
							<p>Type of transport</p>
							<ul>
								<li><input type="radio" name="type" id="motorcar" onclick="typeChange(this);" value="motorcar"/><label for="motorcar">Motorcar</label></li>
								<li><input type="radio" name="type" id="hgv" onclick="typeChange(this);" value="hgv"/><label for="hgv">Heavy goods</label></li>
								<li><input type="radio" name="type" id="goods" onclick="typeChange(this);" value="goods"/><label for="goods">Goods</label></li>
								<li><input type="radio" name="type" id="psv" onclick="typeChange(this);" value="psv"/><label for="psv">Public service</label></li>
								<li><input type="radio" name="type" id="bicycle" onclick="typeChange(this);" value="bicycle" checked="checked"/><label for="bicycle">Bicycle</label></li>
								<li><input type="radio" name="type" id="cycleroute" onclick="typeChange(this);" value="cycleroute"/><label for="cycleroute">Bicycle (routes)</label></li>
								<li><input type="radio" name="type" id="foot" onclick="typeChange(this);" value="foot"/><label for="foot">Foot</label></li>
								<!-- Moped and Mofa routing currently behaves identically to car routing
								<li><input type="radio" name="type" id="moped" onclick="typeChange(this);" value="moped"/><label for="moped">Moped</label></li>
								<li><input type="radio" name="type" id="mofa" onclick="typeChange(this);" value="mofa"/><label for="mofa">Mofa</label></li>
								-->
							</ul>
						</form>
						<form id="options" action="#">
							<p>Routing method</p>
							<ul>
								<li><input type="radio" name="method" id="fast" value="fast"/><label for="fast">Fastest</label></li>
								<li><input type="radio" name="method" id="short" value="short" checked="checked"/><label for="short">Shortest</label></li>
							</ul>
							<!--</div>-->
						</form>
						<div id="status"></div>

					</div>
					<div id="fragment-directions" class="nav_content">
						<div id="directions"></div>
					</div>
					<div id="fragment-info" class="nav_content">
						<label for="distanceUnitId">Distance in</label>
							<!--  TODO define fSelect CSS class! -->
							<select id="distanceUnitId" class="fSelect" onchange="rewriteInitTabContent(this)">
								<option selected="" value="km">Kilometres</option>
								<option value="mi">Miles</option>
							</select>
						<div id="feature_info"></div>
					</div>
					<div id="fragment-export" class="nav_content">
						<form id="export" action="#">
							<p>Export</p>
							<ul>
								<li><input type="radio" name="type" value="gpx" checked="checked"  />GPS exchange format (.gpx)</li>
								<li><input type="radio" name="type" value="wpt"/>Waypoint (.wpt)</li>
							</ul>
							<p>
								<input type="button" name="export" value="Export" onclick="elementClick(this);" />
							</p>
						</form>
					</div>
					<div>
						<script type="text/javascript" src="//yandex.st/share/share.js" charset="utf-8">
						</script>
						<!--
						<div class="yashare-auto-init" data-yashareL10n="en" data-yashareType="button"
							data-yashareQuickServices="facebook,twitter,gplus,lj,blogger,linkedin,liveinternet,friendfeed,surfingbird,delicious,digg,evernote,juick,myspace,pinterest,vkontakte,odnoklassniki,moimir,yaru,yazakladki,moikrug,diary,tutby"
						-->
						<div class="yashare-auto-init" data-yashareL10n="en" data-yashareType="none"
							data-yashareQuickServices="liveinternet,juick,yaru,moikrug,diary,tutby"
							data-yashareLink="<%=page_url%>"
							data-yashareImage="<%=definition.getServerUrl()%>/images/routeplanner.gif"
							data-yashareTitle="<%=page_title%>" 
							data-yashareDescription="<%=page_description%>">
						</div> 
					</div>
				</div>
			</div>
			<div id="content"><div id="map"></div></div>

			<div style="clear:both;"></div>
			<%@ include file="footer.html" %>
		</div>
    </body>
</html>
