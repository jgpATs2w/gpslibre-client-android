package eleuteron.gpslibre.location;

import eleuteron.gpslibre.utils.Statics;
import eleuteron.gpslibre.utils.Utils;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TimeoutGPS extends Service{
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(this.getClass().getCanonicalName(),"Starting GPSTimeout service");
		try {
			Thread.sleep(Statics.GPSTimeout*1000);
			
			Log.i(this.getClass().getCanonicalName(),"GPS timeout reached. Stopping service");
			Statics.GPSService.stopGPS(false);
			Log.i(this.getClass().getCanonicalName(),"GPS timeout reached. Stopping service");
		} catch (InterruptedException e) {e.printStackTrace(); }
		return START_STICKY;
	}
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
