/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Prodevelop and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *   Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibï¿½ï¿½ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *   +34 963862235
 *   gvsig@gva.es
 *   http://www.gvsig.gva.es
 *
 *    or
 *
 *   Prodevelop Integraciï¿½n de Tecnologï¿½as SL
 *   Conde Salvatierra de ï¿½lava , 34-10
 *   46004 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   gis@prodevelop.es
 *   http://www.prodevelop.es
 *
 *    or
 *
 *   Instituto de Robï¿½tica
 *   Apartado de correos 2085
 *   46071 Valencia
 *   (Spain)
 *   
 *   +34 963 543 577
 *   jjordan@robotica.uv.es
 *   http://robotica.uv.es
 *   
 */
package es.prodevelop.gvsig.mini.phonecache;

import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.projection.TileConversor;



/**
 * Uyility class to perform conversions between reference systems.
 * 
 * @see es.prodevelop.geodetic.utils.conversion.Elipsoide
 * @see es.prodevelop.geodetic.utils.conversion.GeoUtils
 * 
 * @author vsanjaime
 * @author jsanz
 * @author jldominguez
 * @author Stephane Falck, stephane [dot] falck [at] onf [dot] fr
 *
 */
public class ConversionCoords {

    /**
     * Default threshold for the Bessel algorithm of the Inverse Geodesy Problem
     */
    private static final double DEFAULT_THRESHOLD = 0.0000000001;
    /**
     * Maximum distance between to geodetic point to use get the UTM distance
     */
    private static final double MAX_SPHER_DIST = 0;
    private static final double PI_DIV_180 = 0.017453292519943295769236907684886;
    public static final double DEGREES_PER_RADIAN = 57.295779513082320876798154814105;
    /**
     * the attribute "hemisferio" shows the hemisfery,
     * initialized to North hemisfery.
     * North hemisfery = 1
     * South hemisfery = -1. 
     */
    private static byte hemisferio = 1;                   // Hemisferio NORTE  ********


    /**
     * Constructor:
     * a  Semimayor Axis in the ellipsoide (WGS84)
     * b  Semiminor Axis in the ellipsoide (WGS84)
     * Flattening in the ellipsoide (WGS84)
     * ep Second excentricity
     * ep2 Second excentricity ^2
     */
    private ConversionCoords() {
    }

    /**	
     * This method makes the conversion between geodesic coordinates to 
     * projected coordinates in a UTM zone forced
     * @param lon Geodesic longitude in decimals deegrees of the point
     * @param lat Geodesic latitude in decimals deegrees of the point 
     * @param husoforced  UTM Zone forced
     * @return Array with two elements. X (UTM), Y (UTM)	 
     */
    public static double[] geo2utm(double lon, double lat, final Elipsoide elip, final int husoforced) {
        /* Elipsoide */

        final double se2 = elip.getSe2();
        final double c = elip.getC();

        /* Correccion de lon y lat por error */
        if (lon > 180) {
            lon = 180;
        }
        if (lon < -180) {
            lon = -180;
        }
        if (lat > 90) {
            lat = 90;
        }
        if (lat < -90) {
            lat = -90;
        }
        /* Transform to radians */
        final double rlon = (lon * PI_DIV_180);
        final double rlat = (lat * PI_DIV_180);
        /* Calculate the zone */
        final String hus = Integer.toString(husoforced);
        final double huso = Double.parseDouble(hus);
        /* average longitude of the zone */
        final double lonmedia = (huso * 6.0) - 183;
        /* Angular distance between the point and the central meridian of the zone */
        final double rilon = rlon - ((lonmedia * PI_DIV_180));
        /*  Operations  */
        final double A = (Math.cos(rlat)) * (Math.sin(rilon));
        final double CC = (0.5) * (Math.log((1 + A) / (1 - A)));
        final double n = (Math.atan((Math.tan(rlat)) / (Math.cos(rilon)))) - rlat;
        final double v = (c * 0.9996) / Math.sqrt((1 + se2 * Math.pow(Math.cos(rlat), 2)));
        final double S = (se2 * Math.pow(CC, 2) * Math.pow(Math.cos(rlat), 2)) / 2.0;
        final double A1 = Math.sin(2 * rlat);
        final double A2 = A1 * Math.pow(Math.cos(rlat), 2);
        final double J2 = rlat + (A1 / 2);
        final double J4 = (3 * J2 + A2) / 4;
        final double J6 = (5 * J4 + A2 * Math.pow(Math.cos(rlat), 2)) / 3;
        final double alfa = (3.0 / 4) * se2;
        final double beta = (5.0 / 3) * Math.pow(alfa, 2);
        final double gamma = (35.0 / 27) * Math.pow(alfa, 3);
        final double B = 0.9996 * c * (rlat - (alfa * J2) + (beta * J4) - (gamma * J6));
        final double x = CC * v * (1 + (S / 3)) + 500000;
        double y = n * v * (1 + S) + B;
        /* For latitudes in the south hemisfery */
        if (lat < 0) {
            y = y + 10000000;
        }
        /* Results = {X(UTM),Y(UTM),zone(UTM)} */
        double xy[] = new double[2];
        xy[0] = x;
        xy[1] = y;

        return xy;
    }

    /**	
     * This method makes the conversion between geodesic coordinates to 
     * projected coordinates in UTM
     * @param lon Geodesic longitude in decimals deegrees of the point
     * @param lat Geodesic latitude in decimals deegrees of the point 
     * @return Array with three elements. X (UTM), Y (UTM) and zone(UTM)	 
     */
    public static double[] geo2utm(double lon, double lat, final Elipsoide elip) {
        /* Elipsoide */

        final double se2 = elip.getSe2();
        final double c = elip.getC();

        /* Correccion de lon y lat por error */
        if (lon > 180) {
            lon = 180;
        }
        if (lon < -180) {
            lon = -180;
        }
        if (lat > 90) {
            lat = 90;
        }
        if (lat < -90) {
            lat = -90;
        }
        /* Transform to radians */
        double rlon = (lon * PI_DIV_180);
        double rlat = (lat * PI_DIV_180);
        /* Calculate the zone */
        final double hus = (lon / 6.0) + 31;
        final double huso = Math.floor(hus);
        /* average longitude of the zone */
        final double lonmedia = (huso * 6.0) - 183;
        /* Angular distance between the point and the central meridian of the zone */
        final double rilon = rlon - ((lonmedia * PI_DIV_180));
        /*  Operations  */
        final double A = (Math.cos(rlat)) * (Math.sin(rilon));
        final double CC = (0.5) * (Math.log((1 + A) / (1 - A)));
        final double n = (Math.atan((Math.tan(rlat)) / (Math.cos(rilon)))) - rlat;
        final double v = (c * 0.9996) / Math.sqrt((1 + se2 * Math.pow(Math.cos(rlat), 2)));
        final double S = (se2 * Math.pow(CC, 2) * Math.pow(Math.cos(rlat), 2)) / 2.0;
        final double A1 = Math.sin(2 * rlat);
        final double A2 = A1 * Math.pow(Math.cos(rlat), 2);
        final double J2 = rlat + (A1 / 2);
        final double J4 = (3 * J2 + A2) / 4;
        final double J6 = (5 * J4 + A2 * Math.pow(Math.cos(rlat), 2)) / 3;
        final double alfa = (3.0 / 4) * se2;
        final double beta = (5.0 / 3) * Math.pow(alfa, 2);
        final double gamma = (35.0 / 27) * Math.pow(alfa, 3);
        final double B = 0.9996 * c * (rlat - (alfa * J2) + (beta * J4) - (gamma * J6));
        final double x = CC * v * (1 + (S / 3)) + 500000;
        double y = n * v * (1 + S) + B;
        /* For latitudes in the south hemisfery */
        if (lat < 0) {
            y = y + 10000000;
        }
        /* Results = {X(UTM),Y(UTM),zone(UTM)} */
        double xyh[] = new double[3];
        xyh[0] = x;
        xyh[1] = y;
        xyh[2] = huso;
        return xyh;
    }

    /**
     * This method makes the projected coordinates conversion in UTM 
     * to geodetic coordenates in the north hemisfery
     * @param x (Coordinate X UTM)  
     * @param y (Coordinate Y UTM) 
     * @param huso (Zone of the UTM projection system)
    
     * @return Array with two elements (longitude, latitude)
     */
    public static double[] utm2geo(double x, double y, double huso, Elipsoide elip) {
        double[] result = utm2geo(x, y, huso, elip, hemisferio);
        return result;
    }

