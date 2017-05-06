package com.telemetry.strategy;

import java.time.LocalDateTime;

public class SolarAngles {

	/**
	 * Returns a SolarPosition object which contains the elevation and azimuth
	 * @param latitude
	 * @param longitude
	 * @param GMTDateTime
	 * @param inWesternHemisphere
	 * @return
	 */
	public static SolarPosition getSolarPosition(double latitude, double longitude, LocalDateTime GMTDateTime, boolean inWesternHemisphere)
	{
		int second = GMTDateTime.getSecond(),
			minute = GMTDateTime.getMinute(),
			hour = GMTDateTime.getHour(),
			day = GMTDateTime.getDayOfMonth(),
			month = GMTDateTime.getMonthValue(),
			year = GMTDateTime.getYear();
		
		//change sign convention for longitude from negative to positive in western hemisphere
		if(inWesternHemisphere)
			longitude *= -1;
		
		if(latitude > 89.8)
			latitude = 89.8;
		
		if(latitude < -89.8)
			latitude = -89.8;
		
		//timenow is GMT time for calculation in hours since 0Z
        double timenow = hour + (minute / 60.0) + (second / 3600.0);
        
        double JD = calcJD(year, month, day); 
        double t = calcTimeJulianCent(JD + timenow / 24.0); 
        //double R = calcSunRadVector(t); 
        //double alpha = calcSunRtAscension(t); 
        double theta = calcSunDeclination(t); 
        double Etime = calcEquationOfTime(t); 

        double eqtime = Etime;
        double solarDec = theta; // in degrees
        //double earthRadVec = R;

        double solarTimeFix = eqtime - 4.0 * longitude; // +60.0 * zone;
        double trueSolarTime = (hour * 60.0) + minute + (second / 60.0) + solarTimeFix;

        while (trueSolarTime > 1440)
        {
            trueSolarTime -= 1440;
        }

        double hourangle = trueSolarTime / 4.0 - 180.0;
        
        if (hourangle < -180)
        {
            hourangle = hourangle + 360.0;
        }

        double harad = degToRad(hourangle); // transferred

        double csz = Math.sin(degToRad(latitude)) * Math.sin(degToRad(solarDec)) +
            Math.cos(degToRad(latitude)) * Math.cos(degToRad(solarDec)) * Math.cos(harad);

        if (csz > 1.0)
        {
            csz = 1.0;
        }
        else if (csz < -1.0)
        {
            csz = -1.0;
        }

        double zenith = radToDeg(Math.acos(csz));

        double azDenom = (Math.cos(degToRad(latitude)) * Math.sin(degToRad(zenith)));

        double azimuth;
        if (Math.abs(azDenom) > 0.001)
        {
            double azRad = ((Math.sin(degToRad(latitude)) * Math.cos(degToRad(zenith))) -
                Math.sin(degToRad(solarDec))) / azDenom;
            if (Math.abs(azRad) > 1.0)
            {
                if (azRad < 0)
                {
                    azRad = -1.0;
                }
                else
                {
                    azRad = 1.0;
                }
            }

            azimuth = 180.0 - radToDeg(Math.acos(azRad));

            if (hourangle > 0.0)
            {
                azimuth = -azimuth;
            }
        }
        else
        {
            if (latitude > 0.0)
            {
                azimuth = 180.0;
            }
            else
            {
                azimuth = 0.0;
            }
        }
        if (azimuth < 0.0)
        {
            azimuth = azimuth + 360.0;
        }

        double exoatmElevation = 90.0 - zenith;

        double refractionCorrection;
        if (exoatmElevation > 85.0)
        {
            refractionCorrection = 0.0;
        }
        else
        {
            double te = Math.tan(degToRad(exoatmElevation));
            if (exoatmElevation > 5.0)
            {
                refractionCorrection = 58.1 / te - 0.07 / (te * te * te) + 0.000086 / (te * te * te * te * te);
            }
            else if (exoatmElevation > -0.575)
            {
                double step1 = (-12.79 + exoatmElevation * 0.711);
                double step2 = (103.4 + exoatmElevation * (step1));
                double step3 = (-518.2 + exoatmElevation * (step2));
                refractionCorrection = 1735.0 + exoatmElevation * (step3);
            }
            else
            {
                refractionCorrection = -20.774 / te;
            }
            refractionCorrection = refractionCorrection / 3600.0;
        }

        double solarZen = zenith - refractionCorrection;

        double solarElevation = 90.0 - solarZen;
		
		return new SolarPosition(azimuth, solarElevation);
	}
	
