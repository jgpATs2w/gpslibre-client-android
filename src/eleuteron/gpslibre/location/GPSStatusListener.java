package eleuteron.gpslibre.location;

import eleuteron.gpslibre.utils.Utils;
import android.location.GpsStatus.Listener;

public class GPSStatusListener implements Listener{
	String[] STATUS = {"STARTED","desconectado", "FIRST FIX", "buscando satelites"};
	@Override
	public void onGpsStatusChanged(int event) {
		Utils.GPSStatusUpdate(""+STATUS[event-1]+" ");
	}
}
