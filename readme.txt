gvSIG Mini © v. 0.2.0
28th-apr-2010

gvSIG Mini is a free viewer of free-access maps for Java and Android cellular phones.

gvSIG Mini is distributed with GNU/GPL v2 license. For more details, please check license.txt file.

gvSIG Mini has been developed by Prodevelop, S.L.

Prodevelop, S.L.
http://www.prodevelop.es
e-mail: prode@prodevelop.es

gvSIG Mini has been funded by Prodevelop, S.L., IMPIVA Institue of Regioanl Government of Valencia and European Union FEDER funds.

gvSIG Mini comprises two products: gvSIG Mini for Android, aimed at cellular phone with Android operative system, and gvSIG Mini for Java, aimed at cellular phones that support Java CLDC 1.1 / MIDP 2.0 applications.

Common features of version 0.2, are listed below:

- Tile based cartography displaying: OpenStreetMap, Yahoo Maps, Microsoft Bing, WMS-C services of ICC, etc.
- Satellite layers, rendered maps and hybrid ones.
- Navigation through the map: zoom in/out, drag&drop panning.
- Show actual ubication based using local GPS.
- Search POIs (Point Of Interest).
- Address searching.
- Route searching between two points.
- Local tiles cacheing for speeding-up and off-line working.

gvSIG Mini for Java version also features:

- WMS Client.

gvSIG Mini for Android version also features:

- Tweetme client.
- Weather information.
- Accelerometer usage (zoom to my position).

gvSIG Mini for Android uses these icons with license restrictions or attributions:

- menu03.png http://www.kde-look.org/content/show.php?content=39988 GNU/GPL
- Icon pack http://www.icons-land.com/gis-gps-map-icons.php 
- menu_navigation http://www.woothemes.com/2009/02/wp-woothemes-ultimate-icon-set-first-release/
- menu00 http://www.everaldo.com/
- menu_download http://earam.deviantart.com/art/the-moonlight-31208162

* Instructions to compile in SDK 1.5

- Rename the folder drawable-nodpi to drawable and delete al the folders starting by drawable-*
- Move the content of the folder layout-nodpi to layout and delete the folder layout-nodpi
- Delete in AndroidManifest.xml the tag <supports-screen>
- Go to the project properties, click in Android and force the target name to Android 1.5