	/**
	 * Returns the Julian Day corresponding to the date
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
    private static double calcJD(int year, int month, int day)
    {
        if (month <= 2)
        {
            year = year - 1;
            month = month + 12;
        }

        double A = Math.floor(year / 100.0);
        double B = 2 - A + Math.floor(A / 4.0);

        double JD = Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + B - 1524.5;
        return JD;
    }
    
    /**
     * Converts Julian Daay to centuries since J2000.0
     * @param JD
     * @return
     */
    private static double calcTimeJulianCent(double JD)
    {
        double t = (JD - 2451545.0) / 36525.0;
        return t;
    }
    
    /**
     * Calculates the distance to the sun in AU
     * @param t - number of Julian centuries since J2000.0
     * @return sun radius vector in AUs
     */
    private static double calcSunRadVector(double t)
    {
        double v = calcSunTrueAnomaly(t);
        double e = calcEccentricityEarthOrbit(t);

        double R = (1.000001018 * (1 - e * e)) / (1 + e * Math.cos(degToRad(v)));
        return R;
    }
    
    /**
     * Calculates the right ascension of the sun
     * @param t - number of Julian centuries since J2000.0
     * @return sun's right ascension in degrees
     */
    private static double calcSunRtAscension(double t)
    {
        double e = calcObliquityCorrection(t);
        double lambda = calcSunApparentLong(t);

        double tananum = (Math.cos(degToRad(e)) * Math.sin(degToRad(lambda)));
        double tanadenom = (Math.cos(degToRad(lambda)));

        double alpha = radToDeg(Math.atan2(tananum, tanadenom));
        return alpha;

    }
    
    /**
     * Calculates the declination of the sun
     * @param t - number of Julian centuries since J2000.0
     * @return sun's declination in degrees
     */
    private static double calcSunDeclination(double t)
    {
        double e = calcObliquityCorrection(t);
        double lambda = calcSunApparentLong(t);

        double sint = Math.sin(degToRad(e)) * Math.sin(degToRad(lambda));
        double theta = radToDeg(Math.asin(sint));
        return theta;
    }
    
    /**
     * Calculate the difference between true solar time
     * and mean solar time
     * @param t - number of Julian centuries since J2000.0
     * @return equation of time in minutes of time
     */
    private static double calcEquationOfTime(double t)
    {
        double epsilon = calcObliquityCorrection(t);
        double l0 = calcGeomMeanLongSun(t);
        double e = calcEccentricityEarthOrbit(t);
        double m = calcGeomMeanAnomalySun(t);

        double y = Math.tan(degToRad(epsilon) / 2.0);
        y = Math.pow(y, 2);

        double sin2l0 = Math.sin(2.0 * degToRad(l0));
        double sinm = Math.sin(degToRad(m));
        double cos2l0 = Math.cos(2.0 * degToRad(l0));
        double sin4l0 = Math.sin(4.0 * degToRad(l0));
        double sin2m = Math.sin(2.0 * degToRad(m));

        double Etime = y * sin2l0 - 2.0 * e * sinm + 4.0 * e * y * sinm * cos2l0
                        - 0.5 * y * y * sin4l0 - 1.25 * e * e * sin2m;

        return radToDeg(Etime) * 4.0;
    }
    
    /**
     * Calculate the true anomaly of the sun
     * @param t - number of Julian centuries since J2000.0
     * @return sun's true anomaly in degrees
     */
    private static double calcSunTrueAnomaly(double t)
    {
        double m = calcGeomMeanAnomalySun(t);
        double c = calcSunEqOfCenter(t);

        double v = m + c;
        return v;
    }
    
