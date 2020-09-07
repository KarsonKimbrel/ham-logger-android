package com.kimbrelk.android.hamlogger.utils;

public class Gridsquare {
	
	String latlon;
	String maidenhead;
	
	public Gridsquare(String p1, String p2) {
		float lat = -100.0f;
		float lon = 0.0f;
		try {
			lat = Float.parseFloat(p1);
			lon = Float.parseFloat(p2);
			
			maidenhead = latLonToGridSquare(lat, lon);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Gridsquare(float lat, float lon) {
		try {
			maidenhead = latLonToGridSquare(lat, lon);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void gridSquareToLatLon(String grid) {
		
	}
	
	public String latLonToGridSquare(float lat, float lon) throws Exception{
		float adjLat,adjLon;
		char GLat,GLon;
		String nLat,nLon;
		char gLat,gLon;
		float rLat,rLon;
		String U = "ABCDEFGHIJKLMNOPQRSTUVWX";
		String L = U.toLowerCase();
		
		// support Chris Veness 2002-2012 LatLon library and
		// other objects with lat/lon properties
		// properties could be getter functions, numbers, or strings
		
		
		if (Float.isNaN(lat)) throw new Exception("lat is NaN");
		if (Float.isNaN(lon)) throw new Exception("lon is NaN");
		if (Math.abs(lat) == 90.0) throw new Exception("grid squares invalid at N/S poles");
		if (Math.abs(lat) > 90) throw new Exception("invalid latitude: "+lat);
		if (Math.abs(lon) > 180) throw new Exception("invalid longitude: "+lon);
		
		
		adjLat = lat + 90;
		adjLon = lon + 180;
		GLat = U.charAt((int) (adjLat/10));
		GLon = U.charAt((int) (adjLon/20));
		nLat = ""+(int)(adjLat % 10);
		nLon = ""+(int)((adjLon/2) % 10);
		rLat = (adjLat - (int)(adjLat)) * 60;
		rLon = (adjLon - 2*(int)(adjLon/2)) *60;
		gLat = L.charAt((int)(rLat/2.5));
		gLon = L.charAt((int)(rLon/5));
		String locator = ""+GLon+GLat+nLon+nLat+gLon+gLat;
		return locator;
	}
}