package eleuteron.gpslibre.location;

import eleuteron.gpslibre.utils.Utils;
import android.location.GpsStatus.Listener;

public class GPSStatusListener implements Listener{
	String[] STATUS = {"STARTED","STOPPED", "FIRST FIX", "SATTELLITE"};
	@Override
	public void onGpsStatusChanged(int event) {
		Utils.GPSStatusUpdate(""+STATUS[event-1]+" ");
		
	}
}