    /**
     * Calculates the eccentricity of earth's orbit
     * @param t - number of Julian centuries since J2000.0
     * @return the unitless eccentricity
     */
    private static double calcEccentricityEarthOrbit(double t)
    {
        double e = 0.016708634 - t * (0.000042037 + 0.0000001267 * t);
        return e;

    }
    
    /**
     * Calculates the Geometric Mean Anomaly of the sun
     * @param t - number of Julian centuries since J2000.0
     * @return the Geometric Mean Anomaly of the Sun in degrees
     */
    private static double calcGeomMeanAnomalySun(double t)
    {
        double m = 357.52911 + t * (35999.05029 - 0.0001537 * t);
        return m;
    }
    
    /**
     * Calculates the equation of center for the sun
     * @param t - number of Julian centuries since J2000.0
     * @return degrees
     */
    private static double calcSunEqOfCenter(double t)
    {
        double m = calcGeomMeanAnomalySun(t);

        double mrad = degToRad(m);
        double sinm = Math.sin(mrad);
        double sin2m = Math.sin(mrad + mrad);
        double sin3m = Math.sin(mrad + mrad + mrad);

        double c = sinm * (1.914602 - t * (0.004817 + 0.000014 * t))
            + sin2m * (0.019993 - 0.000101 * t) + sin3m * 0.000289;
        
        return c;
    }
    
    /**
     * Calculates the obliquity of the ecliptic
     * @param t - number of Julian centuries since J2000.0
     * @return corrected obliquity in degrees
     */
    private static double calcObliquityCorrection(double t)
    {
        double e0 = calcMeanObliquityOfEcliptic(t);

        double omega = 125.04 - 1934.136 * t;
        double e = e0 + 0.00256 * Math.cos(degToRad(omega));
        return e;

    }
    
    /**
     * Calculates the apparent longitude of the sun
     * @param t - number of Julian centuries since J2000.0
     * @return sun's apparent longitude in degrees
     */
    private static double calcSunApparentLong(double t)
    {
        double O = calcSunTrueLong(t);

        double omega = 125.04 - 1934.136 * t;
        double lambda = O - 0.00569 - 0.00478 * Math.sin(degToRad(omega));
        return lambda;
    }
    
    /**
     * Calculate the mean obliquity of the ecliptic
     * @param t - number of Julian centuries since J2000.0
     * @return mean obliquity in degrees
     */
    private static double calcMeanObliquityOfEcliptic(double t)
    {
        double seconds = 21.448 - t * (46.815 + t * (0.00059 - t * (0.001813)));
        return 23.0 + (26.0 + (seconds / 60.0)) / 60.0;
    }
    
    /**
     * Calculates the true longitude of the sun
     * @param t - number of Julian centuries since J2000.0
     * @return sun's true longitude in degrees
     */
    private static double calcSunTrueLong(double t)
    {
        double l0 = calcGeomMeanLongSun(t);
        double c = calcSunEqOfCenter(t);

        return l0 + c;
    }
    
    /**
     * Calculate the Geometric Mean Longitude of the Sun
     * @param t - number of Julian centuries since J2000.0
     * @return the Geometric Mean Longitude of the Sun in degrees
     */
    private static double calcGeomMeanLongSun(double t)
    {
        double l0 = 280.46646 + t * (36000.76983 + 0.0003032 * t);
        while (true)
        {
            if ((l0 <= 360) && (l0 >= 0))
            {
                break;
            }
            if (l0 > 360)
            {
                l0 = l0 - 360;
            }
            if (l0 < 0)
            {
                l0 = l0 + 360;
            }
        }
        return l0;
    }

    /**
     * Converts radians to degrees
     * @param angleRad
     * @return
     */
    private static double radToDeg(double angleRad)
    {
        return (180.0 * angleRad / Math.PI);
    }
    
    /**
     * Converts degrees to radians
     * @param angleDeg
     * @return
     */
    private static double degToRad(double angleDeg)
    {
        return Math.PI * angleDeg / 180.0;
    }
}
