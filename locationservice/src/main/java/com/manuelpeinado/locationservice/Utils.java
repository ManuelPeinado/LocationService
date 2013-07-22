package com.manuelpeinado.locationservice;

import android.location.Location;

/**
 * Created by manuel on 21/07/13.
 */
public class Utils {
    public static String format(Location location) {
        return String.format("%s,%s", location.getLatitude(), location.getLongitude());
    }
}
