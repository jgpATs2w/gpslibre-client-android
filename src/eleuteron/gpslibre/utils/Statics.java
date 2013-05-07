package eleuteron.gpslibre.utils;

import eleuteron.gpslibre.location.*;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.widget.TextView;

public class Statics {
	public static eleuteron.gpslibre.ui.Status Main = null;
	public static TextView LogView, GPSStatusText, GPRSStatus;
	public static Location loc = null;
	
	public static ConnectivityManager ConnMgr = null;
	
	public static LocationManager LocMgr = null;
	public static GPSLocationListener LocLtnr = null;
	public static GPSService GPSService = null;
	
	public static boolean TimerTaskRunning = false;
	
	public static String URL = "http://visor.gpslibre.es/php/save_pos.php", ID="sinid";
	public static int GPSTimeout = 120, GPRSTimeout = 30, Interval=5;
	public static int batt = -1;
	
	public static long IntervalTic = 0;
	
}
