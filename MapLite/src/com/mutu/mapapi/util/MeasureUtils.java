package com.mutu.mapapi.util;

import com.mutu.mapapi.api.IGeoPoint;


public final class MeasureUtils
{
  
  public static double getArea(double x[], double y[]){
	  int i = x.length;
	    double d1 = 0.0D;
	    double d3;
	    double d4;
	    double d5;
	    double d6;

	      int j = i - 1;
	      for (int k = 0; k < i; k++)
	      {
	        d3 = x[j];
	        d4 = y[j];
	        d5 = x[k];
	        d6 = y[k];
	        d1 += d3 * d6 - d5 * d4;
	        j = k;
	      }
	      d1 /= 2.0D;
	      return Math.abs(d1);
  }
  
  public static double getSphericalArea(IGeoPoint geoPoints[])
  {
    int i = geoPoints.length;
    if(i < 3) 
    	return 0.0;
    double d1 = 0.0D;
    double d3;
    double d4;
    double d5;
    double d6;
    
      double d2 = 0.0D;
      d3 = 0.0D;
      d4 = 0.0D;
      d5 = 0.0D;
      d6 = 0.0D;
      double d7 = 0.0D;
      double d8 = 0.0D;
      double d9 = 0.0D;
      double d10 = 0.0D;
      double d11 = 0.0D;
      double d12 = 0.0D;
      double d13 = 0.0D;
      double d14 = 0.0D;
      double d15 = 0.0D;
      double d16 = 0.0D;
      double d17 = 0.0D;
      double d18 = 0.0D;
      double d19 = 0.0D;
      double d20 = 0.0D;
      double d21 = 0.0D;
      double d22 = 0.0D;
      double d23 = 0.0D;
      double d24 = 0.0D;
      double d25 = 0.0D;
      double d26 = 0.0D;
      double d27 = 0.0D;
      double d28 = 0.0D;
      double d29 = 0.0D;
      double d30 = 0.0D;
      double d31 = 0.0D;
      double d32 = 0.0D;
      double d33 = 0.0D;
      for (int m = 0; m < i; m++)
      {
        if (m == 0)
        {
          d2 = geoPoints[i-1].getLongitude() * 3.141592653589793D / 180.0D;
          d3 = geoPoints[i-1].getLatitude() * 3.141592653589793D / 180.0D;
          d4 = geoPoints[0].getLongitude() * 3.141592653589793D / 180.0D;
          d5 = geoPoints[0].getLatitude() * 3.141592653589793D / 180.0D;
          d6 = geoPoints[1].getLongitude() * 3.141592653589793D / 180.0D;
          d7 = geoPoints[1].getLatitude() * 3.141592653589793D / 180.0D;
        }
        else if (m == i - 1)
        {
          d2 = geoPoints[i-2].getLongitude() * 3.141592653589793D / 180.0D;
          d3 = geoPoints[i-2].getLatitude() * 3.141592653589793D / 180.0D;
          d4 = geoPoints[i-1].getLongitude() * 3.141592653589793D / 180.0D;
          d5 = geoPoints[i-1].getLatitude() * 3.141592653589793D / 180.0D;
          d6 = geoPoints[0].getLongitude() * 3.141592653589793D / 180.0D;
          d7 = geoPoints[0].getLatitude() * 3.141592653589793D / 180.0D;
        }
        else
        {
          d2 = geoPoints[m-1].getLongitude() * 3.141592653589793D / 180.0D;
          d3 = geoPoints[m-1].getLatitude() * 3.141592653589793D / 180.0D;
          d4 = geoPoints[m].getLongitude() * 3.141592653589793D / 180.0D;
          d5 = geoPoints[m].getLatitude() * 3.141592653589793D / 180.0D;
          d6 = geoPoints[m+1].getLongitude() * 3.141592653589793D / 180.0D;
          d7 = geoPoints[m+1].getLatitude() * 3.141592653589793D / 180.0D;
        }
        d8 = Math.cos(d5) * Math.cos(d4);
        d9 = Math.cos(d5) * Math.sin(d4);
        d10 = Math.sin(d5);
        d11 = Math.cos(d3) * Math.cos(d2);
        d12 = Math.cos(d3) * Math.sin(d2);
        d13 = Math.sin(d3);
        d14 = Math.cos(d7) * Math.cos(d6);
        d15 = Math.cos(d7) * Math.sin(d6);
        d16 = Math.sin(d7);
        d17 = (d8 * d8 + d9 * d9 + d10 * d10) / (d8 * d11 + d9 * d12 + d10 * d13);
        d18 = (d8 * d8 + d9 * d9 + d10 * d10) / (d8 * d14 + d9 * d15 + d10 * d16);
        d19 = d17 * d11 - d8;
        d20 = d17 * d12 - d9;
        d21 = d17 * d13 - d10;
        d22 = d18 * d14 - d8;
        d23 = d18 * d15 - d9;
        d24 = d18 * d16 - d10;
        d29 = Math.acos(d29 = (d22 * d19 + d23 * d20 + d24 * d21) / (Math.sqrt(d22 * d22 + d23 * d23 + d24 * d24) * Math.sqrt(d19 * d19 + d20 * d20 + d21 * d21)));
        d25 = d23 * d21 - d24 * d20;
        d26 = 0.0D - (d22 * d21 - d24 * d19);
        d27 = d22 * d20 - d23 * d19;
        if (d8 != 0.0D)
          d28 = d25 / d8;
        else if (d9 != 0.0D)
          d28 = d26 / d9;
        else
          d28 = d27 / d10;
        if (d28 > 0.0D)
        {
          d30 += d29;
          d33 += 1.0D;
        }
        else
        {
          d31 += d29;
          d32 += 1.0D;
        }
      }
      double d34;
      if (d30 > d31)
        d34 = d30 + (d32 * 6.283185307179586D - d31);
      else
        d34 = d33 * 6.283185307179586D - d30 + d31;
      d1 = (d34 - (i - 2) * 3.141592653589793D) * 6378000.0D * 6378000.0D;
    
    return Math.abs(d1);
  }

  public static double getSphericalDistance(IGeoPoint geoPoints[])
  {
	  double dis = 0;
  
	  for (int i = 1;i<geoPoints.length;i++){
		  dis += getSphericalDistance(geoPoints[i-1],geoPoints[i]);
	  }
	  
	  return dis;
  }
  
  public static double getSphericalDistance(IGeoPoint geoPoint1, IGeoPoint geoPoint2)
  {
	  return getSphericalDistance(geoPoint1.getLongitude(),geoPoint1.getLatitude(),geoPoint2.getLongitude(),geoPoint2.getLatitude());
  }

  public static double getSphericalDistance(double x1, double y1, double x2, double y2)
  {
      double d1 = y1 / 180.0D * 3.141592653589793D;
      double d2 = x1 / 180.0D * 3.141592653589793D;
      double d3 = y2 / 180.0D * 3.141592653589793D;
      double d4 = x2 / 180.0D * 3.141592653589793D;
      double d5 = Math.acos(d5 = Math.sin(d1) * Math.sin(d3) + Math.cos(d1) * Math.cos(d3) * Math.cos(d4 - d2)) * 6370693.4856530577D;
      return d5;
  }

  public static final double getDistance(double x1, double y1, double x2, double y2)
  {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }

}
 