    /**
     * This method makes the projected coordinates conversion in UTM 
     * to geodetic coordenates
     * @param x (Coordinate X UTM)  
     * @param y (Coordinate Y UTM) 
     * @param huso (Zone of the UTM projection sistem)
     * @param hemisfery (North = 1; South = -1)
     * @return (longitude, latitude)
     */
    public static double[] utm2geo(double x, double y, final double huso, final Elipsoide elip, final int hemisfery) {

        /* Elipsoide */
        final double se2 = elip.getSe2();
        final double c = elip.getC();

        /* Delete the offset in the Xs*/
        x = x - 500000;
        /* Delete the offset of the coordenate Y for the south hemisfery */
        if (hemisfery == -1) {
            y = y - 10000000.0;
        }
        /* average longitude of the zone */
        final double lonmedia = (huso * 6) - 183.0;
        /* Operations */
        final double fip = (y / (6363651.2449104));
        final double v = (c * 0.9996) / Math.sqrt((1 + se2 * Math.pow(Math.cos(fip), 2)));
        final double aa = (x / v);
        final double A1 = Math.sin(2 * fip);
        final double A2 = A1 * Math.pow(Math.cos(fip), 2);
        final double J2 = fip + (A1 / 2);
        final double J4 = (3 * J2 + A2) / 4;
        final double J6 = (5 * J4 + A2 * Math.pow(Math.cos(fip), 2)) / 3.0;
        final double alfa = (3.0 / 4) * se2;
        final double beta = (5.0 / 3) * Math.pow(alfa, 2);
        final double gamma = (35.0 / 27) * Math.pow(alfa, 3);
        final double B = 0.9996 * c * (fip - (alfa * J2) + (beta * J4) - (gamma * J6));
        final double bb = (y - B) / v;
        final double S = (se2 * Math.pow(aa, 2) * Math.pow(Math.cos(fip), 2)) / 2;
        final double CC = aa * (1 - (S / 3.0));
        final double n = (bb * (1.0 - S)) + fip;
        final double sinh = (Math.pow(Math.E, CC) - Math.pow(Math.E, -(CC))) / 2.0;
        final double ilam = Math.atan(sinh / Math.cos(n));
        final double tau = Math.atan(Math.cos(ilam) * Math.tan(n));
        final double gilam = (ilam * DEGREES_PER_RADIAN);
        final double lon = gilam + lonmedia;
        final double lat = fip + (1 + se2 * (Math.pow(Math.cos(fip), 2)) - (3.0 / 2) * se2 * Math.sin(fip) * Math.cos(fip) * (tau - fip)) * (tau - fip);
        final double glat = (lat * DEGREES_PER_RADIAN);
        /* Results = {longitude,latitude} */
        double lonlat[] = new double[2];
        lonlat[0] = lon;
        lonlat[1] = glat;
        return lonlat;
    }

    /**	 
     * This method calculates the projected distance between two points in geodetic coordinates
     * @param lon1 geodetic logitude in decimal deegrees of the initial point in WGS84 
     * @param lat1 geodetic latitude in decimal deegrees of the initial point in WGS84 
     * @param lon2 geodetic logitude in decimal deegrees of the final point in WGS84 
     * @param lat2 geodetic latitude in decimal deegrees of the final point in WGS84  
     * @return double UTM distance in metres between two projected points
     */
    public static double distanciaProj(final double lon1, final double lat1, final double lon2, final double lat2, final Elipsoide elip) {
        double p1[] = new double[2];
        double p2[] = new double[2];
        /* Project to UTM both points */
        p1 = geo2utm(lon1, lat1, elip);
        p2 = geo2utm(lon2, lat2, elip);
        /* Calculate the distance */
        final double dist = Math.sqrt((Math.pow((p2[0] - p1[0]), 2)) + (Math.pow((p2[1] - p1[1]), 2)));
        return dist;
    }

    /**
     * IMPRECISA
     * Este mÃ©todo calcula un segundo punto a partir de un punto inicial y una distancia.
     * This method calculates a second point 
     * This method calculates the distance in deegrees
     * @param lon geodetic logitude geodetic en grados decimales del punto origen en WGS84
     * @param lat Latitude geodetic en grados decimales del punto origen en WGS84
     * @param dist Distance in metres
     * @return Angulo en grados decimales desde el punto origen al punto destino
     */
    public double dist2gra(final double lon, final double lat, final double dist, final Elipsoide elip) {
        double p1[] = new double[2];
        double p2[] = new double[2];
        double p3[] = new double[2];

        p1 = this.geo2utm(lon, lat, elip);
        p2[0] = p1[0] + dist;
        p2[1] = p1[1];
        final double huso = Math.floor((lon / 6) + 31);        // Calculo del huso, redondeando

        p3 = utm2geo(p2[0], p2[1], huso, elip);             // Conversio a coor geodeticas

        final double gra = Math.abs(lon - p3[0]);          // Grados en valor absoluto

        return gra;
    }

    /**
     * This method makes the coordinates conversion (geodetic --> tridimensional cartesian)
     * @param lon = longitude
     * @param lat = latitude
     * @param helip = elipsoidal height
     * @param elip = elipsode
     * @return double[] ={X, Y, Z}
     */
    private static double[] geotri(final double lon, final double lat, final double helip, final Elipsoide elip) {
        final double[] radios = elip.radios(lat);
        final double rn = radios[1];
        final double rlon = lon * PI_DIV_180;
        final double rlat = lat * PI_DIV_180;
        final double xTri = (rn + helip) * Math.cos(rlat) * Math.cos(rlon);
        final double yTri = (rn + helip) * Math.cos(rlat) * Math.sin(rlon);
        final double a2 = Math.pow(elip.getA(), 2);
        final double b2 = Math.pow(elip.getB(), 2);
        final double zTri = ((b2 / a2) * rn + helip) * Math.sin(rlat);
        final double[] tridi = {xTri, yTri, zTri};

        return tridi;
    }

    /**
     * This method makes the coordinates conversion (tridimensional cartesian --> geodetic)
     * @param X 
     * @param Y
     * @param Z
     * @param elip = elipsoide
     * @return double[] = {longitude, latitude, elipsoidal height}
     */
    private static double[] trigeo(final double X, final double Y, final double Z, final Elipsoide elip) {

        final double p = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));
        final double a = elip.getA();
        final double b = elip.getB();
        final double pe2 = elip.getPe2();
        final double se2 = elip.getSe2();
        final double tecta = Math.atan((Z * a) / (p * b));
        final double numerador = Z + se2 * b * Math.pow(Math.sin(tecta), 3);
        final double denominador = p - pe2 * a * Math.pow(Math.cos(tecta), 3);
        final double latgeorad = Math.atan(numerador / denominador);
        final double latgeo = latgeorad * DEGREES_PER_RADIAN;
        double longeorad = 0;
        if (X > 0) {
            longeorad = Math.atan(Y / X);
        }
        if (X < 0 & Y > 0) {
            longeorad = Math.PI + Math.atan(Y / X);
        }
        if (X < 0 & Y < 0) {
            longeorad = -(Math.PI - Math.atan(Y / X));
        }
        if (X == 0 & Y > 0) {
            longeorad = Math.PI / 2;
        }
        if (X == 0 & Y < 0) {
            longeorad = -(Math.PI / 2);
        }
        if (X == 0 & Y == 0) {
            longeorad = 0;
        }

        final double longeo = longeorad * DEGREES_PER_RADIAN;

        final double[] radios = elip.radios(latgeo);
        final double rn = radios[1];
        final double h = p / (Math.cos(latgeo) - rn);

        final double[] geodes = {longeo, latgeo, h};
        return geodes;
    }

    /**
     * This method makes the coordinates transformation between the ED50 elipsoide to WGS84.
     * @param lon longitude
     * @param lat latitude
     * @return double[] = {longitude trans, latitude trans}
     */
    public static double[] transEd50Wgs84(final double lon, final double lat) {
        final double xtransla = -131.032;
        final double ytransla = -100.251;
        final double ztransla = -163.354;

//        double xrot = (1.2438 / 3600.0) * Math.PI / 180.0;
//        double yrot = (0.0195 / 3600.0) * Math.PI / 180.0;
//        double zrot = (1.1436 / 3600.0) * Math.PI / 180.0;
        
        final double xrot = 6.0301125656404086882713516051282e-6;
        final double yrot = 9.45386678163595187500332499598e-8;
        final double zrot = 5.5443292571686536226942576745655e-6;

        final double xrotn = -xrot;
        final double yrotn = -yrot;
        final double zrotn = -zrot;

//        double fscale = ((9.39) / 1000000.0);
        final double fscale = (0.00000939);

        final double w12 = zrot;
        final double w13 = yrotn;
        final double w21 = zrotn;
        final double w23 = xrot;
        final double w31 = yrot;
        final double w32 = xrotn;

        final Elipsoide ed50 = Elipsoide.getED50();
        final Elipsoide wgs84 = Elipsoide.getWGS84();

        final double[] cTri = geotri(lon, lat, 0, ed50);
        final double x = cTri[0];
        final double y = cTri[1];
        final double z = cTri[2];
        final double fx = fscale * x;
        final double fy = fscale * y;
        final double fz = fscale * z;
        final double wx = w12 * y + w13 * z;
        final double wy = w21 * x + w23 * z;
        final double wz = w31 * x + w32 * y;
        final double Xtrans = x + (xtransla + fx + wx);
        final double Ytrans = y + (ytransla + fy + wy);
        final double Ztrans = z + (ztransla + fz + wz);

        final double[] trans = trigeo(Xtrans, Ytrans, Ztrans, wgs84);

        return trans;
    }

    /**
     * This method makes the coordinates transformation between the WGS84 elipsoide to ED50.
     * @param lon longitude
     * @param lat latitude
     * @return double[] = {longitude trans, latitude trans}
     */
    public static double[] transWgs84Ed50(final double lon, final double lat) {
        final double xtransla = 131.032;
        final double ytransla = 100.251;
        final double ztransla = 163.354;

//        double xrot = (-1.2438 / 3600.0) * Math.PI / 180.0;
//        double yrot = (-0.0195 / 3600.0) * Math.PI / 180.0;
//        double zrot = (-1.1436 / 3600.0) * Math.PI / 180.0;
        final double xrot = -6.0301125656404086882713516051282e-6;
        final double yrot = -9.45386678163595187500332499598e-8;
        final double zrot = -5.5443292571686536226942576745655e-6;

        final double xrotn = -xrot;
        final double yrotn = -yrot;
        final double zrotn = -zrot;

//        double fscale = ((-9.39) / 1000000.0);
        final double fscale = (-0.00000939);

        final double w12 = zrot;
        final double w13 = yrotn;
        final double w21 = zrotn;
        final double w23 = xrot;
        final double w31 = yrot;
        final double w32 = xrotn;

        final Elipsoide ed50 = Elipsoide.getED50();
        final Elipsoide wgs84 = Elipsoide.getWGS84();

        final double[] cTri = geotri(lon, lat, 0, wgs84);
        final double x = cTri[0];
        final double y = cTri[1];
        final double z = cTri[2];
        final double fx = fscale * x;
        final double fy = fscale * y;
        final double fz = fscale * z;
        final double wx = w12 * y + w13 * z;
        final double wy = w21 * x + w23 * z;
        final double wz = w31 * x + w32 * y;
        final double Xtrans = x + (xtransla + fx + wx);
        final double Ytrans = y + (ytransla + fy + wy);
        final double Ztrans = z + (ztransla + fz + wz);

        final double[] trans = trigeo(Xtrans, Ytrans, Ztrans, ed50);

        return trans;
    }

    public static double[] PIGbsl(final double lon1_, final double lat1_,
            final double lon2_, final double lat2_, final Elipsoide elip) throws Exception {
        return PIGbsl(lon1_, lat1_, lon2_, lat2_, elip, DEFAULT_THRESHOLD);
    }

    /**
     * geodetic Direct Problem. This method returns the distance between two points on the elipsoide. 
     * Calculate the geodetic line between the two points.
     * @param lon1_ longitude of the initial point
     * @param lat1_ latitude of the initial point
     * @param lon2_ longitude of the final point
     * @param lat2_ latitude of the final point
     * @param elip Elipsoide
     * @return azimut1, azimut2, distance in meters
     * @throws Exception 
     */
    public static double[] PIGbsl(final double lon1_, final double lat1_,
            final double lon2_, final double lat2_, final Elipsoide elip, final double threshold) throws Exception {

        double _lon1 = lon1_;
        double _lon2 = lon2_;
        double _lat1 = lat1_;
        double _lat2 = lat2_;

        if (lon1_ == 0) {
            _lon1 = 0.0000001;
        }
        if (lon2_ == 0) {
            _lon2 = 0.0000001;
        }
        if (lat1_ == 0) {
            _lat1 = 0.0000001;
        }
        if (lat2_ == 0) {
            _lat2 = 0.0000001;
        // ------------------------
        }
        _lon1 = _lon1 * PI_DIV_180;
        _lat1 = _lat1 * PI_DIV_180;
        _lon2 = _lon2 * PI_DIV_180;
        _lat2 = _lat2 * PI_DIV_180;

        final double a = elip.getA();
        final double b = elip.getB();
        final double e2 = elip.getPe2();
        final double e4 = Math.pow(e2, b);
        final double se = elip.getSe();

        /* sphere latitudes */
        final double u1 = Math.atan(b / a * Math.tan(_lat1));
        final double u2 = Math.atan(b / a * Math.tan(_lat2));

        /* initial data */
        final double il = _lon2 - _lon1;
        double wt = il;
        final double mas = (u1 + u2) / 2;
        final double men = (u1 - u2) / 2;
        int flag = 0;

        double w2;
        double g1;
        double g2;
        double a12 = 0.0;
        double a21 = 0.0;

        double M = 0.0;
        double m = 0.0;
        double sig = 0.0;
        double t11;
        double t12;
        double t1;
        double t2;
        double w;

        /* iteration process */
        int ITERATIONS = 100;        

        for (int i = 0; i < ITERATIONS; i++) {
            w2 = wt / 2;
            g1 = Math.cos(men) / (Math.sin(mas) * Math.tan(w2));
            g2 = Math.sin(men) / (Math.cos(mas) * Math.tan(w2));
            a12 = Math.atan(g1) + Math.atan(g2);
            a21 = Math.atan(g2) - Math.atan(g1) + Math.PI;

            while (a12 <= 0) {
                a12 += 6.283185307179586476925286766559;
            }
            while (a12 >= 6.283185307179586476925286766559) {
                a12 -= 6.283185307179586476925286766559;
            }
            while (a21 <= 0) {
                a21 += 6.283185307179586476925286766559;
            }
            while (a21 >= 6.283185307179586476925286766559) {
                a21 -= 6.283185307179586476925286766559;
            }

            M = Math.atan((Math.tan(u1)) / (Math.cos(a12)));
            m = Math.acos((Math.sin(u1)) / (Math.sin(M)));
            sig = Math.acos(Math.sin(u1) * Math.sin(u2) + Math.cos(u1) * Math.cos(u2) * Math.cos(wt));
            t11 = e2 / 8;
            t12 = e2 * Math.pow(Math.cos(m), 2) / 16;
            t1 = (e2 * Math.sin(m) * sig * (0.5 + t11 - t12));
            t2 = e4 * Math.sin(m) * (Math.pow(Math.cos(m), 2) * Math.sin(sig) * Math.cos(2 * M + sig)) / 16;

            /* Control del dominio del incremento de lon */

            w = (il > 0) ? il + t1 + t2 : il - (t1 - t2);

            /* Precision */
            double condi = Math.abs(wt - w);
            if (condi <= threshold) {
                a21 = a21 + Math.PI;

                /* Control dominio azimut */
                if (a21 >= 2 * Math.PI) {
                    a21 -= 2 * Math.PI;
                }

                /* Calculo de la geodï¿½sica */

                final double k = se * Math.cos(m);
                final double tA = 1 + (Math.pow(k, 2) / 4 - 3 * Math.pow(k, 4) / 64);
                final double tB = Math.pow(k, 2) / 4 - Math.pow(k, 4) / 16;
                final double tC = Math.pow(k, 4) / 128;
                final double s = tA * b * sig - tB * b * Math.sin(sig) * Math.cos(2 * M + sig) - tC * b * Math.sin(2 * sig) * Math.cos(4 * M + 2 * sig);

                final double[] result = {a12, a21, s};
                return result;
            } else {
                wt = w;
            // System.out.println("wt = " + wt);
            }
        }

        // No convergence. It may be because coordinate points
        // are equals or because they are at antipodes.
        final double LEPS = 1E-10;
        if (Math.abs(lon1_ - lon2_) <= LEPS && Math.abs(lat1_ - lat2_) <= LEPS) {
            return new double[]{0, 0, 0}; // Coordinate points are equals

        }
        if (Math.abs(lat1_) <= LEPS && Math.abs(lat2_) <= LEPS) {
            return new double[]{0, Math.PI, Math.abs(lon1_ - lon2_) * elip.getA()}; // Points are on the equator.

        }

        throw new Exception("No distance oould be calculated");
    }

    /**
     * Converts the given coordinates into the mercator projection 
     * @param lon longitude
     * @param lat latitude
     * @param elip the ellipsoid
     * @return the coordinates in the mercator projection
     */
    public static double[] geo2mercator(final double lon, final double lat, final Elipsoide elip) {

        /* Elipsoide */
        final double rlon = (lon * PI_DIV_180);
        final double rlat = (lat * PI_DIV_180) ;
        final double a = elip.getA();

        final double x = a * rlon;
        final double ep2 = elip.getPe2();

        final double sl = Math.sin(rlat);
        final double tl = (1 + sl) / (1 - sl);
        double y = Math.pow(((1 - (Math.sqrt(ep2) * sl)) / (1 +
                (Math.sqrt(ep2) * sl))), (Math.sqrt(ep2)));
        y = a / 2 * (Math.log(tl * y));
        final double mer[] = {x, y};
        return mer;
    }

    /**
     * Converts the given coordinates into the Mercator projection (whole world)
     * 
     * @param lon longitude
     * @param lat latitude
     * @return the coordinates in the Mercator projection.
     * The unit is the equatorian degree
     */
    public static double[] newgeo2mercator(final double lon, final double lat) {

        final double rlat = lat / DEGREES_PER_RADIAN;

        final double y = 0.5 * Math.log((1 + Math.sin(rlat)) / (1 - Math.sin(rlat)));
        // double y = 0.5 * 
        // Math.atan(sinh(rlat));
        // double y = Math.log(Math.tan(phi) + sec(phi));

        final double mer[] = {
            lon,
            DEGREES_PER_RADIAN * y
        };
        return mer;
    }

    public static double[] newmercator2geo(final double lon, final double lat) {

        final double rlat = lat / DEGREES_PER_RADIAN;

        final double y = 2.0 * Math.atan(Math.exp(rlat)) - 0.5 * Math.PI;
        // double y = 0.5 * 
        // Math.atan(sinh(rlat));
        // double y = Math.log(Math.tan(phi) + sec(phi));

        final double mer[] = {
            lon,
            DEGREES_PER_RADIAN * y
        };
        return mer;
    }

    private static double avoidPolarLatitude(final double _lat, final boolean input_in_radians) {

        final double sign = (_lat >= 0) ? 1.0 : -1.0;
        double y = Math.abs(_lat);
        if (input_in_radians) {
            y = y * DEGREES_PER_RADIAN;
        }
        if (y > 80) {

            if (y >= 90) {
                return sign * 81.0;
            } else {
                return sign * (80.0 + 0.1 * (y - 80.0));
            }

        } else {
            return sign * y;
        }

    }

    /**
     * Converts the given WGS84 coordinates into the specified projection
     * @param lon longitude
     * @param lat latitude
     * @param p the output projection
     * @return the coordinates in the specified projection
     */
    public static double[] wgs842projection(
            final double lon,
            final double lat,
            final Projection p) {

        double[] resp = new double[2];

        if (GeoUtils.isInED50Meters(p)) {

            int zone = GeoUtils.getZone(p);
            if (zone != -1) {

//                if (lat < 0) {
//                    return null;
//                }
                final double[] coords = ConversionCoords.transWgs84Ed50(lon, lat);                
                resp = ConversionCoords.geo2utm(
                        coords[0],
                        coords[1],
                        Elipsoide.getED50(),
                        zone);
            } else {
                // unexpected epsg abbrev format
//                resp[0] = lon;
//                resp[1] = lat;
                return null;
            }

        } else {

            if (GeoUtils.isInWGS84Meters(p)) {

                int zone = GeoUtils.getZone(p);
                if (zone != -1) {                   
                    

                    resp = ConversionCoords.geo2utm(
                            lon,
                            lat,
                            Elipsoide.getWGS84(),
                            zone);
                } else {
                    // unexpected epsg abbrev format
//                    resp[0] = lon;
//                    resp[1] = lat;
                    return null;
                }

            } else {

                if (p.getAbrev().toUpperCase().compareTo("EPSG:4230") == 0) {

                    final double[] coords = ConversionCoords.transWgs84Ed50(lon, lat);
                    resp[0] = coords[0];
                    resp[1] = coords[1];

                } else {

                    if (p.getAbrev().toUpperCase().compareTo("EPSG:4326") == 0) {
                        // 4326
                        resp[0] = lon;
                        resp[1] = lat;
                    } else {
                        if (p.getAbrev().toUpperCase().compareTo("EPSG:3785") == 0 ||
                                p.getAbrev().toUpperCase().compareTo("EPSG:900913") == 0 ||
                                p.getAbrev().toUpperCase().compareTo("OSGEO:41001") == 0) {
                            // mercator (spherical)
                            final Point point = TileConversor.latLonToMercator(lon, lat);
                            resp = new double[]{point.getX(), point.getY()};
                        //resp = newgeo2mercator(lon, lat);
                        } else {
                            if (p.getAbrev().toUpperCase().compareTo("EPSG:27700") == 0) {
                                // uk national grid
                                resp = epsg27700FromWGS84(lon, lat);
                            } else {
                                if (p.getAbrev().toUpperCase().compareTo("EPSG:27561") == 0) {
                                    final double[] coords = ConversionCoords.WGS84toLambertZoneI(lon, lat);
                                    resp[0] = coords[0];
                                    resp[1] = coords[1];
                                } else {
                                    if (p.getAbrev().toUpperCase().compareTo("EPSG:27572") == 0) {
                                        final double[] coords = ConversionCoords.WGS84toLambert2e(lon, lat);
                                        resp[0] = coords[0];
                                        resp[1] = coords[1];
                                    } else {
                                        if (p.getAbrev().toUpperCase().compareTo("EPSG:27562") == 0) {
                                            final double[] coords = ConversionCoords.WGS84toLambertZoneII(lon, lat);
                                            resp[0] = coords[0];
                                            resp[1] = coords[1];
                                        } else {
                                            if (p.getAbrev().toUpperCase().compareTo("EPSG:27563") == 0) {
                                                final double[] coords = ConversionCoords.WGS84toLambertZoneIII(lon, lat);
                                                resp[0] = coords[0];
                                                resp[1] = coords[1];
                                            } else {
                                                if (p.getAbrev().toUpperCase().compareTo("EPSG:27564") == 0) {
                                                    final double[] coords = ConversionCoords.WGS84toLambertZoneIV(lon, lat);
                                                    resp[0] = coords[0];
                                                    resp[1] = coords[1];
                                                } else {
                                                    // unknown

//                                                    resp[0] = lon;
//                                                    resp[1] = lat;
                                                    return null;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return resp;
    }

    /**
     * Converts the given projection coordinates into WGS84 coordinates
     * @param lon longitude
     * @param lat latitude
     * @param p the output projection
     * @return the coordinates in WGS84
     */
    public static double[] projection2wgs84(
            final double lon,
            final double lat,
            final Projection p) {

        double[] resp = new double[2];
        if (GeoUtils.isLatLonProjection(p.getAbrev())) {
            return new double[]{lon, lat};
        }

        if (GeoUtils.isInED50Meters(p)) {

            int zone = GeoUtils.getZone(p);
            if (zone != -1) {

                final double[] utm_geo = ConversionCoords.utm2geo(lon, lat, zone, Elipsoide.getED50(), 1);
                resp = ConversionCoords.transEd50Wgs84(utm_geo[0], utm_geo[1]);

            } else {
                // unexpected epsg abbrev format
//                resp[0] = lon;
//                resp[1] = lat;
                return null;
            }

        } else {

            if (GeoUtils.isInWGS84Meters(p)) {

                int zone = GeoUtils.getZone(p);
                if (zone != -1) {
                    int h = 1;
                    if (p.getAbrev().indexOf("EPSG:327") == 0) {
                        h = -1;
                    }
                    resp = ConversionCoords.utm2geo(lon, lat, zone, Elipsoide.getWGS84(), h);
                } else {
                    // unexpected epsg abbrev format
//                    resp[0] = lon;
//                    resp[1] = lat;
                    return null;
                }

            } else {


                if (p.getAbrev().toUpperCase().compareTo("EPSG:3785") == 0 ||
                        p.getAbrev().toUpperCase().compareTo("EPSG:900913") == 0 ||
                        p.getAbrev().toUpperCase().compareTo("OSGEO:41001") == 0) {
                    // mercator (spherical)
                    final Point point = TileConversor.mercatorToLatLon(lon, lat);
                    resp = new double[]{point.getX(), point.getY()};
                //resp = newgeo2mercator(lon, lat);                        
                }
            }
        }
        return resp;
    }

    private static double[] wgs84toLambert2e(final double lon, final double lat) {

        final double lambda_w = lon * PI_DIV_180;
        final double phi_w = lat * PI_DIV_180;

        // geographic WGS84 (phi_w,lambda_w) -> cartesian WGS84 (X_w,Y_w,Z_w)
        // based on formulae published by the French IGN
        final double a_w = 6378137.0;
        final double b_w = 6356752.314;

        final double e2_w = (a_w * a_w - b_w * b_w) / (a_w * a_w);

        final double N = a_w / Math.sqrt(1 - e2_w * Math.pow(Math.sin(phi_w), 2));

        final double X_w = N * Math.cos(phi_w) * Math.cos(lambda_w);
        final double Y_w = N * Math.cos(phi_w) * Math.sin(lambda_w);
        final double Z_w = N * (1 - e2_w) * Math.sin(phi_w);

        // cartï¿½sian WGS84 (X_w,Y_w,Z_w) -> cartesian (X_n,Y_n,Z_n)

        // offset
        final double dX = 168.0;
        final double dY = 60.0;
        final double dZ = -320.0;

        final double X_n = X_w + dX;
        final double Y_n = Y_w + dY;
        final double Z_n = Z_w + dZ;

        // cartesian NTF (X_n,Y_n,Z_n) -> geographic NTF (phi_n,lambda_n)
        final double a_n = 6378249.2;
        final double b_n = 6356515.0;

        final double e2_n = (a_n * a_n - b_n * b_n) / (a_n * a_n);

        // convergence tollerance
        final double epsilon = DEFAULT_THRESHOLD;

        double p0 = Math.atan(Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n) * (1 - (a_n * e2_n) / (Math.sqrt(X_n * X_n + Y_n * Y_n + Z_n * Z_n))));
        double p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));

        while (!(Math.abs(p1 - p0) < epsilon)) {
            p0 = p1;
            p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));
        }

        final double phi_n = p1;
        final double lambda_n = Math.atan(Y_n / X_n);

        // geographic NTF (phi_n,lambda_n) -> projected Lambert II ï¿½tendu (X_l2e, Y_l2e)
        final double n = 0.7289686274;
        final double c = 11745793.39;
        final double Xs = 600000.0;
        final double Ys = 8199695.768;

        final double e_n = Math.sqrt(e2_n);
        final double lambda0 = 0.04079234433198;
        final double L = Math.log(Math.tan(Math.PI / 4 + phi_n / 2) * Math.pow(((1 - e_n * Math.sin(phi_n)) / (1 + e_n * Math.sin(phi_n))), (e_n / 2)));

        final double X_l2e = Xs + c * Math.exp((-n * L)) * Math.sin(n * (lambda_n - lambda0));
        final double Y_l2e = Ys - c * Math.exp((-n * L)) * Math.cos(n * (lambda_n - lambda0));

        double[] tabXY = new double[2];

        tabXY[0] = X_l2e;
        tabXY[1] = Y_l2e;

        return tabXY;
    }

    // -----------------------------------
    // epsg 27700, uk
    // -----------------------------------
    public static double[] epsg27700FromWGS84(final double lon, final double lat) {

        final double[] osgbLonLat = wgs84LonLatToOsgbLonLat(lon, lat);
        double[] resp = new double[2];

        resp[0] = eastFromLonLat(
                osgbLonLat[1],
                osgbLonLat[0],
                6377563.396,
                6356256.910,
                400000,
                0.999601272,
                49.00000,
                -2.00000);

        resp[1] = northFromLonLat(
                osgbLonLat[1],
                osgbLonLat[0],
                6377563.396,
                6356256.910,
                400000,
                -100000,
                0.999601272,
                49.00000,
                -2.00000);

        return resp;
    }

    private static double[] wgs84LonLatToOsgbLonLat(final double _lon, final double _lat) {

        double height = 0;

        final double x1 = xFromLonLatH(_lat, _lon, height, 6378137.00, 6356752.313);
        final double y1 = yFromLonLatH(_lat, _lon, height, 6378137.00, 6356752.313);
        final double z1 = zFromLatH(_lat, height, 6378137.00, 6356752.313);

        final double x2 = helmertX(x1, y1, z1, -446.448, -0.2470, -0.8421, 20.4894);
        final double y2 = helmertY(x1, y1, z1, 125.157, -0.1502, -0.8421, 20.4894);
        final double z2 = helmertZ(x1, y1, z1, -542.060, -0.1502, -0.2470, 20.4894);

        final double latitude2 = latFromXYZ(x2, y2, z2, 6377563.396, 6356256.910);
        final double longitude2 = lonFromXYZ(x2, y2);

        double[] resp = new double[2];
        resp[0] = longitude2;
        resp[1] = latitude2;
        return resp;
    }

    private static double northFromLonLat(
            final double PHI,
            final double LAM,
            final double a,
            final double b,
            final double e0,
            final double n0,
            final double f0,
            final double PHI0,
            final double LAM0) {

        // Project Latitude and longitude to Transverse Mercator northings
        // Input: - _
        // Latitude (PHI) and Longitude (LAM) in decimal degrees; _
        // ellipsoid axis dimensions (a & b) in meters; _
        // eastings (e0) and northings (n0) of false origin in meters; _
        // central meridian scale factor (f0); _
        // latitude (PHI0) and longitude (LAM0) of false origin in decimal degrees.

        // REQUIRES THE "Marc" FUNCTION

        // Convert angle measures to radians
        final double RadPHI = PHI * PI_DIV_180;
        final double RadLAM = LAM * PI_DIV_180;
        final double RadPHI0 = PHI0 * PI_DIV_180;
        final double RadLAM0 = LAM0 * PI_DIV_180;

        final double af0 = a * f0;
        final double bf0 = b * f0;
        final double e2 = (Math.pow(af0, 2) - Math.pow(bf0, 2)) / Math.pow(af0, 2);
        final double n = (af0 - bf0) / (af0 + bf0);
        final double nu = af0 / (Math.sqrt(1 - (e2 * Math.pow(Math.sin(RadPHI), 2))));
        final double rho = (nu * (1 - e2)) / (1 - (e2 * Math.pow(Math.sin(RadPHI), 2)));
        final double eta2 = (nu / rho) - 1;
        final double p = RadLAM - RadLAM0;
        final double M = marc(bf0, n, RadPHI0, RadPHI);

        final double I = M + n0;
        final double II = (nu / 2) * (Math.sin(RadPHI)) * (Math.cos(RadPHI));
        final double III = ((nu / 24) * (Math.sin(RadPHI)) * (Math.pow(Math.cos(RadPHI), 3))) * (5 - (Math.pow(Math.tan(RadPHI), 2)) + (9 * eta2));
        final double IIIA = ((nu / 720) * (Math.sin(RadPHI)) * (Math.pow(Math.cos(RadPHI), 5))) * (61 - (58 * (Math.pow(Math.tan(RadPHI), 2))) + (Math.pow(Math.tan(RadPHI), 4)));

        return I + (Math.pow(p, 2) * II) + (Math.pow(p, 4) * III) + (Math.pow(p, 6) * IIIA);
    }

    private static double eastFromLonLat(
            final double PHI,
            final double LAM,
            final double a,
            final double b,
            final double e0,
            final double f0,
            final double PHI0,
            final double LAM0) {
        //Project Latitude and longitude to Transverse Mercator eastings.
        //Input: - _
        //    Latitude (PHI) and Longitude (LAM) in decimal degrees; _
        //    ellipsoid axis dimensions (a & b) in meters; _
        //    eastings of false origin (e0) in meters; _
        //    central meridian scale factor (f0); _
        // latitude (PHI0) and longitude (LAM0) of false origin in decimal degrees.

        // Convert angle measures to radians
        final double RadPHI = PHI * PI_DIV_180;
        final double RadLAM = LAM * PI_DIV_180;
        final double RadLAM0 = LAM0 * PI_DIV_180;

        final double af0 = a * f0;
        final double bf0 = b * f0;
        final double e2 = (Math.pow(af0, 2) - Math.pow(bf0, 2)) / Math.pow(af0, 2);
        final double n = (af0 - bf0) / (af0 + bf0);
        final double nu = af0 / (Math.sqrt(1 - (e2 * Math.pow(Math.sin(RadPHI), 2))));
        final double rho = (nu * (1 - e2)) / (1 - (e2 * Math.pow(Math.sin(RadPHI), 2)));
        final double eta2 = (nu / rho) - 1;
        final double p = RadLAM - RadLAM0;

        final double IV = nu * (Math.cos(RadPHI));
        final double V = (nu / 6) * (Math.pow(Math.cos(RadPHI), 3)) * ((nu / rho) -
                (Math.pow(Math.tan(RadPHI), 2)));
        final double VI = (nu / 120) * (Math.pow(Math.cos(RadPHI), 5)) * (5 -
                (18 * (Math.pow(Math.tan(RadPHI), 2))) + (Math.pow(Math.tan(RadPHI), 4)) + (14 * eta2) - (58 * (Math.pow(Math.tan(RadPHI), 2)) * eta2));

        return e0 + (p * IV) + (Math.pow(p, 3) * V) + (Math.pow(p, 5) * VI);
    }

    private static double marc(final double bf0, final double n, final double PHI0, final double PHI) {
        //Compute meridional arc.
        //Input: - _
        // ellipsoid semi major axis multiplied by central meridian scale factor (bf0) in meters; _
        // n (computed from a, b and f0); _
        // lat of false origin (PHI0) and initial or final latitude of point (PHI) IN RADIANS.

        //THIS FUNCTION IS CALLED BY THE - _
        // "Lat_Long_to_North" and "InitialLat" FUNCTIONS
        // THIS FUNCTION IS ALSO USED ON IT'S OWN IN THE "Projection and Transformation
        // Calculations.xls" SPREADSHEET

        return bf0 * (((1 + n + ((5 / 4) * Math.pow(n, 2)) + ((5 / 4) * Math.pow(n, 3))) * (PHI - PHI0)) - (((3 * n) + (3 * Math.pow(n, 2)) + ((21 / 8) * Math.pow(n, 3))) * (Math.sin(PHI - PHI0)) * (Math.cos(PHI + PHI0))) + ((((15 / 8) * Math.pow(n, 2)) + ((15 / 8) * Math.pow(n, 3))) * (Math.sin(2 * (PHI - PHI0))) * (Math.cos(2 * (PHI + PHI0)))) - (((35 / 24) * Math.pow(n, 3)) *
                (Math.sin(3 * (PHI - PHI0))) * (Math.cos(3 * (PHI + PHI0)))));
    }

    private static double xFromLonLatH(final double PHI, final double LAM, final double H, final double a, final double b) {
        // Convert geodetic coords lat (PHI), long (LAM) and height (H) to cartesian X coordinate.
        // Input: - _
        //    Latitude (PHI)& Longitude (LAM) both in decimal degrees; _
        //  Ellipsoidal height (H) and ellipsoid axis dimensions (a & b) all in meters.

        // Convert angle measures to radians
        final double RadPHI = PHI * PI_DIV_180;
        final double RadLAM = LAM * PI_DIV_180;

        // Compute eccentricity squared and nu
        final double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
        final double V = a / (Math.sqrt(1 - (e2 * (Math.pow(Math.sin(RadPHI), 2)))));

        // Compute X
        return (V + H) * (Math.cos(RadPHI)) * (Math.cos(RadLAM));
    }

    private static double yFromLonLatH(final double PHI, final double LAM, final double H, final double a, final double b) {
        // Convert geodetic coords lat (PHI), long (LAM) and height (H) to cartesian Y coordinate.
        // Input: - _
        // Latitude (PHI)& Longitude (LAM) both in decimal degrees; _
        // Ellipsoidal height (H) and ellipsoid axis dimensions (a & b) all in meters.

        // Convert angle measures to radians
        final double RadPHI = PHI * PI_DIV_180;
        final double RadLAM = LAM * PI_DIV_180;

        // Compute eccentricity squared and nu
        final double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
        final double V = a / (Math.sqrt(1 - (e2 * (Math.pow(Math.sin(RadPHI), 2)))));

        // Compute Y
        return (V + H) * (Math.cos(RadPHI)) * (Math.sin(RadLAM));
    }

    private static double zFromLatH(final double PHI, final double H, final double a, final double b) {
        // Convert geodetic coord components latitude (PHI) and height (H) to cartesian Z coordinate.
        // Input: - _
        //    Latitude (PHI) decimal degrees; _
        // Ellipsoidal height (H) and ellipsoid axis dimensions (a & b) all in meters.

        // Convert angle measures to radians
        final double RadPHI = PHI * PI_DIV_180;

        // Compute eccentricity squared and nu
        final double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
        final double V = a / (Math.sqrt(1 - (e2 * (Math.pow(Math.sin(RadPHI), 2)))));

        // Compute X
        return ((V * (1 - e2)) + H) * (Math.sin(RadPHI));
    }

    private static double helmertX(final double X, final double Y, final double Z, final double DX, final double Y_Rot, final double Z_Rot, final double s) {

        // (X, Y, Z, DX, Y_Rot, Z_Rot, s)
        // Computed Helmert transformed X coordinate.
        // Input: - _
        //    cartesian XYZ coords (X,Y,Z), X translation (DX) all in meters ; _
        // Y and Z rotations in seconds of arc (Y_Rot, Z_Rot) and scale in ppm (s).

        // Convert rotations to radians and ppm scale to a factor
        final double sfactor = s * 0.000001;
        final double RadY_Rot = (Y_Rot / 3600) * PI_DIV_180;
        final double RadZ_Rot = (Z_Rot / 3600) * PI_DIV_180;

        //Compute transformed X coord
        return (X + (X * sfactor) - (Y * RadZ_Rot) + (Z * RadY_Rot) + DX);
    }

    private static double helmertY(final double X, final double Y, final double Z, final double DY, final double X_Rot, final double Z_Rot, final double s) {
        // (X, Y, Z, DY, X_Rot, Z_Rot, s)
        // Computed Helmert transformed Y coordinate.
        // Input: - _
        //    cartesian XYZ coords (X,Y,Z), Y translation (DY) all in meters ; _
        //  X and Z rotations in seconds of arc (X_Rot, Z_Rot) and scale in ppm (s).

        // Convert rotations to radians and ppm scale to a factor
        final double sfactor = s * 0.000001;
        final double RadX_Rot = (X_Rot / 3600) * PI_DIV_180;
        final double RadZ_Rot = (Z_Rot / 3600) * PI_DIV_180;

        // Compute transformed Y coord
        return (X * RadZ_Rot) + Y + (Y * sfactor) - (Z * RadX_Rot) + DY;

    }

    private static double helmertZ(final double X, final double Y, final double Z, final double DZ, final double X_Rot, final double Y_Rot, final double s) {
        // (X, Y, Z, DZ, X_Rot, Y_Rot, s)
        // Computed Helmert transformed Z coordinate.
        // Input: - _
        //    cartesian XYZ coords (X,Y,Z), Z translation (DZ) all in meters ; _
        // X and Y rotations in seconds of arc (X_Rot, Y_Rot) and scale in ppm (s).
        // 
        // Convert rotations to radians and ppm scale to a factor
        final double sfactor = s * 0.000001;
        final double RadX_Rot = (X_Rot / 3600) * PI_DIV_180;
        final double RadY_Rot = (Y_Rot / 3600) * PI_DIV_180;

        // Compute transformed Z coord
        return (-1 * X * RadY_Rot) + (Y * RadX_Rot) + Z + (Z * sfactor) + DZ;
    }

    private static double latFromXYZ(final double X, final double Y, final double Z, final double a, final double b) {
        // Convert XYZ to Latitude (PHI) in Dec Degrees.
        // Input: - _
        // XYZ cartesian coords (X,Y,Z) and ellipsoid axis dimensions (a & b), all in meters.

        // THIS FUNCTION REQUIRES THE "Iterate_XYZ_to_Lat" FUNCTION
        // THIS FUNCTION IS CALLED BY THE "XYZ_to_H" FUNCTION

        final double RootXYSqr = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));
        final double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
        final double PHI1 = Math.atan2(Z, (RootXYSqr * (1 - e2)));

        final double PHI = iterateLatFromXYZ(a, e2, PHI1, Z, RootXYSqr);

        return PHI * DEGREES_PER_RADIAN;
    }

    private static double iterateLatFromXYZ(final double a, final double e2, double PHI1, final double Z, final double RootXYSqr) {
        // Iteratively computes Latitude (PHI).
        // Input: - _
        //    ellipsoid semi major axis (a) in meters; _
        //    eta squared (e2); _
        //    estimated value for latitude (PHI1) in radians; _
        //    cartesian Z coordinate (Z) in meters; _
        // RootXYSqr computed from X & Y in meters.

        // THIS FUNCTION IS CALLED BY THE "XYZ_to_PHI" FUNCTION
        // THIS FUNCTION IS ALSO USED ON IT'S OWN IN THE _
        // "Projection and Transformation Calculations.xls" SPREADSHEET


        double V = a / (Math.sqrt(1 - (e2 * Math.pow(Math.sin(PHI1), 2))));
        double PHI2 = Math.atan2((Z + (e2 * V * (Math.sin(PHI1)))), RootXYSqr);

        while (Math.abs(PHI1 - PHI2) > 0.000000000001) {
            PHI1 = PHI2;
            V = a / (Math.sqrt(1 - (e2 * Math.pow(Math.sin(PHI1), 2))));
            PHI2 = Math.atan2((Z + (e2 * V * (Math.sin(PHI1)))), RootXYSqr);
        }

        return PHI2;
    }

    private static double lonFromXYZ(final double X, final double Y) {
        // Convert XYZ to Longitude (LAM) in Dec Degrees.
        // Input: - _
        // X and Y cartesian coords in meters.
        return Math.atan2(Y, X) * DEGREES_PER_RADIAN;
    }

    // ---------------------------------
    // -------- LAMBERT, STEPHANE FALCK
    // -------------------------------
    /**
     * Converts the given WGS84 coordinates into the Lambert Zone I coordinates (EPSG:27561)
     * @param lon longitude
     * @param lat latitude
     * @return the coordinates in the Lambert Zone I coordinates (EPSG:27561)
     */
    private static double[] WGS84toLambertZoneI(final double lon, final double lat) {

        final double lambda_w = lon * PI_DIV_180;
        final double phi_w = lat * PI_DIV_180;

        // from here, everything is the same:
        // see code: http://www.forumsig.org/showthread.php?t=7418 from Phiz and Ymerej
        /**************************************************************************************************************/
        /**        1) coordonnées géographiques WGS84 (phi_w,lambda_w) -> coordonnées cartésiennes WGS84 (X_w,Y_w,Z_w)  **/
        /**************************************************************************************************************/
        final double a_w = 6378137.0;
        final double b_w = 6356752.314;

        final double e2_w = (a_w * a_w - b_w * b_w) / (a_w * a_w);

        final double N = a_w / Math.sqrt(1 - e2_w * Math.pow(Math.sin(phi_w), 2));

        final double X_w = N * Math.cos(phi_w) * Math.cos(lambda_w);
        final double Y_w = N * Math.cos(phi_w) * Math.sin(lambda_w);
        final double Z_w = N * (1 - e2_w) * Math.sin(phi_w);

        /**************************************************************************************************************/
        /**        2) coordonnées cartésiennes WGS84 (X_w,Y_w,Z_w) -> coordonnées cartésiennes NTF (X_n,Y_n,Z_n)          **/
        /**************************************************************************************************************/
        final double dX = 168.0;
        final double dY = 60.0;
        final double dZ = -320.0;

        final double X_n = X_w + dX;
        final double Y_n = Y_w + dY;
        final double Z_n = Z_w + dZ;

        /**************************************************************************************************************/
        /**        3) coordonnées cartésiennes NTF (X_n,Y_n,Z_n) -> coordonnées géographiques NTF (phi_n,lambda_n)      **/
        /**************************************************************************************************************/
        final double a_n = 6378249.2;
        final double b_n = 6356515.0;

        final double e2_n = (a_n * a_n - b_n * b_n) / (a_n * a_n);

        final double epsilon = DEFAULT_THRESHOLD;

        double p0 = Math.atan(Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n) * (1 - (a_n * e2_n) / (Math.sqrt(X_n * X_n + Y_n * Y_n + Z_n * Z_n))));
        double p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));

        while (!(Math.abs(p1 - p0) < epsilon)) {

            p0 = p1;
            p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));

        }

        final double phi_n = p1;
        final double lambda_n = Math.atan(Y_n / X_n);

        /**********************************************************************************************************************/
        /**        4) coordonnées géographiques NTF (phi_n,lambda_n)  coordonnées projetées en Lambert Zone I (X_lz1, Y_lz1) **/
        /**********************************************************************************************************************/
        final double n = 0.7604059656;
        final double c = 11603796.98;
        final double Xs = 600000.0;
        final double Ys = 5657616.674;

        final double e_n = Math.sqrt(e2_n);
        final double lambda0 = 0.04079234433198;

        final double L = Math.log(Math.tan(Math.PI / 4 + phi_n / 2) * Math.pow(((1 - e_n * Math.sin(phi_n)) / (1 + e_n * Math.sin(phi_n))), (e_n / 2)));


        final double X_lz1 = Xs + c * Math.exp((-n * L)) * Math.sin(n * (lambda_n - lambda0));
        final double Y_lz1 = Ys - c * Math.exp((-n * L)) * Math.cos(n * (lambda_n - lambda0));

        double[] tabXY = new double[2];

        tabXY[0] = X_lz1;
        tabXY[1] = Y_lz1;

        return tabXY;
    }

    /**
     * Converts the given WGS84 coordinates into the Lambert Zone II coordinates (EPSG:27562)
     * @param lon longitude
     * @param lat latitude
     * @return the coordinates in the Lambert Zone II coordinates (EPSG:27562)
     */
    private static double[] WGS84toLambertZoneII(final double lon, final double lat) {

        final double lambda_w = lon * PI_DIV_180;
        final double phi_w = lat * PI_DIV_180;

        // from here, everything is the same:
        // see code: http://www.forumsig.org/showthread.php?t=7418 from Phiz and Ymerej
        /**************************************************************************************************************/
        /**        1) coordonnées géographiques WGS84 (phi_w,lambda_w) -> coordonnées cartésiennes WGS84 (X_w,Y_w,Z_w)  **/
        /**************************************************************************************************************/
        final double a_w = 6378137.0;
        final double b_w = 6356752.314;

        final double e2_w = (a_w * a_w - b_w * b_w) / (a_w * a_w);

        final double N = a_w / Math.sqrt(1 - e2_w * Math.pow(Math.sin(phi_w), 2));

        final double X_w = N * Math.cos(phi_w) * Math.cos(lambda_w);
        final double Y_w = N * Math.cos(phi_w) * Math.sin(lambda_w);
        final double Z_w = N * (1 - e2_w) * Math.sin(phi_w);

        /**************************************************************************************************************/
        /**        2) coordonnées cartésiennes WGS84 (X_w,Y_w,Z_w) -> coordonnées cartésiennes NTF (X_n,Y_n,Z_n)          **/
        /**************************************************************************************************************/
        final double dX = 168.0;
        final double dY = 60.0;
        final double dZ = -320.0;

        final double X_n = X_w + dX;
        final double Y_n = Y_w + dY;
        final double Z_n = Z_w + dZ;

        /**************************************************************************************************************/
        /**        3) coordonnées cartésiennes NTF (X_n,Y_n,Z_n) -> coordonnées géographiques NTF (phi_n,lambda_n)      **/
        /**************************************************************************************************************/
        final double a_n = 6378249.2;
        final double b_n = 6356515.0;

        final double e2_n = (a_n * a_n - b_n * b_n) / (a_n * a_n);

        final double epsilon = Math.pow(10, -10);

        double p0 = Math.atan(Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n) * (1 - (a_n * e2_n) / (Math.sqrt(X_n * X_n + Y_n * Y_n + Z_n * Z_n))));
        double p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));

        while (!(Math.abs(p1 - p0) < epsilon)) {

            p0 = p1;
            p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));

        }

        final double phi_n = p1;
        final double lambda_n = Math.atan(Y_n / X_n);

        /**********************************************************************************************************************/
        /**        4) coordonnées géographiques NTF (phi_n,lambda_n)  coordonnées projetées en Lambert Zone II (X_lz2, Y_lz2) **/
        /**********************************************************************************************************************/
        final double n = 0.7289686274;
        final double c = 11745793.39;
        final double Xs = 600000.0;
        final double Ys = 6199965.768;

        final double e_n = Math.sqrt(e2_n);
        final double lambda0 = 0.04079234433198;

        final double L = Math.log(Math.tan(Math.PI / 4 + phi_n / 2) * Math.pow(((1 - e_n * Math.sin(phi_n)) / (1 + e_n * Math.sin(phi_n))), (e_n / 2)));


        final double X_lz2 = Xs + c * Math.exp((-n * L)) * Math.sin(n * (lambda_n - lambda0));
        final double Y_lz2 = Ys - c * Math.exp((-n * L)) * Math.cos(n * (lambda_n - lambda0));

        double[] tabXY = new double[2];

        tabXY[0] = X_lz2;
        tabXY[1] = Y_lz2;

        return tabXY;
    }

    /**
     * Converts the given WGS84 coordinates into the Extended Lambert II coordinates  (EPSG:27572)
     * note: Lambert Zone II is EPSG:27562
     * @param lon longitude
     * @param lat latitude
     * @return the coordinates in Lambert II etendu coordinates (EPSG:27572)
     */
    private static double[] WGS84toLambert2e(final double lon, final double lat) {

        final double lambda_w = lon * PI_DIV_180;
        final double phi_w = lat * PI_DIV_180;

        // from here, everything is the same:
        // see code: http://www.forumsig.org/showthread.php?t=7418 from Phiz and Ymerej
        /**************************************************************************************************************/
        /**        1) coordonnées géographiques WGS84 (phi_w,lambda_w) -> coordonnées cartésiennes WGS84 (X_w,Y_w,Z_w)  **/
        /**************************************************************************************************************/
        final double a_w = 6378137.0;
        final double b_w = 6356752.314;

        final double e2_w = (a_w * a_w - b_w * b_w) / (a_w * a_w);

        final double N = a_w / Math.sqrt(1 - e2_w * Math.pow(Math.sin(phi_w), 2));

        final double X_w = N * Math.cos(phi_w) * Math.cos(lambda_w);
        final double Y_w = N * Math.cos(phi_w) * Math.sin(lambda_w);
        final double Z_w = N * (1 - e2_w) * Math.sin(phi_w);

        /**************************************************************************************************************/
        /**        2) coordonnées cartésiennes WGS84 (X_w,Y_w,Z_w) -> coordonnées cartésiennes NTF (X_n,Y_n,Z_n)          **/
        /**************************************************************************************************************/
        final double dX = 168.0;
        final double dY = 60.0;
        final double dZ = -320.0;

        final double X_n = X_w + dX;
        final double Y_n = Y_w + dY;
        final double Z_n = Z_w + dZ;

        /**************************************************************************************************************/
        /**        3) coordonnées cartésiennes NTF (X_n,Y_n,Z_n) -> coordonnées géographiques NTF (phi_n,lambda_n)      **/
        /**************************************************************************************************************/
        final double a_n = 6378249.2;
        final double b_n = 6356515.0;

        final double e2_n = (a_n * a_n - b_n * b_n) / (a_n * a_n);

        final double epsilon = DEFAULT_THRESHOLD;

        double p0 = Math.atan(Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n) * (1 - (a_n * e2_n) / (Math.sqrt(X_n * X_n + Y_n * Y_n + Z_n * Z_n))));
        double p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));

        while (!(Math.abs(p1 - p0) < epsilon)) {

            p0 = p1;
            p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));

        }

        final double phi_n = p1;
        final double lambda_n = Math.atan(Y_n / X_n);

        /**********************************************************************************************************************/
        /**        4) coordonnées géographiques NTF (phi_n,lambda_n)  coordonnées projetées en Lambert II étendu (X_l2e, Y_l2e) **/
        /**********************************************************************************************************************/
        final double n = 0.7289686274;
        final double c = 11745793.39;
        final double Xs = 600000.0;
        final double Ys = 8199695.768;

        final double e_n = Math.sqrt(e2_n);
        final double lambda0 = 0.04079234433198;

        final double L = Math.log(Math.tan(Math.PI / 4 + phi_n / 2) * Math.pow(((1 - e_n * Math.sin(phi_n)) / (1 + e_n * Math.sin(phi_n))), (e_n / 2)));


        final double X_l2e = Xs + c * Math.exp((-n * L)) * Math.sin(n * (lambda_n - lambda0));
        final double Y_l2e = Ys - c * Math.exp((-n * L)) * Math.cos(n * (lambda_n - lambda0));

        double[] tabXY = new double[2];

        tabXY[0] = X_l2e;
        tabXY[1] = Y_l2e;

        return tabXY;
    }

    /**
     * Converts the given WGS84 coordinates into the Lambert Zone III coordinates (EPSG:27563)
     * @param lon longitude
     * @param lat latitude
     * @return the coordinates in the Lambert Zone III coordinates (EPSG:27563)
     */
    private static double[] WGS84toLambertZoneIII(final double lon, final double lat) {

        final double lambda_w = lon * PI_DIV_180;
        final double phi_w = lat * PI_DIV_180;

        // from here, everything is the same:
        // see code: http://www.forumsig.org/showthread.php?t=7418 from Phiz and Ymerej
        /**************************************************************************************************************/
        /**        1) coordonnées géographiques WGS84 (phi_w,lambda_w) -> coordonnées cartésiennes WGS84 (X_w,Y_w,Z_w)  **/
        /**************************************************************************************************************/
        final double a_w = 6378137.0;
        final double b_w = 6356752.314;

        final double e2_w = (a_w * a_w - b_w * b_w) / (a_w * a_w);

        final double N = a_w / Math.sqrt(1 - e2_w * Math.pow(Math.sin(phi_w), 2));

        final double X_w = N * Math.cos(phi_w) * Math.cos(lambda_w);
        final double Y_w = N * Math.cos(phi_w) * Math.sin(lambda_w);
        final double Z_w = N * (1 - e2_w) * Math.sin(phi_w);

        /**************************************************************************************************************/
        /**        2) coordonnées cartésiennes WGS84 (X_w,Y_w,Z_w) -> coordonnées cartésiennes NTF (X_n,Y_n,Z_n)          **/
        /**************************************************************************************************************/
        final double dX = 168.0;
        final double dY = 60.0;
        final double dZ = -320.0;

        final double X_n = X_w + dX;
        final double Y_n = Y_w + dY;
        final double Z_n = Z_w + dZ;

        /**************************************************************************************************************/
        /**        3) coordonnées cartésiennes NTF (X_n,Y_n,Z_n) -> coordonnées géographiques NTF (phi_n,lambda_n)      **/
        /**************************************************************************************************************/
        final double a_n = 6378249.2;
        final double b_n = 6356515.0;

        final double e2_n = (a_n * a_n - b_n * b_n) / (a_n * a_n);

        final double epsilon = DEFAULT_THRESHOLD;

        double p0 = Math.atan(Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n) * (1 - (a_n * e2_n) / (Math.sqrt(X_n * X_n + Y_n * Y_n + Z_n * Z_n))));
        double p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));

        while (!(Math.abs(p1 - p0) < epsilon)) {

            p0 = p1;
            p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));

        }

        final double phi_n = p1;
        final double lambda_n = Math.atan(Y_n / X_n);

        /**********************************************************************************************************************/
        /**        4) coordonnées géographiques NTF (phi_n,lambda_n)  coordonnées projetées en Lambert Zone III (X_lz3, Y_lz3) **/
        /**********************************************************************************************************************/
        final double n = 0.6959127966;
        final double c = 11947992.52;
        final double Xs = 600000.0;
        final double Ys = 6791905.085;

        final double e_n = Math.sqrt(e2_n);
        final double lambda0 = 0.04079234433198;

        final double L = Math.log(Math.tan(Math.PI / 4 + phi_n / 2) * Math.pow(((1 - e_n * Math.sin(phi_n)) / (1 + e_n * Math.sin(phi_n))), (e_n / 2)));


        final double X_lz3 = Xs + c * Math.exp((-n * L)) * Math.sin(n * (lambda_n - lambda0));
        final double Y_lz3 = Ys - c * Math.exp((-n * L)) * Math.cos(n * (lambda_n - lambda0));

        double[] tabXY = new double[2];

        tabXY[0] = X_lz3;
        tabXY[1] = Y_lz3;

        return tabXY;
    }

    /**
     * Converts the given WGS84 coordinates into the Lambert Zone IV coordinates (EPSG:27564)
     * @param lon longitude
     * @param lat latitude
     * @return the coordinates in the Lambert Zone IV coordinates(EPSG:27564)
     */
    private static double[] WGS84toLambertZoneIV(double lon, double lat) {

        final double lambda_w = lon * PI_DIV_180;
        final double phi_w = lat * PI_DIV_180;

        // from here, everything is the same:
        // see code: http://www.forumsig.org/showthread.php?t=7418 from Phiz and Ymerej
        /**************************************************************************************************************/
        /**        1) coordonnées géographiques WGS84 (phi_w,lambda_w) -> coordonnées cartésiennes WGS84 (X_w,Y_w,Z_w)  **/
        /**************************************************************************************************************/
        final double a_w = 6378137.0;
        final double b_w = 6356752.314;

        final double e2_w = (a_w * a_w - b_w * b_w) / (a_w * a_w);

        final double N = a_w / Math.sqrt(1 - e2_w * Math.pow(Math.sin(phi_w), 2));

        final double X_w = N * Math.cos(phi_w) * Math.cos(lambda_w);
        final double Y_w = N * Math.cos(phi_w) * Math.sin(lambda_w);
        final double Z_w = N * (1 - e2_w) * Math.sin(phi_w);

        /**************************************************************************************************************/
        /**        2) coordonnées cartésiennes WGS84 (X_w,Y_w,Z_w) -> coordonnées cartésiennes NTF (X_n,Y_n,Z_n)          **/
        /**************************************************************************************************************/
        final double dX = 168.0;
        final double dY = 60.0;
        final double dZ = -320.0;

        final double X_n = X_w + dX;
        final double Y_n = Y_w + dY;
        final double Z_n = Z_w + dZ;

        /**************************************************************************************************************/
        /**        3) coordonnées cartésiennes NTF (X_n,Y_n,Z_n) -> coordonnées géographiques NTF (phi_n,lambda_n)      **/
        /**************************************************************************************************************/
        final double a_n = 6378249.2;
        final double b_n = 6356515.0;

        final double e2_n = (a_n * a_n - b_n * b_n) / (a_n * a_n);

        final double epsilon = DEFAULT_THRESHOLD;

        double p0 = Math.atan(Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n) * (1 - (a_n * e2_n) / (Math.sqrt(X_n * X_n + Y_n * Y_n + Z_n * Z_n))));
        double p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));

        while (!(Math.abs(p1 - p0) < epsilon)) {

            p0 = p1;
            p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));

        }

        final double phi_n = p1;
        final double lambda_n = Math.atan(Y_n / X_n);

        /**********************************************************************************************************************/
        /**        4) coordonnées géographiques NTF (phi_n,lambda_n)  coordonnées projetées en Lambert Zone IV (X_lz4, Y_lz4) **/
        /**********************************************************************************************************************/
        final double n = 0.6712679322;
        final double c = 12136281.99;
        final double Xs = 234.358;
        final double Ys = 7239161.542;

        final double e_n = Math.sqrt(e2_n);
        final double lambda0 = 0.04079234433198;

        final double L = Math.log(Math.tan(Math.PI / 4 + phi_n / 2) * Math.pow(((1 - e_n * Math.sin(phi_n)) / (1 + e_n * Math.sin(phi_n))), (e_n / 2)));


        final double X_lz4 = Xs + c * Math.exp((-n * L)) * Math.sin(n * (lambda_n - lambda0));
        final double Y_lz4 = Ys - c * Math.exp((-n * L)) * Math.cos(n * (lambda_n - lambda0));

        double[] tabXY = new double[2];

        tabXY[0] = X_lz4;
        tabXY[1] = Y_lz4;

        return tabXY;
    }

    public static double[] reproject(double lon, double lat, Projection from, Projection to) {
        double[] resp = new double[2];
        try {
            if (from == null && to == null) {
                return null;
            }

            if (GeoUtils.equals(from.getAbrev(), to.getAbrev())) {
                resp[0] = lon;
                resp[1] = lat;
                return resp;
            }

            if (GeoUtils.isLatLonProjection(from.getAbrev())) {
                resp = ConversionCoords.wgs842projection(lon, lat, to);
            } else if (GeoUtils.isMercatorProjection(from.getAbrev())) {
                if (GeoUtils.isLatLonProjection(to.getAbrev())) {
                    final Point p = TileConversor.mercatorToLatLon(lon, lat);
                    resp[0] = p.getX();
                    resp[1] = p.getY();
                } else if (GeoUtils.isMercatorProjection(to.getAbrev())){
                    final Point p = TileConversor.mercatorToLatLon(lon, lat);
                    resp = ConversionCoords.wgs842projection(p.getX(), p.getY(), to);
                } else {
                    final Point p = TileConversor.mercatorToLatLon(lon, lat);
                    resp = ConversionCoords.wgs842projection(p.getX(), p.getY(), to);
                }
            } else {
                if (GeoUtils.isLatLonProjection(to.getAbrev())) {
                    resp = ConversionCoords.projection2wgs84(lon, lat, from);
                } else if (GeoUtils.isMercatorProjection(to.getAbrev())) {
                    resp = ConversionCoords.projection2wgs84(lon, lat, from);
                    final Point p = TileConversor.latLonToMercator(resp[0], resp[1]);
                    resp[0] = p.getX();
                    resp[1] = p.getY();
                } else {
                    final double[] wgs84 = ConversionCoords.projection2wgs84(lon, lat, from);
                    resp = ConversionCoords.wgs842projection(wgs84[0], wgs84[1], to);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return resp;
    }
}
